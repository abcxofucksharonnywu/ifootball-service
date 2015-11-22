package com.abcxo.ifootball.service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by shadow on 15/11/15.
 */
public class Constants {
    public static final String ACCESS_KEY = "7_XDBktTNrUwzTEbx0w_6aaCLUy9qGdafLBHOnMx";
    public static final String SECRET_KEY = "WRj8yZzWFIQ0-0F_inqG3oMWmpWC4LVbYT57hOdK";
    public static final String IMAGE_HOST = "http://7xoc9s.com1.z0.glb.clouddn.com/";
    public static final String IMAGE_NAME = "ifootball";


    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "参数错误")
    public static class ArgumentException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "上传图片失败")
    public static class ImageException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱或者密码错误")
    public static class UserValidateException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱已经被占用")
    public static class UserAlreadyExistException extends RuntimeException {
    }
}
