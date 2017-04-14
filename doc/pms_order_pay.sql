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
#   `pay_start_time` timestamp NULL DEFAULT NULL COMMENT '支付等待开始时间',
  `close_time` timestamp NULL DEFAULT NULL COMMENT '下单支付记录的实际关闭时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注信息',
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

DROP TABLE `pms_order_pay_http_log`;

CREATE TABLE `pms_order_pay_http_log` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  -- `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `order_id` varchar(50) NOT NULL COMMENT '正式订单流水号',
  `operation` varchar(20) DEFAULT NULL COMMENT '服务、操作名称',
  `is_request` TINYINT(1) COMMENT '区分请求还是回复',
  `path` varchar(200) DEFAULT NULL COMMENT 'http请求路径',
  `body` text COMMENT 'http消息体',
#   `request_url` varchar(200) DEFAULT NULL COMMENT 'http请求路径',
#   `request_body` text COMMENT 'http请求消息体',
#   `response_code` int(3) DEFAULT NULL COMMENT 'http返回状态代码',
#   `response_body` text COMMENT 'http返回消息体',
#   `client` varchar(20) NOT NULL COMMENT '请求发起者',
#   `server` varchar(20) NOT NULL COMMENT '服务响应者',
  KEY `order_id` (`order_id`),
  CONSTRAINT `pms_order_pay_http_log_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `pms_order_pay` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='下单、支付相关http详细日志表';

DROP TABLE `pms_pay_detail_wx`;

CREATE TABLE `pms_pay_detail_wx` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据库记录产生时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据库记录更新时间',
  `order_id` varchar(50) NOT NULL COMMENT '后台生成的订单流水号',
  `spbill_create_ip` VARCHAR(16) not null COMMENT '终端IP',
  `out_trade_no` VARCHAR(32) COMMENT '商户订单号',
  `prepay_id` VARCHAR(64) COMMENT '预支付交易会话标识',
  `transaction_id` VARCHAR(32) COMMENT '微信支付订单号',
  `trade_type` VARCHAR(16) COMMENT '交易类型',
  `limit_pay` VARCHAR(32) COMMENT '指定支付方式',
  `product_id` VARCHAR(32) COMMENT '商品ID',
  `code_url` VARCHAR(64) COMMENT '二维码链接',
  `trade_state` VARCHAR(32) COMMENT '交易状态',
  `trade_state_desc` VARCHAR(256) COMMENT '交易状态描述',
  `bank_type` VARCHAR(16) COMMENT '付款银行',
  `total_fee` INT COMMENT '标价金额',
  `fee_type` VARCHAR(8) COMMENT '标价货种',
  `refund_fee` INT COMMENT '退款金额',
  `refund_fee_type` VARCHAR(8) COMMENT '退款货币种类',
  `cash_fee` INT COMMENT '现金支付金额',
  `cash_fee_type` VARCHAR(16) COMMENT '现金支付币种',
  `cash_refund_fee` INT COMMENT '现金退款金额',
  `settlement_total_fee` INT COMMENT '应结订单金额',
  `coupon_fee` INT COMMENT '代金券金额',
  `goods_tag` VARCHAR(32) COMMENT '商品标记',
  `openid` VARCHAR(128) COMMENT '用户标识',
  `is_subscribe` VARCHAR(1) COMMENT '是否关注公众账号',
  `time_start` timestamp NULL DEFAULT NULL COMMENT '支付开始时间',
  `time_expire` TIMESTAMP NULL DEFAULT NULL COMMENT '交易失效时间',
  `time_end` timestamp NULL DEFAULT NULL COMMENT '支付完成时间',
  `sync_time` timestamp NULL DEFAULT NULL COMMENT '上次向微信查询订单时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_id` (`order_id`),
  CONSTRAINT `pms_pay_detail_wx_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `pms_order_pay` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信支付记录表';
