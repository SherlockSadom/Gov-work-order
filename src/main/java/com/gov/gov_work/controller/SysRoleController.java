package com.gov.gov_work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.gov_work.common.Result;
import com.gov.gov_work.entity.SysRole;
import com.gov.gov_work.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    // 1. 查询所有角色列表（新增用户时的下拉选择用）
    @GetMapping("/listAll")
    public Result listAll() {
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }

    // 2. 分页查询角色
    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestBody(required = false) SysRole query) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (query != null && query.getRoleName() != null && !query.getRoleName().isEmpty()) {
            wrapper.like(SysRole::getRoleName, query.getRoleName());
        }
        wrapper.orderByDesc(SysRole::getCreateTime);
        return Result.success(sysRoleService.page(page, wrapper));
    }

    // 3. 新增角色
    @PostMapping("/add")
    public Result add(@RequestBody SysRole sysRole) {
        // 校验角色编码不能重复
        long count = sysRoleService.lambdaQuery()
                .eq(SysRole::getRoleCode, sysRole.getRoleCode())
                .count();
        if (count > 0) {
            return Result.fail("角色编码已存在，请勿重复添加");
        }
        sysRole.setCreateTime(LocalDateTime.now());
        return sysRoleService.save(sysRole) ? Result.success() : Result.fail("新增角色失败");
    }

    // 4. 修改角色信息
    @PostMapping("/update")
    public Result update(@RequestBody SysRole sysRole) {
        return sysRoleService.updateById(sysRole) ? Result.success() : Result.fail("修改角色失败");
    }

    // 5. 删除角色
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return sysRoleService.removeById(id) ? Result.success() : Result.fail("删除角色失败");
    }
}
