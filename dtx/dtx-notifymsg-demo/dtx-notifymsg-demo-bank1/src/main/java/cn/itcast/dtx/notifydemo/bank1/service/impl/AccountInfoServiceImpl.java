package cn.itcast.dtx.notifydemo.bank1.service.impl;

import cn.itcast.dtx.notifydemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import cn.itcast.dtx.notifydemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Transactional
    @Override
    public void updateAccountBalance(AccountPay accountPay) {
        if (accountInfoDao.isExistTx(accountPay.getId())>0){
            return ;
        }
         accountInfoDao.updateAccountBalance(accountPay.getAccountNo(), accountPay.getPayAmount());
        accountInfoDao.addTx(accountPay.getId());

    }
}
