package com.sitinspring.common.sqlparser.single;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
//test-success
// throws Exception
public class SingleSqlParserFactory 
{
	
	public static List<String> generateParser(String[][] tokens)
	{
		List<String> gramarList = new ArrayList<>();
		
		if(tokens[0][1].equals("select")) {			
//			System.out.println("select");
			gramarList = SelectSqlParser.SelectAnalyze(tokens);
		}
		
		else if(tokens[0][1].equals("delete")) {
//			System.out.println("delet");
			gramarList = DeleteSqlParser.DeleteAnalyze(tokens);
		}
		
		else if(tokens[0][1].equals("update")) {
//			System.out.println("update");
			gramarList = UpdateSqlParser.UpdateAnalyze(tokens);
		}
		
		else if(tokens[0][1].equals("insert")) {
//			System.out.println("insert");
			gramarList = InsertSqlParser.InsertAnalyze(tokens);
		}
		else if(tokens[0][1].equals("create")) {
//			System.out.println("insert");
			gramarList = CreateSqlParser.CreateAnalyze(tokens);
		}
		else {
			System.out.println("语法错误");
		}
//		BaseSingleSqlParser tmp = null;
//		
//		if(contains(sql,"(insert into)(.+)(select)(.+)(from)(.+)"))
//		{
//			System.out.println("insert_select");
//			tmp = new InsertSelectSqlParser(sql);
//			//return tmp.splitSql2Segment();
//		}
//		else if(contains(sql,"(select)(.+)(from)(.+)"))
//		{
//			
//			System.out.println("select");
//			tmp = new SelectSqlParser(sql);
//			//System.out.println("初始化SelectSqlParser结束");
//		}
//		else if(contains(sql,"(delete from)(.+)"))
//		{
//			System.out.println("delete");
//			tmp = new DeleteSqlParser(sql);
//			//return new DeleteSqlParser(sql);
//		}
//		else if(contains(sql,"(update)(.+)(set)(.+)"))
//		{
//			System.out.println("update");
//			tmp = new UpdateSqlParser(sql);
//			//return new UpdateSqlParser(sql);
//		}
//		else if(contains(sql,"(insert into)(.+)(values)(.+)"))
//		{
//			System.out.println("insert");
//			tmp = new InsertSqlParser(sql);
//			//return new InsertSqlParser(sql);
//		}
//		else if(contains(sql,"(show databases)"))
//		{
//			System.out.println("show databases");
//		// return new InsertSqlParser(sql);
//		}
//		else if(contains(sql,"(use)(.+)"))
//		{
//			System.out.println("use");
//		// return new InsertSqlParser(sql);
//		}
//		else 
//			{
//				System.out.println("Input errors, please re-enter");
//			}
//		//sql=sql.replaceAll("ENDSQL", "");
//	//	throw new Exception(sql.replaceAll("ENDOFSQL", ""));
//		//return null;
		
		return gramarList;
	}
}