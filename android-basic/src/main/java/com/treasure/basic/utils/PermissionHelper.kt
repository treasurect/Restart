package com.treasure.basic.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.treasure.basic.ContextHolder
import com.treasure.basic.R
import com.treasure.basic.helper.CommonCallback
import com.treasure.basic.ui.dialog.DialogExt
import com.treasure.basic.utils.ScreenUtils.getScreenWidth
import com.treasure.basic.utils.ToastUtils.show
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object PermissionHelper {

    /**
     * 检查普通权限是否已授权
     */
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasOpenAlarmPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+（API 33）不需要 READ_EXTERNAL_STORAGE
            return hasPermissions(context, Manifest.permission.READ_MEDIA_IMAGES);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6 ~ 12
            return hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return true;
    }

    /**
     * 发起普通权限申请
     */
    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        onGranted: () -> Unit,
    ) {
        val deniedList = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedList.isEmpty()) {
            onGranted()
        } else {
            ActivityCompat.requestPermissions(activity, deniedList.toTypedArray(), 1001)
        }
    }

    /**
     * 处理权限回调（用于 BaseActivity 中）
     */
    fun onRequestPermissionsResult(
        context: Context,
        permissions: Array<out String>,
        grantResults: IntArray,
        onGranted: () -> Unit,
        onDenied: ((List<String>) -> Unit)? = null
    ) {
        val deniedList = permissions.zip(grantResults.toTypedArray())
            .filter { it.second != PackageManager.PERMISSION_GRANTED }.map { it.first }
        if (deniedList.isEmpty()) onGranted()
        else onDenied?.let { onDenied(deniedList) } ?: showPermissionDeniedDialog(context)
    }

    /**
     * Android 13+ 通知权限
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    fun requestNotificationPermission(activity: Activity, onGranted: () -> Unit) {
        if (hasNotificationPermission(activity)) {
            onGranted()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1002
            )
        }
    }

    /**
     * Android 12+ 蓝牙权限
     */
    fun getBluetoothPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(Manifest.permission.BLUETOOTH)
        }
    }

    /**
     * 悬浮窗权限判断
     */
    fun hasOverlayPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else true
    }

    fun requestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    /**
     * 跳转 App 系统权限设置页面
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun showPermissionDeniedDialog(
        context: Context,
        message: String = "权限被拒绝，部分功能可能无法使用。\n请前往设置页面手动开启权限。",
        onConfirm: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(context).setTitle("权限申请").setMessage(message).setCancelable(false)
            .setPositiveButton("去设置") { dialog, _ ->
                openAppSettings(context)
                dialog.dismiss()
                onConfirm?.invoke()
            }.setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun hasPermission(activity: Activity, permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            activity, permission
        ) == PackageManager.PERMISSION_GRANTED

    fun checkCameraStoragePermission(activity: Activity?, cb: CommonCallback<Boolean?>) {
        if (activity == null) return
        if (hasPermission(activity, Manifest.permission.CAMERA) && hasPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            cb.onContinue(true)
        } else {
            showReqPermissionDialog(
                activity,
                "相机、存储",
                "用于拍摄照片和存储图片",
                object : Success {
                    @SuppressLint("CheckResult")
                    override fun getOk() {
                        val mRxPermissions = RxPermissions(activity)
                        mRxPermissions.request(
                            *arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        ).subscribe { granted: Boolean ->
                            if (granted) { // Always true pre-M
                                cb.onContinue(true)
                            } else {
                                //判断权限是否被允许
                                jumpPermissionPageResult(activity,
                                    "相机、存储",
                                    "用于拍摄照片和存储图片",
                                    object : Success {
                                        override fun doCancel() {
                                            cb.onContinue(false)
                                        }

                                        override fun getOk() {
                                            cb.onContinue(false)
                                        }
                                    })
                            }
                        }
                    }

                    override fun doCancel() {
                        cb.onContinue(false)
                    }
                })
        }
    }

    fun checkAlarmPermission(context: Activity, cb: CommonCallback<Boolean>) {
        if (hasOpenAlarmPermissions(context)) {
            cb.onContinue(true)
        } else {
            val rxPermissions = RxPermissions(context)
            showReqPermissionDialog(context, "存储", "用于存储图片", object : Fail {
                @SuppressLint("CheckResult")
                override fun getOk() {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    rxPermissions.request(*arrayOf<String>(permission))
                        .subscribe { granted: Boolean ->
                            if (granted) { // Always true pre-M
                                cb.onContinue(true)
                            } else {
                                jumpPermissionPage(context, "存储", "用于存储图片")
                            }
                        }
                }

                override fun fail() {
                }
            })
        }
    }



    fun checkCameraPermission(activity: Activity, cb: CommonCallback<Boolean>) {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (hasPermissions(activity, *permissions)) {
            cb.onContinue(true)
        } else {
            showReqPermissionDialog(activity, "相机", "用于拍摄照片", object : Fail {
                @SuppressLint("CheckResult")
                override fun getOk() {
                    val rxPermissions = RxPermissions(activity)
                    rxPermissions.request(*permissions).subscribe { granted: Boolean ->
                        if (granted) {
                            cb.onContinue(true)
                        } else {
                            jumpPermissionPage(activity, "相机", "用于拍摄照片")
                        }
                    }
                }

                override fun fail() {
                    jumpPermissionPage(activity, "相机", "用于拍摄照片")
                }
            })
        }
    }

    fun checkSinglePermission(
        activity: Activity?,
        permission: String,
        errorTitle: String,
        errorMessage: String,
        cb: CommonCallback<Boolean?>
    ) {
        if (activity == null) return
        if (ContextCompat.checkSelfPermission(
                activity, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cb.onContinue(true)
        } else {
            showReqPermissionDialog(activity, errorTitle, errorMessage, object : Success {
                @SuppressLint("CheckResult")
                override fun getOk() {
                    val rxPermissions = RxPermissions(activity)
                    rxPermissions.request(*arrayOf(permission)).subscribe { granted: Boolean ->
                        if (granted) { // Always true pre-M
                            cb.onContinue(true)
                        } else {
                            //判断权限是否被允许
                            showReqPermissionDialog(
                                activity, errorTitle, errorMessage
                            )
                        }
                    }
                }
            })
        }
    }

    fun goLGMainager(mContext: Activity) {
        try {
            val intent = Intent(ContextHolder.app().packageName)
            val comp = ComponentName(
                "com.android.settings", "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            intent.setComponent(comp)
            mContext.startActivity(intent)
        } catch (e: Exception) {
            show("跳转失败")
            e.printStackTrace()
            goIntentSetting(mContext)
        }
    }

    fun goSonyMainager(mContext: Activity) {
        try {
            val intent = Intent(ContextHolder.app().packageName)
            val comp = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.setComponent(comp)
            mContext.startActivity(intent)
        } catch (e: Exception) {
            show("跳转失败")
            e.printStackTrace()
            goIntentSetting(mContext)
        }
    }

    fun goHuaWeiMainager(mContext: Activity) {
        try {
            val intent = Intent(ContextHolder.app().packageName)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val comp = ComponentName(
                "com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"
            )
            intent.setComponent(comp)
            mContext.startActivity(intent)
        } catch (e: Exception) {
            show("跳转失败")
            e.printStackTrace()
            goIntentSetting(mContext)
        }
    }

    fun getMiuiVersion(): String? {
        val propName = "ro.miui.ui.version.name"
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(
                InputStreamReader(p.inputStream), 1024
            )
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } finally {
            try {
                input!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return line
    }

    fun goXiaoMiMainager(mContext: Activity) {
        val rom = getMiuiVersion()
        val intent = Intent()
        if ("V6" == rom || "V7" == rom) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR")
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", ContextHolder.app().packageName)
        } else if ("V8" == rom || "V9" == rom) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR")
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", ContextHolder.app().packageName)
        } else {
            goIntentSetting(mContext)
        }
        mContext.startActivity(intent)
    }

    fun goMeizuMainager(mContext: Activity) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", ContextHolder.app().packageName)
            mContext.startActivity(intent)
        } catch (localActivityNotFoundException: ActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace()
            goIntentSetting(mContext)
        }
    }

    fun goSangXinMainager(mContext: Activity) {
        //三星4.3可以直接跳转
        goIntentSetting(mContext)
    }

    fun goIntentSetting(mContext: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext.packageName, null)
        intent.setData(uri)
        try {
            mContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goOppoMainager(mContext: Activity) {
        doStartApplicationWithPackageName("com.coloros.safecenter", mContext)
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    fun goCoolpadMainager(mContext: Activity) {
        doStartApplicationWithPackageName("com.yulong.android.security:remote", mContext)/*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    fun goVivoMainager(mContext: Activity) {
        doStartApplicationWithPackageName("com.bairenkeji.icaller", mContext)/*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    /**
     * 此方法在手机各个机型设置中已经失效
     *
     * @return
     */
    private fun getAppDetailSettingIntent(mContext: Activity): Intent {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
            localIntent.setData(Uri.fromParts("package", mContext.packageName, null))
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW)
            localIntent.setClassName(
                "com.android.settings", "com.android.settings.InstalledAppDetails"
            )
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.packageName)
        }
        return localIntent
    }


    fun jumpPermissionPageResult(activity: Activity, text: String, msg: String, cb: Success) {
        val inflate: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_permission_show, null, false)
        val dia_text_msg = inflate.findViewById<TextView>(R.id.dia_text_msg)
        dia_text_msg.text =
            "是否允许【" + activity.getString(R.string.app_name) + "】获取" + text + "权限？" + msg + "，您可在系统设置中开启"
        val dia_cancle = inflate.findViewById<Button>(R.id.dia_cancle)
        dia_cancle.text = "禁止"
        val dia_confirm = inflate.findViewById<Button>(R.id.dia_confirm)
        dia_confirm.text = "允许"
        val builder = android.app.AlertDialog.Builder(activity)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.setView(inflate)
        alertDialog.show()
        alertDialog.window!!.setLayout(
            (getScreenWidth(activity) * 0.7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT
        )
        DoubleClickUtils.withSafeClick(dia_cancle) { v: View? ->
            alertDialog.dismiss()
            cb.doCancel()
        }
        DoubleClickUtils.withSafeClick(dia_confirm) { v: View? ->
            alertDialog.dismiss()
            cb.getOk()
            val packageURI = Uri.parse("package:" + activity.packageName)
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
            activity.startActivity(intent)
        }
    }


    fun showReqPermissionDialog(activity: Activity, text: String, msg: String) {
        val inflate: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_permission_show, null, false)
        val dia_text_msg = inflate.findViewById<TextView>(R.id.dia_text_msg)
        dia_text_msg.text =
            "是否允许【" + activity.getString(R.string.app_name) + "】获取" + text + "权限？" + msg + "，您可在系统设置中开启"
        val dia_cancle = inflate.findViewById<Button>(R.id.dia_cancle)
        dia_cancle.text = "禁止"
        val dia_confirm = inflate.findViewById<Button>(R.id.dia_confirm)
        dia_confirm.text = "允许"
        val builder = android.app.AlertDialog.Builder(activity)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window!!.setBackgroundDrawableResource(com.treasure.basic.R.color.transparent)
        alertDialog.setView(inflate)
        alertDialog.show()
        alertDialog.window!!.setLayout(
            (getScreenWidth(activity) * 0.7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT
        )
        DoubleClickUtils.withSafeClick(dia_cancle) { v: View? ->
            alertDialog.dismiss()
        }
        DoubleClickUtils.withSafeClick(dia_confirm) { v: View? ->
            alertDialog.dismiss()
            //            String name = Build.MANUFACTURER;
//            switch (name) {
//                case "HUAWEI":
//                    goHuaWeiMainager(activity);
//                    break;
//                case "vivo":
//                    goVivoMainager(activity);
//                    break;
//                case "OPPO":
//                    goOppoMainager(activity);
//                    break;
//                case "Coolpad":
//                    goCoolpadMainager(activity);
//                    break;
//                case "Meizu":
//                    goMeizuMainager(activity);
//                    break;
//                case "Xiaomi":
//                    goXiaoMiMainager(activity);
//                    break;
//                case "samsung":
//                    goSangXinMainager(activity);
//                    break;
//                case "Sony":
//                    goSonyMainager(activity);
//                    break;
//                case "LG":
//                    goLGMainager(activity);
//                    break;
//                default:
//                    goIntentSetting(activity);
//                    break;
            val packageURI = Uri.parse("package:" + activity.packageName)
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
            activity.startActivity(intent)
        }
    }

    interface Success {
        fun getOk() {
        }

        fun doCancel() {
        }
    }

    var mSuccess: Success? = null

    interface Fail {
        fun getOk() {
        }

        fun fail() {

        }
    }

    var mFail: Fail? = null
    fun showReqPermissionDialog(activity: Activity, text: String, msg: String, success: Fail) {
        if (activity.isFinishing || activity.isDestroyed) return
        val inflate: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_permission_show, null, false)
        val dia_text_msg = inflate.findViewById<TextView>(R.id.dia_text_msg)
        dia_text_msg.text =
            "是否允许【" + activity.getString(R.string.app_name) + "】获取" + text + "权限？" + msg
        val dia_cancle = inflate.findViewById<Button>(R.id.dia_cancle)
        dia_cancle.text = "禁止"
        val dia_confirm = inflate.findViewById<Button>(R.id.dia_confirm)
        dia_confirm.text = "允许"
        val builder = android.app.AlertDialog.Builder(activity)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.setView(inflate)
        alertDialog.show()
        alertDialog.window!!.setLayout(
            (getScreenWidth(activity) * 0.7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dia_cancle.setOnClickListener {
            activity.runOnUiThread { // 在这里执行 Fragment 管理器的操作
                alertDialog.dismiss()
                success.fail()
            }
        }
        dia_confirm.setOnClickListener {
            activity.runOnUiThread { // 在这里执行 Fragment 管理器的操作
                alertDialog.dismiss()
                success.getOk()
            }
        }
    }

    fun jumpPermissionPage(activity: Activity, text: String, msg: String) {
        DialogExt.showCommonDialog(
            activity,
            content = "请前往系统设置中允许应用访问$text",
            title = "\"${activity.getString(R.string.app_name)}\"无法访问你的$text",
            leftBtnText = activity.getString(R.string.cancel),
            rightBtnText = activity.getString(R.string.go_setting),
            rightCallback = {
                val packageURI = Uri.parse("package:" + activity.packageName)
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                activity.startActivity(intent)
                if (activity.isFinishing.not() && activity.isDestroyed.not()){
                    activity.finish()
                }
            }
        )
    }

    fun showReqPermissionDialog(activity: Activity, text: String, msg: String, success: Success) {
        val inflate: View = LayoutInflater.from(activity)
            .inflate(com.treasure.basic.R.layout.dialog_permission_show, null, false)
        val dia_text_msg = inflate.findViewById<TextView>(com.treasure.basic.R.id.dia_text_msg)
        dia_text_msg.text =
            "是否允许【" + activity.getString(R.string.app_name) + "】获取" + text + "权限？" + msg
        val dia_cancle = inflate.findViewById<Button>(R.id.dia_cancle)
        dia_cancle.text = "禁止"
        val dia_confirm = inflate.findViewById<Button>(R.id.dia_confirm)
        dia_confirm.text = "允许"
        val builder = android.app.AlertDialog.Builder(activity)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.setView(inflate)
        alertDialog.show()
        alertDialog.window!!.setLayout(
            (getScreenWidth(activity) * 0.7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dia_cancle.setOnClickListener {
            alertDialog.dismiss()
            success.doCancel()
        }
        dia_confirm.setOnClickListener {
            activity.runOnUiThread { // 在这里执行 Fragment 管理器的操作
                alertDialog.dismiss()
                success.getOk()
            }
        }

//        DoubleClickUtils.withSafeClick(dia_confirm, () -> {
//            alertDialog.dismiss();
////            String name = Build.MANUFACTURER;
////            switch (name) {
////                case "HUAWEI":
////                    goHuaWeiMainager(activity);
////                    break;
////                case "vivo":
////                    goVivoMainager(activity);
////                    break;
////                case "OPPO":
////                    goOppoMainager(activity);
////                    break;
////                case "Coolpad":
////                    goCoolpadMainager(activity);
////                    break;
////                case "Meizu":
////                    goMeizuMainager(activity);
////                    break;
////                case "Xiaomi":
////                    goXiaoMiMainager(activity);
////                    break;
////                case "samsung":
////                    goSangXinMainager(activity);
////                    break;
////                case "Sony":
////                    goSonyMainager(activity);
////                    break;
////                case "LG":
////                    goLGMainager(activity);
////                    break;
////                default:
////                    goIntentSetting(activity);
////                    break;
//
//
//
//        });
    }

    fun doStartApplicationWithPackageName(packagename: String?, mContext: Activity) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = mContext.packageManager.getPackageInfo(packagename!!, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageinfo == null) {
            return
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)
        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList = mContext.packageManager.queryIntentActivities(resolveIntent, 0)
        Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size)
        for (i in resolveinfoList.indices) {
            Log.e(
                "PermissionPageManager",
                resolveinfoList[i].activityInfo.packageName + resolveinfoList[i].activityInfo.name
            )
        }
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            val cn = ComponentName(packageName, className)
            intent.setComponent(cn)
            try {
                mContext.startActivity(intent)
            } catch (e: Exception) {
                goIntentSetting(mContext)
                e.printStackTrace()
            }
        }
    }

    fun checkLocationPermission(activity: Activity?, cb: CommonCallback<Boolean?>) {
        if (activity == null) return
        if (hasPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            cb.onContinue(true)
        } else {
            showReqPermissionDialog(activity, "定位", "用于获取当前位置", object : Success {
                @SuppressLint("CheckResult")
                override fun getOk() {
                    val mRxPermissions = RxPermissions(activity)
                    mRxPermissions.request(
                        *arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    ).subscribe { granted: Boolean ->
                        if (granted) { // Always true pre-M
                            cb.onContinue(true)
                        } else {
                            //判断权限是否被允许
                            jumpPermissionPageResult(
                                activity,
                                "定位",
                                "用于获取当前位置",
                                object : Success {
                                    override fun doCancel() {
                                        cb.onContinue(false)
                                    }

                                    override fun getOk() {
                                        cb.onContinue(false)
                                    }
                                })
                        }
                    }
                }

                override fun doCancel() {
                    cb.onContinue(false)
                }
            })
        }
    }

}

