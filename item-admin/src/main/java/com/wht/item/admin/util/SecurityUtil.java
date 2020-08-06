package com.wht.item.admin.util;

import cn.hutool.core.util.ObjectUtil;
import com.wht.item.admin.bo.AdminUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security工具类
 *
 * @author wht
 * @since 2020-08-04 4:46
 */
public class SecurityUtil {


    /**
     * 获取当前登录用户用户名
     *
     * @return 当前登录用户用户名
     */
    public static String getCurrentUsername() {
        AdminUserDetails currentUser = getCurrentUser();
        return ObjectUtil.isNull(currentUser) ? null : currentUser.getUsername();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 当前登录用户ID
     */
    public static Long getCurrentUserId() {
        AdminUserDetails currentUser = getCurrentUser();
        return ObjectUtil.isNull(currentUser) ? null : currentUser.getUserId();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息，匿名登录时，为null
     */
    public static AdminUserDetails getCurrentUser() {
        Object userInfo = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (userInfo instanceof UserDetails) {
            return (AdminUserDetails) userInfo;
        }
        return null;
    }
}
