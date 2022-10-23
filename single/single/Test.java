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
		System.out.println("��ӭ����¼���뿪ʼʹ�á�");
		/*
		 * Ӧ����һ����֤����Ա���û��Ĺ��ܡ�
		 */
		System.out.println("������sql���");
		int flag=1;
		while(true) {
			Scanner input = new Scanner(System.in);
	 		String sql = input.nextLine();
			while(sql.lastIndexOf(";")!=sql.length()-1) {			
				sql = sql + " " + input.nextLine();			
			}
			sql = sql.toLowerCase();
			
//			�ʷ����������token����
			LexicalAnalyzer.init();
			tokens = LexicalAnalyzer.analyze(sql);
			if(tokens!=null) {
//				�﷨����
				List<String> parameter_list=new ArrayList<String>();
				parameter_list = SingleSqlParserFactory.generateParser(tokens);
				System.out.println("ִ�н���SingleSqlParserFactory.generateParser����");
				System.out.println(parameter_list);
				if(!parameter_list.isEmpty()) {
//					�﷨���ת��		
					String garameterString = new String();
					garameterString = TransToString(parameter_list);
					System.out.println(garameterString);
					Assemble assemble = new Assemble();
					assemble.Execute(garameterString);
				}
			}
			System.out.print("��ѡ���Ƿ��������\n 1������   0���˳�\n");
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
