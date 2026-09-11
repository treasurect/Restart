package com.treasure.basic.constant

import com.treasure.basic.AppConfig
import java.util.Locale

/**
 * Date:2025/7/12
 * Author: treasure_ct
 * function:
 */
object WebLoadKey {
     val URL_PRIVACY: String
        get() = if (AppConfig.isParkApp) "https://privacy.link-x.cn/zhaoshangpai/Privacy_Policy.html" else "https://privacy.link-x.cn/lingxihuoban/Privacy_Policy.html"
    val URL_AGREEMENT: String
        get() = if (AppConfig.isParkApp)"https://privacy.link-x.cn/zhaoshangpai/User_Agreement.html" else "https://privacy.link-x.cn/lingxihuoban/User_Agreement.html"
    val URL_SERVICE_AGREEMENT: String
        get() = if (AppConfig.isParkApp)"https://privacy.link-x.cn/zhaoshangpai/Service_Agreement.html" else "https://privacy.link-x.cn/lingxihuoban/Partnership_Agreement.html"
    val URL_CANCEL_AGREEMENT: String
        get() = if (AppConfig.isParkApp)"https://privacy.link-x.cn/zhaoshangpai/Account_Deactivation_Agreement.html" else "https://privacy.link-x.cn/lingxihuoban/Account_Deactivation_Agreement.html"
    val URL_PERSONAL_COLLECTION: String
        get() = if (AppConfig.isParkApp)"https://privacy.link-x.cn/zhaoshangpai/Personal_Information_Collection_List.html" else "https://privacy.link-x.cn/lingxihuoban/Personal_Information_Collection_List.html"
    val URL_THIRD_SHARE: String
        get() = if (AppConfig.isParkApp)"https://privacy.link-x.cn/zhaoshangpai/Third_Party_Information_Sharing_List.html" else "https://privacy.link-x.cn/lingxihuoban/Third_Party_Information_Sharing_List.html"

     val SUFFIX_H5_URL: String
        get() = if (AppConfig.isParkApp) "&oemType=chainpark" else "&oemType=chainpartner"

    @JvmField
    var BASE_H5: String = ""
    var SHARE_BASE_URL: String = "" //我的分享 复制链接
    var LINGXIAOXI_MPZF: String = ""
    var ERWEIMA_URL: String = ""
    var CARD_URL: String = ""

    //========== 园区端 ====================================================
    var ORG_CREATE: String = ""
    var ORG_MINE: String = ""
    var PROJECT_MANAGE = ""
    var HIRED_MANAGE = ""
    var PARK_DYN = ""
    var PARK_PRIVACY = ""
    var PARK_DETAIL = ""
    var PARK_ALBUM = ""
    var PARK_CHAT_AI = ""
    var INVEST_CT_ICON = ""

    var INVEST_ITEM_DETAIL = ""
    /**
     * 刘铁军：我的委托-订单详情   http://10.21.6.66:1212/#/entrustDetail?id=1943497834619731968&token=xx&User_Client=app
     *
     * 刘铁军：我的委托-补充需求   http://10.21.6.66:1212/#/requirementDetail?id=1943497834619731968&token=xx&User_Client=app
     * 刘铁军：我的委托-查看名单   http://10.21.6.66:1212/#/lookList?id=1943497834619731968&token=xx&User_Client=app
     * 刘铁军：我的委托-查看行程   http://10.21.6.66:1212/#/lookRoute?id=1943497834619731968&token=xx&User_Client=app
     */
    var   h5EntrustDetailUrl = ""
    var   h5EntrustCostDetailUrl = ""
    var   h5RequirementDetailUrl = ""
    var   h5LookListUrl = ""
    var   h5LookRouteUrl = ""



    //========= 伙伴端 ====================================================
    const val CUSTOMER_SERVICE_PHONE_NUMBER = "400-088-5570"
    var OCR_PRIVACY = ""

    var ORDER_DETAIL = ""
    var ORDER_VIEW_LIST = ""
    var ORDER_ITINERARY = ""
    var ORDER_FEEDBACK = ""
    var PARTNER_ADD_ENTERPRISES = ""



    fun String.transUrlByBaseH5():String{
        return BASE_H5 + this
    }

    fun String.transUrlWithSuffix(
        token: String, orgId: String? = null, parkId: String? = null,
        ocrName: String? = null, ocrIDCode: String? = null, mobile: String? = null
    ): String {
        return this + when{
            orgId != null -> String.format(Locale.getDefault(), "?token=%1\$s&User_Client=app&orgId=%2\$s", token, orgId)

            parkId!=null -> String.format(Locale.getDefault(), "?token=%1\$s&User_Client=app&parkId=%2\$s", token,parkId)

            ocrName!=null -> String.format(Locale.getDefault(), "?name=%1\$s&code=%2\$s&mobile=%3\$s&token=%4\$s&User_Client=app", ocrName, ocrIDCode, mobile, token)

            else->String.format(Locale.getDefault(), "?token=%1\$s&User_Client=app", token)
        }
    }
}