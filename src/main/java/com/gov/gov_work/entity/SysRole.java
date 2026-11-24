package com.gov.gov_work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_role")
public class SysRole {
    // 主键id，自增
    @TableId(type = IdType.AUTO)
    private Long id;
    // 角色名称，比如：超级管理员、派单员、运维人员
    private String roleName;
    // 角色编码，比如：admin、dispatcher、operator
    private String roleCode;
    // 创建时间
    private LocalDateTime createTime;
}