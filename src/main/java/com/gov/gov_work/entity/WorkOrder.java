package com.gov.gov_work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("work_order")
public class WorkOrder {
    // 主键id，自增
    @TableId(type = IdType.AUTO)
    private Long id;
    // 工单编号，唯一
    private String orderNo;
    // 工单标题
    private String title;
    // 问题描述
    private String content;
    // 提交人id
    private Long submitUserId;
    // 处理人id
    private Long receiveUserId;
    // 上报部门
    private String deptName;
    // 工单状态：0待派单 1处理中 2已完成 3关闭
    private Integer orderStatus;
    // 优先级：1普通 2紧急 3特急
    private Integer priority;
    // 提交时间
    private LocalDateTime createTime;
    // 办结时间
    private LocalDateTime finishTime;
    // 更新时间
    private LocalDateTime updateTime;
}
