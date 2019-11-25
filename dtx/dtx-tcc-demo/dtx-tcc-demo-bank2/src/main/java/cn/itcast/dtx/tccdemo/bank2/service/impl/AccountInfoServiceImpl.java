package cn.itcast.dtx.tccdemo.bank2.service.impl;

import cn.itcast.dtx.tccdemo.bank2.dao.AccountInfoDao;
import cn.itcast.dtx.tccdemo.bank2.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service("AccountInfo")
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Transactional
    @Hmily(cancelMethod = "cancelMethod",confirmMethod = "confirmMethod")
    @Override
    public void updateAccountBalance(String account, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("Bank2 Service  try end ,事务id:{}",transId);

    }
    @Transactional
    public void confirmMethod(String account, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("Bank2 Service  try begin ,事务id:{}",transId);
        int existConfirm = accountInfoDao.isExistConfirm(transId);
        if (existConfirm>0){
            log.info("Bank2 Service confirm已执行，无需任何操作");
            return;
        }
        //增加金额
        accountInfoDao.addAccountBalance(account,amount);
        //记录日志，幂等性标识
        accountInfoDao.addConfirm(transId);
        log.info("Bank2 Service confirm end");

    }
    @Transactional
    public void cancelMethod(String account, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        //金额增加,不需要做业务检查
        log.info("Bank2 Service  cancel end ,事务id:{}",transId);

    }
}
