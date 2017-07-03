package com.byzx.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.byzx.po.course1;
import com.byzx.po.root;
import com.byzx.po.selects;
import com.byzx.po.student1;
import com.byzx.service.IstudentService;
import com.byzx.utilts.Page;

@Controller
public class StudentController {
	@Resource(name = "studentService")
	private IstudentService studentService;

	// 登录

	@RequestMapping("/logins2")
	public String login(HttpSession session, root r, student1 s, Model m, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		s.setStu_id(request.getParameter("root_login"));
		student1 st = studentService.loginss(s);
		/* System.out.println(st.toString()); */
		if (st != null) {
			if (st.getStu_password().equals(r.getRoot_password())) {
				session.setAttribute("stu_id", st.getStu_id());
				session.setAttribute("r_id", st.getRoot_id());
				session.setAttribute("stu", st);
				return "index.jsp";
			} else {
				String str = "密码不正确";
				m.addAttribute("message", str);
				return "login11.jsp";
			}

		} else {
			String str = "帐号不存在";
			m.addAttribute("message", str);
			return "login11.jsp";
		}
	}

	@RequestMapping("/exit")
	public String eixt(HttpSession session) {
		// 关闭session
		session.invalidate();
		return "login11.jsp";
	}

	// 查个人信息
	@RequestMapping("/info")
	public String stuInfo(student1 se, HttpServletRequest request, HttpSession session) {
		se.setStu_id(request.getParameter("stuid"));
		student1 st = studentService.findStuByStu_id(se);
		System.out.println(st.getRoot_id());
		session.setAttribute("stu", st);
		return "backPage/info.jsp";

	}

	// 修改密码
	@RequestMapping("pass.action")
	public String updatePass(student1 se, HttpServletRequest request, HttpSession session) {
		se.setStu_id(request.getParameter("stuid"));
		se.setStu_password(se.getNewpass());
		studentService.updateStu_pwd(se);
		student1 st = studentService.findStuByStu_id(se);
		session.setAttribute("stu", st);
		return "backPage/info.jsp";
	}

	// 查询可选课程
	@RequestMapping("allCourse.action")
	public String allCourse(Model m, course1 c, HttpServletRequest request, Integer pageNum, Integer pageSize) {
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageSize == null) {
			pageSize = 1;
		}
		Integer startPage = (pageNum - 1) * pageSize;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startPage", startPage);
		map.put("pageSize", pageSize);
		// 查询的数据
		List<course1> course = studentService.findCourse(map);
		// 获取总条数
		Integer totalNum = studentService.count();
		Integer totalPage = (totalNum % pageSize == 0) ? (totalNum / pageSize) : (totalNum / pageSize + 1);
		m.addAttribute("totalPage", totalPage);
		m.addAttribute("pageNum", pageNum);
		System.out.println(request.getParameter("stuid"));
		m.addAttribute("stid", request.getParameter("stuid"));
		m.addAttribute("course", course);
		return "backPage/allCourse.jsp";
	}

	// 模糊查询
	@RequestMapping("/findCname")
	public String findCname(HttpServletRequest request, Model m) throws IOException {

		System.out.println(new String(request.getParameter("cosname").getBytes("iso8859-1"), "utf-8"));
		List<course1> course = studentService
				.findCname(new String(request.getParameter("cosname").getBytes("iso8859-1"), "utf-8"));
		m.addAttribute("course", course);
		return "backPage/allCourse.jsp";

	}

	// 选择课程
	@RequestMapping("/selectCourse")
	public String selectCourse(HttpServletRequest request, course1 c, selects sel) {
		c.setCourse_id(Integer.parseInt(request.getParameter("cid")));
		System.out.println(request.getParameter("stuid"));
		course1 course = studentService.findCourse1(c);
		sel.setStu_id(request.getParameter("stuid"));
		System.out.println(sel.getStu_id());
		sel.setSelects_id(course.getCourse_id());
		System.out.println(sel.getSelects_id());
		sel.setSelects_name(course.getCourse_name());
		System.out.println(sel.getSelects_name());
		sel.setSelects_profession(course.getCourse_profession());
		sel.setSelects_score(course.getCourse_score());
		sel.setSelects_room(course.getCourse_room());
		sel.setTeacher_id(course.getTeacher1().getTeacher_id());
		studentService.insertCourse(sel);
		return "selectCourse1.action";
	}

	// 查询已选课程
	@RequestMapping("/selectCourse1")
	public String selectCourse1(selects s, Model m, root r, HttpServletRequest request, Integer pageNum,
			Integer pageSize) {
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 1;
		}
		Page page = new Page(pageNum, pageSize);
		System.out.println(page.getTotalNum());
		page = studentService.searchPage(page);
		System.out.println(page.getTotal()+"+++++++++++++++++");
		s.setStu_id(request.getParameter("stuid"));
		List<selects> sel = studentService.myCourse(s);
		m.addAttribute("page", page);
		m.addAttribute("selects", sel);
		m.addAttribute("r_id", 2);
		return "backPage/selectCourse.jsp";
	}

	// 模糊查已选
	@RequestMapping("/selectCourse2")
	public String selectCourse2(selects se, Model m, HttpServletRequest request) throws Exception {
		System.out.println(new String(request.getParameter("cosname").getBytes("iso8859-1"), "utf-8"));
		se.setSelects_name(new String(request.getParameter("cosname").getBytes("iso8859-1"), "utf-8"));
		List<selects> sel = studentService.myCourse1(se);
		m.addAttribute("selects", sel);
		return "backPage/selectCourse.jsp";
	}

	// 删除已选课程
	@RequestMapping("/deleteCourse")
	public String deleteCourse(HttpServletRequest request) {
		studentService.deleteCourse(Integer.parseInt(request.getParameter("sid")));
		return "selectCourse1.action";
	}

}
