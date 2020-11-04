package com.nomad.data.agent.domain.mappers.common;

import com.nomad.data.agent.domain.dao.common.AipImgLibMap;
import com.nomad.data.agent.domain.dao.common.AipImgLibMapExample;
import com.nomad.data.agent.domain.dao.common.AipImgLibMapKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AipImgLibMapMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    long countByExample(AipImgLibMapExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int deleteByExample(AipImgLibMapExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int deleteByPrimaryKey(AipImgLibMapKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int insert(AipImgLibMap record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int insertSelective(AipImgLibMap record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    List<AipImgLibMap> selectByExampleWithRowbounds(AipImgLibMapExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    List<AipImgLibMap> selectByExample(AipImgLibMapExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    AipImgLibMap selectByPrimaryKey(AipImgLibMapKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int updateByExampleSelective(@Param("record") AipImgLibMap record, @Param("example") AipImgLibMapExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int updateByExample(@Param("record") AipImgLibMap record, @Param("example") AipImgLibMapExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int updateByPrimaryKeySelective(AipImgLibMap record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AIP_IMG_LIB_MAP
     *
     * @mbg.generated Wed Apr 22 17:54:03 KST 2020
     */
    int updateByPrimaryKey(AipImgLibMap record);
}