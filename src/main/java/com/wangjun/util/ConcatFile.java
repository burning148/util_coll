package com.wangjun.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjun294
 * @description 合并国际化文件
 */
public class ConcatFile {
    public static void main(String[] args) {
        List<String> zhFile = FileUtil.readLines("D:\\output.txt", StandardCharsets.UTF_8);
        List<String> usFile = FileUtil.readLines("D:\\MyData\\wangjun294\\Desktop\\新建 Microsoft Word 文档(翻译结果).txt", StandardCharsets.UTF_8);
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < zhFile.size(); i++) {
            if (StrUtil.isNotBlank(zhFile.get(i))) {
                builder.setLength(0);
                builder.append(zhFile.get(i).trim()).append("=").append(usFile.get(i).trim());
                result.add(builder.toString());
            }
        }
        FileUtil.writeLines(result ,"D:\\MyData\\wangjun294\\Desktop\\output_us.txt", StandardCharsets.UTF_8);
    }
}
