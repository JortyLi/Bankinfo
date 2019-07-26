package com.example.bankinfo.service;

import com.example.bankinfo.domain.Record;
import com.example.bankinfo.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    RecordMapper mapper;

    public int deleteByPrimaryKey(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    
    public void insertSelective(Record record) {
        Record queryRecord = mapper.findRecord(record);
        if (queryRecord == null) {
            mapper.insertSelective(record);
            System.out.println("新抓取数据：" + record.toString());
        }
    }


    public Record selectByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Record record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    public List<Record> findAll(Record record) {
        return mapper.findAll(record);
    }

    public Float findSumMoney(String tableName,String changeTime) {
        return mapper.findSumMoney(tableName,changeTime);
    }

    public Float findDaySumMoney(String tableName,String startTime,String endTime){
        return mapper.findDaySumMoney(tableName,startTime,endTime);
    }

    public Float findBalance(String tableName) {
        return mapper.findBalance(tableName);
    }

    public Record findRecord(Record record) {
        return mapper.findRecord(record);
    }

    public  Record findNamber(Record record){return  mapper.findNamber(record);}

    public Float findNewMoney(String tableName, String changeTime) {return mapper.findNewMoney(tableName,changeTime);
    }

    public int updateBnameByExpend(Record record) {
        return mapper.updateBnameByExpend(record);
    }

    public Record findNewRecord(String tableName) {
        return mapper.findNewRecord(tableName);
    }
}
