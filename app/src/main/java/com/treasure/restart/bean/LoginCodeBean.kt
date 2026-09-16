package com.treasure.restart.bean

/**
 * date:2021/5/27
 * author:李超(licha)
 * function:
 */
class LoginCodeBean {
    /**
     * access_token
     */
    var access_token: String? = null

    /**
     * token_type
     */
    var token_type: String? = null

    /**
     * refresh_token
     */
    var refresh_token: String? = null

    /**
     * expires_in
     */
    var expires_in: Int? = null

    /**
     * scope
     */
    var scope: String? = null

    /**
     * tenant_id
     */
    var tenant_id: String? = null

    /**
     * user_name
     */
    var user_name: String? = null

    /**
     * real_name
     */
    var real_name: String? = null

    /**
     * avatar
     */
    var avatar: String? = null

    /**
     * client_id
     */
    var client_id: String? = null

    /**
     * role_name
     */
    var role_name: String? = null

    /**
     * license
     */
    var license: String? = null

    /**
     * post_id
     */
    var post_id: String? = null

    /**
     * user_id
     */
    var user_id: String? = null

    /**
     * role_id
     */
    var role_id: String? = null

    /**
     * nick_name
     */
    var nick_name: String? = null

    /**
     * oauth_id
     */
    var oauth_id: String? = null

    /**
     * detail
     */
    var detail: DetailBean? = null

    /**
     * dept_id
     */
    var dept_id: String? = null

    /**
     * account
     */
    var account: String? = null

    /**
     * jti
     */
    var jti: String? = null

    /**
     * DetailBean
     */
    class DetailBean {
        /**
         * type
         */
        private val type: String? = null
    }

    var error: String? = null
    var error_description: String? = null

    var loginType: Int = 0 //1一键登录 2登陆密码 3验证码登录 4 支付宝  5微信 0 other
}
