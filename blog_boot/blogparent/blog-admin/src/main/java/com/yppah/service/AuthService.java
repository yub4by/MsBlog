package com.yppah.service;

import com.yppah.pojo.Admin;
import com.yppah.pojo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication){
        //权限认证
        //请求路径
        String requestURI = request.getRequestURI();
        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)){
            return false; //未登录
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUsername(username);
        if (admin == null){
            return  false;
        }
        if (1 == admin.getId()){
            return true; //超级管理员，直接放行
        }
        Long id = admin.getId();
        List<Permission> permissionList = this.adminService.findPermissionByAdminId(id);
        requestURI = StringUtils.split(requestURI,'?')[0];
        for (Permission permission : permissionList) {
            if (requestURI.equals(permission.getPath())){
                return true;
            }
        }
        return false;
    }

}
