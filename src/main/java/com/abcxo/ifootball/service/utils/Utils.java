package com.abcxo.ifootball.service.utils;

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.utils.Constants.UploadException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shadow on 15/11/15.
 */
public class Utils {
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

            Auth auth = Auth.create(Constants.ACCESS_KEY, Constants.SECRET_KEY);
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

    public static void message(Message message) {

    }


    public static String getNameIndex(String name) {
        return "A";
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
        return (long)d;
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
