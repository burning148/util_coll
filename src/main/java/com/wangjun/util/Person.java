package com.wangjun.util;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wangjun294
 */
@Data
@AllArgsConstructor
public class Person {
    @ExcelProperty(value = "id", index = 0)
    private Long id;
    @ExcelProperty(value = "名称", index = 1)
    private String name;
    @ExcelProperty(value = "年龄", index = 2)
    private String age;

    private BigDecimal price;
}
