use csjbotdb;

select * from information_schema.innodb_trx;

SET SQL_MODE='ALLOW_INVALID_DATES';

DROP TABLE `pms_order_pay`;

CREATE TABLE `pms_order_pay` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `order_id` varchar(50) NOT NULL COMMENT '后台生成的正式订单流水号，全局唯一',
  `order_time` timestamp NOT NULL COMMENT '设备端顾客下单时间',
  `order_pseudo_no` varchar(50) DEFAULT NULL COMMENT '设备端自建的非正式订单号',
  `order_device_id` varchar(50) NOT NULL COMMENT '下单请求的来源设备id、在各自group中唯一',
  `order_device_group` varchar(20) NOT NULL COMMENT '下单请求的来源设备所属类别或型号',
  `order_total_fee` int(11) DEFAULT NULL COMMENT '订单总金额，单位为分，取整',
  `order_status` varchar(20) NOT NULL COMMENT '下单状态',
  `order_err_code` varchar(20) DEFAULT NULL COMMENT '下单错误代码',
  `order_err_desc` varchar(50) DEFAULT NULL COMMENT '下单错误描述',
  `pay_service` varchar(20) NOT NULL COMMENT '提供支付服务的三方名称',
  `pay_status` varchar(20) NOT NULL COMMENT '支付状态',
  `pay_err_code` varchar(20) DEFAULT NULL COMMENT '支付错误代码',
  `pay_err_desc` varchar(50) DEFAULT NULL COMMENT '支付错误描述',
  `pay_start_time` timestamp NULL DEFAULT NULL COMMENT '支付等待开始时间',
  `pay_close_time` timestamp NULL DEFAULT NULL COMMENT '支付实际关闭时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单、支付状态表';

DROP TABLE `pms_order_item`;

CREATE TABLE `pms_order_item` (
  `order_id` varchar(50) NOT NULL,
  `item_id` varchar(50) NOT NULL  COMMENT '商品id',
  `item_qty` int(11) NOT NULL COMMENT '商品份数，取整',
  `unit_price` int(11) DEFAULT NULL COMMENT '实际结算单价，单位为分，取整数',
  KEY `order_id` (`order_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `pms_order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `pms_order_pay_bak` (`order_id`),
  CONSTRAINT `pms_order_item_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `pms_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单、支付状态表';


CREATE TABLE `pms_order_pay_http_log` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `order_id` varchar(50) NOT NULL COMMENT '正式订单流水号',
  FOREIGN KEY (order_id) REFERENCES pms_order_pay(order_id),
  `request_url` varchar(200) COMMENT 'http请求路径',
  `request_body` text COMMENT 'http请求消息体',
  `response_code` int(3) COMMENT 'http返回状态代码',
  `response_body` text COMMENT 'http返回消息体',
  `client_type` varchar(10) COMMENT '客户端类型',
  `server_type` varchar(10) COMMENT '服务器类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='下单、支付相关http详细日志表';

DROP TABLE `pms_pay_detail_wx`;

CREATE TABLE `pms_pay_detail_wx` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `order_id` varchar(50) NOT NULL COMMENT '后台生成的订单流水号',
  `trade_type` varchar(50) NOT NULL COMMENT '',
  `code_url` varchar(200) DEFAULT NULL,
  `product_id` varchar(32) DEFAULT NULL,
  `prepay_id` varchar(64) DEFAULT NULL,
  `device_info` varchar(32) DEFAULT NULL COMMENT 'device_info 设备号',
  `openid` varchar(128) NOT NULL COMMENT 'openid 用户标识',
  `is_subscribe` varchar(1) DEFAULT NULL COMMENT 'is_subscribe 是否关注公众账号',
  `trade_type` varchar(16) NOT NULL COMMENT 'trade_type 交易类型',
  `bank_type` varchar(16) NOT NULL COMMENT 'bank_type 付款银行',
  `total_fee` int(11) NOT NULL COMMENT 'total_fee 订单金额',
  `settlement_total_fee` int(11) DEFAULT NULL COMMENT 'settlement_total_fee 应结订单金额',
  `fee_type` varchar(8) DEFAULT NULL COMMENT 'fee_type 货币种类',
  `cash_fee` int(11) NOT NULL COMMENT 'cash_fee 现金支付金额',
  `cash_fee_type` varchar(16) DEFAULT NULL COMMENT 'cash_fee_type 现金支付货币类型',
  `coupon_fee` int(11) DEFAULT NULL COMMENT 'coupon_fee 总代金券金额',
  `coupon_count` int(11) DEFAULT NULL COMMENT 'coupon_count 代金券使用数量',
  `coupon_type_$n` int(11) DEFAULT NULL COMMENT 'coupon_type_$n 代金券类型',
  `coupon_id_$n` varchar(20) DEFAULT NULL COMMENT 'coupon_id_$n 代金券ID',
  `coupon_fee_$n` int(11) DEFAULT NULL COMMENT 'coupon_fee_$n 单个代金券支付金额',
  `transaction_id` varchar(32) NOT NULL COMMENT 'transaction_id 微信支付订单号',
  `out_trade_no` varchar(32) NOT NULL COMMENT 'out_trade_no 商户订单号',
  `attach` varchar(128) DEFAULT NULL COMMENT 'attach 商家数据包',
  `time_end` varchar(14) NOT NULL COMMENT 'time_end 支付完成时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_id` (`order_id`),
  CONSTRAINT `pms_pay_detail_wx_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `pms_order_pay` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信支付记录表';
