package cn.itcast.dtx.seatademo.bank1.controller;

import cn.itcast.dtx.seatademo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Bank1Controller {


        @Autowired
        AccountInfoService accountInfoService;
        //掉用bank2服务转账
        @GetMapping("/transfer")
        public String transfer(@RequestParam("amount") Double amount){
            accountInfoService.updateAccount("1",amount);
            return "bank1"+amount;
        }


}
