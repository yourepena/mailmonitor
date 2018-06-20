package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 * Home object for domain model class LogSoBlue.
 * @see br.com.bluesi.mailmonitor.entities.LogSoBlue
 * @author Hibernate Tools
 */
@Stateless
public class LogSoBlueDAO {

	final static Logger log = Logger.getLogger(AgrupamentoOcorrenciaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(LogSoBlue transientInstance) {
		log.debug("persisting LogSoBlue instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(LogSoBlue persistentInstance) {
		log.debug("removing LogSoBlue instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public LogSoBlue merge(LogSoBlue detachedInstance) {
		log.debug("merging LogSoBlue instance");
		try {
			LogSoBlue result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogSoBlue findById(Integer id) {
		log.debug("getting LogSoBlue instance with id: " + id);
		try {
			LogSoBlue instance = entityManager.find(LogSoBlue.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
