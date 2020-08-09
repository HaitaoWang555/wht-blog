package com.wht.item.admin.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本项目 通用工具类
 *
 * @author wht
 * @since 2020-08-08 22:49
 */
public class Util {

    /**
     * 创建上传文件路径
     * @return upload - 模块 - 用户ID - 时间 - 文件名
     */
    public static String createFilePath(String module) {
        String fileTempPath = "upload/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String format = sdf.format(new Date());
        return fileTempPath + module + "/" + SecurityUtil.getCurrentUserId() + "/" + format + "/";
    }

    /**
     * 压缩图片
     * @param localFilePath 原文件地址
     * @param thumbnailFilePath 压缩文件地址
     */
    public static void imgThumbnail(String localFilePath, String thumbnailFilePath) throws IOException {
        Thumbnails.of(localFilePath)
                .scale(1)
                .outputQuality(0.5)
                .outputFormat("jpg")
                .toFile(thumbnailFilePath);
    }

}
