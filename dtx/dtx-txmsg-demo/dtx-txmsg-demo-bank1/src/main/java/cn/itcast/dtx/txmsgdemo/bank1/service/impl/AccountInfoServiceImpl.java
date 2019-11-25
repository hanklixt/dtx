package cn.itcast.dtx.txmsgdemo.bank1.service.impl;

import cn.itcast.dtx.txmsgdemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank1.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 向mq发送转账消息
     * @param accountChangeEvent
     */
    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange",accountChangeEvent);
        String jsonString = jsonObject.toJSONString(jsonObject);
        //转成消息体
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        /**
         * String txProducerGroup, 事务消息组
         * String destination, topic主题
         * Message<?> message, 消息体
         * Object arg  非必填参数
         * 向mq发送事务消息
         */

        rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1","txmsg_topic",message,null);
    }

    /**
     *
     * @param accountChangeEvent
     * 更新账户余额，同个事务里添加幂等性标识
     */
    @Override
    @Transactional
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        log.info("开始更新本地事务，事务号：{}",accountChangeEvent.getTxNo());
        //扣减余额
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount()*-1);
        //添加事务成功处理记录
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        //人为制造异常
        if(accountChangeEvent.getAmount() == 2){
            throw new RuntimeException("bank1更新本地事务时抛出异常");
        }
        log.info("结束更新本地事务，事务号：{}",accountChangeEvent.getTxNo());

    }
}
