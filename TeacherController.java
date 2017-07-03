package com.byzx.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.byzx.po.root;
import com.byzx.po.selects;
import com.byzx.po.teacher1;
import com.byzx.service.ITeacherService;

@Controller
public class TeacherController {
	@Resource(name = "teacherService")
	private ITeacherService teacherService;
	@RequestMapping("/logins3")
	public String login(Model m, teacher1 t, root r, HttpServletRequest request) {
		t.setTeacher_id(request.getParameter("r_login"));
		System.out.println(t.getTeacher_id());
		teacher1 te = teacherService.logins(t);
		if (te != null) {
			if (te.getTeacher_password().equals(r.getRoot_password())) {
				m.addAttribute("r_login", te.getTeacher_id());
				m.addAttribute("r_id", te.getRoot_id());
				return "index.jsp";
			} else {
				String s = "密码不正确";
				m.addAttribute("message", s);
				return "login11.jsp";
			}
		} else {
			String s = "帐号不存在";
			m.addAttribute("message", s);
			return "login11.jsp";
		}
	}

	// 查个人信息
	@RequestMapping("/infoT")
	public String stuInfo(teacher1 te, HttpServletRequest request, HttpSession session) {
		te.setTeacher_id(request.getParameter("r_login"));
		teacher1 tea = teacherService.findTeaByTea_id(te);
		session.setAttribute("tea", tea);
		return "backPage/info.jsp";

	}

	// 修改密码
	@RequestMapping("pass1.action")
	public String updatePass(teacher1 te, HttpServletRequest request, HttpSession session) {
		System.out.println(request.getParameter("r_login"));
		te.setTeacher_id(request.getParameter("r_login"));
		System.out.println(te.getNewpass());
		te.setTeacher_password(te.getNewpass());
		teacherService.updateTea_pwd(te);
		;
		teacher1 t = teacherService.findTeaByTea_id(te);
		session.setAttribute("tea", t);
		return "backPage/info.jsp";
	}

	// 查询自己教学课程
	@RequestMapping("/selectStu")
	public String findCourseByTid(teacher1 te, HttpServletRequest request, Model m) {
		te.setTeacher_id(request.getParameter("r_login"));
		List<selects> se = teacherService.findCourseByTid(te);
		m.addAttribute("rid", 3);
		m.addAttribute("selects", se);
		return "backPage/selectCourse.jsp";

	}
}
