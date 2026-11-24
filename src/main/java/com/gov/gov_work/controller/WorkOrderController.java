package com.gov.gov_work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.gov_work.common.Result;
import com.gov.gov_work.entity.WorkOrder;
import com.gov.gov_work.entity.WorkOrderLog;
import com.gov.gov_work.service.WorkOrderService;
import com.gov.gov_work.service.WorkOrderLogService;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private WorkOrderLogService workOrderLogService;

    // 1. 提交工单
    @PostMapping("/add")
    public Result add(@RequestBody WorkOrder workOrder) {
        // 自动生成唯一工单编号（雪花算法，WO前缀）
        workOrder.setOrderNo("WO" + IdUtil.getSnowflakeNextIdStr());
        // 初始状态：0 待派单
        workOrder.setOrderStatus(0);
        workOrder.setCreateTime(LocalDateTime.now());
        workOrder.setUpdateTime(LocalDateTime.now());
        // 默认优先级1：普通
        if (workOrder.getPriority() == null) {
            workOrder.setPriority(1);
        }

        boolean save = workOrderService.save(workOrder);
        if (save) {
            // 自动记录提交日志
            WorkOrderLog log = new WorkOrderLog();
            log.setOrderId(workOrder.getId());
            log.setOperateUserId(workOrder.getSubmitUserId());
            log.setOperateType("提交工单");
            log.setOperateDesc("提交工单，编号：" + workOrder.getOrderNo());
            log.setCreateTime(LocalDateTime.now());
            workOrderLogService.save(log);

            return Result.success(workOrder);
        }
        return Result.fail("工单提交失败");
    }

    // 2. 分页查询工单列表，支持按状态、提交人、处理人筛选
    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestBody(required = false) WorkOrder query) {
        Page<WorkOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();

        if (query != null) {
            if (query.getOrderStatus() != null) {
                wrapper.eq(WorkOrder::getOrderStatus, query.getOrderStatus());
            }
            if (query.getSubmitUserId() != null) {
                wrapper.eq(WorkOrder::getSubmitUserId, query.getSubmitUserId());
            }
            if (query.getReceiveUserId() != null) {
                wrapper.eq(WorkOrder::getReceiveUserId, query.getReceiveUserId());
            }
        }
        // 按提交时间倒序，最新的排在最前面
        wrapper.orderByDesc(WorkOrder::getCreateTime);

        Page<WorkOrder> result = workOrderService.page(page, wrapper);
        return Result.success(result);
    }

    // 3. 根据ID查询工单详情
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        WorkOrder workOrder = workOrderService.getById(id);
        return Result.success(workOrder);
    }

    // 4. 修改工单状态（派单/处理中/办结/关闭）
    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestParam Long id,
                               @RequestParam Integer status,
                               @RequestParam Long operateUserId,
                               @RequestParam(required = false) String remark) {
        WorkOrder workOrder = workOrderService.getById(id);
        if (workOrder == null) {
            return Result.fail("工单不存在");
        }

        workOrder.setOrderStatus(status);
        workOrder.setUpdateTime(LocalDateTime.now());
        // 办结时自动填充办结时间
        if (status == 2) {
            workOrder.setFinishTime(LocalDateTime.now());
        }

        boolean update = workOrderService.updateById(workOrder);
        if (update) {
            // 自动记录操作日志
            String type = switch (status) {
                case 1 -> "派单";
                case 2 -> "办结";
                case 3 -> "关闭";
                default -> "状态变更";
            };

            WorkOrderLog log = new WorkOrderLog();
            log.setOrderId(id);
            log.setOperateUserId(operateUserId);
            log.setOperateType(type);
            log.setOperateDesc(remark != null ? remark : "工单状态变更为：" + status);
            log.setCreateTime(LocalDateTime.now());
            workOrderLogService.save(log);

            return Result.success();
        }
        return Result.fail("状态修改失败");
    }
    // 5. 根据工单ID查询操作日志列表
    @GetMapping("/log/list/{orderId}")
    public Result getLogList(@PathVariable Long orderId) {
        LambdaQueryWrapper<WorkOrderLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkOrderLog::getOrderId, orderId);
        wrapper.orderByDesc(WorkOrderLog::getCreateTime);
        return Result.success(workOrderLogService.list(wrapper));
    }
    // 6. 工单状态统计（首页仪表盘用）
    @GetMapping("/stat/status")
    public Result statByStatus() {
        // 分别统计各状态工单数量
        long waitCount = workOrderService.lambdaQuery().eq(WorkOrder::getOrderStatus, 0).count();
        long processingCount = workOrderService.lambdaQuery().eq(WorkOrder::getOrderStatus, 1).count();
        long finishCount = workOrderService.lambdaQuery().eq(WorkOrder::getOrderStatus, 2).count();
        long closeCount = workOrderService.lambdaQuery().eq(WorkOrder::getOrderStatus, 3).count();

        // 封装统计结果返回
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("waitCount", waitCount);
        result.put("processingCount", processingCount);
        result.put("finishCount", finishCount);
        result.put("closeCount", closeCount);
        result.put("total", waitCount + processingCount + finishCount + closeCount);
        return Result.success(result);
    }

}
