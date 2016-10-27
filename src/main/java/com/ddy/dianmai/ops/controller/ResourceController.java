package com.ddy.dianmai.ops.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddy.dianmai.ops.po.DDYRsp;
import com.ddy.dianmai.ops.po.Resource;
import com.ddy.dianmai.ops.service.ResourceService;


@Controller
@RequestMapping(value="/resource",produces="text/plain;charset=UTF-8")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ModelAttribute("types")
    public Resource.ResourceType[] resourceTypes() {
        return Resource.ResourceType.values();
    }

    @RequiresPermissions("sys:resource:view")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String list(ModelMap model, HttpServletRequest request) {
    	DDYRsp rsp = new DDYRsp();
    	rsp.setData(resourceService.findAll());
        return rsp.toString();
    }

    @RequiresPermissions("sys:resource:create")
    @RequestMapping(value = "/{parentId}/appendChild", method = RequestMethod.GET)
    @ResponseBody
    public String showAppendChildForm(@PathVariable("parentId") Long parentId, ModelMap model) {
        Resource parent = resourceService.findOne(parentId);
        model.addAttribute("parent", parent);
        Resource child = new Resource();
        child.setParentId(parentId);
        child.setParentIds(parent.makeSelfAsParentIds());
        model.addAttribute("resource", child);
        model.addAttribute("op", "新增子节点");
        model.addAttribute("icon_class", "icon-plus");
        if(parent.getPermission().indexOf("*") != -1){
        	model.addAttribute("permission", parent.getPermission().substring(0, parent.getPermission().indexOf("*")));
        }else{
        	model.addAttribute("permission", parent.getPermission());
        }
        DDYRsp rsp = new DDYRsp();
        rsp.setData(model);
        return rsp.toString();
    }

    @RequiresPermissions("sys:resource:create")
    @RequestMapping(value = "/{parentId}/appendChild", method = RequestMethod.POST)
    @ResponseBody
    public String create(Resource resource, RedirectAttributes redirectAttributes) {
        resourceService.createResource(resource);
        DDYRsp rsp = new DDYRsp();
        rsp.setData("新增子节点成功");
        return rsp.toString();
    }

    @RequiresPermissions("sys:resource:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public String showUpdateForm(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("resource", resourceService.findOne(id));
        model.addAttribute("op", "修改");
        model.addAttribute("icon_class", "icon-edit");
        DDYRsp rsp = new DDYRsp();
        rsp.setData(model);
        return rsp.toString();
    }

    @RequiresPermissions("sys:resource:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(Resource resource, RedirectAttributes redirectAttributes) {
        resourceService.updateResource(resource);
        DDYRsp rsp = new DDYRsp();
        rsp.setData("修改成功");
        return rsp.toString();
    }

    @RequiresPermissions("sys:resource:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        resourceService.deleteResource(id);
        DDYRsp rsp = new DDYRsp();
        rsp.setData("删除成功");
        return rsp.toString();
    }


}
