SET SQL_MODE='ALLOW_INVALID_DATES';

ALTER TABLE `pms_pay_detail_wx` DROP `refund_fee`, DROP `refund_fee_type`, DROP `cash_refund_fee`, DROP `sync_time`;
ALTER TABLE `pms_order_pay` ADD COLUMN `sync_time` timestamp NULL DEFAULT NULL COMMENT '上次向三方查询、同步信息的时间' AFTER `pay_err_desc`;
ALTER TABLE pms_order_pay_http_log DROP FOREIGN KEY pms_order_pay_http_log_ibfk_1;
ALTER TABLE pms_order_pay_http_log DROP INDEX order_id;
ALTER TABLE pms_order_pay_http_log CHANGE COLUMN `order_id` `id` VARCHAR(50) NOT NULL COMMENT '订单号或退款单号';

DROP TABLE IF EXISTS `pms_refund`;

CREATE TABLE `pms_refund`(
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `order_id` varchar(50) NOT NULL COMMENT '后台生成的正式订单流水号，全局唯一',
  `refund_no` varchar(50) NOT NULL PRIMARY KEY COMMENT '后台生成的订单退款流水号，全局唯一，一个order_id可能有多个refund_no',
#   `refund_time` TIMESTAMP NULL DEFAULT NULL COMMENT '用户申请退款时间',
  `refund_fee` INT NOT NULL COMMENT '一笔订单可能分多次退款，该栏位仅为某一笔退款申请的总金额，单位人民币/分取整',
  `refund_status` VARCHAR(20) NOT NULL COMMENT '退款状态',
  `refund_err_code` VARCHAR(20) COMMENT '退款错误代码',
  `refund_err_desc` VARCHAR(20) COMMENT '退款错误描述',
  `sync_time` timestamp NULL DEFAULT NULL COMMENT '上次向三方查询、同步信息的时间',
  `is_closed` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '该笔退款是否处理完成（结果可能成功或失败）',
  `close_time` TIMESTAMP NULL DEFAULT NULL COMMENT '退款处理完成时间',
  `remark` VARCHAR(200) COMMENT '备注',
  FOREIGN KEY (`order_id`) REFERENCES pms_order_pay (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款状态表';


DROP TABLE IF EXISTS `pms_refund_detail_wx`;

CREATE TABLE `pms_refund_detail_wx` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `refund_no` VARCHAR(50) NOT NULL COMMENT '后台生成的订单退款流水号',
  `refund_count` INT COMMENT '退款笔数',
  `refund_id_sn` INT COMMENT '微信退款编号，即 _$n 中n值',
  `refund_id` VARCHAR(32) COMMENT '微信退款单号',
  `refund_status` VARCHAR(16) COMMENT '退款状态',
  `refund_success_time` TIMESTAMP NULL DEFAULT NULL COMMENT '退款成功时间',
  `refund_account` VARCHAR(30) COMMENT '退款资金来源',
  `refund_recv_accout` VARCHAR(64) COMMENT '退款入账账户',
  `refund_channel` VARCHAR(30) COMMENT '退款渠道',
  `refund_fee` INT COMMENT '退款金额',
  `refund_fee_type` VARCHAR(8) COMMENT '退款货币种类',
  `settlement_total_fee` INT COMMENT '应结退款金额',
  `cash_refund_fee` INT COMMENT '现金退款金额',
  `coupon_refund_fee` INT COMMENT '代金券退款总金额',
  FOREIGN KEY (`refund_no`) REFERENCES pms_refund (`refund_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信支付退款明细表';

