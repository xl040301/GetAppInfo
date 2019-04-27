package com.uniscope.getappinfo.dao;

/**
 * author：majun
 * date：2019/4/23 18:44
 * email:xl040301@126.com
 * description：
 */
public class SharedUserIdData {

    private String sharedUserName;
    private String sharedUserId;
    private String permission;

    public String getSharedUserName() {
        return sharedUserName;
    }

    public void setSharedUserName(String sharedUserName) {
        this.sharedUserName = sharedUserName;
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
