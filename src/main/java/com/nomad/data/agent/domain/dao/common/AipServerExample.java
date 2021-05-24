package com.nomad.data.agent.domain.dao.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AipServerExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public AipServerExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
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
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
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

        public Criteria andServerIdIsNull() {
            addCriterion("SERVER_ID is null");
            return (Criteria) this;
        }

        public Criteria andServerIdIsNotNull() {
            addCriterion("SERVER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andServerIdEqualTo(String value) {
            addCriterion("SERVER_ID =", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotEqualTo(String value) {
            addCriterion("SERVER_ID <>", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThan(String value) {
            addCriterion("SERVER_ID >", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdGreaterThanOrEqualTo(String value) {
            addCriterion("SERVER_ID >=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThan(String value) {
            addCriterion("SERVER_ID <", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLessThanOrEqualTo(String value) {
            addCriterion("SERVER_ID <=", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdLike(String value) {
            addCriterion("SERVER_ID like", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotLike(String value) {
            addCriterion("SERVER_ID not like", value, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdIn(List<String> values) {
            addCriterion("SERVER_ID in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotIn(List<String> values) {
            addCriterion("SERVER_ID not in", values, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdBetween(String value1, String value2) {
            addCriterion("SERVER_ID between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerIdNotBetween(String value1, String value2) {
            addCriterion("SERVER_ID not between", value1, value2, "serverId");
            return (Criteria) this;
        }

        public Criteria andServerNmIsNull() {
            addCriterion("SERVER_NM is null");
            return (Criteria) this;
        }

        public Criteria andServerNmIsNotNull() {
            addCriterion("SERVER_NM is not null");
            return (Criteria) this;
        }

        public Criteria andServerNmEqualTo(String value) {
            addCriterion("SERVER_NM =", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmNotEqualTo(String value) {
            addCriterion("SERVER_NM <>", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmGreaterThan(String value) {
            addCriterion("SERVER_NM >", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmGreaterThanOrEqualTo(String value) {
            addCriterion("SERVER_NM >=", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmLessThan(String value) {
            addCriterion("SERVER_NM <", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmLessThanOrEqualTo(String value) {
            addCriterion("SERVER_NM <=", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmLike(String value) {
            addCriterion("SERVER_NM like", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmNotLike(String value) {
            addCriterion("SERVER_NM not like", value, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmIn(List<String> values) {
            addCriterion("SERVER_NM in", values, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmNotIn(List<String> values) {
            addCriterion("SERVER_NM not in", values, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmBetween(String value1, String value2) {
            addCriterion("SERVER_NM between", value1, value2, "serverNm");
            return (Criteria) this;
        }

        public Criteria andServerNmNotBetween(String value1, String value2) {
            addCriterion("SERVER_NM not between", value1, value2, "serverNm");
            return (Criteria) this;
        }

        public Criteria andSeqIsNull() {
            addCriterion("SEQ is null");
            return (Criteria) this;
        }

        public Criteria andSeqIsNotNull() {
            addCriterion("SEQ is not null");
            return (Criteria) this;
        }

        public Criteria andSeqEqualTo(BigDecimal value) {
            addCriterion("SEQ =", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotEqualTo(BigDecimal value) {
            addCriterion("SEQ <>", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThan(BigDecimal value) {
            addCriterion("SEQ >", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("SEQ >=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThan(BigDecimal value) {
            addCriterion("SEQ <", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThanOrEqualTo(BigDecimal value) {
            addCriterion("SEQ <=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqIn(List<BigDecimal> values) {
            addCriterion("SEQ in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotIn(List<BigDecimal> values) {
            addCriterion("SEQ not in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SEQ between", value1, value2, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SEQ not between", value1, value2, "seq");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmIsNull() {
            addCriterion("DISPLAY_SERVER_NM is null");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmIsNotNull() {
            addCriterion("DISPLAY_SERVER_NM is not null");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmEqualTo(String value) {
            addCriterion("DISPLAY_SERVER_NM =", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmNotEqualTo(String value) {
            addCriterion("DISPLAY_SERVER_NM <>", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmGreaterThan(String value) {
            addCriterion("DISPLAY_SERVER_NM >", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmGreaterThanOrEqualTo(String value) {
            addCriterion("DISPLAY_SERVER_NM >=", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmLessThan(String value) {
            addCriterion("DISPLAY_SERVER_NM <", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmLessThanOrEqualTo(String value) {
            addCriterion("DISPLAY_SERVER_NM <=", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmLike(String value) {
            addCriterion("DISPLAY_SERVER_NM like", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmNotLike(String value) {
            addCriterion("DISPLAY_SERVER_NM not like", value, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmIn(List<String> values) {
            addCriterion("DISPLAY_SERVER_NM in", values, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmNotIn(List<String> values) {
            addCriterion("DISPLAY_SERVER_NM not in", values, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmBetween(String value1, String value2) {
            addCriterion("DISPLAY_SERVER_NM between", value1, value2, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andDisplayServerNmNotBetween(String value1, String value2) {
            addCriterion("DISPLAY_SERVER_NM not between", value1, value2, "displayServerNm");
            return (Criteria) this;
        }

        public Criteria andServerTpIsNull() {
            addCriterion("SERVER_TP is null");
            return (Criteria) this;
        }

        public Criteria andServerTpIsNotNull() {
            addCriterion("SERVER_TP is not null");
            return (Criteria) this;
        }

        public Criteria andServerTpEqualTo(String value) {
            addCriterion("SERVER_TP =", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpNotEqualTo(String value) {
            addCriterion("SERVER_TP <>", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpGreaterThan(String value) {
            addCriterion("SERVER_TP >", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpGreaterThanOrEqualTo(String value) {
            addCriterion("SERVER_TP >=", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpLessThan(String value) {
            addCriterion("SERVER_TP <", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpLessThanOrEqualTo(String value) {
            addCriterion("SERVER_TP <=", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpLike(String value) {
            addCriterion("SERVER_TP like", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpNotLike(String value) {
            addCriterion("SERVER_TP not like", value, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpIn(List<String> values) {
            addCriterion("SERVER_TP in", values, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpNotIn(List<String> values) {
            addCriterion("SERVER_TP not in", values, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpBetween(String value1, String value2) {
            addCriterion("SERVER_TP between", value1, value2, "serverTp");
            return (Criteria) this;
        }

        public Criteria andServerTpNotBetween(String value1, String value2) {
            addCriterion("SERVER_TP not between", value1, value2, "serverTp");
            return (Criteria) this;
        }

        public Criteria andHostEqualTo(String value) {
            addCriterion("HOST =", value, "host");
            return (Criteria) this;
        }

        public Criteria andPrvIpIsNull() {
            addCriterion("PRV_IP is null");
            return (Criteria) this;
        }

        public Criteria andPrvIpIsNotNull() {
            addCriterion("PRV_IP is not null");
            return (Criteria) this;
        }

        public Criteria andPrvIpEqualTo(String value) {
            addCriterion("PRV_IP =", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpNotEqualTo(String value) {
            addCriterion("PRV_IP <>", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpGreaterThan(String value) {
            addCriterion("PRV_IP >", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpGreaterThanOrEqualTo(String value) {
            addCriterion("PRV_IP >=", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpLessThan(String value) {
            addCriterion("PRV_IP <", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpLessThanOrEqualTo(String value) {
            addCriterion("PRV_IP <=", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpLike(String value) {
            addCriterion("PRV_IP like", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpNotLike(String value) {
            addCriterion("PRV_IP not like", value, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpIn(List<String> values) {
            addCriterion("PRV_IP in", values, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpNotIn(List<String> values) {
            addCriterion("PRV_IP not in", values, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpBetween(String value1, String value2) {
            addCriterion("PRV_IP between", value1, value2, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPrvIpNotBetween(String value1, String value2) {
            addCriterion("PRV_IP not between", value1, value2, "prvIp");
            return (Criteria) this;
        }

        public Criteria andPubIpIsNull() {
            addCriterion("PUB_IP is null");
            return (Criteria) this;
        }

        public Criteria andPubIpIsNotNull() {
            addCriterion("PUB_IP is not null");
            return (Criteria) this;
        }

        public Criteria andPubIpEqualTo(String value) {
            addCriterion("PUB_IP =", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpNotEqualTo(String value) {
            addCriterion("PUB_IP <>", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpGreaterThan(String value) {
            addCriterion("PUB_IP >", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpGreaterThanOrEqualTo(String value) {
            addCriterion("PUB_IP >=", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpLessThan(String value) {
            addCriterion("PUB_IP <", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpLessThanOrEqualTo(String value) {
            addCriterion("PUB_IP <=", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpLike(String value) {
            addCriterion("PUB_IP like", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpNotLike(String value) {
            addCriterion("PUB_IP not like", value, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpIn(List<String> values) {
            addCriterion("PUB_IP in", values, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpNotIn(List<String> values) {
            addCriterion("PUB_IP not in", values, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpBetween(String value1, String value2) {
            addCriterion("PUB_IP between", value1, value2, "pubIp");
            return (Criteria) this;
        }

        public Criteria andPubIpNotBetween(String value1, String value2) {
            addCriterion("PUB_IP not between", value1, value2, "pubIp");
            return (Criteria) this;
        }

        public Criteria andDnsIsNull() {
            addCriterion("DNS is null");
            return (Criteria) this;
        }

        public Criteria andDnsIsNotNull() {
            addCriterion("DNS is not null");
            return (Criteria) this;
        }

        public Criteria andDnsEqualTo(String value) {
            addCriterion("DNS =", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsNotEqualTo(String value) {
            addCriterion("DNS <>", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsGreaterThan(String value) {
            addCriterion("DNS >", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsGreaterThanOrEqualTo(String value) {
            addCriterion("DNS >=", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsLessThan(String value) {
            addCriterion("DNS <", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsLessThanOrEqualTo(String value) {
            addCriterion("DNS <=", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsLike(String value) {
            addCriterion("DNS like", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsNotLike(String value) {
            addCriterion("DNS not like", value, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsIn(List<String> values) {
            addCriterion("DNS in", values, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsNotIn(List<String> values) {
            addCriterion("DNS not in", values, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsBetween(String value1, String value2) {
            addCriterion("DNS between", value1, value2, "dns");
            return (Criteria) this;
        }

        public Criteria andDnsNotBetween(String value1, String value2) {
            addCriterion("DNS not between", value1, value2, "dns");
            return (Criteria) this;
        }

        public Criteria andCpuNumIsNull() {
            addCriterion("CPU_NUM is null");
            return (Criteria) this;
        }

        public Criteria andCpuNumIsNotNull() {
            addCriterion("CPU_NUM is not null");
            return (Criteria) this;
        }

        public Criteria andCpuNumEqualTo(BigDecimal value) {
            addCriterion("CPU_NUM =", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotEqualTo(BigDecimal value) {
            addCriterion("CPU_NUM <>", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumGreaterThan(BigDecimal value) {
            addCriterion("CPU_NUM >", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("CPU_NUM >=", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumLessThan(BigDecimal value) {
            addCriterion("CPU_NUM <", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("CPU_NUM <=", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumIn(List<BigDecimal> values) {
            addCriterion("CPU_NUM in", values, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotIn(List<BigDecimal> values) {
            addCriterion("CPU_NUM not in", values, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CPU_NUM between", value1, value2, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CPU_NUM not between", value1, value2, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumIsNull() {
            addCriterion("GPU_NUM is null");
            return (Criteria) this;
        }

        public Criteria andGpuNumIsNotNull() {
            addCriterion("GPU_NUM is not null");
            return (Criteria) this;
        }

        public Criteria andGpuNumEqualTo(BigDecimal value) {
            addCriterion("GPU_NUM =", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotEqualTo(BigDecimal value) {
            addCriterion("GPU_NUM <>", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumGreaterThan(BigDecimal value) {
            addCriterion("GPU_NUM >", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("GPU_NUM >=", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumLessThan(BigDecimal value) {
            addCriterion("GPU_NUM <", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("GPU_NUM <=", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumIn(List<BigDecimal> values) {
            addCriterion("GPU_NUM in", values, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotIn(List<BigDecimal> values) {
            addCriterion("GPU_NUM not in", values, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("GPU_NUM between", value1, value2, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("GPU_NUM not between", value1, value2, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andMemSzIsNull() {
            addCriterion("MEM_SZ is null");
            return (Criteria) this;
        }

        public Criteria andMemSzIsNotNull() {
            addCriterion("MEM_SZ is not null");
            return (Criteria) this;
        }

        public Criteria andMemSzEqualTo(BigDecimal value) {
            addCriterion("MEM_SZ =", value, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzNotEqualTo(BigDecimal value) {
            addCriterion("MEM_SZ <>", value, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzGreaterThan(BigDecimal value) {
            addCriterion("MEM_SZ >", value, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("MEM_SZ >=", value, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzLessThan(BigDecimal value) {
            addCriterion("MEM_SZ <", value, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzLessThanOrEqualTo(BigDecimal value) {
            addCriterion("MEM_SZ <=", value, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzIn(List<BigDecimal> values) {
            addCriterion("MEM_SZ in", values, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzNotIn(List<BigDecimal> values) {
            addCriterion("MEM_SZ not in", values, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MEM_SZ between", value1, value2, "memSz");
            return (Criteria) this;
        }

        public Criteria andMemSzNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MEM_SZ not between", value1, value2, "memSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzIsNull() {
            addCriterion("DISK_SZ is null");
            return (Criteria) this;
        }

        public Criteria andDiskSzIsNotNull() {
            addCriterion("DISK_SZ is not null");
            return (Criteria) this;
        }

        public Criteria andDiskSzEqualTo(BigDecimal value) {
            addCriterion("DISK_SZ =", value, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzNotEqualTo(BigDecimal value) {
            addCriterion("DISK_SZ <>", value, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzGreaterThan(BigDecimal value) {
            addCriterion("DISK_SZ >", value, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("DISK_SZ >=", value, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzLessThan(BigDecimal value) {
            addCriterion("DISK_SZ <", value, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzLessThanOrEqualTo(BigDecimal value) {
            addCriterion("DISK_SZ <=", value, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzIn(List<BigDecimal> values) {
            addCriterion("DISK_SZ in", values, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzNotIn(List<BigDecimal> values) {
            addCriterion("DISK_SZ not in", values, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DISK_SZ between", value1, value2, "diskSz");
            return (Criteria) this;
        }

        public Criteria andDiskSzNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DISK_SZ not between", value1, value2, "diskSz");
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
     * This class corresponds to the database table AIP_SERVER
     *
     * @mbg.generated do_not_delete_during_merge Tue Apr 14 15:21:16 KST 2020
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table AIP_SERVER
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
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