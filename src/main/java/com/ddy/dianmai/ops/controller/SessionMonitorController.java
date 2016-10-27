package com.ddy.dianmai.ops.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddy.dianmai.ops.Constants;
import com.ddy.dianmai.ops.po.DDYRsp;
import com.ddy.dianmai.ops.taglib.Functions;




/**
 * @ClassName SessionController.java
 * @Description 在线会话管理
 *
 * @author 1904
 * @version V1.0 
 * @Date 2015年6月24日 下午3:41:43
 */
@Controller
@RequestMapping(value="/sessions",produces="text/plain;charset=UTF-8")
public class SessionMonitorController {
	@Autowired
	private SessionDAO sessionDAO;

	
	@RequiresPermissions("sys:session:view")
	@RequestMapping()
	@ResponseBody
	public String list(ModelMap model) {
		List<Session> sessions = new ArrayList<Session>();
		Collection<Session> sessiondao = sessionDAO.getActiveSessions();  //获取所有活跃会话集合
		for(Session session : sessiondao){
			String username = Functions.principal(session);
			if(session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) != null || username == null){
				continue;
			}
			sessions.add(session);
		}
		model.addAttribute("sessions", sessions);
		model.addAttribute("sessionCount", sessions.size());
		DDYRsp rsp = new DDYRsp();
		rsp.setData(model);
		return rsp.toString();
	}

	
	@RequiresPermissions("sys:session:forceLogout")
	@RequestMapping("/{sessionId}/forceLogout")
	@ResponseBody
	public String forceLogout(@PathVariable("sessionId") String sessionId, RedirectAttributes redirectAttributes) {
		try {
			Session session = sessionDAO.readSession(sessionId);
			if (session != null) {
				session.setAttribute(Constants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);
			}
		} catch (Exception e) {
		}
		DDYRsp rsp = new DDYRsp();
		rsp.setData("强制退出成功");
		return rsp.toString();
	}
}