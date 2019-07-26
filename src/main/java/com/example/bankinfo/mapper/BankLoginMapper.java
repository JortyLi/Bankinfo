package com.example.bankinfo.mapper;

import com.example.bankinfo.domain.BankLogin;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface BankLoginMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(BankLogin record);

    BankLogin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BankLogin record);

    List<BankLogin> findAll(BankLogin record);

    List<BankLogin> findAll(BankLogin record, RowBounds bounds);
}