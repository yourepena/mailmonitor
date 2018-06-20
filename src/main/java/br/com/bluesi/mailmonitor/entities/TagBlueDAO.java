package br.com.bluesi.mailmonitor.entities;
// Generated 01/09/2017 00:05:13 by Hibernate Tools 5.2.3.Final

import java.util.List;

import javax.ejb.Stateless;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.bluesi.mailmonitor.hibernate.SoBlueDAO;
import br.com.bluesi.mailmonitor.hibernate.hibernateUtil;

/**
 * Home object for domain model class TagBlue.
 * @see br.com.bluesi.mailmonitor.entities2.TagBlue
 * @author Hibernate Tools
 */
@Stateless
public class TagBlueDAO {

	final static Logger log = Logger.getLogger(OcorrenciaDAO.class);
	
	private static SoBlueDAO blueDAO = new SoBlueDAO();
	
	
	public List<TagBlue> getTagByName(String nome){
		Session session = hibernateUtil.getSession();
		
		Criteria criterio = session.createCriteria(TagBlue.class);
		criterio.add(Restrictions.eq("nome", nome));
		
		List<TagBlue> clientes = (List<TagBlue>) criterio.list();
		
		session.close();
		
		return clientes;
	}

	public void persist(TagBlue transientInstance) {
		log.debug("persisting TagBlue instance");
		try {
			blueDAO.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(TagBlue persistentInstance) {
		log.debug("removing TagBlue instance");
		try {
			blueDAO.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void merge(TagBlue detachedInstance) {
		log.debug("merging TagBlue instance");
		try {
			blueDAO.merge(detachedInstance);
			log.debug("merge successful");
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TagBlue findById(Integer id) {
		log.debug("getting TagBlue instance with id: " + id);
		try {
			TagBlue instance = (TagBlue) blueDAO.findById(TagBlue.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
