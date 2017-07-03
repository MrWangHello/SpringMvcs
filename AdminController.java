package com.byzx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.byzx.po.admin;
import com.byzx.po.course1;
import com.byzx.po.root;
import com.byzx.po.student1;
import com.byzx.po.teacher1;
import com.byzx.service.IAdminService;
@Controller
public class AdminController {
	@Resource(name="adminService")
	private IAdminService adminService;
	@RequestMapping("/logins1")
	public String login(HttpSession session,Model m, admin a,root r, HttpServletRequest request) {
		a.setAdmin_id(request.getParameter("root_login"));
		admin ad = adminService.logins(a);
		if(ad!=null){
			System.out.println(r.getRoot_password());
			if(r.getRoot_password().equals(ad.getAdmin_password())){
				session.setAttribute("r_id", ad.getRoot_id());
				session.setAttribute("r_login", ad.getAdmin_id());
				return "index.jsp";
			}
			else{
				String s="密码不正确";
				m.addAttribute("message", s);
			return "forward:login11.jsp"; }
		}
		else {String s="帐号不存在";
		m.addAttribute("message", s);
		return "login11.jsp";}	
	}
	@RequestMapping("/infoA")
	public String findInfoA(admin a,HttpServletRequest request,Model m){
	a.setAdmin_id(request.getParameter("r_login"));
	List<admin> adm=adminService.findAdmin(a);
	m.addAttribute("adm", adm.get(0));
		return "backPage/info1.jsp";
	}
	@RequestMapping("/infoSt")
	public String InfoSt(student1 s,HttpServletRequest request,Model m){
		s.setStu_id(null);
		s.setStu_name(null);
	List<student1> st=adminService.findStudent1(s);
	m.addAttribute("list", st);
	m.addAttribute("r_id", 2);
		return "backPage/info1.jsp";
	}
	@RequestMapping("/infoTe")
	public String infoTe(teacher1 t,HttpServletRequest request,Model m){
		t.setTeacher_id(null);
		t.setTeacher_name(null);
	List<teacher1> te=adminService.findTeacher1(t);
	m.addAttribute("list", te);
	m.addAttribute("r_id", 3);
		return "backPage/info1.jsp";
	}
	@RequestMapping("/infoCo")
	public String findinfoCo(course1 c,HttpServletRequest request,Model m){
		c.setCourse_id(0);
		c.setCourse_name(null);
	List<course1> co=adminService.findCourse1(c);
	m.addAttribute("list", co);
	m.addAttribute("r_id", 0);
		return "backPage/info1.jsp";
	}
//	添加学生
	@RequestMapping("/addInfoSt.action")
//	解析json或xml时，将此转换为所需要的对象
	@ResponseBody
	public Map<String, Object> addInfo(student1 s){
	int r=adminService.addStudent1(s);
	Map<String, Object>map=new HashMap<String, Object>();
	map.put("n", r);
		return map;
	}
//	修改学生信息
	@RequestMapping("/updateInfoSt.action")
	public String selectInfoSt(student1 s){
		 adminService.updateStuInfo(s);
		 return "infoSt.action";
				 }
//	删除学生信息
	@RequestMapping("/deleteInfoStu.action")
	public String deleteInfoStu(student1 s,HttpServletRequest request){
		s.setStu_id(request.getParameter("sid"));
		 adminService.deleteStuInfo(s);
		 return "infoSt.action";
				 }
}
