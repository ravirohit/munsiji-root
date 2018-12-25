package com.munsiji.hibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class HibernateCfg {
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		Session session=sessionFactory.openSession();
		return session;
	}

}
