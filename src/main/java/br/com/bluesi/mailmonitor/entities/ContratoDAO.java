package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
/**
 * Home object for domain model class Contrato.
 * @see br.com.bluesi.mailmonitor.entities.Contrato
 * @author Hibernate Tools
 */
@Stateless
public class ContratoDAO {

	final static Logger log = Logger.getLogger(AgrupamentoOcorrenciaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Contrato transientInstance) {
		log.debug("persisting Contrato instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Contrato persistentInstance) {
		log.debug("removing Contrato instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Contrato merge(Contrato detachedInstance) {
		log.debug("merging Contrato instance");
		try {
			Contrato result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Contrato findById(Integer id) {
		log.debug("getting Contrato instance with id: " + id);
		try {
			Contrato instance = entityManager.find(Contrato.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
