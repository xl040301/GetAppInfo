package com.uniscope.getappinfo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * author：majun
 * date：2019/4/23 15:03
 * email:xl040301@126.com
 * description：
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "appinfo.db";//数据库名称
    private static final int VERSION = 2;//数据库版本

    //存放权限，包含系统权限与用户自定义权限
    public class PERMISSIONS_TABLE {
        public final static String TABLE_NAME = "permissions_table";
        public final static String PERM_NAME = "perm_name";
        //权限所在包名，package="android"表示系统权限，其他则表示用户自定义权限
        public final static String PKG_NAME= "pkg_name";
        //用户权限级别
        //protection = 1 :dangerous
        //protection = 2 :signature
        //protection = 3 :signatureOrSystem
        //protection = 18 :signature|privileged
        //protection = 50 :signature|privileged|development
        //protection = 258 :signature|installer
        //protection = 1026 :signature|preinstalled
        //protection = 1250 :ignature|preinstalled|appop|pre23|development
        //protection = 2050 :signature|setup
        //protection = 4096 :normal|instant
        //protection = 4097 :dangerous|instant
        //protection = 4194 :signature|development|instant|appop
        //protection = 8193 :dangerous|runtime
        public final static String PROTECTION= "protection";
    }

    public String CREATE_PERMISSIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + PERMISSIONS_TABLE.TABLE_NAME
            + " (" + "_id integer primary key autoincrement,"
            + PERMISSIONS_TABLE.PERM_NAME + " TEXT,"
            + PERMISSIONS_TABLE.PKG_NAME + " TEXT,"
            + PERMISSIONS_TABLE.PROTECTION + " TEXT)";

    public class PACKAGE_TABLE {
        public final static String TABLE_NAME = "packages_table";
        public final static String PKG_NAME= "pkg_name";
        public final static String CODEPATH = "code_path";
        public final static String FILE_NAME = "file_name";
        public final static String PKG_SOURCE = "package_source";
        public final static String PERM_NAME = "perm_name";
        public final static String SHARED_USERID= "sharedUserId";
    }

    public String CREATE_PACKAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + PACKAGE_TABLE.TABLE_NAME
            + " (" + "_id integer primary key autoincrement,"
            + PACKAGE_TABLE.PKG_NAME + " TEXT,"
            + PACKAGE_TABLE.CODEPATH + " TEXT,"
            + PACKAGE_TABLE.FILE_NAME + " TEXT,"
            + PACKAGE_TABLE.PKG_SOURCE + " TEXT,"
            + PACKAGE_TABLE.PERM_NAME + " TEXT,"
            + PACKAGE_TABLE.SHARED_USERID + " TEXT)";


    public class SHARED_USER_TABLE {
        public final static String TABLE_NAME = "shared_user_table";
        public final static String SHARED_USER_NAME= "shared_user_name";
        public final static String SHARED_USERID= "userId";
        public final static String PERM_NAME = "perm_name";
    }

    public String CREATE_SHARED_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + SHARED_USER_TABLE.TABLE_NAME
            + " (" + "_id integer primary key autoincrement,"
            + SHARED_USER_TABLE.SHARED_USER_NAME + " TEXT,"
            + SHARED_USER_TABLE.SHARED_USERID + " TEXT,"
            + SHARED_USER_TABLE.PERM_NAME + " TEXT)";



    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("majun","DBHelper & onCreate");
        db.execSQL(CREATE_PERMISSIONS_TABLE);
        db.execSQL(CREATE_PACKAGES_TABLE);
        db.execSQL(CREATE_SHARED_USER_TABLE);
    }

    //升级更新数据库版本时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PERMISSIONS_TABLE.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PACKAGE_TABLE.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SHARED_USER_TABLE.TABLE_NAME);
        onCreate(db);
    }
}
