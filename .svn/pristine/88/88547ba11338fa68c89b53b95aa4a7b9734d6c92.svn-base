package com.example.bankinfo.task;

import com.example.bankinfo.BankInfoApplication;
import com.example.bankinfo.common.jms.ClsMakeSentMQData;
import com.example.bankinfo.domain.Record;
import com.example.bankinfo.service.RecordService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendRiskRecordInfoTask {
    /*日志*/
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RecordService recordService;

    @Value("${station}")
    private String station;

    @Value("${risk_url_add}")
    private String riskUrlAdd;
    private ClsMakeSentMQData clsMakeSentMQData = new ClsMakeSentMQData("{\"BrokerUrl\":\"failover:(tcp://14.29.47.99:61616)?timeout=3000\",\"QueueName\":\"bank_data\",\"User\":\"admin\",\"Psw\":\"admin\"}");

    /*加币任务*/
    @Scheduled(fixedRate = 30 * 1000)
    public void sendRiskRecordInfoTask() {
        List<Record> list1 = recordService.findAll(new Record(BankInfoApplication.BANK_RECORD_TABLE_NAME, 0, "notNull","ok"));
        List<Record> list2 = recordService.findAll(new Record(BankInfoApplication.BANK_RECORD_TABLE_NAME_2, 0, "notNull","ok"));
        JSONArray jsonArray = new JSONArray();
        for (Record record : list1) {
            addArrayJson(jsonArray, record);
        }
        for (Record record : list2) {
            addArrayJson(jsonArray, record);
        }
        if (list1.size() > 0 || list2.size() > 0) {
            clsMakeSentMQData.pushMsgToACMQ(jsonArray.toString());
            logger.info("发送风控数据{}！成功", jsonArray.toString());
            for (Record record : list1) {
                record.setTableName(BankInfoApplication.BANK_RECORD_TABLE_NAME);
                record.setIsSum(1);
                recordService.updateByPrimaryKeySelective(record);
            }
            for (Record record : list2) {
                record.setTableName(BankInfoApplication.BANK_RECORD_TABLE_NAME_2);
                record.setIsSum(1);
                recordService.updateByPrimaryKeySelective(record);
            }

        }
    }

    /**
     * 将未发送数据整合到json数组里
     *
     * @param jsonArray json数组里
     * @param record    银行记录对象
     */
    private void addArrayJson(JSONArray jsonArray, Record record) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("station", station);
        jsonObject.put("incoming", record.getIncoming());
        jsonObject.put("balance", record.getBalance());
        if (!record.getTradinghour().trim().contains(" "))
            record.setTradinghour(record.getTradinghour() + " 00:00:00");
        jsonObject.put("tradingHour", record.getTradinghour().trim());
        if ("\\".equals(record.getRemark())) {
            jsonObject.put("Remark", "手续费");
        } else {
            jsonObject.put("Remark", record.getRemark().trim());
        }
        jsonObject.put("bname", record.getBname().trim());
        jsonObject.put("videoMoneyToken", record.getToken().trim());

        jsonArray.add(jsonObject);
    }
}
