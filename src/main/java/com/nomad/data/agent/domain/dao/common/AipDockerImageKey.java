package com.nomad.data.agent.domain.dao.common;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table AIP_DOCKER_IMAGE
 *
 * @mbg.generated do_not_delete_during_merge Wed Apr 22 17:39:33 KST 2020
 */
public class AipDockerImageKey {
    /**
     */
    private String imgId;

    /**
     */
    public String getImgId() {
        return imgId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column AIP_DOCKER_IMAGE.IMG_ID
     *
     * @param imgId the value for AIP_DOCKER_IMAGE.IMG_ID
     *
     * @mbg.generated Wed Apr 22 17:39:33 KST 2020
     */
    public void setImgId(String imgId) {
        this.imgId = imgId == null ? null : imgId.trim();
    }
}