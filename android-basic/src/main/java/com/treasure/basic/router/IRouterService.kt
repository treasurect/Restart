package com.treasure.basic.router

import android.content.Context
import android.os.Bundle

interface IRouterService {
    fun startMainActivity(context: Context, bundle: Bundle? = null)

    fun startSplashActivity(context: Context, bundle: Bundle? = null)

    fun startLoginActivity(context: Context, bundle: Bundle? = null){
    }

    fun startOCRPreActivity(context: Context, bundle: Bundle? = null){
    }

    fun startOCRActivity(context: Context, bundle: Bundle? = null){
    }

    fun startOCRAgreementActivity(context: Context, bundle: Bundle? = null){
    }

    fun startEntrustActivity(context: Context, bundle: Bundle? = null){
    }
    fun startInterestChooseActivity(context: Context, bundle: Bundle? = null){
    }
    fun startPayActivity(context: Context, bundle: Bundle? = null){
    }

    fun startPublicPayResultActivity(context: Context, title: String, isSuccess: Boolean) {
    }

    fun startNextArrivedActivity(context: Context,bundle: Bundle? =null) {
    }
}