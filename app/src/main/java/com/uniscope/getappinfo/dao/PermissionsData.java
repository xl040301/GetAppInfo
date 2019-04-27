package com.uniscope.getappinfo.dao;

/**
 * author：majun
 * date：2019/4/23 18:38
 * email:xl040301@126.com
 * description：
 */
public class PermissionsData {
    private String permissionName;
    private String packageName;
    private String protection;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection;
    }
}
