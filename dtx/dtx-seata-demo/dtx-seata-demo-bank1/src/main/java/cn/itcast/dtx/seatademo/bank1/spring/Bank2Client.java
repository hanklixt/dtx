package cn.itcast.dtx.seatademo.bank1.spring;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-demo-bank2",fallback = Bank2ClientFallback.class)
public interface Bank2Client {
    @GetMapping("/bank2/transfer")
    String transfer(@RequestParam("amount") Double amount);
}
