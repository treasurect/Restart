package com.treasure.basic.network;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.treasure.basic.constant.ConstantUtil;
import com.treasure.basic.EnvManager;
import com.treasure.basic.helper.LogHelper;
import com.treasure.basic.network.BaseResponseTypeAdapterFactory;import com.treasure.basic.utils.ShareUtils;
import com.moczul.ok2curl.CurlInterceptor;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * date:2021/5/24
 * author:李超(licha)
 * function: use RetrofitClient
 */
@Deprecated
public class RetrofitUtil {
    public static String BASE_URL = EnvManager.INSTANCE.getBaseUrl();
    private static RetrofitUtil instance;
    private static Retrofit retrofit;
    public static OkHttpClient okHttpClient;

    static {
        getOkHttpClient();
    }

    //设置okhttpbuild
    //单例模式获取okhttp
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            //打印拦截器日志
                            .addInterceptor(chain -> {
                                String reqUrl = chain.request().url().url().toString();
                                if (reqUrl.contains("lingxi-system/qrCode/commitLoginQrCode")) {
                                    return chain.proceed(chain.request().newBuilder().build());
                                }
                                String lingxiAuth = reqUrl.contains("lingxi-auth/oauth/token") ? "" : ShareUtils.getDefaultString(ConstantUtil.LOGIN_TOKEN);
                                //if (ConstantUtil.OAID == null) ConstantUtil.OAID = "";
                                Request build = chain.request().newBuilder()
                                        .addHeader("oaid", "")
                                        .addHeader("androidid", "")
                                        .addHeader("channelCode", ConstantUtil.getMediaType())
                                        .addHeader("os", "0")
                                        .addHeader("Authorization", "Basic " + Base64.encodeToString("lingxi_saas_app:lingxi_saas_app_secret".getBytes(), Base64.NO_WRAP))
                                        .addHeader("lingxi-auth", lingxiAuth)
                                        .addHeader("User-Client", "app")
                                        // .addHeader("Tenant-Id","000000")
                                        // .addHeader("entId","")
                                        // .addHeader("regionId",ConstantUtil.PegionCode)
                                        // .addHeader("lnglat", ScreenUtils.getLastKnownLocation()==null?"":ScreenUtils.getLastKnownLocation().getLongitude()+","+ScreenUtils.getLastKnownLocation().getLatitude())
                                        .build();
                                return chain.proceed(build);
                            })
                            .addNetworkInterceptor(new CurlInterceptor(message -> {
                                if (EnvManager.INSTANCE.logEnable())
                                    LogHelper.INSTANCE.i("okhttp-CUrl -> " + message);
                            }))
                            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(EnvManager.INSTANCE.logEnable() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                            .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                            .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(30, TimeUnit.SECONDS);//设置写入超时时间
                    if (!EnvManager.INSTANCE.logEnable()) {
                        //防止抓包设置
                        builder.proxySelector(new ProxySelector() {
                            @Override
                            public List<Proxy> select(URI uri) {
                                return Collections.singletonList(Proxy.NO_PROXY);
                            }

                            @Override
                            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

                            }
                        });
                    }
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }

    //打包要改推送配置 json文件
    //获取retrofit build
    public RetrofitUtil() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new BaseResponseTypeAdapterFactory())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            instance = new RetrofitUtil();
        }
        return instance;
    }

    public <T> T creatShow(Class<T> cla) {
        T t = retrofit.create(cla);
        return t;
    }
}
