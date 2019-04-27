package com.uniscope.getappinfo.db;

import android.content.Context;

import com.uniscope.getappinfo.dao.PackageData;
import com.uniscope.getappinfo.dao.PermissionsData;
import com.uniscope.getappinfo.dao.SharedUserIdData;

/**
 * author：majun
 * date：2019/4/23 16:20
 * email:xl040301@126.com
 * description：
 */
public class DbManager {

    private static DbManager dbManager;
    private DBHelper dbHelper;

    private DbManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static  DbManager getInstance(Context context){
        if (dbManager == null) {
            synchronized (DbManager.class) {
                if (dbManager == null) {
                    dbManager = new DbManager(context.getApplicationContext());
                }
            }
        }
        return dbManager;
    }

    public void dropTable(String taleName) {
        dbHelper.getWritableDatabase().execSQL(
                "DROP TABLE IF EXISTS " + taleName);

    }

    public void closeDatabase() {
        dbHelper.getWritableDatabase().close();
    }

    public void createPermTable() {
        dbHelper.getWritableDatabase().execSQL(dbHelper.CREATE_PERMISSIONS_TABLE);
    }

    public void createPkgTable() {
        dbHelper.getWritableDatabase().execSQL(dbHelper.CREATE_PACKAGES_TABLE);
    }

    public void createSharduserTable() {
        dbHelper.getWritableDatabase().execSQL(dbHelper.CREATE_SHARED_USER_TABLE);
    }

    public void beginTransaction() {
        dbHelper.getWritableDatabase().beginTransaction();
    }

    public void setTransactionSuccessful() {
        dbHelper.getWritableDatabase().setTransactionSuccessful();
    }

    public void endTransaction() {
        dbHelper.getWritableDatabase().endTransaction();
    }




    public void insertPermInfo(PermissionsData permissions) {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into permissions_table ")
                .append("(perm_name,pkg_name,protection) ")
                .append("values(")
                .append("'").append(permissions.getPermissionName()).append("'").append(",")
                .append("'").append(permissions.getPackageName()).append("'").append(",")
                .append("'").append(permissions.getProtection()).append("'")
                .append(")");
        dbHelper.getWritableDatabase().execSQL(builder.toString());
    }

    public void insertPkgInfo(PackageData packages) {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into packages_table ")
                .append("(pkg_name,code_path,file_name,package_source,perm_name,sharedUserId) ")
                .append("values(")
                .append("'").append(packages.getPackageName()).append("'").append(",")
                .append("'").append(packages.getCodePath()).append("'").append(",")
                .append("'").append(packages.getFileName()).append("'").append(",")
                .append("'").append(packages.getPackageSource()).append("'").append(",")
                .append("'").append(packages.getPermission()).append("'").append(",")
                .append("'").append(packages.getSharedUserId()).append("'")
                .append(")");
        //Log.e("majun","insertPkgInfo & SQL = "+builder.toString());
        dbHelper.getWritableDatabase().execSQL(builder.toString());
    }

    public void insertShareduserInfo(SharedUserIdData sharedUserIds) {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into shared_user_table ")
                .append("(shared_user_name,userId,perm_name) ")
                .append("values(")
                .append("'").append(sharedUserIds.getSharedUserName()).append("'").append(",")
                .append("'").append(sharedUserIds.getSharedUserId()).append("'").append(",")
                .append("'").append(sharedUserIds.getPermission()).append("'")
                .append(")");
        //Log.i("majun","insertShareduserInfo & SQL = "+builder.toString());
        dbHelper.getWritableDatabase().execSQL(builder.toString());
    }

    /**
     * packagegs表同sharedUserId与permission表数据集合查询
     */
    public void createPackageInfoView() {
        dbHelper.getWritableDatabase().execSQL("CREATE VIEW pkg_perm_userid as "
                + "SELECT distinct pkg.[_id],pkg.[pkg_name],pkg.[code_path],"
                + "pkg.[file_name],pkg.[perm_name],perm.[protection],"
                + "perm.[pkg_name] AS permsource,sharedId.[shared_user_name],"
                + "pkg.[package_source]"
                + " FROM packages_table pkg"
                + " LEFT OUTER JOIN permissions_table perm"
                + " ON pkg.[perm_name]=perm.[perm_name]"
                + " LEFT OUTER JOIN shared_user_table sharedId"
                + " ON pkg.[sharedUserId]=sharedId.[userId]"
                + " group by pkg.[_id]");
    }

}
