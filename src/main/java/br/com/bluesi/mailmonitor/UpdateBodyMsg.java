package br.com.bluesi.mailmonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import com.eaio.stringsearch.BNDMWildcardsCI;

import br.com.bluesi.mailmonitor.entities.Ocorrencia;
import br.com.bluesi.mailmonitor.entities.OcorrenciaDAO;

public class UpdateBodyMsg {
	
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("C:\\Users\\youre.fernandez\\Documents\\blue\\deployment\\log4j_proc.properties");
		
		OcorrenciaDAO dao = new OcorrenciaDAO();

		List<Ocorrencia> ocorrencias = dao.getOcorrencias();
		
	    BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
	    
		for (Ocorrencia ocorrencia : ocorrencias) {
			
			String bodyMsg = new String(ocorrencia.getBodyMsg());
			
			System.err.println("\n\n\n\n");
			
			System.err.println(bodyMsg);
			
			System.err.println("\n\n\n\n");
			
			int j = bndmWildcardsCI.searchString(bodyMsg, "<style");
			int k = bndmWildcardsCI.searchString(bodyMsg, "</style>");
			
			System.out.println("Index j: " + j + " Index k: " + k);
			
			if(j > 0 && k > 0) {
				String textStyle = bodyMsg.substring(j, k+8);
				
				System.err.println("Texto de Style para deletar \n\n" + textStyle + "\n\n");
				
				bodyMsg = StringUtils.remove(bodyMsg, textStyle);
				
				System.err.println("Texto sem o style \n\n" + bodyMsg + "\n\n");
				
				System.err.println("Ocorrência "+ocorrencia.getId()+" atualizada com sucesso");
			}else {
				System.err.println("Não encontrei style tentando continuar com o <hr>");
			}
			
			int l = bndmWildcardsCI.searchString(bodyMsg, "<hr>");
			
			if( l > 0 ) 
				bodyMsg = bodyMsg.substring(0, l);
			else
				System.err.println("Não encontrei <hr>");
			
			System.err.println("Texto definitivo para guardar: \n\n" + bodyMsg + "\n\n");
			
			ocorrencia.setBodyMsg(bodyMsg.getBytes());
			dao.merge(ocorrencia);
				
		}
		
		System.exit(0);
	}

}
