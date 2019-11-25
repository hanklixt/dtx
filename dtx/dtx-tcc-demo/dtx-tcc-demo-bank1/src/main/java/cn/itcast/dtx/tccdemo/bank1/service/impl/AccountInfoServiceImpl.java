package cn.itcast.dtx.tccdemo.bank1.service.impl;

import cn.itcast.dtx.tccdemo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.tccdemo.bank1.service.AccountInfoService;
import cn.itcast.dtx.tccdemo.bank1.spring.Bank2Client;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.bean.context.HmilyTransactionContext;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("AccountInfo")
public class AccountInfoServiceImpl  implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;

   @Autowired
   private Bank2Client bank2Client;

    /**
     *
     * @param account
     * @param amount
     */
    @Hmily(cancelMethod = "cancel",confirmMethod = "confirm")  //加上@Hmily注解，默认该方法为try方法,开启全局事务
    @Transactional
    @Override
    public void updateUserBalance(String account, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("******** Bank1 Service begin try... "+transId );
        //检查幂等性  如果已经存在该事务回滚的记录，不再执行任务
        int existTry = accountInfoDao.isExistTry(transId);
        if (existTry>0){
            log.info("Bank1 Service 已经存在事务id为:{}的try操作，无需执行try操作",transId);
            return;
        }
        //悬挂操作
        if (accountInfoDao.isExistCancel(transId)>0 ||accountInfoDao.isExistConfirm(transId)>0){
            log.info("bank1 Service  已经执行 id:{} 的confirm 或cancal 流程，悬挂处理，无需执行操作",transId);
            return;
        }
        //检查余额扣减并扣减余额
        if (accountInfoDao.subtractAccountBalance(account,amount)<=0){
            throw  new RuntimeException("账号扣减失败，请检查余额，事务id:{}"+transId);
        }
        //账号扣减成功后增加try执行记录，方便校验幂等性
        accountInfoDao.addTry(transId);
        String transfer = bank2Client.transfer(amount);
        //回滚以上本地事务
        if ("fallback".equals(transfer)){
            throw new RuntimeException("调用bank2增加金额失败，事务id"+transId);
        }
        if (amount==10){
            throw new RuntimeException("人为制造异常，回滚以上事务");
        }
        //try方法结束
        log.info("******** Bank1 Service end try... "+transId );
    }

    /**
     * confirm方法
     * @param account
     * @param amount
     */
    @Transactional
    public void confirm(String account, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("******** Bank1 Service end confirm:事务id{}:",transId);
    }

    /**
     * cancel方法
     * @param account
     * @param amount
     */
    @Transactional
    public void cancel(String account, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("******** Bank1 Service begin cancel(rollback)");
        int existTry = accountInfoDao.isExistTry(transId);
        if (existTry==0){
            log.info("Bank1 Service try执行失败，什么也不用做");
            return;
        }
        //空回滚
        if (accountInfoDao.isExistCancel(transId)>0){
            log.info("Bank1 Service cancel 已执行，无需回滚");
            return;
        }
        //执行回滚操作
        accountInfoDao.addAccountBalance(account,amount);
        //添加cancel执行记录，添加保持幂等性标识
        accountInfoDao.addCancel(transId);
        log.info("Bank1 Service cacel end");
    }



}
