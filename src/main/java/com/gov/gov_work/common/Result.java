package com.gov.gov_work.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    // 状态码：200成功，500失败
    private Integer code;
    // 提示信息
    private String msg;
    // 返回的数据
    private Object data;

    // 成功：不带数据
    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    // 成功：带数据
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    // 失败：带错误提示
    public static Result fail(String msg) {
        return new Result(500, msg, null);
    }
}
