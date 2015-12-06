package com.abcxo.ifootball.service.utils;

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.utils.Constants.UploadException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import push.android.AndroidUnicast;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shadow on 15/11/15.
 */
@Component
public class Utils {

    private static UserRepo userRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        Utils.userRepo = userRepo;
    }

    public static class UploadRet {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }

    public static String image(MultipartFile image) {
        try {
            return upload(image.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String content(String content) {
        try {
            return upload(content.getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String upload(byte[] bytes) {
        try {

            Auth auth = Auth.create(Constants.UPLOAD_APP_KEY, Constants.UPLOAD_SECRET_KEY);
            UploadManager uploadManager = new UploadManager();
            String token = auth.uploadToken(Constants.UPLOAD_NAME);
            Response response = uploadManager.put(bytes, null, token);
            if (response.isOK()) {
                UploadRet ret = response.jsonToObject(UploadRet.class);
                String url = Constants.UPLOAD_HOST + "/" + ret.key;
                return url;
            } else {
                throw new UploadException();
            }

        } catch (Exception e) {
            throw new UploadException();
        }
    }


    /***
     * MD5加码 生成32位md5码
     */
    public static String md5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }


    public static void message(Message message) {
        try {
            User user2 = userRepo.findOne(message.getUid2());
            String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000L));
            String appKey = Constants.MESSAGE_APP_KEY;
            String secretKey = Constants.MESSAGE_SECRET_KEY;
            String deviceToken = user2.getDeviceToken();
            AndroidUnicast broadcast = new AndroidUnicast();
            broadcast.setAppMasterSecret(secretKey);
            broadcast.setPredefinedKeyValue("appkey", appKey);
            broadcast.setPredefinedKeyValue("timestamp", timestamp);
            broadcast.setPredefinedKeyValue("device_tokens", deviceToken);
            broadcast.setPredefinedKeyValue("ticker", message.getId());
            broadcast.setPredefinedKeyValue("title", message.getTitle());
            broadcast.setPredefinedKeyValue("text", message.getDescription());
            broadcast.setPredefinedKeyValue("img", message.getIcon());
            broadcast.setPredefinedKeyValue("after_open", "go_app");
            broadcast.setPredefinedKeyValue("display_type", "notification");
            broadcast.setPredefinedKeyValue("production_mode", "true");
            ObjectMapper mapper = new ObjectMapper();
            broadcast.setExtraField("message", mapper.writeValueAsString(message));
            broadcast.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNameIndex(String str) {
        if (!StringUtils.isEmpty(str)) {
            String alphabet = str.substring(0, 1);
            if (alphabet.matches("[\\u4e00-\\u9fa5]+")) {
                HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
                // 输出拼音全部小写
                defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
                // 不带声调
                defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
                String pinyin = null;
                try {
                    pinyin = (String) PinyinHelper.toHanyuPinyinStringArray(str.charAt(0), defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
                alphabet = pinyin.substring(0, 1);
            }
            return alphabet.toUpperCase();

        }
        return "";

    }


    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH时mm分");//设置日期格式
        return dateFormat.format(new Date());

    }

    public static long distance(double long1, double lat1, double long2,
                                double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        return (long) d;
    }

    public static Document getDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .timeout(30000)
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
