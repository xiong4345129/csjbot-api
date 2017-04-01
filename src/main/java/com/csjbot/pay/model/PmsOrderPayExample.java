package com.csjbot.pay.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PmsOrderPayExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public PmsOrderPayExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("order_id like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("order_id not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNull() {
            addCriterion("order_time is null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNotNull() {
            addCriterion("order_time is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeEqualTo(Date value) {
            addCriterion("order_time =", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotEqualTo(Date value) {
            addCriterion("order_time <>", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThan(Date value) {
            addCriterion("order_time >", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("order_time >=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThan(Date value) {
            addCriterion("order_time <", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThanOrEqualTo(Date value) {
            addCriterion("order_time <=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIn(List<Date> values) {
            addCriterion("order_time in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotIn(List<Date> values) {
            addCriterion("order_time not in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeBetween(Date value1, Date value2) {
            addCriterion("order_time between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotBetween(Date value1, Date value2) {
            addCriterion("order_time not between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNull() {
            addCriterion("order_status is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("order_status is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(String value) {
            addCriterion("order_status =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(String value) {
            addCriterion("order_status <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(String value) {
            addCriterion("order_status >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(String value) {
            addCriterion("order_status >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(String value) {
            addCriterion("order_status <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(String value) {
            addCriterion("order_status <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLike(String value) {
            addCriterion("order_status like", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotLike(String value) {
            addCriterion("order_status not like", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<String> values) {
            addCriterion("order_status in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<String> values) {
            addCriterion("order_status not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(String value1, String value2) {
            addCriterion("order_status between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(String value1, String value2) {
            addCriterion("order_status not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeIsNull() {
            addCriterion("order_total_fee is null");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeIsNotNull() {
            addCriterion("order_total_fee is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeEqualTo(Integer value) {
            addCriterion("order_total_fee =", value, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeNotEqualTo(Integer value) {
            addCriterion("order_total_fee <>", value, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeGreaterThan(Integer value) {
            addCriterion("order_total_fee >", value, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_total_fee >=", value, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeLessThan(Integer value) {
            addCriterion("order_total_fee <", value, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeLessThanOrEqualTo(Integer value) {
            addCriterion("order_total_fee <=", value, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeIn(List<Integer> values) {
            addCriterion("order_total_fee in", values, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeNotIn(List<Integer> values) {
            addCriterion("order_total_fee not in", values, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeBetween(Integer value1, Integer value2) {
            addCriterion("order_total_fee between", value1, value2, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andOrderTotalFeeNotBetween(Integer value1, Integer value2) {
            addCriterion("order_total_fee not between", value1, value2, "orderTotalFee");
            return (Criteria) this;
        }

        public Criteria andPayMethodIsNull() {
            addCriterion("pay_method is null");
            return (Criteria) this;
        }

        public Criteria andPayMethodIsNotNull() {
            addCriterion("pay_method is not null");
            return (Criteria) this;
        }

        public Criteria andPayMethodEqualTo(String value) {
            addCriterion("pay_method =", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodNotEqualTo(String value) {
            addCriterion("pay_method <>", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodGreaterThan(String value) {
            addCriterion("pay_method >", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodGreaterThanOrEqualTo(String value) {
            addCriterion("pay_method >=", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodLessThan(String value) {
            addCriterion("pay_method <", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodLessThanOrEqualTo(String value) {
            addCriterion("pay_method <=", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodLike(String value) {
            addCriterion("pay_method like", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodNotLike(String value) {
            addCriterion("pay_method not like", value, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodIn(List<String> values) {
            addCriterion("pay_method in", values, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodNotIn(List<String> values) {
            addCriterion("pay_method not in", values, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodBetween(String value1, String value2) {
            addCriterion("pay_method between", value1, value2, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayMethodNotBetween(String value1, String value2) {
            addCriterion("pay_method not between", value1, value2, "payMethod");
            return (Criteria) this;
        }

        public Criteria andPayUrlIsNull() {
            addCriterion("pay_url is null");
            return (Criteria) this;
        }

        public Criteria andPayUrlIsNotNull() {
            addCriterion("pay_url is not null");
            return (Criteria) this;
        }

        public Criteria andPayUrlEqualTo(String value) {
            addCriterion("pay_url =", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotEqualTo(String value) {
            addCriterion("pay_url <>", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlGreaterThan(String value) {
            addCriterion("pay_url >", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlGreaterThanOrEqualTo(String value) {
            addCriterion("pay_url >=", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlLessThan(String value) {
            addCriterion("pay_url <", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlLessThanOrEqualTo(String value) {
            addCriterion("pay_url <=", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlLike(String value) {
            addCriterion("pay_url like", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotLike(String value) {
            addCriterion("pay_url not like", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlIn(List<String> values) {
            addCriterion("pay_url in", values, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotIn(List<String> values) {
            addCriterion("pay_url not in", values, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlBetween(String value1, String value2) {
            addCriterion("pay_url between", value1, value2, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotBetween(String value1, String value2) {
            addCriterion("pay_url not between", value1, value2, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayStatusIsNull() {
            addCriterion("pay_status is null");
            return (Criteria) this;
        }

        public Criteria andPayStatusIsNotNull() {
            addCriterion("pay_status is not null");
            return (Criteria) this;
        }

        public Criteria andPayStatusEqualTo(String value) {
            addCriterion("pay_status =", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusNotEqualTo(String value) {
            addCriterion("pay_status <>", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusGreaterThan(String value) {
            addCriterion("pay_status >", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusGreaterThanOrEqualTo(String value) {
            addCriterion("pay_status >=", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusLessThan(String value) {
            addCriterion("pay_status <", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusLessThanOrEqualTo(String value) {
            addCriterion("pay_status <=", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusLike(String value) {
            addCriterion("pay_status like", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusNotLike(String value) {
            addCriterion("pay_status not like", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusIn(List<String> values) {
            addCriterion("pay_status in", values, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusNotIn(List<String> values) {
            addCriterion("pay_status not in", values, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusBetween(String value1, String value2) {
            addCriterion("pay_status between", value1, value2, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusNotBetween(String value1, String value2) {
            addCriterion("pay_status not between", value1, value2, "payStatus");
            return (Criteria) this;
        }

        public Criteria andRobotUidIsNull() {
            addCriterion("robot_uid is null");
            return (Criteria) this;
        }

        public Criteria andRobotUidIsNotNull() {
            addCriterion("robot_uid is not null");
            return (Criteria) this;
        }

        public Criteria andRobotUidEqualTo(String value) {
            addCriterion("robot_uid =", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidNotEqualTo(String value) {
            addCriterion("robot_uid <>", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidGreaterThan(String value) {
            addCriterion("robot_uid >", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidGreaterThanOrEqualTo(String value) {
            addCriterion("robot_uid >=", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidLessThan(String value) {
            addCriterion("robot_uid <", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidLessThanOrEqualTo(String value) {
            addCriterion("robot_uid <=", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidLike(String value) {
            addCriterion("robot_uid like", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidNotLike(String value) {
            addCriterion("robot_uid not like", value, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidIn(List<String> values) {
            addCriterion("robot_uid in", values, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidNotIn(List<String> values) {
            addCriterion("robot_uid not in", values, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidBetween(String value1, String value2) {
            addCriterion("robot_uid between", value1, value2, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotUidNotBetween(String value1, String value2) {
            addCriterion("robot_uid not between", value1, value2, "robotUid");
            return (Criteria) this;
        }

        public Criteria andRobotModelIsNull() {
            addCriterion("robot_model is null");
            return (Criteria) this;
        }

        public Criteria andRobotModelIsNotNull() {
            addCriterion("robot_model is not null");
            return (Criteria) this;
        }

        public Criteria andRobotModelEqualTo(String value) {
            addCriterion("robot_model =", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelNotEqualTo(String value) {
            addCriterion("robot_model <>", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelGreaterThan(String value) {
            addCriterion("robot_model >", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelGreaterThanOrEqualTo(String value) {
            addCriterion("robot_model >=", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelLessThan(String value) {
            addCriterion("robot_model <", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelLessThanOrEqualTo(String value) {
            addCriterion("robot_model <=", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelLike(String value) {
            addCriterion("robot_model like", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelNotLike(String value) {
            addCriterion("robot_model not like", value, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelIn(List<String> values) {
            addCriterion("robot_model in", values, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelNotIn(List<String> values) {
            addCriterion("robot_model not in", values, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelBetween(String value1, String value2) {
            addCriterion("robot_model between", value1, value2, "robotModel");
            return (Criteria) this;
        }

        public Criteria andRobotModelNotBetween(String value1, String value2) {
            addCriterion("robot_model not between", value1, value2, "robotModel");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}