package com.ddy.dianmai.ops.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ddy.dianmai.ops.dao.ResourceDao;
import com.ddy.dianmai.ops.po.Resource;
import com.ddy.dianmai.ops.service.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    public Resource createResource(Resource resource) {
        return resourceDao.createResource(resource);
    }

    public Resource updateResource(Resource resource) {
        return resourceDao.updateResource(resource);
    }

    public void deleteResource(Long resourceId) {
        resourceDao.deleteResource(resourceId);
    }

    public Resource findOne(Long resourceId) {
        return resourceDao.findOne(resourceId);
    }

    public List<Resource> findAll() {
        return resourceDao.findAll();
    }

    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        for(Long resourceId : resourceIds) {
            Resource resource = findOne(resourceId);
            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }

    public List<Resource> findMenus(Set<String> permissions) {
        List<Resource> allResources = findAll();
        List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : allResources) {
            if(resource.isRootNode()) {
                continue;
            }
            if(resource.getType() != Resource.ResourceType.menu) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            if(resource.getParentIds().split("/").length != 2) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }

    private boolean hasPermission(Set<String> permissions, Resource resource) {
        if(StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }
        for(String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
            if(p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }

	@Override
	public List<Resource> findMenus(Long parent_id, Set<String> permissions) {
		List<Resource> sesources = resourceDao.findMenus(parent_id);
		List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : sesources) {
            if(resource.isRootNode()) {
                continue;
            }
            if(resource.getType() != Resource.ResourceType.menu) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
	}
}
