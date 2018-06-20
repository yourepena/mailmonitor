package br.com.bluesi.mailmonitor.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class hibernateUtil {
	public static String erroMsg = "";
	private static SessionFactory fabrica;
	
	static {
		try{
			if(fabrica == null){
				fabrica = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
			}
			
		}catch(Exception e){
			erroMsg = e.getMessage();
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("ERRO AO ENCONTRAR O DATASOURCE !!!");
			System.out.println("MENSAGEM: " + e.getMessage());
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			e.printStackTrace();
		}
	}
	
	public static Session getSession(){
		return fabrica.openSession();
	}
	
	public static void showStatistics(){
		
		fabrica.getStatistics().setStatisticsEnabled(true);
		
		System.out.println("***************************************************************************************************************************************************************");
		System.out.println("1: " + fabrica.getStatistics().getCloseStatementCount());
		System.out.println("2: " + fabrica.getStatistics().getCollectionFetchCount());
		System.out.println("3: " + fabrica.getStatistics().getCollectionFetchCount());
		System.out.println("4: " + fabrica.getStatistics().getCollectionLoadCount());
		System.out.println("5: " + fabrica.getStatistics().getCollectionRecreateCount());
		System.out.println("6: " + fabrica.getStatistics().getCollectionRemoveCount());
		System.out.println("7: " + fabrica.getStatistics().getCollectionUpdateCount());
		System.out.println("8: " + fabrica.getStatistics().getConnectCount());
		System.out.println("9: " + fabrica.getStatistics().getEntityDeleteCount());
		System.out.println("10: " + fabrica.getStatistics().getEntityFetchCount());
		System.out.println("11: " + fabrica.getStatistics().getEntityInsertCount());
		System.out.println("12: " + fabrica.getStatistics().getEntityLoadCount());
		System.out.println("13: " + fabrica.getStatistics().getEntityUpdateCount());
		System.out.println("14: " + fabrica.getStatistics().getFlushCount());
		System.out.println("15: " + fabrica.getStatistics().getOptimisticFailureCount());
		System.out.println("16: " + fabrica.getStatistics().getPrepareStatementCount());
		System.out.println("17: " + fabrica.getStatistics().getQueryCacheHitCount());
		System.out.println("18: " + fabrica.getStatistics().getQueryCacheMissCount());
		System.out.println("19: " + fabrica.getStatistics().getQueryCachePutCount());
		System.out.println("20: " + fabrica.getStatistics().getQueryCachePutCount());
		System.out.println("21: " + fabrica.getStatistics().getQueryCachePutCount());
		System.out.println("22: " + fabrica.getStatistics().getQueryExecutionCount());
		System.out.println("23: " + fabrica.getStatistics().getQueryExecutionMaxTime());
		System.out.println("24: " + fabrica.getStatistics().getQueryExecutionMaxTimeQueryString());
		System.out.println("25: " + fabrica.getStatistics().getSecondLevelCacheHitCount());
		System.out.println("26: " + fabrica.getStatistics().getSecondLevelCacheMissCount());
		System.out.println("27: " + fabrica.getStatistics().getSecondLevelCachePutCount());
		System.out.println("28: " + fabrica.getStatistics().getSessionCloseCount());
		System.out.println("29: " + fabrica.getStatistics().getSessionOpenCount());
		System.out.println("30: " + fabrica.getStatistics().getStartTime());
		System.out.println("31: " + fabrica.getStatistics().getSuccessfulTransactionCount());
		System.out.println("32: " + fabrica.getStatistics().getTransactionCount());
	 
		for(int i = 0 ; i < fabrica.getStatistics().getQueries().length ; i ++ ){
			System.out.println("35: " + fabrica.getStatistics().getQueries()[i]);
		}
		for(int i = 0 ; i < fabrica.getStatistics().getSecondLevelCacheRegionNames().length ; i ++ ){
			System.out.println("36: " + fabrica.getStatistics().getSecondLevelCacheRegionNames()[i]);
		}
		System.out.println("***************************************************************************************************************************************************************");	
	}
	
	

}
