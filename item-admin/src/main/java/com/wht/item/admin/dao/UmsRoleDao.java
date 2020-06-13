package com.wht.item.admin.dao;

import com.wht.item.model.UmsMenu;
import com.wht.item.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 后台用户角色自定义Dao
 *
 * @author wht
 * @since 2020-05-19 4:36
 */
public interface UmsRoleDao {
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);

    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);
}
