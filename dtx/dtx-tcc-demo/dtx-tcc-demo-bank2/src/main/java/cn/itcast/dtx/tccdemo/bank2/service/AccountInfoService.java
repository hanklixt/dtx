package cn.itcast.dtx.tccdemo.bank2.service;

public interface AccountInfoService {

    /**
     * 更新金额
     * @param account
     * @param amount
     */
    void updateAccountBalance(String account,Double amount);
}
