package cn.itcast.dtx.txmsgdemo.bank2.Listener;

import cn.itcast.dtx.txmsgdemo.bank2.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank2.service.AccountService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

@Component
@RocketMQMessageListener(topic = "txmsg_topic",consumerGroup = "producer_group_txmsg_bank2")
@Slf4j
public class TxmsgConsumer implements RocketMQListener<String> {
    @Autowired
    private AccountService accountService;
    @Override
    public void onMessage(String s) {
        log.info("MQ_message:{}",s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String accountChangeString = jsonObject.getString("accountChange");
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
        //手动设置用户为李四
        accountChangeEvent.setAccountNo("2");
        accountService.addAccountInfoBalance(accountChangeEvent);
        if (accountChangeEvent.getAmount()==4){
            log.info("人为制造异常，消费失败");
            throw new RuntimeException("人为制造异常，消费失败,事务id:{}"+accountChangeEvent.getTxNo());
        }
    }
}
