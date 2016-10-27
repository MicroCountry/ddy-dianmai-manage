package com.ddy.dianmai.ops.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddy.dianmai.ops.po.DDYRsp;
import com.ddy.dianmai.ops.po.User;
import com.ddy.dianmai.ops.service.RoleService;
import com.ddy.dianmai.ops.service.UserService;
import com.ddy.dianmai.ops.util.Page;

@Controller
@RequestMapping(value="/user",produces="text/plain;charset=UTF-8")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("sys:user:view")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String list(ModelMap model, HttpServletRequest request) {
    	int pageNo = 1;   
		int pageSize = 20;
		String pageNoString = request.getParameter("pageNo");
		String pageSizeString = request.getParameter("pageSize");
		
		if(StringUtils.isNotBlank(pageNoString)){
			pageNo = Integer.valueOf(pageNoString);
		}
		
		if(StringUtils.isNotBlank(pageSizeString)){
			pageSize = Integer.valueOf(pageSizeString);
		}
		
		Map<String, String> map = new HashMap<String, String>();
		Page<User> page = userService.fingPages(pageNo, pageSize, map);
        model.addAttribute("userList", /*userService.findAll()*/page.getList());
        model.addAttribute("page", page);
        model.addAttribute("pageNo", pageNo);
		model.addAttribute("pageSize", pageSize);
		DDYRsp rsp = new DDYRsp();
		rsp.setData(model);
        return rsp.toString();
    }

    @RequiresPermissions("sys:user:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public String showCreateForm(ModelMap model, HttpServletRequest request) {
        setCommonData(model);
        model.addAttribute("user", new User());
        model.addAttribute("op", "新增");
        model.addAttribute("icon_class", "icon-plus");
        DDYRsp rsp = new DDYRsp();
        rsp.setData(model);
        return rsp.toString();
    }

    
    @RequiresPermissions("sys:user:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(User user, RedirectAttributes redirectAttributes) {
        userService.createUser(user);
        DDYRsp rsp = new DDYRsp();
        rsp.setData("删除成功");
        return rsp.toString();
    }

    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public String showUpdateForm(@PathVariable("id") Long id, ModelMap model) {
        setCommonData(model);
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("op", "修改");
        model.addAttribute("icon_class", "icon-edit");
        DDYRsp rsp = new DDYRsp();
        rsp.setData(model);
        return rsp.toString();
    }

    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(User user, RedirectAttributes redirectAttributes) {
        userService.updateUser(user);
        DDYRsp rsp = new DDYRsp();
        rsp.setData("修改成功");
        return rsp.toString();
    }

    
    @RequiresPermissions("sys:user:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String showDeleteForm(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	userService.deleteUser(id);
    	DDYRsp rsp = new DDYRsp();
    	rsp.setData("删除成功");
    	return rsp.toString();
    }

    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/{id}/changePassword", method = RequestMethod.GET)
    @ResponseBody
    public String showChangePasswordForm(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("op", "修改密码");
        model.addAttribute("icon_class", "icon-edit");
        DDYRsp rsp = new DDYRsp();
        rsp.setData(model);
        return rsp.toString();
    }

    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/{id}/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public String changePassword(@PathVariable("id") Long id, String newPassword, RedirectAttributes redirectAttributes) {
        userService.changePassword(id, newPassword);
        DDYRsp rsp = new DDYRsp();
        rsp.setData("修改密码成功");
        return rsp.toString();
    }

    private void setCommonData(ModelMap model) {
        model.addAttribute("roleList", roleService.findAll());
    }
}
