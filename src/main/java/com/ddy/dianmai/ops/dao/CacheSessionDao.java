package com.ddy.dianmai.ops.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
public class CacheSessionDao extends CachingSessionDAO{

	@Resource
	private SessionDao sessionDao;
	
	@Override
	protected void doUpdate(Session session) {
		this.sessionDao.updateSession(session);
	}

	@Override
	protected void doDelete(Session session) {
		this.sessionDao.deleteSession(session.getId());
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        this.sessionDao.createSession(session);
		return session.getId();
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		return this.sessionDao.selectSession(sessionId);
	}

}
