package cn.itcast.dtx.notifymsg.pay.service;

import cn.itcast.dtx.notifymsg.pay.entity.AccountPay;
import cn.itcast.dtx.notifymsg.pay.model.AccountChangeEvent;

public interface AccountPayService {
    /**
     * 充值后发送通知
     * @param accountPay
     * @return
     */
    AccountPay insertAccountPay(AccountPay accountPay);

    /**
     * 提供远程调用接口供查询充值记录
     * @param txNo
     * @return
     */
    AccountPay getAccountPay(String txNo);
}
