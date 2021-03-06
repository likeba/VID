package com.nomad.data.agent.domain.dao.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AipLogsExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public AipLogsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
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

        public Criteria andLogIdIsNull() {
            addCriterion("LOG_ID is null");
            return (Criteria) this;
        }

        public Criteria andLogIdIsNotNull() {
            addCriterion("LOG_ID is not null");
            return (Criteria) this;
        }

        public Criteria andLogIdEqualTo(String value) {
            addCriterion("LOG_ID =", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotEqualTo(String value) {
            addCriterion("LOG_ID <>", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThan(String value) {
            addCriterion("LOG_ID >", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThanOrEqualTo(String value) {
            addCriterion("LOG_ID >=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThan(String value) {
            addCriterion("LOG_ID <", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThanOrEqualTo(String value) {
            addCriterion("LOG_ID <=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLike(String value) {
            addCriterion("LOG_ID like", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotLike(String value) {
            addCriterion("LOG_ID not like", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdIn(List<String> values) {
            addCriterion("LOG_ID in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotIn(List<String> values) {
            addCriterion("LOG_ID not in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdBetween(String value1, String value2) {
            addCriterion("LOG_ID between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotBetween(String value1, String value2) {
            addCriterion("LOG_ID not between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andLogTpIsNull() {
            addCriterion("LOG_TP is null");
            return (Criteria) this;
        }

        public Criteria andLogTpIsNotNull() {
            addCriterion("LOG_TP is not null");
            return (Criteria) this;
        }

        public Criteria andLogTpEqualTo(String value) {
            addCriterion("LOG_TP =", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotEqualTo(String value) {
            addCriterion("LOG_TP <>", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpGreaterThan(String value) {
            addCriterion("LOG_TP >", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpGreaterThanOrEqualTo(String value) {
            addCriterion("LOG_TP >=", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpLessThan(String value) {
            addCriterion("LOG_TP <", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpLessThanOrEqualTo(String value) {
            addCriterion("LOG_TP <=", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpLike(String value) {
            addCriterion("LOG_TP like", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotLike(String value) {
            addCriterion("LOG_TP not like", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpIn(List<String> values) {
            addCriterion("LOG_TP in", values, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotIn(List<String> values) {
            addCriterion("LOG_TP not in", values, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpBetween(String value1, String value2) {
            addCriterion("LOG_TP between", value1, value2, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotBetween(String value1, String value2) {
            addCriterion("LOG_TP not between", value1, value2, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogStrIsNull() {
            addCriterion("LOG_STR is null");
            return (Criteria) this;
        }

        public Criteria andLogStrIsNotNull() {
            addCriterion("LOG_STR is not null");
            return (Criteria) this;
        }

        public Criteria andLogStrEqualTo(String value) {
            addCriterion("LOG_STR =", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrNotEqualTo(String value) {
            addCriterion("LOG_STR <>", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrGreaterThan(String value) {
            addCriterion("LOG_STR >", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrGreaterThanOrEqualTo(String value) {
            addCriterion("LOG_STR >=", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrLessThan(String value) {
            addCriterion("LOG_STR <", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrLessThanOrEqualTo(String value) {
            addCriterion("LOG_STR <=", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrLike(String value) {
            addCriterion("LOG_STR like", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrNotLike(String value) {
            addCriterion("LOG_STR not like", value, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrIn(List<String> values) {
            addCriterion("LOG_STR in", values, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrNotIn(List<String> values) {
            addCriterion("LOG_STR not in", values, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrBetween(String value1, String value2) {
            addCriterion("LOG_STR between", value1, value2, "logStr");
            return (Criteria) this;
        }

        public Criteria andLogStrNotBetween(String value1, String value2) {
            addCriterion("LOG_STR not between", value1, value2, "logStr");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("USER_ID =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("USER_ID <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("USER_ID >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("USER_ID >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("USER_ID <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("USER_ID <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("USER_ID like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("USER_ID not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("USER_ID in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("USER_ID not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("USER_ID between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("USER_ID not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andLogDetailIsNull() {
            addCriterion("LOG_DETAIL is null");
            return (Criteria) this;
        }

        public Criteria andLogDetailIsNotNull() {
            addCriterion("LOG_DETAIL is not null");
            return (Criteria) this;
        }

        public Criteria andLogDetailEqualTo(String value) {
            addCriterion("LOG_DETAIL =", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailNotEqualTo(String value) {
            addCriterion("LOG_DETAIL <>", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailGreaterThan(String value) {
            addCriterion("LOG_DETAIL >", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailGreaterThanOrEqualTo(String value) {
            addCriterion("LOG_DETAIL >=", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailLessThan(String value) {
            addCriterion("LOG_DETAIL <", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailLessThanOrEqualTo(String value) {
            addCriterion("LOG_DETAIL <=", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailLike(String value) {
            addCriterion("LOG_DETAIL like", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailNotLike(String value) {
            addCriterion("LOG_DETAIL not like", value, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailIn(List<String> values) {
            addCriterion("LOG_DETAIL in", values, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailNotIn(List<String> values) {
            addCriterion("LOG_DETAIL not in", values, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailBetween(String value1, String value2) {
            addCriterion("LOG_DETAIL between", value1, value2, "logDetail");
            return (Criteria) this;
        }

        public Criteria andLogDetailNotBetween(String value1, String value2) {
            addCriterion("LOG_DETAIL not between", value1, value2, "logDetail");
            return (Criteria) this;
        }

        public Criteria andRegDtIsNull() {
            addCriterion("REG_DT is null");
            return (Criteria) this;
        }

        public Criteria andRegDtIsNotNull() {
            addCriterion("REG_DT is not null");
            return (Criteria) this;
        }

        public Criteria andRegDtEqualTo(Date value) {
            addCriterion("REG_DT =", value, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtNotEqualTo(Date value) {
            addCriterion("REG_DT <>", value, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtGreaterThan(Date value) {
            addCriterion("REG_DT >", value, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtGreaterThanOrEqualTo(Date value) {
            addCriterion("REG_DT >=", value, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtLessThan(Date value) {
            addCriterion("REG_DT <", value, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtLessThanOrEqualTo(Date value) {
            addCriterion("REG_DT <=", value, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtIn(List<Date> values) {
            addCriterion("REG_DT in", values, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtNotIn(List<Date> values) {
            addCriterion("REG_DT not in", values, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtBetween(Date value1, Date value2) {
            addCriterion("REG_DT between", value1, value2, "regDt");
            return (Criteria) this;
        }

        public Criteria andRegDtNotBetween(Date value1, Date value2) {
            addCriterion("REG_DT not between", value1, value2, "regDt");
            return (Criteria) this;
        }

        public Criteria andModDtIsNull() {
            addCriterion("MOD_DT is null");
            return (Criteria) this;
        }

        public Criteria andModDtIsNotNull() {
            addCriterion("MOD_DT is not null");
            return (Criteria) this;
        }

        public Criteria andModDtEqualTo(Date value) {
            addCriterion("MOD_DT =", value, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtNotEqualTo(Date value) {
            addCriterion("MOD_DT <>", value, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtGreaterThan(Date value) {
            addCriterion("MOD_DT >", value, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtGreaterThanOrEqualTo(Date value) {
            addCriterion("MOD_DT >=", value, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtLessThan(Date value) {
            addCriterion("MOD_DT <", value, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtLessThanOrEqualTo(Date value) {
            addCriterion("MOD_DT <=", value, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtIn(List<Date> values) {
            addCriterion("MOD_DT in", values, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtNotIn(List<Date> values) {
            addCriterion("MOD_DT not in", values, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtBetween(Date value1, Date value2) {
            addCriterion("MOD_DT between", value1, value2, "modDt");
            return (Criteria) this;
        }

        public Criteria andModDtNotBetween(Date value1, Date value2) {
            addCriterion("MOD_DT not between", value1, value2, "modDt");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table AIP_LOGS
     *
     * @mbg.generated do_not_delete_during_merge Thu Apr 09 11:36:59 KST 2020
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table AIP_LOGS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
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