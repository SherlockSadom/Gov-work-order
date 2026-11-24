package com.gov.gov_work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.gov_work.entity.WorkOrderLog;
import com.gov.gov_work.mapper.WorkOrderLogMapper;
import com.gov.gov_work.service.WorkOrderLogService;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderLogServiceImpl extends ServiceImpl<WorkOrderLogMapper, WorkOrderLog> implements WorkOrderLogService {
}

