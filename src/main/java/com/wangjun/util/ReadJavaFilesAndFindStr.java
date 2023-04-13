package com.wangjun.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangjun294
 * @description 在java文件中查到指定规则的字符串
 */
public class ReadJavaFilesAndFindStr {
    public static void main(String[] args) {
        List<File> javaFiles = listAllJavaFiles(new File("E:\\xiaopeng\\cloud-nsrm-extend\\center\\rbac-center-extend"));
        Set<String> assertMsg = new HashSet<>();
        for (File javaFile : javaFiles) {
            List<String> lines = FileUtil.readLines(javaFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.contains("BaseException")) {
                    List<String> all = ReUtil.findAll("\"([^\"]*)\"", line, 0);
                    assertMsg.addAll(all);
                }
            }
        }
        assertMsg.removeIf(x -> x.contains("{") || StrUtil.isBlank(x));
        List<String> list = new ArrayList<>(assertMsg);
        List<String> result = contractWithEquals(list);
        List<String> changeRes = new ArrayList<>();
        for (String s : result) {
            s = s.replace("\"", "");
            changeRes.add(s);
        }
        FileUtil.writeLines(changeRes, "D:\\MyData\\wangjun294\\Desktop\\output_zh.properties", StandardCharsets.UTF_8);
    }

    /**
     * 查找文件夹中所有java文件
     *
     * @param folder
     * @return
     */
    @NotNull
    public static List<File> listAllJavaFiles(@NotNull File folder) {
        File[] files = folder.listFiles();
        List<File> javaFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                } else if (file.isDirectory()) {
                    javaFiles.addAll(listAllJavaFiles(file));
                }
            }
        }
        return javaFiles;
    }


    /**
     * 将中文内容拼接成中文配置文件格式
     *
     * @param list
     * @return
     */
    @NotNull
    public static List<String> contractWithEquals(@NotNull List<String> list) {
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.setLength(0);
            builder.append(s).append("=").append(s);
            result.add(builder.toString());
        }
        return result;
    }
}
