package br.com.bluesi.mailmonitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eaio.stringsearch.BNDMWildcardsCI;

import bsh.EvalError;
import bsh.Interpreter;

public class bshTest {

	/**
	   * @param args
	   * @throws EvalError 
	   */
	  public static void main(String[] args) throws EvalError {
	    Interpreter interpreter = new Interpreter();  
	    
	    String subject = "Instance BDANTT23_REMOTE:Critical alert Blabalbala.....";
	    
	    String body = "<p style=\"font-family: Arial, sans-serif; font-size=9pt\">The Oracle Alert Log metric exceeded its Near Critical threshold."
	    		+ "<br />The metric's value was: <STYLE type=\"text/css\">table			{ font-family: Arial, sans-serif; font-size: 9pt;			"
	    		+ "	  border-collapse: collapse; border: 1px solid rgb(194,211,227); }td				{ border: 1px solid #C2D3E3; }tr.header td	"
	    		+ "{ background-color: #E7F2F9; color: rgb(50,36,103) ; font-style: normal;				  border: 1px solid #C2D3E3; }tr.row1 td	"
	    		+ "	{ background-color: white ; color: #3C3C3D ; }tr.row2 td		{ background-color: #F3F3F3; color: #3C3C3D ; }</STYLE><br />"
	    		+ "<table cellpadding='4' cellspacing='0'><tr class='header'><td>Oracle Alerts</td><td></td><td>Alert Status</td></tr><tr class='row1'>"
	    		+ "<td>Wed Aug 30 21:55:18 2017  ORA-01555 caused by SQL statement below (SQL ID: 18uqc2uqqkq6d, Query Duration=112802 sec, SCN: 0x0037.79ced704):"
	    		+ " </td><td>1.0</td><td>Near-Critical</td></tr><tr class='row2'><td>Wed Aug 30 22:22:33 2017  ORA-00604: error occurred at recursive SQL level 1 </td>"
	    		+ "<td>1.0</td><td>Near-Critical</td></tr><tr class='row1'>"
	    		+ "<td>Wed Aug 30 22:22:33 2017  ORA-20000: Acesso negado ao VS01! Favor entrar em contato com a GESIS para maiores informacoes. Ramal: 6518. </td>"
	    		+ "<td>1.0</td><td>Near-Critical</td>"
	    		+ "</tr><tr class='row2'><td>Wed Aug 30 22:22:33 2017  ORA-06512: at line 437 </td><td>1.0</td><td>Near-Critical</td></tr></table>.<br />"
	    		+ "The metric's critical threshold is: 2<br />The metric's near-critical threshold is: 1.<br />Metric sample time: 2017-08-30 23:00:01.0 <br />"
	    		+ "Launch to Precise: http://anvssdf461:20790</p>";
	    
	    BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
	    
	    
	    List<String> tags = new ArrayList<String>();
	    //Launch to Precise:
	    
	    interpreter.set("subject", subject);            
	    interpreter.set("bndmWildcardsCI", bndmWildcardsCI);
	    interpreter.set("body", body);
	    interpreter.set("tags", tags);
	    
	    interpreter.eval("String subjectTmp = subject;"
	    		+ "int i = bndmWildcardsCI.searchString(subjectTmp, \":\");"
	    		+ " String text = subjectTmp.substring(0, i);"
	    		+ "	String[] tokens = text.split(\" \");"
	    		+ " String instance = tokens[1];"
	    		+ " subject = subject.substring(i + 1, subject.length() - 1); "
	    		+ " int j = bndmWildcardsCI.searchString(subject, \"alert\");"
	    		+ " String severity = subject.substring(0, j).trim();"
	    		+ " int k = bndmWildcardsCI.searchString(body, \"Metric sample time: \");"
	    		+ " String ks = body.substring(k+20, k+42); "
	    		+ " tags.add(ks);");  
	    
	    
	    String instance = (String ) interpreter.get("instance");
	    String severity = (String ) interpreter.get("severity");
	    tags = (List<String> ) interpreter.get("tags");
	    
	    if(!tags.isEmpty())
	    {
	    	for (String string : tags) {
				System.out.println("Horário como tag: " + string);
			}
	    	
	    }
	    
	    System.out.println("Instance: " + instance + " Criticidade: " + severity);
	    
	  }

	  
	  private static String filterSubjectInstance(String subject) {
			if (subject != null && !subject.isEmpty()) {
				
				BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
				
				int i = bndmWildcardsCI.searchString(subject, ":");
				
				String text = subject.substring(0, i);
				
				String[] tokens = text.split(" ");
				
				return tokens[2].trim();
			} else
				return null;
		}

		private static String filterSubjectSeverity(String subject) {

			if (subject != null && !subject.isEmpty()) {

				BNDMWildcardsCI bndmWildcardsCI = new BNDMWildcardsCI();
				int i = bndmWildcardsCI.searchString(subject, ":");

				subject = subject.substring(i + 1, subject.length() - 1);

				int j = bndmWildcardsCI.searchString(subject, "alert");

				subject = subject.substring(0, j);

				return subject.trim();

			}
			return null;
		}


}
