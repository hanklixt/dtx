package cn.itcast.dtx.notifydemo.bank1.spring;

import cn.itcast.dtx.notifydemo.bank1.entity.AccountPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(fallback = FClientFallback.class,name = "dtx-notifymsg-demo-pay")
public interface FClient {
    @GetMapping(value = "/pay/payresult/{txNo}")
    public AccountPay payresult(@PathVariable("txNo") String txNo);
}
