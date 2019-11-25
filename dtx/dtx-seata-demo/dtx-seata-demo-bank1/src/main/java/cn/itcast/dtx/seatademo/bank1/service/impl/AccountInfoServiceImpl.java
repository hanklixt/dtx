package cn.itcast.dtx.seatademo.bank1.service.impl;

import cn.itcast.dtx.seatademo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.seatademo.bank1.service.AccountInfoService;
import cn.itcast.dtx.seatademo.bank1.spring.Bank2Client;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;
    @Autowired
    private Bank2Client bank2Client;

    //将@GlobalTransactional注解标注在全局事务发起的Service实现方法上，开启全局事务：
    //GlobalTransactionalInterceptor会拦截@GlobalTransactional注解的方法，生成全局事务ID(XID)，XID会在整个
    //分布式事务中传递。
    //在远程调用时，spring-cloud-alibaba-seata会拦截Feign调用将XID传递到下游服务。
    @GlobalTransactional
    @Transactional
    @Override
    public void updateAccount(String account, Double amount) {
        //张三扣减金额
        accountInfoDao.updateAccountBalance(account,amount*-1);
       //向李四转账
        String remoteRst = bank2Client.transfer(amount);
        //远程调用失败
        if(remoteRst.equals("fallback")){
            throw new RuntimeException("bank1 下游服务异常");
        }
        //人为制造错误
        if(amount==3){
            throw new RuntimeException("bank1 make exception 3");
        }

    }
}
