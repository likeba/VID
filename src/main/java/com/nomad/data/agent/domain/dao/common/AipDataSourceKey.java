package com.nomad.data.agent.domain.dao.common;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table AIP_DATA_SOURCE
 *
 * @mbg.generated do_not_delete_during_merge Thu Apr 09 16:00:16 KST 2020
 */
public class AipDataSourceKey {
    /**
     */
    private String dsId;

    /**
     */
    public String getDsId() {
        return dsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_DATA_SOURCE.DS_ID
     *
     * @param dsId the value for AIP_DATA_SOURCE.DS_ID
     *
     * @mbg.generated Thu Apr 09 16:00:16 KST 2020
     */
    public void setDsId(String dsId) {
        this.dsId = dsId == null ? null : dsId.trim();
    }
}