package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 * Home object for domain model class Interacao.
 * @see br.com.bluesi.mailmonitor.entities.Interacao
 * @author Hibernate Tools
 */
@Stateless
public class InteracaoDAO {

	final static Logger log = Logger.getLogger(AgrupamentoOcorrenciaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Interacao transientInstance) {
		log.debug("persisting Interacao instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Interacao persistentInstance) {
		log.debug("removing Interacao instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Interacao merge(Interacao detachedInstance) {
		log.debug("merging Interacao instance");
		try {
			Interacao result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Interacao findById(Integer id) {
		log.debug("getting Interacao instance with id: " + id);
		try {
			Interacao instance = entityManager.find(Interacao.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
