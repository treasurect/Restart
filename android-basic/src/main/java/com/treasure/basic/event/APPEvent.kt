package com.treasure.basic.event

import com.treasure.basic.bean.AdvancedSearchBean
import com.treasure.basic.ui.tree.Node

enum class EventType {
    LOGIN_IN,
    LOGOUT,
    SHOW_CITY_SELECT,
    SHOW_INDUSTRY_LIKE,
    SUBMIT_ADVANCE_SEARCH,
    ORG_STATE_CHANGE,
    PARK_STATE_CHANGE,
    GROUP_DELETE,
    GROUP_UPDATE_NAME,
    MONITOR_COMPANY_SUCCESS,
    MONITOR_ONE_COMPANY_SUCCESS,
    CANCEL_MONITOR_ORG,
    GROUP_DYN_ISREAD,
    ADD_TEMPLATE,
    CANCEL_GROUP,
    SETING_DYN,
    ENTRUST_LIST_CLOSE_ORDER,
    ENTRUST_LIST_REFRESH_TYPE_STATS,
    SHOW_OCR_ID,
    WXPAY_STATUS,
    REFRESH_DATA,
    INDUSTRY_RESULT,
    ENTRUST_ORDER_STATE,
    MESSAGE_READ_EVENT,
    ORDER_ACCEPTANCE_STATUS,  // 新增: 接单状态通知
    REFRESH_INVEST_DATA,
    ITEM_PLANNED_STATE_CHANGE,
    REFRESH_INVEST_MAIN,//刷新首页列表
    RESET_MULTI_STATE,//恢复多选状态

    OCR_VERIFY_RESULT,//OCR认证结果
}

sealed class AppEvent<T>(
    val type: EventType,
    val data: T?
)

data class LoginInEvent(val message: String?) : AppEvent<String>(EventType.LOGIN_IN, message)
data class LogoutEvent(val message: String) : AppEvent<String>(EventType.LOGOUT, message)
data class ShowCitySelEvent(val d: Any?) : AppEvent<Any>(EventType.SHOW_CITY_SELECT, d)
data class ShowIndustryLikeEvent(val d: Any?) : AppEvent<Any>(EventType.SHOW_INDUSTRY_LIKE, d)
data class SubmitAdvanceSearch(val d: AdvancedSearchBean) : AppEvent<AdvancedSearchBean>(EventType.SUBMIT_ADVANCE_SEARCH, d)
data class OrgStateChange(val state:Int):AppEvent<Int>(EventType.ORG_STATE_CHANGE,state)//0 退出 1加入
data class ParkStateChange(val state:Int):AppEvent<Int>(EventType.PARK_STATE_CHANGE,state)//0 退出 1加入
data class EntrustListCloseOrderEvent(val state: String):AppEvent<String>(EventType.ENTRUST_LIST_CLOSE_ORDER,state)
data class EntrustListRefreshTypeStats(val state: String):AppEvent<String>(EventType.ENTRUST_LIST_REFRESH_TYPE_STATS,state)

data class ShowOcrIDEvent(val state: Int):AppEvent<Int>(EventType.SHOW_OCR_ID,state)
data class RefreshDataEvent(val state: Int):AppEvent<Int>(EventType.REFRESH_DATA,state)
data class RefreshInvestDataEvent(val state: Int):AppEvent<Int>(EventType.REFRESH_INVEST_DATA,state)
data class EntrustOrderStateEvent(val state: String):AppEvent<String>(EventType.ENTRUST_ORDER_STATE,state)
data class RefreshInvestMainEvent(val state: String):AppEvent<String>(EventType.REFRESH_INVEST_MAIN,state)
data class ResetMultiStateEvent(val state: Boolean):AppEvent<Boolean>(EventType.RESET_MULTI_STATE,state)

/**
 * 通知更新消息已读状态
 */
data class MessageReadEvent(val messageId: String):AppEvent<String>(EventType.MESSAGE_READ_EVENT,messageId)
/**
 * 接单状态事件
 * @param isAccepting true表示正在接单，false表示已停止接单
 */
data class OrderAcceptanceStatusEvent(val isAccepting: Boolean): AppEvent<Boolean>(EventType.ORDER_ACCEPTANCE_STATUS, isAccepting)
data class ItemPlannedStateChangeEvent(val s:Pair<MutableList<String>,Boolean>): AppEvent<Pair<MutableList<String>,Boolean>>(EventType.ITEM_PLANNED_STATE_CHANGE, s)

/**
 * 群组相关
 */
data class GroupDeleteEvent(val state:Int = 0):AppEvent<Int>(EventType.GROUP_DELETE,state)
data class GroupUpdateNameEvent(val state:Int):AppEvent<Int>(EventType.GROUP_UPDATE_NAME,state)
data class MonitorCompanySuccessEvent(val state:Int):AppEvent<Int>(EventType.MONITOR_COMPANY_SUCCESS,state)
data class MonitorOneCompanySuccessEvent(val state:Int):AppEvent<Int>(EventType.MONITOR_ONE_COMPANY_SUCCESS,state)
data class CancelMonitorOrgEvent(val state:Int):AppEvent<Int>(EventType.CANCEL_MONITOR_ORG,state)
data class GroupDynIsReadEvent(val state:Int):AppEvent<Int>(EventType.GROUP_DYN_ISREAD,state)
data class AddTemplateEvent(val state:Int):AppEvent<Int>(EventType.ADD_TEMPLATE,state)
data class CancelGroupEvent(val state:Int):AppEvent<Int>(EventType.CANCEL_GROUP,state)
data class SettingDYNEvent(val state: Node<Any>):AppEvent<Node<Any>>(EventType.SETING_DYN,state)
data class WXPayEvent(val state: Boolean):AppEvent<Boolean>(EventType.WXPAY_STATUS,state)//微信支付状态
data class IndustryEvent(val state: String):AppEvent<String>(EventType.INDUSTRY_RESULT,state)//行业结果

data class OCRResultEvent(val state: Boolean):AppEvent<Boolean>(EventType.OCR_VERIFY_RESULT,state)


