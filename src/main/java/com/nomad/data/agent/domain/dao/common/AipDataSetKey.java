package com.nomad.data.agent.domain.dao.common;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table aip_data_set
 *
 * @mbg.generated do_not_delete_during_merge Wed Apr 29 17:19:36 KST 2020
 */
public class AipDataSetKey {
    /**
     *   데이터셋아이디
     */
    private String dataId;

    /**
     *   데이터셋아이디
     */
    public String getDataId() {
        return dataId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column aip_data_set.DATA_ID
     *
     * @param dataId the value for aip_data_set.DATA_ID
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    public void setDataId(String dataId) {
        this.dataId = dataId == null ? null : dataId.trim();
    }
}