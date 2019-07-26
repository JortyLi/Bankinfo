package com.example.bankinfo.service;

import com.example.bankinfo.domain.BankLogin;
import com.example.bankinfo.mapper.BankLoginMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankLoginService {
    @Autowired
    BankLoginMapper bankLoginMapper;

    public int deleteByPrimaryKey(Integer id) {
        return bankLoginMapper.deleteByPrimaryKey(id);
    }


    public int insertSelective(BankLogin record) {
        return bankLoginMapper.insertSelective(record);
    }


    public BankLogin selectByPrimaryKey(Integer id) {
        return bankLoginMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(BankLogin record) {
        return bankLoginMapper.updateByPrimaryKeySelective(record);
    }


    public List<BankLogin> findAll(BankLogin record) {
        return bankLoginMapper.findAll(record);
    }


    public List<BankLogin> findAll(BankLogin record, RowBounds bounds) {
        return bankLoginMapper.findAll(record, bounds);
    }
}
