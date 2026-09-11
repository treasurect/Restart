package com.treasure.basic.constant;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import com.treasure.basic.AppEnvironment;
import com.treasure.basic.ContextHolder;
import com.treasure.basic.EnvManager;

import java.util.HashMap;

/**
 * date:2021/6/17
 * author:李超(licha)
 * function:
 */
public class ConstantUtil {

    public static String LOGIN_TOKEN = "login_token";
    //用户头像
    public static String USER_IMG = "userimg";
    //用户账号
    public static String USER_ADMIN = "user_admin";
    public static final String USER_ADDR = "user_address";
    //用户真实姓名
    public static String USER_REALNAME = "user_realname";
    //用户手机号
    public static String USER_PHONE = "user_phone";
    //用户邮箱
    public static String USER_EMAIL = "user_email";
    //租户所在地址
    public static String USER_CITY = "user_city";
    //租户名称
    public static String USER_FIRM = "user_firm";
    //租户昵称
    public static String USER_NAME = "user_name";
    //所在部门
    public static String USER_DEPT = "user_dept";
    //用户性别
    public static String USER_SEX = "user_sex";

    public static String USER_DEPTS = "user_depts";
    public static String ID = "id";
    public static String customer = "";
    public static String vcardType = "vcardType";
    //所在职位
    public static String USER_POST = "user_post";
    //是否是管理员
    public static String USER_ISADMIN = "user_isadmin";
    //是否已经认证
    public static String USER_ISCERT = "user_iscert";
    //用户im账号
    public static String USER_IMADMIN = "user_imadmin";
    //用户解散用的imid
    public static String USER_IMID = "user_imid";
    //公司logo
    public static String USER_FIRMIMG = "user_firmimg";
    //公司简称
    public static String USER_SHORTFIRM = "user_shortfirm";
    //企业服务介绍
    public static String USER_COMPANYSERVER = "user_companyserver";
    //企业id
    public static String USER_COMPANYID = "user_companyid";
    //租户类型  1-个人 2-政府 3-企业 4-园企 5-机构 6 -- 国央企
    public static String USER_TENANTTYPE = "user_tenanttype";
    //是否是vip
    public static String USER_ISVIP = "user_isvip";
    //会员状态 1 非会员 2会员期间 3会员过期
    public static String USER_VIP_STATUS = "user_vip_status";
    //是否展示蒙层
    public static String USER_MANTLA = "user_mantla";
    public static String USER_OPEN_RECOMMEND = "user_open_recommend";
    public static String WXORDERID = "";
    public static String PRIVACY = "privacy";
    public static String MOBILE = "mobile";
    public static String NICE_NAME = "nick_name";

    public static String TENANT_ID = "tenant_id";
    public static String ACCESS_TOKEN = "access_token";

    public static String USER_ID = "user_id";
    public static String TEMP_USER_ID = "temp_user_id";
    public static String CHAT_NOTIY = "chat_notiy";
    public static String FIRST_SHOW = "first_show";
    public static String AUTHENTIDBYENID = "AuthEntIdByEntId";
    public static String ENTID = "entId";
    public static String ACCOUTN = "account";


    public static String NOWTIME = "nowtime";
    public static String USERINFOISCHANGE = "userinfoischange";
    public static String USERINFO = "userinfo";

    public static String licenseID = "LingxiCloudChain-face-android";
    public static String licenseFileName = "idl-license.face-android";

    public static String isX5load = "isX5load";
    public static HashMap<String, String> grouptype = new HashMap<>();

    public static String FirstOpen = "FirstOpen";
    public static String VersionCode = "VersionCode";
    public static HashMap<String, String> pagehash = new HashMap<String, String>();

    static {
        pagehash.put("com.lingxi.chain.ui.activity.HomeActivity", "home_page");
    }

    public static String PAY_GONE = "政府套餐";
    public static String PAY_STAFF = "";
    public static String OAID = "";
    public static String LAST_SHOW_NOTIFICATION_DIALOG = "LAST_SHOW_NOTIFICATION_DIALOG";


    public static String getMediaType() {
//        switch (getChannel()) {
//            case "hw":
//                return "13";
//            case "xiaomi":
//                return "14";
//            case "oppo":
//                return "16";
//            case "vivo":
//                return "15";
//            case "tx":
//                return "5";
//            case "dy":
//                return "7";
//            case "chaolian":
//                return "11";
//
//        }
        return "11";
    }

    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER; // 设备制造商
        String model = Build.MODEL; // 设备型号
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * 判断是否为平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getChannel() {
        ApplicationInfo applicationInfo = null;
        try {
            //getPackageManager():返回PackageManager实例以查找全局包信息。
            //getApplicationInfo(String packageName, int flags)：
            // 检索我们所了解的有关特定软件包/应用程序的所有信息。
            //参数一：  getPackageName()：返回此应用程序包的名称
            //参数二：PackageManager.GET_META_DATA
            // ComponentInfo标志：返回与组件关联的metaData数据Bundle 。 这适用于返回ComponentInfo子类的任何API。
            applicationInfo = ContextHolder.INSTANCE.app().getPackageManager().getApplicationInfo(ContextHolder.INSTANCE.app().getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
                //metaData属性在其ApplicationInfo类的父类PackageItemInfo里面
                //metaData:与此组件关联的其他元数据。
                //BaseBundle.getString():返回与给定键相关联的值
                return applicationInfo.metaData.getString("UMENG_CHANNEL");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 正式版:0，测试版(开发版):1，体验版:2
     *
     * @return
     */
    public static int wx() {
        if (TextUtils.equals(EnvManager.INSTANCE.getEnvName(), AppEnvironment.DEV.name())) {
            return 1;
        } else if (TextUtils.equals(EnvManager.INSTANCE.getEnvName(), AppEnvironment.TEST.name())) {
            return 2;
        } else /*if (TextUtils.equals(ENV,ENV_TYPE.PROD.value))*/ {
            return 0;
        }
    }


}
