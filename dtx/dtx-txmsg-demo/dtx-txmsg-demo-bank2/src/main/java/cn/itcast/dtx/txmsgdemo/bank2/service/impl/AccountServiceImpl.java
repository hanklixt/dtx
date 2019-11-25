package cn.itcast.dtx.txmsgdemo.bank2.service.impl;

import cn.itcast.dtx.txmsgdemo.bank2.dao.AccountInfoDao;
import cn.itcast.dtx.txmsgdemo.bank2.model.AccountChangeEvent;
import cn.itcast.dtx.txmsgdemo.bank2.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Transactional
    @Override
    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
        log.info("修改账户金额，事务id:{}",accountChangeEvent.getTxNo());
        //检查幂等性
        if (accountInfoDao.isExistTx(accountChangeEvent.getTxNo())>0){
            return;
        }
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        accountInfoDao.addTx(accountChangeEvent.getTxNo());

    }
}
