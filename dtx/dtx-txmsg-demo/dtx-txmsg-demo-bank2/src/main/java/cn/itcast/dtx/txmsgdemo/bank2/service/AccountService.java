package cn.itcast.dtx.txmsgdemo.bank2.service;

import cn.itcast.dtx.txmsgdemo.bank2.model.AccountChangeEvent;

public interface AccountService {
    //更新账户金额
    void addAccountInfoBalance(AccountChangeEvent accountChangeEvent);
}
