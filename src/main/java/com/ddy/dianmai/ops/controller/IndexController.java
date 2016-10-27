package com.ddy.dianmai.ops.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddy.dianmai.ops.annotation.CurrentUser;
import com.ddy.dianmai.ops.po.DDYRsp;
import com.ddy.dianmai.ops.po.Resource;
import com.ddy.dianmai.ops.po.User;
import com.ddy.dianmai.ops.service.ResourceService;
import com.ddy.dianmai.ops.service.UserService;
import com.google.gson.Gson;

@Controller
@RequestMapping(produces="text/plain;charset=UTF-8")
public class IndexController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    
    private Gson gson = new Gson();

    @RequestMapping("/")
    @ResponseBody
    public String index(@CurrentUser User loginUser, ModelMap model) {
        Set<String> permissions = userService.findPermissions(loginUser.getUsername());
        List<Resource> menus = resourceService.findMenus(permissions);
        DDYRsp rsp =new DDYRsp();
        rsp.setData(menus);
        return rsp.toString();
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return "index/welcome";
    }
    
    
    @RequestMapping(value = "submenu", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String submenu(@CurrentUser User loginUser, HttpServletRequest request){
    	Set<String> permissions = userService.findPermissions(loginUser.getUsername());
    	List<Resource> resources = new ArrayList<Resource>();
    	String parent_id = request.getParameter("parent_id");
    	List<Resource> list = resourceService.findMenus(Long.valueOf(parent_id), permissions);
    	for(Resource resource : list){
    		if(resourceService.findMenus(resource.getId(), permissions).size() > 0){
    			resource.setIsChild(true);
    		}
    		resources.add(resource);
    	}
    	DDYRsp rsp = new DDYRsp();
    	rsp.setData(resources);
    	return rsp.toString();
    }

}
