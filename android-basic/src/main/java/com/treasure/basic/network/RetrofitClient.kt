package com.treasure.basic.network

import android.util.Base64
import com.google.gson.GsonBuilder
import com.treasure.basic.EnvManager
import com.treasure.basic.constant.ConstantUtil
import com.treasure.basic.helper.LogHelper
import com.treasure.basic.utils.ShareUtils
import com.moczul.ok2curl.CurlInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * Created by treasure_ct on 2025/07/09
 * Description:
 */
class RetrofitClient private constructor() {
    companion object {
        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            return instance ?: synchronized(this) {
                instance ?: RetrofitClient().also { instance = it }
            }
        }

    }

    private val retrofit: Retrofit

    private val proxySelector = object : ProxySelector() {
        override fun select(uri: URI): List<Proxy> {
            return listOf<Proxy>(Proxy.NO_PROXY)
        }

        override fun connectFailed(uri: URI, sa: SocketAddress, ioe: IOException) {}
    }

    private val interceptor = Interceptor { chain ->
        val reqUrl = chain.request().url.toUrl().toString()
        if (reqUrl.contains("lingxi-system/qrCode/commitLoginQrCode")
        ) {
            return@Interceptor chain.proceed(chain.request().newBuilder().build())
        }
        val lingxiAuth = if (reqUrl.contains("lingxi-auth/oauth/token")) "" else ShareUtils.getDefaultString(ConstantUtil.LOGIN_TOKEN)
        if (ConstantUtil.OAID == null) ConstantUtil.OAID = ""
        val authorization = "Basic " + Base64.encodeToString(
            "lingxi_saas_app:lingxi_saas_app_secret".toByteArray(), Base64.NO_WRAP
        )
        val build: Request = chain.request().newBuilder()
            .addHeader("oaid", ConstantUtil.OAID)
            .addHeader("androidid", "")
            .addHeader("channelCode", ConstantUtil.getMediaType())
            .addHeader("os", "0")
            .addHeader("Authorization", authorization)
            .addHeader("lingxi-auth", lingxiAuth)
            .addHeader("User-Client", "app") // .addHeader("Tenant-Id","000000")
            // .addHeader("entId","")
            // .addHeader("regionId",ConstantUtil.PegionCode)
            // .addHeader("lnglat", ScreenUtils.getLastKnownLocation()==null?"":ScreenUtils.getLastKnownLocation().getLongitude()+","+ScreenUtils.getLastKnownLocation().getLatitude())
            .build()
        chain.proceed(build)
    }

    init {
        val builder = OkHttpClient.Builder().addInterceptor(interceptor)
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(if (EnvManager.logEnable()) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                .addNetworkInterceptor(CurlInterceptor {
                    if (EnvManager.logEnable()) LogHelper.w("okhttp-CUrl -> $it")
                }).connectTimeout(3, TimeUnit.SECONDS) //设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS) //设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//设置写入超时时间
        if (EnvManager.logEnable().not()) {
            builder.proxySelector(proxySelector)
        }
        val okHttpClient = builder.build()

        val gson =
            GsonBuilder().registerTypeAdapterFactory(BaseResponseTypeAdapterFactory()).create()
        retrofit = Retrofit.Builder().baseUrl(EnvManager.getBaseUrl()).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}