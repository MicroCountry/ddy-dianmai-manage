package com.ddy.dianmai.ops.dao;

import java.io.Serializable;

import org.apache.shiro.session.Session;



public interface SessionDao {
	public Session createSession(Session session);
	public Session updateSession(Session session);
	public void deleteSession(Serializable id);
	public Session selectSession(Serializable id);
}
