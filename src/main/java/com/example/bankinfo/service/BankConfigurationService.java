package com.example.bankinfo.service;


import com.example.bankinfo.domain.BankConfiguration;
import com.example.bankinfo.mapper.BankConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bankConfigurationService")
public class BankConfigurationService {

    @Autowired
    BankConfigurationMapper mapper;

    public int deleteByPrimaryKey(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public int insertSelective(BankConfiguration record) {
        return mapper.insertSelective(record);
    }

    public BankConfiguration selectByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(BankConfiguration record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    public List<BankConfiguration> findAll(BankConfiguration record) {
        return mapper.findAll(record);
    }

    public void updateAllNotOpen(String tableName){
        mapper.updateAllNotOpen(tableName);
    };

    public BankConfiguration findOpenBank(String tableName) {
        return mapper.findOpenBank(tableName);
    }

}
