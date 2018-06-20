package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.bluesi.mailmonitor.hibernate.SoBlueDAO;
import br.com.bluesi.mailmonitor.hibernate.hibernateUtil;

/**
 * Home object for domain model class Configuracao.
 * @see br.com.bluesi.mailmonitor.entities.Configuracao
 * @author Hibernate Tools
 */
public class ConfiguracaoDAO {

	final static Logger log = Logger.getLogger(ClienteDAO.class);
	
	private static SoBlueDAO blueDAO = new SoBlueDAO();
	
	public static void main(String[] args) {
		ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();

		Configuracao transientInstance = new Configuracao("MONITORADOR",  "pix.teste@pix.com.br", "MAIL.USERNAME");
		Configuracao transientInstance2 = new Configuracao("MONITORADOR", "mail.pix.com.br", "MAIL.HOST");
		Configuracao transientInstance3 = new Configuracao("MONITORADOR", "pix2000", "MAIL.PASSWORD");
		
		List<Configuracao> configuracaos = new ArrayList<Configuracao>();
		configuracaos.add(transientInstance);
		configuracaos.add(transientInstance2);
		configuracaos.add(transientInstance3);
		
		
		
		
		for (Configuracao configuracao : configuracaos) {
			configuracaoDAO.persist(configuracao);	
		}
		
		
	   
		
		
	}
	
	public List<Configuracao> getConfiguracoes(String context){
		Session session = hibernateUtil.getSession();
		
		Criteria criterio = session.createCriteria(Configuracao.class);
		criterio.add(Restrictions.eq("contexto", context));
		
		List<Configuracao> areas = (List<Configuracao>) criterio.list();
		
		session.close();
		
		return areas;
	}
	
	@SuppressWarnings("unchecked")
	public Configuracao getConfiguracoes(String key, String context){
		Session session = hibernateUtil.getSession();
		
		Criteria criterio = session.createCriteria(Configuracao.class);
		criterio.add(Restrictions.eq("chave", key));
		criterio.add(Restrictions.eq("contexto", context));
		
		List<Configuracao> configuracoes = (List<Configuracao>) criterio.list();
		
		session.close();
		
		if(configuracoes != null && !configuracoes.isEmpty())
			return configuracoes.get(0);
		else
			return null;
	}
	
	

	public void persist(Configuracao transientInstance) {
		log.debug("persisting Configuracao instance");
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

	public void remove(Configuracao persistentInstance) {
		log.debug("removing Configuracao instance");
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

	public void merge(Configuracao detachedInstance) {
		log.debug("merging Configuracao instance");
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

	public Configuracao findById(Integer id) {
		log.debug("getting Configuracao instance with id: " + id);
		try {
			Configuracao instance = (Configuracao) blueDAO.findById(Configuracao.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
