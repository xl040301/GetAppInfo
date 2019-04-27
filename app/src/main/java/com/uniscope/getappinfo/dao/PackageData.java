package com.uniscope.getappinfo.dao;

/**
 * author：majun
 * date：2019/4/23 18:41
 * email:xl040301@126.com
 * description：
 */
public class PackageData {
    private String packageName;
    private String fileName;
    private String codePath;
    private String packageSource;
    private String sharedUserId;
    private String permission;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackageSource() {
        return packageSource;
    }

    public void setPackageSource(String packageSource) {
        this.packageSource = packageSource;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(String sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
