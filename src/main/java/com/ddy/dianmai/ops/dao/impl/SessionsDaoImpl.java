package com.ddy.dianmai.ops.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ddy.dianmai.ops.dao.SessionDao;
import com.ddy.dianmai.ops.util.SerializableUtils;

@Repository
public class SessionsDaoImpl implements SessionDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
	public Session createSession(Session session) {
		String sql = "insert into sys_sessions(id, session) values(?,?)";
        jdbcTemplate.update(sql, session.getId(), SerializableUtils.serialize(session));
		return session;
	}

	@Override
	public Session updateSession(Session session) {
		String sql = "update sys_sessions set session=? where id=?";
        jdbcTemplate.update(sql, SerializableUtils.serialize(session), session.getId());
        return session;
	}

	@Override
	public void deleteSession(Serializable id) {
		String sql = "delete from sys_sessions where id=?";
        jdbcTemplate.update(sql, id);
	}

	@Override
	public Session selectSession(Serializable id) {
		  String sql = "select session from sys_sessions where id=?";
	        List<String> sessionStrList = jdbcTemplate.queryForList(sql, String.class, id);
	        if(sessionStrList.size() == 0) return null;
	        return SerializableUtils.deserialize(sessionStrList.get(0));
	}

}
