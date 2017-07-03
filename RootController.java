package com.byzx.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.byzx.po.root;
import com.byzx.service.IRootService;

@Controller
public class RootController {
@Resource(name="rootService")
private IRootService rootService;
@RequestMapping("/login")
public String login(root r){
	if(r.getRoot_id()==2){
	
		return "logins2.action?r_login="+r.getRoot_login();
	}
	if(r.getRoot_id()==3){
		
		return "logins3.action?r_login="+r.getRoot_login();
	}
	else{return "logins1.action?r_login="+r.getRoot_login();}
	}
}
