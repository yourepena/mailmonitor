package br.com.bluesi.mailmonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import com.eaio.stringsearch.BNDMWildcardsCI;

import br.com.bluesi.mailmonitor.entities.Ocorrencia;
import br.com.bluesi.mailmonitor.entities.OcorrenciaDAO;

public class UpdateDate {
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("/dados/monitorador/log4j_apa.properties");
		
		OcorrenciaDAO dao = new OcorrenciaDAO();

		List<Ocorrencia> ocorrencias = dao.getOcorrencias();
		
	    BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
	    
		for (Ocorrencia ocorrencia : ocorrencias) {
			
			String bodyMsg = new String(ocorrencia.getBodyMsg());
			
			int j = bndmWildcardsCI.searchString(bodyMsg, "Metric sample time:");
			String text = bodyMsg.substring(j+20, j+41);
			
			System.out.println("Ocorrência " + ocorrencia.getId() + " foi no dia " + text.trim());
			
			try {
				
				Date date = dateFormat.parse(text.trim());
				ocorrencia.setDataHora(date);
				
				dao.merge(ocorrencia);
				
				System.out.println("Ocorrência "+ocorrencia.getId()+" atualizada com sucesso");
				
			} catch (ParseException e) {
				System.out.println("Achei um lixo ==> " + text.trim() );
				e.printStackTrace();
			}
			
		}
	}

}
