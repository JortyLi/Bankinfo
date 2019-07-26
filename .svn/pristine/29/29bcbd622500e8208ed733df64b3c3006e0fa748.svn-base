package com.example.bankinfo.mapper;

import com.example.bankinfo.domain.BankConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BankConfigurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(BankConfiguration record);

    BankConfiguration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BankConfiguration record);

    List<BankConfiguration> findAll(BankConfiguration record);

    void updateAllNotOpen(@Param("tableName") String tableName);

    List<BankConfiguration> findAll1(BankConfiguration bankConfiguration);

    BankConfiguration findOpenBank(@Param("tableName") String tableName);

}