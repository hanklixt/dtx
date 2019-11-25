package cn.itcast.seatademo.bank2.service.impl;

import cn.itcast.seatademo.bank2.dao.AccountInfoDao;
import cn.itcast.seatademo.bank2.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AccountInfo")
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;
    //分支事务(非全局事务发起方，只需开启本地事务)
    @Transactional
    @Override
    public void updateAccountBalance(String account, Double amount) {
        log.info("李四增加金额");
        //李四增加金额
        accountInfoDao.updateAccountBalance(account,amount);
          //制造异常
        if(amount==2){
            throw new RuntimeException("bank1 make exception 2");
        }
    }
}
