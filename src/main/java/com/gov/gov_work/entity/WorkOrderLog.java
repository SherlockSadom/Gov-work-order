package com.gov.gov_work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("work_order_log")
public class WorkOrderLog {
    // 主键id，自增
    @TableId(type = IdType.AUTO)
    private Long id;
    // 关联的工单id
    private Long orderId;
    // 操作人id
    private Long operateUserId;
    // 操作类型：提交/派单/处理/办结/关闭
    private String operateType;
    // 操作备注说明
    private String operateDesc;
    // 操作时间
    private LocalDateTime createTime;
}