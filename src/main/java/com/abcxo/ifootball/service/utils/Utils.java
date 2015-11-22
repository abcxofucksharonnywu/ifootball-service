package com.abcxo.ifootball.service.utils;

import com.abcxo.ifootball.service.models.Message;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;
import com.abcxo.ifootball.service.utils.Constants.ImageException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shadow on 15/11/15.
 */
public class Utils {
    public static class ImageRet {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }

    public static String image(MultipartFile image) {
        try {
            Auth auth = Auth.create(Constants.ACCESS_KEY, Constants.SECRET_KEY);
            UploadManager uploadManager = new UploadManager();
            String token = auth.uploadToken(Constants.IMAGE_NAME);
            Response response = uploadManager.put(image.getBytes(), null, token);
            if (response.isOK()) {
                ImageRet ret = response.jsonToObject(ImageRet.class);
                String imageUrl = Constants.IMAGE_HOST + "/" + ret.key;
                return imageUrl;
            } else {
                throw new ImageException();
            }

        } catch (Exception e) {
            throw new ImageException();
        }
    }

    public static void message(Message message) {

    }


    public static String getNameIndex(String name) {
        return "A";
    }


    public static String getTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH时mm分");//设置日期格式
        return dateFormat.format(new Date());

    }

}
