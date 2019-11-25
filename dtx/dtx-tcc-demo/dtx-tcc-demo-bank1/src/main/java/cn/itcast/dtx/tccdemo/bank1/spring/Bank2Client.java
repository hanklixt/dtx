package cn.itcast.dtx.tccdemo.bank1.spring;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "tcc-demo-bank2",fallback = Bank2ClientFallback.class)
public interface Bank2Client {
    @GetMapping("/bank2/transfer")
    @Hmily
    String transfer(@RequestParam("amount") Double amount);

}
