package com.example.bankinfo.mapper;

import com.example.bankinfo.domain.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Record record);

    List<Record> findAll(Record record);

    Float findSumMoney(@Param("tableName") String tableName,@Param("changeTime") String changeTime);

    Float findDaySumMoney(@Param("tableName") String tableName,@Param("startTime")String startTime,@Param("endTime")String endTime);

    Float findBalance(@Param("tableName")  String tableName);

    Record findRecord(Record record);

    Record findNamber(Record record);

    Float findNewMoney(@Param("tableName") String tableName, @Param("changeTime") String changeTime);

    int updateBnameByExpend(Record record);

    Record findNewRecord(@Param("tableName")String tableName);
}