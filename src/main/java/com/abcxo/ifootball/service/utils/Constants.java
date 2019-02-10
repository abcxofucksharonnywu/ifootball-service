package com.abcxo.ifootball.service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by shadow on 15/11/15.
 */
public class Constants {
    public static final String UPLOAD_APP_KEY = "AOlh5X-PbSxjtP6hEe-4joD3lzLeI7we6oVUUHe8";
    public static final String UPLOAD_SECRET_KEY = "tU2f8BPJ79MIdMLPXSDZR-Y1rweX9eN46v3-oGVv";
    public static final String UPLOAD_HOST = "http://plxxif0at.bkt.clouddn.com";
    public static final String UPLOAD_NAME = "ifootball";

    public static final String MESSAGE_APP_KEY = "565465fce0f55a849e003de9";
    public static final String MESSAGE_SECRET_KEY = "uiy1rgsggqfkst4cyspplfzchmt3jzsn";

    public static final String NAME = "足球狗";

    public static final String EMAIL = "iamthefootball@qq.com";
    public static final String EMAIL_NAME = "2957543823";
    public static final String EMAIL_PASSWORD = "srnrtunumukwdccd";


    public static final int MAX_SUMMARY = 70;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";


    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "参数错误")
    public static class ArgumentException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "上传失败")
    public static class UploadException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱或者密码错误")
    public static class UserValidateException extends RuntimeException {
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱没有注册")
    public static class UserNotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "邮箱已经被占用")
    public static class UserAlreadyExistException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "昵称已经被占用")
    public static class UserNameAlreadyExistException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "保存失败")
    public static class SaveException extends RuntimeException {
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
    public static final String NEWS_VIDEO = "精彩视频";
    public static final String SPECIAL_BREAK = "Breaking News";


    public static final String TWEET_HTML_CONTENT_TAG = "#content#";
    public static final String TWEET_HTML_IMAGES_TAG = "#images#";
    public static final String TWEET_HTML_IMAGE_TAG = "#image#";
    public static final String TWEET_HTML_PROMPT_TAG = "#prompt#";
    public static final String TWEET_HTML_VIDEO = "<html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no,minimal-ui\"><style>.video_holder{width:100%;margin:0;padding:0;background-size:cover}.video_icon{height:100%;width:100%;background:url(https://dqdfiles.b0.upaiyun.com/app/video.png) center center no-repeat;background-size:60px 60px}iframe{width:100%;margin:0;padding:0;height:100%}</style><script type=\"text/javascript\" src=\"https://dqdfiles.b0.upaiyun.com/assets/js/zepto.min.js\"></script><script type=\"text/javascript\">function load(){for(var n=document.getElementsByTagName(\"img\"),c=0;c<n.length;c++)!function(c){var e=n[c];e.onclick=function(){handler.onImageClick(e.src)}}(c)}</script></head><body>#content#<script>$(function(){$(\".video_holder\").each(function(){var i=$(this).width();$(this).height(i/1.5)}),$(\".video_holder\").click(function(){var i=$(this).attr(\"src\"),e=' <iframe allowfullscreen=\"\" src=\"'+i+'\" frameborder=\"0\" ></iframe>';$(this).append(e),$(this).find(\".video_icon\").hide()})});</script></body></html>";
    public static final String TWEET_HTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"><title></title><style type=\"text/css\">img{width:100%}</style><script type=\"text/javascript\">function load(){for(var n=document.getElementsByTagName(\"img\"),c=0;c<n.length;c++)!function(c){var e=n[c];e.onclick=function(){handler.onImageClick(e.src)}}(c)}</script></head><body onload=\"load()\">#content#</body></html>";
    public static final String TWEET_HTML_DATA = "<html><head><meta charset=\"utf-8\"><meta name=\"viewport\"content=\"initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"><script src=\"http://static.dongqiudi.com/assets/js/jQuery.1.11.min.js\"></script><link rel=\"shortcut icon\"href=\"http://www.dongqiudi.com/web/images/fav.ico\"type=\"image/x-icon\"><link rel=\"stylesheet\"href=\"http://7xosf3.com1.z0.glb.clouddn.com/style3.css\"><link rel=\"stylesheet\"href=\"http://www.dongqiudi.com/web/css/jPaginate.css\"></head><body>#content#</body></html>";

    public static final String TWEET_ADD_HTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"><title></title><style type=\"text/css\">.image{width:100%;object-fit:cover}.prompt{color:#319AFF}</style><script type=\"text/javascript\">function load(){for(var n=document.getElementsByTagName(\"img\"),t=0;t<n.length;t++)!function(t){var e=n[t];e.onclick=function(){handler.onImageClick(e.src)}}(t);for(var e=document.getElementsByClassName(\"prompt\"),o=0;o<e.length;o++)!function(n){var t=e[n];t.onclick=function(){handler.onPromptClick(t.textContent)}}(o)}</script></head><body onload=\"load()\"><div class=\"content\" style=\"margin-bottom:1rem\">#content#</div>#images#</body></html>";
    public static final String TWEET_ADD_PRO_HTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"><title></title><style type=\"text/css\">.image{width:100%;object-fit:cover}.prompt{color:#319AFF}</style><script type=\"text/javascript\">function load(){for(var n=document.getElementsByTagName(\"img\"),t=0;t<n.length;t++)!function(t){var e=n[t];e.onclick=function(){handler.onImageClick(e.src)}}(t);for(var e=document.getElementsByClassName(\"prompt\"),o=0;o<e.length;o++)!function(n){var t=e[n];t.onclick=function(){handler.onPromptClick(t.textContent)}}(o)}</script></head><body onload=\"load()\"><div class=\"content\" style=\"margin-bottom:1rem\">#content#</div>#images#</body></html>";
    public static final String TWEET_ADD_IMAGE_HTML = "<img class=\"image\" src=\"#image#\" />";
    public static final String TWEET_ADD_PROMPT_HTML = "<a class=\"prompt\">#prompt#</a>";


}
