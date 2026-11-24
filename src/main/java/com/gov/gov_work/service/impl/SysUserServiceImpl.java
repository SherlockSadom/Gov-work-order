package com.gov.gov_work.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.gov_work.entity.SysUser;
import com.gov.gov_work.mapper.SysUserMapper;
import com.gov.gov_work.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
