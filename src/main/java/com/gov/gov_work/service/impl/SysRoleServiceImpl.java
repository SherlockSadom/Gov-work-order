package com.gov.gov_work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.gov_work.entity.SysRole;
import com.gov.gov_work.mapper.SysRoleMapper;
import com.gov.gov_work.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
