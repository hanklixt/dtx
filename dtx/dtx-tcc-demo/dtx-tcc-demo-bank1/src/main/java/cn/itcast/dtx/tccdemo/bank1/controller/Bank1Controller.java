package cn.itcast.dtx.tccdemo.bank1.controller;

import cn.itcast.dtx.tccdemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Bank1Controller {

    @Resource(name = "AccountInfo")
    private AccountInfoService accountInfoService;
    @RequestMapping("/transfer")
    public String test(@RequestParam("amount") Double amount) {
        accountInfoService.updateUserBalance("1", amount);
        return "bank1:" + amount;
    }
}