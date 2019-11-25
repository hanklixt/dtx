package cn.itcast.dtx.txmsgdemo.bank1.message;

import cn.itcast.dtx.txmsgdemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank1.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 接口实现类，实现执行本地事务和事务回查两个方法。
 * @author lxt
 * @date 2019-11-24
 */
@Slf4j
@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1")
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccountInfoDao accountInfoDao;
    /**
     * 当事务消息发送成功之后，mqServer会回调此方法
     * @param message
     * @param o
     * @return
     */
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try{
            AccountChangeEvent accountChangeEvent = parseMessage(message);
            //账户更新
            accountInfoService.doUpdateAccountBalance(accountChangeEvent);
            //操作成功，返回commit标识，mq会将该条消息修改为可消费
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            e.printStackTrace();
            log.error("executeLocalTransaction 事务执行失败",e);
            //在更新余额时出现异常，mq会将该条消息删除，不让消费方消费
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 当由于各种原因mqServer未收到Commit指令后，会回调此方法检查消息提交状态
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        AccountChangeEvent accountChangeEvent = parseMessage(message);
        String txNo = accountChangeEvent.getTxNo();
        int existTx = accountInfoDao.isExistTx(txNo);
        //如果mqserver未收到commit指令，但是，该本地事务已经成功提交，则认为状态正常，返回Commit指令，让消费者可消费
        if (existTx>0){
            return RocketMQLocalTransactionState.COMMIT;
        }
        //未知，mq会重试检查
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    private AccountChangeEvent parseMessage(Message message) {
        Object payload = message.getPayload();
        //mq发送成功，取出消息参数执行账户余额更新
        String msg=new String((byte[]) payload);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String accountChange = jsonObject.getString("accountChange");
        return JSONObject.parseObject(accountChange, AccountChangeEvent.class);
    }
}
