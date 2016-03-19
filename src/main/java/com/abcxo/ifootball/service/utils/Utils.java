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
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import push.android.AndroidUnicast;

import java.io.*;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by shadow on 15/11/15.
 */
@Component
public class Utils {

    private static UserRepo userRepo;

    private static String grepPath = "/src/main/webapp/grep.json";

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
        if (bytes != null && bytes.length > 0) {
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
        return null;

    }


    public static String md52(String str) {
        return md5(md5(str));
    }

    public static String md5(String str) {
        try {
            StringBuffer buf = null;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString().substring(8, 24);
        } catch (Exception e) {
        }
        return "";
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


    public static void email(String email, String password) {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        senderImpl.setDefaultEncoding("UTF-8");
        mailMessage.setTo(email);
        mailMessage.setFrom(Constants.EMAIL);
        mailMessage.setSubject("足球狗密码找回");
        mailMessage.setText(String.format("%s 重置密码：%s", email, password));

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtps.ssl.checkserveridentity", "true");
        props.put("mail.smtps.ssl.trust", "*");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");
        senderImpl.setJavaMailProperties(props);
        senderImpl.setUsername(Constants.EMAIL_NAME); // 根据自己的情况,设置username
        senderImpl.setPassword(Constants.EMAIL_PASSWORD); // 根据自己的情况, 设置password
        senderImpl.send(mailMessage);

        System.out.println(" 邮件发送成功.. ");
    }

    public static long getDate(String time) {
        try {
            if (!StringUtils.isEmpty(time)) {
                if (time.matches("\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}")) {

                } else if (time.matches("\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//设置日期格式
                    String year = dateFormat.format(new Date());
                    time = year + "-" + time;
                } else if (time.matches("\\d{2}:\\d{2}")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                    String date = dateFormat.format(new Date());
                    time = date + " " + time;
                }
                return new SimpleDateFormat(Constants.DATE_FORMAT).parse(time).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String getTime(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH时mm分");//设置日期格式
        return dateFormat.format(new Date(date));

    }

    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return dateFormat.format(System.currentTimeMillis());
    }

    public static String getTime() {
        return getTime(new Date().getTime());
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
        return getDocument(url, new HashMap<>());
    }


    public static Document getDocument(String url, Map<String, String> cookies) {
        try {
            return Jsoup.connect(url)
                    .cookies(cookies)
                    .timeout(30000)
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGrepString() {
        try {
            String path = new File("").getCanonicalPath() + grepPath;
            System.out.println("grep path" + path);
            InputStream is = new FileInputStream(path);
            String result = IOUtils.toString(is);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveGrepString(String grepString) {
        try {
            File file = new File(new File("").getCanonicalPath() + grepPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(grepString);
            bw.close();
            System.out.println("Done");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
