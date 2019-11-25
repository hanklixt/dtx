package cn.itcast.dtx.txmsgdemo.bank1.service;

import cn.itcast.dtx.txmsgdemo.bank1.model.AccountChangeEvent;

public interface AccountInfoService {

    /**
     * 向mq发送转账消息
     * @param accountChangeEvent
     */
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

    /**
     * 更新账户金额
     * @param accountChangeEvent
     */
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

}
