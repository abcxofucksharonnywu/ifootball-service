package com.abcxo.ifootball.service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by shadow on 15/11/15.
 */
public class Constants {
    public static final String ACCESS_KEY = "AOlh5X-PbSxjtP6hEe-4joD3lzLeI7we6oVUUHe8";
    public static final String SECRET_KEY = "tU2f8BPJ79MIdMLPXSDZR-Y1rweX9eN46v3-oGVv";
    public static final String UPLOAD_HOST = "http://7xosf3.com1.z0.glb.clouddn.com";
    public static final String UPLOAD_NAME = "ifootball";

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "参数错误")
    public static class ArgumentException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "上传失败")
    public static class UploadException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱或者密码错误")
    public static class UserValidateException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱已经被占用")
    public static class UserAlreadyExistException extends RuntimeException {
    }


    public static final String GROUP_YINGCHAO = "英超";
    public static final String GROUP_XIJIA = "西甲";
    public static final String GROUP_DEJIA = "德甲";
    public static final String GROUP_YIJIA = "意甲";
    public static final String GROUP_FAJIA = "法甲";
    public static final String GROUP_ZHONGCHAO = "中超";
    public static final String GROUP_NEWS = "新闻";
    public static final String GROUP_PUBLIC = "公众";
    public static final String GROUP_SPECIAL = "特殊";


    public static final String NEWS_YINGCHAO = "英超新闻";
    public static final String NEWS_XIJIA = "西甲新闻";
    public static final String NEWS_DEJIA = "德甲新闻";
    public static final String NEWS_YIJIA = "意甲新闻";
    public static final String NEWS_FAJIA = "法甲新闻";
    public static final String NEWS_ZHONGCHAO = "中超新闻";
    public static final String NEWS_OUGUAN = "欧冠新闻";
    public static final String NEWS_HUABIAN = "花边新闻";
    public static final String PUBLIC_ZHONGDA = "重大新闻";


    public static final String TWEET_HTML_CONTENT_TAG="#content#";
    public static final String TWEET_HTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"><title></title><style type=\"text/css\">img{width:100%}</style><script type=\"text/javascript\">function load(){for(var n=document.getElementsByTagName(\"img\"),o=0;o<n.length;o++)!function(o){var c=n[o];c.onclick=function(){window.open(c.src)}}(o)}</script></head><body onload=\"load()\">#content#</body></html>";
    public static final String TWEET_ADD_HTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"><style type=\"text/css\">img{width:33%%}</style></head><body><div>%s</div><table width=\"100%%\">%s</table></body></html>";
    public static final String TWEET_ADD_IMAGE_HTML = "<img src=\"%s\" />";


}
