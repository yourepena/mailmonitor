package br.com.bluesi.mailmonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.eaio.stringsearch.BNDMWildcardsCI;

import br.com.bluesi.mailmonitor.entities.Cliente;
import br.com.bluesi.mailmonitor.entities.ClienteDAO;
import br.com.bluesi.mailmonitor.entities.Configuracao;
import br.com.bluesi.mailmonitor.entities.ConfiguracaoDAO;
import br.com.bluesi.mailmonitor.entities.Ocorrencia;
import br.com.bluesi.mailmonitor.entities.OcorrenciaDAO;
import br.com.bluesi.mailmonitor.entities.TagBlue;
import br.com.bluesi.mailmonitor.util.RunsConstants;
import bsh.EvalError;
import bsh.Interpreter;

public class MailMonitor {

	private String folderSrc;
	private String folderGoal;
	private Integer timeout;
	private String rootPath;
	private String attachmentFolder;

	private String username;
	private String password;
	private String host;

	final Logger logger = Logger.getLogger(MailMonitor.class);

	private void carregarConfigs() {
		ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
		
		this.folderSrc 			= configuracaoDAO.getConfiguracoes(RunsConstants.FOLDERSRC_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null ? configuracaoDAO.getConfiguracoes(RunsConstants.FOLDERSRC_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor() : "inbox";
		this.folderGoal 		= configuracaoDAO.getConfiguracoes(RunsConstants.FOLDERGOAL_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null ? configuracaoDAO.getConfiguracoes(RunsConstants.FOLDERGOAL_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor()	: "MONITORADOS";
		this.timeout			= configuracaoDAO.getConfiguracoes(RunsConstants.TIMEOUT_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null	? Integer.parseInt(configuracaoDAO.getConfiguracoes(RunsConstants.TIMEOUT_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor()) : 5;
		this.rootPath 			= configuracaoDAO.getConfiguracoes(RunsConstants.ROOTPATH_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null ? configuracaoDAO.getConfiguracoes(RunsConstants.ROOTPATH_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor() : "/opt/monitorador/outbox/";
		this.attachmentFolder	= configuracaoDAO.getConfiguracoes(RunsConstants.ATTACHMENT_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null ? configuracaoDAO.getConfiguracoes(RunsConstants.ATTACHMENT_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor() : "/opt/monitorador/outbox/anexos/";
		this.username 			= configuracaoDAO.getConfiguracoes(RunsConstants.USERNAME_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null ? configuracaoDAO.getConfiguracoes(RunsConstants.USERNAME_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor() : "ops@bluesi.com.br";
		this.host 				= configuracaoDAO.getConfiguracoes(RunsConstants.HOST_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null ? configuracaoDAO.getConfiguracoes(RunsConstants.HOST_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor() : "smtp.gmail.com";
	
		String passwordEncrypt = configuracaoDAO.getConfiguracoes(RunsConstants.PASSWORD_NAME, RunsConstants.MAIL_CONTEXT_NAME) != null	? configuracaoDAO.getConfiguracoes(RunsConstants.PASSWORD_NAME, RunsConstants.MAIL_CONTEXT_NAME).getValor() : "YhPFoBRYRpdHyZU0lDbUsQ==";

		this.password =  EncriptaDecriptaDES.decryptString(passwordEncrypt);

	}


	public static void main(String[] args) {

		MailMonitor gmail = new MailMonitor();

		while (true) {
			try {
//				PropertyConfigurator.configure("/opt/monitorador/log4j.properties");
				PropertyConfigurator.configure("/dados/monitorador/log4j.properties");
//				PropertyConfigurator.configure("/dados/monitorador/log4j_proc.properties");
//				PropertyConfigurator.configure("C:\\Users\\youre.fernandez\\Documents\\blue\\deployment\\log4j_proc.properties");
				gmail.read();
				Thread.sleep(gmail.getTimeout() * 1000);
			} catch (InterruptedException e) {
				System.out.println("Terminando o processo. Tchau...");
				e.printStackTrace();
			}
		}

	}

	public void read() {

		carregarConfigs();

		ClienteDAO clienteDAO = new ClienteDAO();

		logger.info(String.format(
				"Iniciando o monitorador com as seguintes configurações:\n Pasta Origem: [%s]\n Pasta de Destino: [%s]\n Timeout de Processamento: [%s]\n Home do Projeto: [%s]\n Diretório para download dos anexos: [%s]\nUsário email: [%s]\n",
				folderSrc, folderGoal, timeout, rootPath, attachmentFolder, username));

		Store store = null;
		Folder inbox = null;

		try {

			Properties props = new Properties();

//			props.load(new FileInputStream(new File("C:\\Users\\youre.fernandez\\workspace\\mailmonitor\\src\\main\\resources\\smtp.properties")));
//			props.load(new FileInputStream(new File("/opt/monitorador/smtp.properties")));
			props.load(new FileInputStream(new File("/dados/monitorador/smtp.properties")));

			if (logger.isDebugEnabled())
				logger.debug("Carregando as propriedades do smtp");

			Session session = Session.getDefaultInstance(props, null);
			store = session.getStore("imaps");

			if (logger.isDebugEnabled())
				logger.debug("Propriedades carregadas com sucesso");

			store.connect(host, username, password);

			logger.info(String.format("Conectando no host [%s], com usuário [%s] e senha [%s]\n", host, username, toHidePass(password)));

			inbox = store.getFolder(folderSrc);
			if (inbox == null || !inbox.exists()) {
				logger.error(String.format("Pasta [%s] inválida. Finalizando programa...", folderSrc));
				System.exit(1);
			}

			int messageCount = inbox.getMessageCount();
			if (messageCount == 0) { // No messages in the source folder
				logger.info(String.format("Pasta [%s] está vazia. Esperando o timeout de [%s] segundos\n", folderSrc, timeout));
			}

			inbox.open(Folder.READ_WRITE);

			if (logger.isDebugEnabled())
				logger.debug(String.format("Abrindo a pasta [%s] em modo de leitura e escrita\n", folderSrc));

			Message[] messages = inbox.getMessages();

			if (messages != null && messages.length != 0) {

				logger.info(String.format("Total de mensagens na pasta [%s]: [%s]\n", folderSrc, messageCount));

				List<Cliente> clientes = clienteDAO.getClientes();

				if (clientes != null) {
					logger.info(String.format("[%s] clientes cadastrados\n", clientes.size()));

					for (Cliente cliente : clientes) {
						String clienteNome = cliente.getNome();
						String condition = new String(cliente.getFilterRuleJexl());
						String email = cliente.getContato();

						logger.info(String.format("Lendo as mensagens do cliente [%s] com a regra [%s]", clienteNome, condition));

						for (int i = messages.length - 1; i >= 0; i--) {
							logger.info(String.format("Mensagem [%s] de [%s]. Assunto: %s", (i + 1), messageCount, messages[i].getSubject()));

							if (messages[i].getFrom() != null && messages[i].getFrom().length > 0) {

								Address[] adress = messages[i].getFrom();
								boolean contain = false;
								for (int j = 0; j < adress.length; j++) {
									if (adress[j].toString().toLowerCase().contains(email)) {
										contain = true;
									}
								}

								if (contain) {
									logger.info(String.format("O email [%s] pertence ao cliente [%s] ", messages[i].getSubject(), clienteNome));
									Ocorrencia oco = createOcorrencia(messages[i], cliente) ;
									if(oco != null)
										logger.info(String.format("Ocorrência cadastrada com sucesso: id: ", oco.getId()));
									
									moveMail(store, inbox, clienteNome, new Message[] {messages[i]} );
									logger.info("Movendo email para a pasta Monitorados");
								} else
									logger.info(String.format("O email monitorado não corresponde com o email [%s] do cliente [%s]", email, clienteNome));

							} else {
								logger.info(String.format("Não foi possível encontrar o remetente do email", messages[i].getSubject()));
							}
						}
					}

				} else {
					logger.error(String.format(
							"Não tem clientes cadastrados. Por favor cadastre novos clientes para fazer a monitoração dos emails.\n Esperando o timeout de [%s] segundos",
							timeout));
				}
			} else {
				logger.error(String.format("Não foi possivel ler os mensagens da pasta [%s]. Esperando o timeout de [%s] segundos",	folderSrc, timeout));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Aconteceu um erro inesperado. Esperando o timeout de [%s] segundos", timeout),
					e);
		} finally {
			if (inbox != null) {
				try {
					inbox.close(true);
					if (logger.isDebugEnabled())
						logger.debug(String.format("Fechando a pasta [%s], Esperando o timeout de [%s] segundos",
								folderSrc, timeout));
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}

			if (store != null) {
				try {
					store.close();
					if (logger.isDebugEnabled())
						logger.debug(
								String.format("Fechando a sessão imap, Esperando o timeout de [%s] segundos", timeout));
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	private void moveMail(Store store, Folder inbox, String clienteNome, Message[] msgsArray) throws MessagingException {
		Folder dfolder = store.getFolder(folderGoal + "_" + clienteNome);
		if (!dfolder.exists()) {
			logger.info(String.format("A pasta [%s] não existe. Procedendo com a criação dela\n",
					folderGoal));
			dfolder.create(Folder.HOLDS_MESSAGES);
		}

		inbox.copyMessages(msgsArray, dfolder);

		inbox.setFlags(msgsArray, new Flags(Flags.Flag.DELETED), true);

		int countNotDelete = 0;
		for (Message message : msgsArray) {
			if (!message.isSet(Flags.Flag.DELETED))
				countNotDelete++;
		}

		if (countNotDelete > 0)
			logger.error(String.format("[%s] mensagens não foram deletas da pasta de origem [%s]\n",
					countNotDelete, folderSrc));

	}

	private Ocorrencia createOcorrencia(Message msg, Cliente cliente) throws MessagingException, IOException {
		String attachFiles = "";
		String messageContent = "";
		String attachName = null;

		String contentType = msg.getContentType();

		Ocorrencia ocorrencia = null;


		if (logger.isDebugEnabled())
			logger.debug(String.format("ContentType = [%s]", contentType));

		if (contentType.toLowerCase().contains("multipart")) {
			Multipart multiPart = (Multipart) msg.getContent();

			int numberOfParts = multiPart.getCount();

			for (int partCount = 0; partCount < numberOfParts; partCount++) {

				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);

				logger.debug(String.format("Parte #%s - Disposition=%s text/plain=%s text/html=%s\n", partCount, part.getDisposition(), part.isMimeType("text/plain"), part.isMimeType("text/html")));

				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {

					String fileName = part.getFileName();
					attachFiles += fileName + ", ";

					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}

					attachName = attachmentFolder + File.separator + fileName;
					part.saveFile(attachName);

					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Possui anexo(s) [%s]. Gravando em [%s]", fileName, rootPath));
					}

				} else {
					messageContent = getText(part);
				}
			}

		} else if (contentType.toLowerCase().contains("text/plain") || contentType.toLowerCase().contains("text/html")) {
			Object content = msg.getContent();
			if (content != null) {
				messageContent = content.toString();


				if (logger.isDebugEnabled())
					logger.debug("Não possui anexos.");
			}
		}
		
		messageContent = tratarBodyMsg(messageContent);
		
		ArrayList<Object> tokens = filterSubject(msg.getSubject(), messageContent,  new String(cliente.getFilterRuleJexl()));

		String instance = null;
		String severity = null;
		
		ArrayList<String> tags = null;

		if (tokens != null && tokens.size() > 0) {
			instance = (String) tokens.get(0);
			severity = (String) tokens.get(1);
			tags = (ArrayList<String>) tokens.get(2);
		}
		
		Configuracao configSituation = getConfigPending();
		Configuracao severityConfig = getConfigSeverity(severity);
		
		ocorrencia = new Ocorrencia(null, cliente, configSituation, severityConfig, null, msg.getSubject(), null, new Date(), attachName, messageContent.getBytes(), instance, null);

		if (ocorrencia != null) {
			List<TagBlue> tagsBlue = new ArrayList<TagBlue>();
			TagBlue tagBlue = new TagBlue();
			tagBlue.setNome(ocorrencia.getInstancia());
			tagsBlue.add(tagBlue);
			
			OcorrenciaDAO dao = new OcorrenciaDAO();
			
			if(tags != null && !tags.isEmpty() ) {
				for (String tagName : tags) {
					System.out.println("TagName: " + tagName);
					tagBlue = new TagBlue();
					tagBlue.setNome(tagName);
					tagsBlue.add(tagBlue);
				}
				
			}
			
			dao.persist(ocorrencia, tagsBlue);
		}
		
		return ocorrencia;
	}

	private String toHidePass(String password) {
		String character = "*";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < password.length(); i++) {
			sb.append(character);
		}

		return sb.toString();

	}

	/**
	 * Return the primary text content of the message.
	 */
	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	private Configuracao getConfigSeverity(String severity) {
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

	private Configuracao getConfigPending() {
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

	private ArrayList<Object> filterSubject(String subject, String body, String filterRule) {

		try {
			
			ArrayList<Object> retorno = new ArrayList<Object>();

			Interpreter interpreter = new Interpreter();

			BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
			
			List<String> tags = new ArrayList<String>();

			interpreter.set("subject", subject);

			interpreter.set("bndmWildcardsCI", bndmWildcardsCI);
			
			interpreter.set("tags", tags);
			
			interpreter.set("body", body);

			logger.info(String.format("Rodando a regra [\n%s\n]", filterRule));

			interpreter.eval(filterRule);
			
			retorno.add(interpreter.get("instance"));
			retorno.add(interpreter.get("severity"));
			retorno.add(interpreter.get("tags"));
			
			return retorno;

		} catch (EvalError e) {
			logger.error("Não consegui validar a REGRA: ", e);
			return null;
		}

	}
	
	public static String tratarBodyMsg(String bodyMsg) {
		
	    BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
		
		int j = bndmWildcardsCI.searchString(bodyMsg, "<style");
		int k = bndmWildcardsCI.searchString(bodyMsg, "</style>");
		
		System.out.println("Index j: " + j + " Index k: " + k);
		
		if(j > 0 && k > 0) {
			String textStyle = bodyMsg.substring(j, k+8);
			bodyMsg = StringUtils.remove(bodyMsg, textStyle);
		}
		
		int l = bndmWildcardsCI.searchString(bodyMsg, "<hr>");
		
		if( l > 0 ) 
			bodyMsg = bodyMsg.substring(0, l);
		
		return bodyMsg;
	}

	public String getFolderSrc() {
		return folderSrc;
	}

	public void setFolderSrc(String folderSrc) {
		this.folderSrc = folderSrc;
	}

	public String getFolderGoal() {
		return folderGoal;
	}

	public void setFolderGoal(String folderGoal) {
		this.folderGoal = folderGoal;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getAttachmentFolder() {
		return attachmentFolder;
	}

	public void setAttachmentFolder(String attachmentFolder) {
		this.attachmentFolder = attachmentFolder;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Logger getLogger() {
		return logger;
	}

}