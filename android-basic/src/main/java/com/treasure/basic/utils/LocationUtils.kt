package com.treasure.basic.utils

import android.app.Activity
import android.content.Context
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.treasure.basic.helper.CommonCallback
import com.treasure.basic.helper.LogHelper
import org.json.JSONObject
import java.io.IOException
import java.util.Locale


object LocationUtils {

     fun getLocationInfo(context: Activity, cb:CommonCallback<String?>) {
        PermissionHelper.checkLocationPermission(context) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // 优先使用 GPS
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            val provider = when {
                isGpsEnabled -> LocationManager.GPS_PROVIDER
                isNetworkEnabled -> LocationManager.NETWORK_PROVIDER
                else -> null
            }
            if (provider == null) {
                ToastUtils.show("请开启定位服务")
                cb.onContinue(null)
                return@checkLocationPermission
            }
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.powerRequirement = Criteria.POWER_LOW
            locationManager.requestSingleUpdate(criteria, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val fullAddress = address.getAddressLine(0) ?: "未知地址"
                            val jo = JSONObject()
                            jo.put("address", fullAddress)
                            jo.put("latitude", latitude)
                            jo.put("longitude", longitude)
                            LogHelper.i("Location -> $jo")
                            cb.onContinue(jo.toString())
                        } else {
                            LogHelper.d("Location -> 无法获取地址")
                            cb.onContinue(null)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        LogHelper.i("Location -> Geocoder -> 获取地址失败：${e.message}")
                        cb.onContinue(null)
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, null)
        }
    }

}