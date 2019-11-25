package cn.itcast.seatademo.bank2.controller;

import cn.itcast.seatademo.bank2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Bank2Controller {

    @Autowired
    private AccountInfoService accountInfoService;

    @GetMapping("/transfer")
    public   String transfer(@RequestParam("amount") Double amount){
          accountInfoService.updateAccountBalance("2",amount);
          return "bank2"+amount;
    }
}
