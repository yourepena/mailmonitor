package br.com.bluesi.mailmonitor.entities;
// Generated 07/08/2017 22:29:01 by Hibernate Tools 4.3.1.Final

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.bluesi.mailmonitor.hibernate.SoBlueDAO;
import br.com.bluesi.mailmonitor.hibernate.hibernateUtil;

/**
 * Home object for domain model class Cliente.
 * @see br.com.bluesi.mailmonitor.entities.Cliente
 * @author Hibernate Tools
 */
public class ClienteDAO {

	final static Logger log = Logger.getLogger(ClienteDAO.class);
	
	private static SoBlueDAO blueDAO = new SoBlueDAO();
	
	public static void main(String[] args) {
		
		/*String nome = "MEIO_AMBIENTE";
		String endereco = "Endereço do MEIO_AMBIENTE";
		String cnpj = "74951781000136";
		String telefone = "(61) 554556662";
		String contato = "contato@meio.com.br";
		String filterRuleJexl = "MSG.contain (SUBJECT,'Meio') or MSG.contain (SUBJECT,'MEIO')";
		String usarios_notificados = "Os usuários notificados são ssss@xxx.com";
		
		Cliente transientInstance = new Cliente(nome, endereco, cnpj, telefone, contato, filterRuleJexl.getBytes(), usarios_notificados, null, null);
		transientInstance.setId("1");*/
		
		
		String regra = "String subjectTmp = subject;"
	    		+ "int i = bndmWildcardsCI.searchString(subjectTmp, \":\");"
	    		+ " String text = subjectTmp.substring(0, i);"
	    		+ "	String[] tokens = text.split(\" \");"
	    		+ " String instance = tokens[2];"
	    		+ " subject = subject.substring(i + 1, subject.length() - 1); "
	    		+ " int j = bndmWildcardsCI.searchString(subject, \"alert\");"
	    		+ " String severity = subject.substring(0, j).trim();";
		
		ClienteDAO clienteDAO = new ClienteDAO();
		
		Cliente cliente;
		try {
			cliente = clienteDAO.findById(2);
			if(cliente != null) {
				cliente.setFilterRuleJexl(regra.getBytes());
				clienteDAO.merge(  cliente );	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	//	clienteDAO.persist(transientInstance );
		
	}
	
	public List<Cliente> getClientes(){
		Session session = hibernateUtil.getSession();
		
		Criteria criterio = session.createCriteria(Cliente.class);
		
		List<Cliente> clientes = (List<Cliente>) criterio.list();
		
		session.close();
		
		return clientes;
	}

	public void persist(Cliente transientInstance) {
		log.debug("persisting Cliente instance");
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

	public void remove(Cliente persistentInstance) {
		
		log.debug("removing Cliente instance");
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

	public void merge(Cliente detachedInstance) {
		log.debug("merging Cliente instance");
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

	public Cliente findById(Integer id) throws Exception {
		log.debug("getting Cliente instance with id: " + id);
		try {
			Cliente instance = (Cliente) blueDAO.findById(Cliente.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} catch (Exception e) {
			throw e;
		}
		
	}
}
