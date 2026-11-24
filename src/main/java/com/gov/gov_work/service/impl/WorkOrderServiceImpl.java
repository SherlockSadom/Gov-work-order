package com.gov.gov_work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.gov_work.entity.WorkOrder;
import com.gov.gov_work.mapper.WorkOrderMapper;
import com.gov.gov_work.service.WorkOrderService;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {
}

