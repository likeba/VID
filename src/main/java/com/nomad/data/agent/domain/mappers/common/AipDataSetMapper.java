package com.nomad.data.agent.domain.mappers.common;

import com.nomad.data.agent.domain.dao.common.AipDataSet;
import com.nomad.data.agent.domain.dao.common.AipDataSetExample;
import com.nomad.data.agent.domain.dao.common.AipDataSetKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AipDataSetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    long countByExample(AipDataSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int deleteByExample(AipDataSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int deleteByPrimaryKey(AipDataSetKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int insert(AipDataSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int insertSelective(AipDataSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    List<AipDataSet> selectByExampleWithRowbounds(AipDataSetExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    List<AipDataSet> selectByExample(AipDataSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    AipDataSet selectByPrimaryKey(AipDataSetKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int updateByExampleSelective(@Param("record") AipDataSet record, @Param("example") AipDataSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int updateByExample(@Param("record") AipDataSet record, @Param("example") AipDataSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int updateByPrimaryKeySelective(AipDataSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aip_data_set
     *
     * @mbg.generated Wed Apr 29 17:19:36 KST 2020
     */
    int updateByPrimaryKey(AipDataSet record);
}