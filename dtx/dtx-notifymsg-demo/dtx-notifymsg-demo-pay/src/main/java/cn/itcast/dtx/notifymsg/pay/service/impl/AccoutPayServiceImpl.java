package cn.itcast.dtx.notifymsg.pay.service.impl;

import cn.itcast.dtx.notifymsg.pay.dao.AccountPayDao;
import cn.itcast.dtx.notifymsg.pay.entity.AccountPay;
import cn.itcast.dtx.notifymsg.pay.model.AccountChangeEvent;
import cn.itcast.dtx.notifymsg.pay.service.AccountPayService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class AccoutPayServiceImpl  implements AccountPayService {
    @Autowired
    private AccountPayDao accountPayDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Transactional
    @Override
    public AccountPay insertAccountPay(AccountPay accountPay) {
        int result = accountPayDao.insertAccountPay(accountPay.getId(),
                accountPay.getAccountNo(), accountPay.getPayAmount(), "success");
        //充值成功发送通知
        if(result>0){
            rocketMQTemplate.convertAndSend("topic_notifymsg",accountPay);
            return accountPay;
        }
        return accountPay;
    }

    @Override
    public AccountPay getAccountPay(String txNo) {
        return accountPayDao.findByIdTxNo(txNo);
    }
}
