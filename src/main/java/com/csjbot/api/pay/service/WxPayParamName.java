package com.csjbot.api.pay.service;

public final class WxPayParamName {

    /** 计算前面时最后加的key=(apikey) */
    public static final String K_API_KEY = "key";

    /** 公众账号ID */
    public static final String K_APPID = "appid";
    /** 附加数据 */
    public static final String K_ATTACH = "attach";
    /** 付款银行 */
    public static final String K_BANK_TYPE = "bank_type";
    /** 商品描述 */
    public static final String K_BODY = "body";
    /** 现金支付金额 */
    public static final String K_CASH_FEE = "cash_fee";
    /** 现金支付币种 */
    public static final String K_CASH_FEE_TYPE = "cash_fee_type";
    /** 现金退款金额 */
    public static final String K_CASH_REFUND_FEE = "cash_refund_fee";
    /** 二维码链接 */
    public static final String K_CODE_URL = "code_url";
    /** 代金券使用数量 */
    public static final String K_COUPON_COUNT = "coupon_count";
    /** 代金券金额 */
    public static final String K_COUPON_FEE = "coupon_fee";
    /** 单个代金券支付金额 */
    public static final String K_COUPON_FEE_$N = "coupon_fee_$n";
    /** 代金券ID */
    public static final String K_COUPON_ID_$N = "coupon_id_$n";
    /** 退款代金券使用数量 */
    public static final String K_COUPON_REFUND_COUNT = "coupon_refund_count";
    /** 退款代金券使用数量 */
    public static final String K_COUPON_REFUND_COUNT_$N = "coupon_refund_count_$n";
    /** 代金券退款总金额 */
    public static final String K_COUPON_REFUND_FEE = "coupon_refund_fee";
    // /**单个代金券退款金额*/public static final String K_COUPON_REFUND_FEE_$N="coupon_refund_fee_$n";
    // /**总代金券退款金额*/public static final String K_COUPON_REFUND_FEE_$N="coupon_refund_fee_$n";
    // /**单个代金券退款金额*/public static final String K_COUPON_REFUND_FEE_$N_$M="coupon_refund_fee_$n_$m";
    /** 退款代金券ID */
    public static final String K_COUPON_REFUND_ID_$N = "coupon_refund_id_$n";
    /** 退款代金券ID */
    public static final String K_COUPON_REFUND_ID_$N_$M = "coupon_refund_id_$n_$m";
    /** 代金券类型 */
    public static final String K_COUPON_TYPE_$N = "coupon_type_$n";
    /** 商品详情 */
    public static final String K_DETAIL = "detail";
    /** 设备号 */
    public static final String K_DEVICE_INFO = "device_info";
    /** 错误代码 */
    public static final String K_ERR_CODE = "err_code";
    /** 错误代码描述 */
    public static final String K_ERR_CODE_DES = "err_code_des";
    /** 标价货种 */
    public static final String K_FEE_TYPE = "fee_type";
    /** 商品标记 */
    public static final String K_GOODS_TAG = "goods_tag";
    /** 是否关注公众账号 */
    public static final String K_IS_SUBSCRIBE = "is_subscribe";
    /** 指定支付方式 */
    public static final String K_LIMIT_PAY = "limit_pay";
    /** 商户号 */
    public static final String K_MCH_ID = "mch_id";
    /** 随机字符串 */
    public static final String K_NONCE_STR = "nonce_str";
    /** 通知地址 */
    public static final String K_NOTIFY_URL = "notify_url";
    /** 操作员 */
    public static final String K_OP_USER_ID = "op_user_id";
    /** 用户标识 */
    public static final String K_OPENID = "openid";
    /** 商户退款单号 */
    public static final String K_OUT_REFUND_NO = "out_refund_no";
    /** 商户退款单号 */
    public static final String K_OUT_REFUND_NO_$N = "out_refund_no_$n";
    /** 商户订单号 */
    public static final String K_OUT_TRADE_NO = "out_trade_no";
    /** 预支付交易会话标识 */
    public static final String K_PREPAY_ID = "prepay_id";
    /** 商品ID */
    public static final String K_PRODUCT_ID = "product_id";
    /** 退款资金来源 */
    public static final String K_REFUND_ACCOUNT = "refund_account";
    /** 退款资金来源 */
    public static final String K_REFUND_ACCOUNT_$N = "refund_account_$n";
    /** 退款渠道 */
    public static final String K_REFUND_CHANNEL_$N = "refund_channel_$n";
    /** 退款笔数 */
    public static final String K_REFUND_COUNT = "refund_count";
    /** 退款金额 */
    public static final String K_REFUND_FEE = "refund_fee";
    /** 申请退款金额 */
    public static final String K_REFUND_FEE_$N = "refund_fee_$n";
    /** 退款货币种类 */
    public static final String K_REFUND_FEE_TYPE = "refund_fee_type";
    /** 微信退款单号 */
    public static final String K_REFUND_ID = "refund_id";
    /** 微信退款单号 */
    public static final String K_REFUND_ID_$N = "refund_id_$n";
    /** 退款入账账户 */
    public static final String K_REFUND_RECV_ACCOUT_$N = "refund_recv_accout_$n";
    /** 退款状态 */
    public static final String K_REFUND_STATUS_$N = "refund_status_$n";
    /** 退款成功时间 */
    public static final String K_REFUND_SUCCESS_TIME_$N = "refund_success_time_$n";
    /** 业务结果 */
    public static final String K_RESULT_CODE = "result_code";
    /** 业务结果描述 */
    public static final String K_RESULT_MSG = "result_msg";
    /** 返回状态码 */
    public static final String K_RETURN_CODE = "return_code";
    /** 返回信息 */
    public static final String K_RETURN_MSG = "return_msg";
    /** 应结退款金额 */
    public static final String K_SETTLEMENT_REFUND_FEE = "settlement_refund_fee";
    /** 退款金额 */
    public static final String K_SETTLEMENT_REFUND_FEE_$N = "settlement_refund_fee_$n";
    /** 应结订单金额 */
    public static final String K_SETTLEMENT_TOTAL_FEE = "settlement_total_fee";
    /** 签名 */
    public static final String K_SIGN = "sign";
    /** 签名类型 */
    public static final String K_SIGN_TYPE = "sign_type";
    /** 终端IP */
    public static final String K_SPBILL_CREATE_IP = "spbill_create_ip";
    /** 支付完成时间 */
    public static final String K_TIME_END = "time_end";
    /** 交易结束时间 */
    public static final String K_TIME_EXPIRE = "time_expire";
    /** 交易起始时间 */
    public static final String K_TIME_START = "time_start";
    /** 标价金额 */
    public static final String K_TOTAL_FEE = "total_fee";
    /** 交易状态 */
    public static final String K_TRADE_STATE = "trade_state";
    /** 交易状态描述 */
    public static final String K_TRADE_STATE_DESC = "trade_state_desc";
     /** 交易类型 */
    public static final String K_TRADE_TYPE = "trade_type";
    /** 微信支付订单号 */
    public static final String K_TRANSACTION_ID = "transaction_id";

}
