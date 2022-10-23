package com.sitinspring.common.sqlparser.single;
import test2.Assemble;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 
public class Test {
	
	static String [][]tokens = new String[100][2];
	public static void main (String arg[])
	{
		sqlInput();
	}
	
	public static void sqlInput() {
		System.out.println("欢迎您登录，请开始使用。");
		/*
		 * 应该有一个验证管理员和用户的功能。
		 */
		System.out.println("请输入sql语句");
		int flag=1;
		while(true) {
			Scanner input = new Scanner(System.in);
	 		String sql = input.nextLine();
			while(sql.lastIndexOf(";")!=sql.length()-1) {			
				sql = sql + " " + input.nextLine();			
			}
			sql = sql.toLowerCase();
			
//			词法分析，获得token流；
			LexicalAnalyzer.init();
			tokens = LexicalAnalyzer.analyze(sql);
			if(tokens!=null) {
//				语法分析
				List<String> parameter_list=new ArrayList<String>();
				parameter_list = SingleSqlParserFactory.generateParser(tokens);
				System.out.println("执行结束SingleSqlParserFactory.generateParser函数");
				System.out.println(parameter_list);
				if(!parameter_list.isEmpty()) {
//					语法结果转换		
					String garameterString = new String();
					garameterString = TransToString(parameter_list);
					System.out.println(garameterString);
					Assemble assemble = new Assemble();
					assemble.Execute(garameterString);
				}
			}
			System.out.print("请选择是否继续输入\n 1：继续   0：退出\n");
			flag = input.nextInt();
			if(flag==0)
				break;
		}
	}
	
	public static String TransToString(List<String> garameter_list) {
		String garameterString = new String();
		for(int i=0; i<garameter_list.size(); i++) {
			if(i+1<garameter_list.size()&&!garameter_list.get(i+1).equals(";")&&!garameter_list.get(i).equals(";")) {
				garameterString += garameter_list.get(i);
				garameterString += ",";
			}
			else {
				garameterString += garameter_list.get(i);
			}
		}
		return garameterString;
	}
}
