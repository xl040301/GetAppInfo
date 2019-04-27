package com.uniscope.getappinfo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.uniscope.getappinfo.dao.PackageData;
import com.uniscope.getappinfo.dao.PermissionsData;
import com.uniscope.getappinfo.dao.SharedUserIdData;
import com.uniscope.getappinfo.db.DbManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author：majun
 * date：2019/4/12 10:11
 * email:xl040301@126.com
 * description：
 */
public class XmlParserUtils {

    private DbManager dbManager;

    private List<PermissionsData> permissionsData;

    private List<PackageData> packageData;

    private List<SharedUserIdData> sharedUserIdData;


    public XmlParserUtils(Context context) {
        dbManager = DbManager.getInstance(context);
    }

    /**
     *
     * @param context
     * @param inputStream
     * @param encode
     * @return 0:error 1:success
     */
    public Integer parserPackageXml(Context context, InputStream inputStream, String encode) {
        Integer result = 0;
        try {
            boolean isPermissionData = false;

            boolean isPackageHasPerm = false;
            boolean isPackageData = false;
            String pkgName = "";
            String fileName = "";
            String filePath = "";
            String pkgSource = "";
            String pkgUserId = "";

            boolean isSharedUserIdData = false;
            String shareUserIdName = "";
            String sharedUserId = "";


            //获得一个XMLPULL工厂类的实例
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //获得一个XML解析器的实例
            XmlPullParser parser = factory.newPullParser();
            //设置解析器的输入，使用inputStream流式数据。
            parser.setInput(inputStream, encode);
            //判断当前的事件类型
            int eventType = parser.getEventType();

            int depth = parser.getDepth();
            while ((eventType != XmlPullParser.END_TAG || parser
                    .getDepth() > depth) && eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.w("majun", "START_DOCUMENT");
                        permissionsData = new ArrayList<>();
                        packageData = new ArrayList<>();
                        sharedUserIdData = new ArrayList<>();
                        break;

                    case XmlPullParser.START_TAG:
                        String itemName = parser.getName();
                       // Log.i("majun","START_TAG & name = "+itemName);
                        if (itemName.equals("permissions")) { //<permissions />
                            isPermissionData = true;
                        } else if (isPermissionData && itemName.equals("item")) {
                            PermissionsData permissions = new PermissionsData();
                            permissions.setPermissionName(parser.getAttributeValue(0));
                            permissions.setPackageName(parser.getAttributeValue(1));
                            int protection = 0;
                            if (parser.getAttributeCount() > 2) {
                                protection = Integer.parseInt(parser.getAttributeValue(2));
                            }
                            permissions.setProtection(getProtectionInfo(protection));
                            permissionsData.add(permissions);
                        } else if (itemName.equals("package")) {  //<package />
                            isPackageData = true;
                            pkgName = parser.getAttributeValue(0);
                            String pkgPath = parser.getAttributeValue(1);
                            fileName = pkgPath.substring(pkgPath.lastIndexOf("/")+1);
                            filePath = pkgPath.substring(0,pkgPath.length() - fileName.length() - 1);
                            fileName = fileName + ".apk";
                            pkgSource = getPackageSource(pkgName);
                            int attrCount = parser.getAttributeCount();
                            for (int i = attrCount - 1; i >= 8; i--) {
                                if (parser.getAttributeName(i).equals("sharedUserId")) {
                                    pkgUserId = parser.getAttributeValue(i);
                                    break;
                                }
                            }
                           // Log.e("majun", "pkgName =" + pkgName + " pkgPath =" + pkgPath
                            //        + " pkgUserId=" + pkgUserId);
                        } else if (isPackageData && itemName.equals("item")) {
                            isPackageHasPerm = true;
                            PackageData packages = new PackageData();
                            packages.setPackageName(pkgName);
                            packages.setCodePath(filePath);
                            packages.setFileName(fileName);
                            packages.setPackageSource(pkgSource);
                            packages.setSharedUserId(pkgUserId);
//                            Log.e("majun","pkgName="+pkgName
//                                    + " filePath="+filePath
//                                    + " fileName="+fileName
//                                    + " pkgSource="+pkgSource
//                                    + " pkgUserId="+pkgUserId
//                                    +" getPrem ="+parser.getAttributeValue(0));
                            packages.setPermission(parser.getAttributeValue(0));
                            packageData.add(packages);
                        } else if (itemName.equals("shared-user")) { //<shared-user />
                            isSharedUserIdData = true;
                            shareUserIdName = parser.getAttributeValue(0);
                            sharedUserId = parser.getAttributeValue(1);
                        } else if (isSharedUserIdData && itemName.equals("item")) {
                            SharedUserIdData sharedUserIds = new SharedUserIdData();
                            sharedUserIds.setSharedUserName(shareUserIdName);
                            sharedUserIds.setSharedUserId(sharedUserId);
                            sharedUserIds.setPermission(parser.getAttributeValue(0));
                            //Log.w("majun","shareUserIdName="+shareUserIdName+"sharedUserId="+sharedUserId +
                            // " sharePerm="+parser.getAttributeValue(0));
                            sharedUserIdData.add(sharedUserIds);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        //Log.d("majun","END_TAG & name = "+parser.getName());
                        if (parser.getName().equals("permissions")) {
                            isPermissionData = false;
                        } else if (parser.getName().equals("package")) {
                            if (!isPackageHasPerm) {
                                PackageData pkgNoPerm = new PackageData();
                                pkgNoPerm.setPackageName(pkgName);
                                pkgNoPerm.setCodePath(filePath);
                                pkgNoPerm.setFileName(fileName);
                                pkgNoPerm.setPackageSource(pkgSource);
                                pkgNoPerm.setSharedUserId(pkgUserId);
                                packageData.add(pkgNoPerm);
                            }
                            isPackageData = false;
                            isPackageHasPerm = false;
                            pkgName = "";
                            fileName = "";
                            filePath = "";
                            pkgSource = "";
                            pkgUserId = "";
                        } else if (parser.getName().equals("shared-user")) {
                            isSharedUserIdData = false;
                            shareUserIdName = "";
                            sharedUserId = "";
                        } else if (parser.getName().equals("packages")) {
                            setPkgInfoIntoDatabase();
                            result = 1;
                        }
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        Log.w("majun", "END_DOCUMENT");
                        break;


                    default:
                        break;
                }
                eventType = parser.next();
            }
            Log.d("majun", "parser is over and return true");
        } catch (XmlPullParserException e) {
            Log.e("majun", "XmlPullParserException & e=" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e1) {
            Log.e("majun", "IOException & e1=" + e1.getMessage());
            e1.printStackTrace();
        }
        return result;
    }

    private void setPkgInfoIntoDatabase() {
        if (permissionsData != null
                && permissionsData.size() > 0) {
            int size = permissionsData.size();
            Log.i("majun", "permissionsData & size = " + size);
            dbManager.beginTransaction();
            try {
                for (int i = 0;i < size;i++) {
                    dbManager.insertPermInfo(permissionsData.get(i));
                }
                dbManager.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbManager.endTransaction();
            }
        }

        if (packageData != null
                && packageData.size() > 0) {
            int size = packageData.size();
            Log.i("majun", "packageData & size = " + size);
            dbManager.beginTransaction();
            try {
                for (int i = 0;i < size;i++) {
                    dbManager.insertPkgInfo(packageData.get(i));
                }
                dbManager.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbManager.endTransaction();
            }
        }

        if (sharedUserIdData != null
                && sharedUserIdData.size() > 0) {
            int size = sharedUserIdData.size();
            Log.i("majun", "sharedUserIdData & size = " + size);
            dbManager.beginTransaction();
            try {
                for (int i = 0;i < size;i++) {
                    dbManager.insertShareduserInfo(sharedUserIdData.get(i));
                }
                dbManager.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbManager.endTransaction();
            }
        }

        //创建联合视图
        try {
            dbManager.beginTransaction();
            dbManager.createPackageInfoView();
            dbManager.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.endTransaction();
        }

        dbManager.closeDatabase();
    }


    public String getProtectionInfo(int protection) {
        String protectionInfo = "";
        switch (protection) {
            case 0:
                protectionInfo = "normal";
                break;
            case 1:
                protectionInfo = "dangerous";
                break;

            case 2:
                protectionInfo = "signature";
                break;

            case 3:
                protectionInfo = "signatureOrSystem";
                break;

            case 18:
                protectionInfo = "signature|privileged";
                break;

            case 50:
                protectionInfo = "signature|privileged|development";
                break;

            case 258:
                protectionInfo = "signature|installer";
                break;

            case 1026:
                protectionInfo = "signature|preinstalled";
                break;

            case 1250:
                protectionInfo = "ignature|preinstalled|appop|pre23|development";
                break;

            case 2050:
                protectionInfo = "signature|setup";
                break;

            case 4096:
                protectionInfo = "normal|instant";
                break;

            case 4097:
                protectionInfo = "dangerous|instant";
                break;

            case 4194:
                protectionInfo = "signature|development|instant|appop";
                break;

            case 8193:
                protectionInfo = "dangerous|runtime";
                break;

            default:
                protectionInfo = "normal";
                break;
        }
        return protectionInfo;
    }

    public String getPackageSource(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return "Unknown";
        }
        if (packageName.indexOf("com.android") != -1) {
            return "AOSP-unmodified";
        } else if (packageName.indexOf("google") != -1) {
            return "Google";
        } else if (packageName.indexOf("qualcomm") != -1) {
            return "OEM-sourced";
        } else if (packageName.equals("org.codeaurora.gallery")) {
            return "OEM-modified AOSP";
        } else {
            return "3rd party";
        }
    }


}
