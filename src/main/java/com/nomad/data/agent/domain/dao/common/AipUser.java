package com.nomad.data.agent.domain.dao.common;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table AIP_USER
 *
 * @mbg.generated do_not_delete_during_merge Thu Apr 09 11:36:59 KST 2020
 */
public class AipUser extends AipUserKey {
    /**
     */
    private String userNm;

    /**
     */
    private String email;

    /**
     */
    private String bankId;

    /**
     */
    private String dept;

    /**
     */
    private String gitUserNm;

    /**
     */
    private String gitEmail;

    /**
     */
    private String pswd;

    /**
     */
    private String gitToken;

    /**
     */
    private Integer userLv;

    /**
     */
    private Integer accAuth;

    /**
     */
    private String snupSts;

    /**
     */
    private String tkn;

    /**
     */
    private Date regDt;

    /**
     */
    private Date modDt;

    /**
     */
    private BigDecimal gitPswd;

    /**
     */
    private String ip;

    /**
     */
    public String getUserNm() {
        return userNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.USER_NM
     *
     * @param userNm the value for AIP_USER.USER_NM
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setUserNm(String userNm) {
        this.userNm = userNm == null ? null : userNm.trim();
    }

    /**
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.EMAIL
     *
     * @param email the value for AIP_USER.EMAIL
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.BANK_ID
     *
     * @param bankId the value for AIP_USER.BANK_ID
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setBankId(String bankId) {
        this.bankId = bankId == null ? null : bankId.trim();
    }

    /**
     */
    public String getDept() {
        return dept;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.DEPT
     *
     * @param dept the value for AIP_USER.DEPT
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setDept(String dept) {
        this.dept = dept == null ? null : dept.trim();
    }

    /**
     */
    public String getGitUserNm() {
        return gitUserNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.GIT_USER_NM
     *
     * @param gitUserNm the value for AIP_USER.GIT_USER_NM
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setGitUserNm(String gitUserNm) {
        this.gitUserNm = gitUserNm == null ? null : gitUserNm.trim();
    }

    /**
     */
    public String getGitEmail() {
        return gitEmail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.GIT_EMAIL
     *
     * @param gitEmail the value for AIP_USER.GIT_EMAIL
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setGitEmail(String gitEmail) {
        this.gitEmail = gitEmail == null ? null : gitEmail.trim();
    }

    /**
     */
    public String getPswd() {
        return pswd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.PSWD
     *
     * @param pswd the value for AIP_USER.PSWD
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setPswd(String pswd) {
        this.pswd = pswd == null ? null : pswd.trim();
    }

    /**
     */
    public String getGitToken() {
        return gitToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.GIT_TOKEN
     *
     * @param gitToken the value for AIP_USER.GIT_TOKEN
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setGitToken(String gitToken) {
        this.gitToken = gitToken == null ? null : gitToken.trim();
    }

    /**
     */
    public Integer getUserLv() {
        return userLv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.USER_LV
     *
     * @param userLv the value for AIP_USER.USER_LV
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setUserLv(Integer userLv) {
        this.userLv = userLv;
    }

    /**
     */
    public Integer getAccAuth() {
        return accAuth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.ACC_AUTH
     *
     * @param accAuth the value for AIP_USER.ACC_AUTH
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setAccAuth(Integer accAuth) {
        this.accAuth = accAuth;
    }

    /**
     */
    public String getSnupSts() {
        return snupSts;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.SNUP_STS
     *
     * @param snupSts the value for AIP_USER.SNUP_STS
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setSnupSts(String snupSts) {
        this.snupSts = snupSts == null ? null : snupSts.trim();
    }

    /**
     */
    public String getTkn() {
        return tkn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.TKN
     *
     * @param tkn the value for AIP_USER.TKN
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setTkn(String tkn) {
        this.tkn = tkn == null ? null : tkn.trim();
    }

    /**
     */
    public Date getRegDt() {
        return regDt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.REG_DT
     *
     * @param regDt the value for AIP_USER.REG_DT
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    /**
     */
    public Date getModDt() {
        return modDt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.MOD_DT
     *
     * @param modDt the value for AIP_USER.MOD_DT
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setModDt(Date modDt) {
        this.modDt = modDt;
    }

    /**
     */
    public BigDecimal getGitPswd() {
        return gitPswd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.GIT_PSWD
     *
     * @param gitPswd the value for AIP_USER.GIT_PSWD
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setGitPswd(BigDecimal gitPswd) {
        this.gitPswd = gitPswd;
    }

    /**
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_USER.IP
     *
     * @param ip the value for AIP_USER.IP
     *
     * @mbg.generated Thu Apr 09 11:36:59 KST 2020
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }
}