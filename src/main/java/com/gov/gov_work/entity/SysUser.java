package com.gov.gov_work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    // 主键id，自增
    @TableId(type = IdType.AUTO)
    private Long id;
    // 登录账号
    private String username;
    // 密码
    private String password;
    // 真实姓名
    private String realName;
    // 手机号
    private String phone;
    // 角色id
    private Long roleId;
    // 账号状态 1启用 0禁用
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
