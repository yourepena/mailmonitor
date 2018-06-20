package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.bluesi.mailmonitor.hibernate.SoBlueDAO;
import br.com.bluesi.mailmonitor.hibernate.hibernateUtil;


/**
 * Home object for domain model class AgrupamentoOcorrencia.
 * @see br.com.bluesi.mailmonitor.entities.AgrupamentoOcorrencia
 * @author Hibernate Tools
 */
@Stateless
public class AgrupamentoOcorrenciaDAO {
	
	/*public static void main(String[] args) {
		AgrupamentoOcorrencia agrupamentoOcorrencia = new AgrupamentoOcorrencia();
		agrupamentoOcorrencia.setDataHora(new Date());
		agrupamentoOcorrencia.setNome("Agrupamento de Teste");
		agrupamentoOcorrencia.setOcorrencias(null);
		agrupamentoOcorrencia.setPalavrasChaves("palavra1,palavra2,palavra3");
		
		
		AgrupamentoOcorrenciaDAO agrupamentoOcorrenciaHome = new AgrupamentoOcorrenciaDAO();
		
		//agrupamentoOcorrenciaHome.persist(agrupamentoOcorrencia);
		
		List<AgrupamentoOcorrencia> agrupamentos = agrupamentoOcorrenciaHome.getAgrupamentos();
		
		for (AgrupamentoOcorrencia agrupamentoOcorrencia2 : agrupamentos) {
			System.out.println(agrupamentoOcorrencia2.getNome());
		}
		
	}*/
	
	private static SoBlueDAO blueDAO = new SoBlueDAO();
	
	final static Logger log = Logger.getLogger(AgrupamentoOcorrenciaDAO.class);
	
	public List<AgrupamentoOcorrencia> getAgrupamentos(){
		Session session = hibernateUtil.getSession();
		
		Criteria criterio = session.createCriteria(AgrupamentoOcorrencia.class);
		List<AgrupamentoOcorrencia> agrupamentos = (List<AgrupamentoOcorrencia>) criterio.list();
		
		session.close();
		
		return agrupamentos;
	}

	public void persist(AgrupamentoOcorrencia transientInstance) {
		log.debug("persisting AgrupamentoOcorrencia instance");
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

	public void remove(AgrupamentoOcorrencia persistentInstance) {
		
		log.debug("removing AgrupamentoOcorrencia instance");
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

	public  void merge(AgrupamentoOcorrencia detachedInstance) {
		
		log.debug("merging AgrupamentoOcorrencia instance");
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

	public AgrupamentoOcorrencia findById(Integer id) throws Exception {
		
		log.debug("getting AgrupamentoOcorrencia instance with id: " + id);
		try {
			AgrupamentoOcorrencia instance = (AgrupamentoOcorrencia) blueDAO.findById(AgrupamentoOcorrencia.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
