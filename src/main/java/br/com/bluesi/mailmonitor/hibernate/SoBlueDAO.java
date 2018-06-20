package br.com.bluesi.mailmonitor.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class SoBlueDAO {

	public void persist(Object obj) throws Exception {
		Session session = hibernateUtil.getSession();
		try {

			Transaction transacao = session.beginTransaction();
			session.save(obj);
			session.flush(); // optional
			transacao.commit();
			session.close();
		} catch (Exception e) {
			try {
				session.close();
			} catch (Exception e2) {
			}

			throw e;
		}

	}
	

	public void merge(Object obj) throws Exception {
		Session session = hibernateUtil.getSession();
		try {
			Transaction transacao = session.beginTransaction();
			session.update(obj);
			session.flush();
			transacao.commit();
			session.close();
		} catch (Exception e) {
			try {
				session.close();
			} catch (Exception e2) {
			}

			throw e;
		}
	}

	public void remove(Object obj) throws Exception {
		Session session = hibernateUtil.getSession();
		try {
			Transaction transacao = session.beginTransaction();
			session.delete(obj);
			session.flush();
			transacao.commit();
			session.close();
		} catch (Exception e) {
			try {
				session.close();
			} catch (Exception e2) {
			}

			throw e;
		}
	}

	public List listar(Class clazz, Session session, Criteria criteria) throws Exception {
		Transaction transacao = session.beginTransaction();
		List objts;
		objts = null;
		try {
			objts = criteria.list();
			transacao.commit();
			session.close();
		} catch (Exception e) {
			try {
				session.close();
			} catch (Exception e2) {
			}

			throw e;
		}
		return objts;
	}

	public Object findById(Class clazz, Integer id) throws Exception {
		Session session = hibernateUtil.getSession();
		
		Transaction transacao = session.beginTransaction();
		Object obj = null;
		try {
			Criteria criterio = session.createCriteria(clazz);
			criterio.add(Restrictions.idEq(id));

			obj = criterio.uniqueResult();
			transacao.commit();
			session.close();
		} catch (Exception e) {
			try {
				session.close();
			} catch (Exception e2) {
			}

			throw e;
		}
		return obj;
	}
 
}
