package com.gov.gov_work.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.gov_work.common.Result;
import com.gov.gov_work.entity.SysUser;
import com.gov.gov_work.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    // 1. 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody SysUser loginUser) {
        String md5Password = MD5.create().digestHex(loginUser.getPassword());

        SysUser user = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, loginUser.getUsername())
                .eq(SysUser::getPassword, md5Password)
                .one();

        if (user == null) {
            return Result.fail("账号或密码错误");
        }
        if (user.getStatus() == 0) {
            return Result.fail("账号已被禁用，无法登录");
        }
        return Result.success(user);
    }

    // 2. 分页查询用户列表，支持按姓名、状态筛选
    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestBody(required = false) SysUser query) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (query != null) {
            if (query.getRealName() != null && !query.getRealName().isEmpty()) {
                wrapper.like(SysUser::getRealName, query.getRealName());
            }
            if (query.getStatus() != null) {
                wrapper.eq(SysUser::getStatus, query.getStatus());
            }
        }
        wrapper.orderByDesc(SysUser::getCreateTime);
        return Result.success(sysUserService.page(page, wrapper));
    }

    // 3. 新增用户，默认密码123456
    @PostMapping("/add")
    public Result add(@RequestBody SysUser sysUser) {
        // 校验账号是否重复
        long count = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, sysUser.getUsername())
                .count();
        if (count > 0) {
            return Result.fail("账号已存在，请勿重复添加");
        }

        sysUser.setPassword(MD5.create().digestHex("123456"));
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        if (sysUser.getStatus() == null) {
            sysUser.setStatus(1);
        }

        return sysUserService.save(sysUser) ? Result.success() : Result.fail("新增用户失败");
    }

    // 4. 修改用户信息
    @PostMapping("/update")
    public Result update(@RequestBody SysUser sysUser) {
        sysUser.setUpdateTime(LocalDateTime.now());
        // 密码为空则不修改密码
        if (sysUser.getPassword() != null && !sysUser.getPassword().isEmpty()) {
            sysUser.setPassword(MD5.create().digestHex(sysUser.getPassword()));
        } else {
            sysUser.setPassword(null);
        }
        return sysUserService.updateById(sysUser) ? Result.success() : Result.fail("修改用户失败");
    }

    // 5. 删除用户
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return sysUserService.removeById(id) ? Result.success() : Result.fail("删除用户失败");
    }
}
