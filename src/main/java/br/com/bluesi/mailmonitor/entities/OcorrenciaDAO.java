package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.eaio.stringsearch.BNDMWildcardsCI;
import com.sun.javafx.binding.StringFormatter;

import br.com.bluesi.mailmonitor.hibernate.SoBlueDAO;
import br.com.bluesi.mailmonitor.hibernate.hibernateUtil;
import br.com.bluesi.mailmonitor.util.RunsConstants;

/**
 * Home object for domain model class Ocorrencia.
 * 
 * @see br.com.bluesi.mailmonitor.entities.Ocorrencia
 * @author Hibernate Tools
 */
public class OcorrenciaDAO {

	final static Logger log = Logger.getLogger(OcorrenciaDAO.class);

	private static SoBlueDAO blueDAO = new SoBlueDAO();

	public static Configuracao getConfigPending() {
		ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
		Configuracao configSituation = configuracaoDAO.getConfiguracoes(RunsConstants.TICKET_SITUATION_PENDING_NAME,
				RunsConstants.TICKET_SITUATION_CONTEXT_NAME);

		if (configSituation == null) {
			configSituation = new Configuracao();
			configSituation.setChave(RunsConstants.TICKET_SITUATION_PENDING_NAME);
			configSituation.setContexto(RunsConstants.TICKET_SITUATION_CONTEXT_NAME);
			configSituation.setValor("Pendente");
			configuracaoDAO.persist(configSituation);
		}

		return configSituation;
	}

	public static Configuracao getConfigSeverity(String severity) {
		ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
		List<Configuracao> criticidades = configuracaoDAO.getConfiguracoes(RunsConstants.TICKET_SEVERITY_CONTEXT_NAME);

		Configuracao severityConfig = null;

		if (criticidades != null && criticidades.size() > 0) {
			for (Configuracao configuracao : criticidades) {
				if (configuracao.getValor().equals(severity))
					severityConfig = configuracao;
			}
		}

		if (severityConfig == null && severity != null) {
			severityConfig = new Configuracao();
			severityConfig.setChave(severity.toUpperCase());
			severityConfig.setContexto(RunsConstants.TICKET_SEVERITY_CONTEXT_NAME);
			severityConfig.setValor(severity);
			configuracaoDAO.persist(severityConfig);
		}

		return severityConfig;
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * ClienteDAO clienteDAO = new ClienteDAO();
	 * 
	 * Cliente cliente = clienteDAO.getClientes().get(0);
	 * 
	 * Ocorrencia ocorrencia = new Ocorrencia(null, cliente, getConfigPending(),
	 * getConfigSeverity("Critical"), null, "Titulo", "", new Date(), null,
	 * "Mensagem".getBytes(), "Av34", null);
	 * 
	 * TagBlue tagBlue = new TagBlue(); tagBlue.setNome(ocorrencia.getInstancia());
	 * 
	 * dao.persist(ocorrencia, tagBlue);
	 * 
	 * }
	 */

	public boolean persist(Ocorrencia ocorrencia, List<TagBlue> tagsBlue) {

		Session session = hibernateUtil.getSession();

		try {

			TagBlueDAO tagBlueDAO = new TagBlueDAO();

			Transaction transacao = session.beginTransaction();
			session.save(ocorrencia);
			session.flush();

			for (TagBlue tagBlue : tagsBlue) {
				
				List<TagBlue> tagsBlueList = tagBlueDAO.getTagByName(tagBlue.getNome());

				if (tagsBlueList == null || tagsBlueList.isEmpty()) {
					session.save(tagBlue);
					session.flush();
				} else {
					tagBlue = tagsBlueList.get(0);
				}

				TagsBlueOcorrenciaId id = new TagsBlueOcorrenciaId();
				id.setOcorrenciaIdFk(ocorrencia.getId());
				id.setTagBlueIdFk(tagBlue.getId());

				TagsBlueOcorrencia tagsBlueOcorrencia = new TagsBlueOcorrencia();

				tagsBlueOcorrencia.setId(id);
				tagsBlueOcorrencia.setOcorrencia(ocorrencia);
				tagsBlueOcorrencia.setTagBlue(tagBlue);
				tagsBlueOcorrencia.setDataAssociacao(new Date());
				tagsBlueOcorrencia.setUsuario(null);

				session.save(tagsBlueOcorrencia);


			}
			

			session.flush();
			transacao.commit();
			session.close();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				session.close();
			} catch (Exception e2) {
			}
		}

		return false;

	}

	public boolean persist(Ocorrencia transientInstance) {
		log.info("Cadastrando uma nova Ocorrencia");
		try {
			blueDAO.persist(transientInstance);
			log.info("Ocorrencia cadastrada com sucesso");
			return true;
		} catch (RuntimeException re) {
			log.error("Aconteceu um erro tentando cadastrar a Ocorrencia", re);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void remove(Ocorrencia persistentInstance) {
		log.debug("removing Ocorrencia instance");
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

	public void merge(Ocorrencia detachedInstance) {
		log.debug("merging Ocorrencia instance");
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

	public Ocorrencia findById(Integer id) {
		log.debug("getting Ocorrencia instance with id: " + id);
		try {
			Ocorrencia instance = (Ocorrencia) blueDAO.findById(Ocorrencia.class, id);
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

	@SuppressWarnings("unchecked")
	public List<Ocorrencia> getOcorrencias() {
		Session session = hibernateUtil.getSession();

		Criteria criterio = session.createCriteria(Ocorrencia.class);

		List<Ocorrencia> ocorrencias = (List<Ocorrencia>) criterio.list();

		session.close();

		return ocorrencias;
	}
}
