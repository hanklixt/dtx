package cn.itcast.dtx.tccdemo.bank1.service;

public interface AccountInfoService {

    /**
     * 转账服务
     * @param account
     * @param amount
     */
    public void updateUserBalance(String account,Double amount);
}
