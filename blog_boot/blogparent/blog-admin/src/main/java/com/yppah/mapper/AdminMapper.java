package com.yppah.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yppah.pojo.Admin;
import com.yppah.pojo.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("SELECT * FROM ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{id})")
    List<Permission> findPermissionByAdminId(Long id);

}
