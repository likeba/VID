package com.nomad.data.agent.domain.dao.common;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table AIP_SERVER
 *
 * @mbg.generated do_not_delete_during_merge Tue Apr 14 15:21:16 KST 2020
 */
public class AipServer extends AipServerKey {
    /**
     */
    private String serverNm;

    /**
     */
    private BigDecimal seq;

    /**
     */
    private String displayServerNm;

    /**
     */
    private String serverTp;

    /**
     * 호스트 명
     */
    @Getter @Setter
    private String host;

    /**
     * 서버 포트
     */
    @Getter @Setter
    private Integer port;

    /**
     */
    private String prvIp;

    /**
     */
    private String pubIp;

    /**
     */
    private String dns;

    /**
     */
    private BigDecimal cpuNum;

    /**
     */
    private BigDecimal gpuNum;

    /**
     */
    private BigDecimal memSz;

    /**
     */
    private BigDecimal diskSz;

    /**
     */
    private Date regDt;

    /**
     */
    private Date modDt;

    /**
     */
    public String getServerNm() {
        return serverNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.SERVER_NM
     *
     * @param serverNm the value for AIP_SERVER.SERVER_NM
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setServerNm(String serverNm) {
        this.serverNm = serverNm == null ? null : serverNm.trim();
    }

    /**
     */
    public BigDecimal getSeq() {
        return seq;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.SEQ
     *
     * @param seq the value for AIP_SERVER.SEQ
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setSeq(BigDecimal seq) {
        this.seq = seq;
    }

    /**
     */
    public String getDisplayServerNm() {
        return displayServerNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.DISPLAY_SERVER_NM
     *
     * @param displayServerNm the value for AIP_SERVER.DISPLAY_SERVER_NM
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setDisplayServerNm(String displayServerNm) {
        this.displayServerNm = displayServerNm == null ? null : displayServerNm.trim();
    }

    /**
     */
    public String getServerTp() {
        return serverTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.SERVER_TP
     *
     * @param serverTp the value for AIP_SERVER.SERVER_TP
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setServerTp(String serverTp) {
        this.serverTp = serverTp == null ? null : serverTp.trim();
    }

    /**
     */
    public String getPrvIp() {
        return prvIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.PRV_IP
     *
     * @param prvIp the value for AIP_SERVER.PRV_IP
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setPrvIp(String prvIp) {
        this.prvIp = prvIp == null ? null : prvIp.trim();
    }

    /**
     */
    public String getPubIp() {
        return pubIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.PUB_IP
     *
     * @param pubIp the value for AIP_SERVER.PUB_IP
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setPubIp(String pubIp) {
        this.pubIp = pubIp == null ? null : pubIp.trim();
    }

    /**
     */
    public String getDns() {
        return dns;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.DNS
     *
     * @param dns the value for AIP_SERVER.DNS
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setDns(String dns) {
        this.dns = dns == null ? null : dns.trim();
    }

    /**
     */
    public BigDecimal getCpuNum() {
        return cpuNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.CPU_NUM
     *
     * @param cpuNum the value for AIP_SERVER.CPU_NUM
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setCpuNum(BigDecimal cpuNum) {
        this.cpuNum = cpuNum;
    }

    /**
     */
    public BigDecimal getGpuNum() {
        return gpuNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.GPU_NUM
     *
     * @param gpuNum the value for AIP_SERVER.GPU_NUM
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setGpuNum(BigDecimal gpuNum) {
        this.gpuNum = gpuNum;
    }

    /**
     */
    public BigDecimal getMemSz() {
        return memSz;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.MEM_SZ
     *
     * @param memSz the value for AIP_SERVER.MEM_SZ
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setMemSz(BigDecimal memSz) {
        this.memSz = memSz;
    }

    /**
     */
    public BigDecimal getDiskSz() {
        return diskSz;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.DISK_SZ
     *
     * @param diskSz the value for AIP_SERVER.DISK_SZ
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setDiskSz(BigDecimal diskSz) {
        this.diskSz = diskSz;
    }

    /**
     */
    public Date getRegDt() {
        return regDt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_SERVER.REG_DT
     *
     * @param regDt the value for AIP_SERVER.REG_DT
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
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
     * This method sets the value of the database column AIP_SERVER.MOD_DT
     *
     * @param modDt the value for AIP_SERVER.MOD_DT
     *
     * @mbg.generated Tue Apr 14 15:21:16 KST 2020
     */
    public void setModDt(Date modDt) {
        this.modDt = modDt;
    }
}