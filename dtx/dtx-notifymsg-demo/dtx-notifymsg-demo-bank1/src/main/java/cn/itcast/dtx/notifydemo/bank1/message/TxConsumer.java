package cn.itcast.dtx.notifydemo.bank1.message;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import cn.itcast.dtx.notifydemo.bank1.service.AccountInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

@RocketMQMessageListener(topic = "topic_notifymsg",consumerGroup = "dtx_notifymsg_bank1")
public class TxConsumer implements RocketMQListener<AccountPay> {

    @Autowired
    private AccountInfoService accountInfoService;
    @Override
    public void onMessage(AccountPay accountPay) {
       accountInfoService.updateAccountBalance(accountPay);
    }
}
