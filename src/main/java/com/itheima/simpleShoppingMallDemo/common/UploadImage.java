package com.itheima.simpleShoppingMallDemo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public final class UploadImage {

    // 用于匹配 base64 图片数据的正则表达式
    private static final Pattern DATA_URI = Pattern.compile("^data:image/(\\w+);base64,");

    // 文件上传的根路径
    private static String uploadPath;

    // 从配置文件中获取上传路径
    @Value("${file.upload-path}")
    public void setUploadPath(String path) {
        uploadPath = path;
    }

    /**
     * 保存 base64 编码的图片并返回其相对路径
     * @param data 带有 data URI 头或纯 base64 编码的图片数据
     * @param url 图片保存的子目录
     * @return 图片的绝对路径或错误信息
     */
    public static String uploadPostImage(String data, String url) {
        try {
            // 解析图片扩展名
            String ext = parseExt(data);
            if (ext == null) {
                throw new IllegalArgumentException("无法识别图片类型");
            }

            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;

            // 从 data URI 中提取 base64 编码部分
            String base64Image = data.contains(",") ? data.split(",")[1] : data;
            byte[] bytes = Base64.getDecoder().decode(base64Image);

            // 限制图片最大为 10MB
            if (bytes.length > 10 * 1024 * 1024) {
                return "图片大小不能超过10MB";
            }

            // 创建保存图片的目录
            Path dir = Paths.get(uploadPath, url);
            Files.createDirectories(dir);

            // 保存图片文件到指定路径
            Path file = dir.resolve(fileName);
            Files.write(file, bytes);

            // 返回图片的相对路径
            return "./img"+ url + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "保存失败：" + e.getMessage();
        }
    }

    /* =============== 以下功能不变 =============== */

    /**
     * 解析图片的扩展名
     * @param base64 base64 编码的图片数据
     * @return 图片的扩展名
     */
    private static String parseExt(String base64) {
        // 1. 尝试从 data URI 头解析图片类型
        var m = DATA_URI.matcher(base64);
        if (m.find()) return m.group(1).toLowerCase();

        // 2. 如果没有 URI 头，根据魔数判断图片类型
        String pure = base64.contains(",") ? base64.split(",")[1] : base64;
        byte[] bytes = Base64.getDecoder().decode(pure);
        return guessExtFromMagic(bytes);
    }

    /**
     * 根据文件的魔数猜测图片扩展名
     * @param bytes 图片的字节数据
     * @return 扩展名
     */
    private static String guessExtFromMagic(byte[] bytes) {
        if (bytes.length < 4) return null;
        String hex = bytesToHex(Arrays.copyOfRange(bytes, 0, 4)).toUpperCase();
        return Map.of(
                        "FFD8FF", "jpg",
                        "89504E47", "png",
                        "47494638", "gif",
                        "52494646", "webp"
                ).entrySet().stream()
                .filter(e -> hex.startsWith(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02X", b));
        return sb.toString();
    }
}
