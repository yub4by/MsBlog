package com.yppah.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yppah.mapper.PermissionMapper;
import com.yppah.model.params.PageParam;
import com.yppah.pojo.Permission;
import com.yppah.vo.PageResult;
import com.yppah.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public Result listPermission(PageParam pageParam) {
        /**
         * 需要的数据：管理后台表的所有的字段 Permission
         * 其他需求：分页查询
         */
        Page<Permission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Permission::getName, pageParam.getQueryString());
        }
        Page<Permission> permissionPage = permissionMapper.selectPage(page, queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return Result.success(pageResult);
    }

    public Result add(Permission permission) {
        int insert = permissionMapper.insert(permission);
        return Result.success(null);
    }


    public Result update(Permission permission) {
        int update = permissionMapper.updateById(permission);
        return Result.success(null);
    }

    public Result delete(Long id) {
        int delete = permissionMapper.deleteById(id);
        return Result.success(null);
    }

}
