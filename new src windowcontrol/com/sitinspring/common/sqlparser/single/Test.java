package com.sitinspring.common.sqlparser.single;

import test2.Assemble;
import Logistics.Procedure;
import test1.SharedData;
import test1.window;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

 

public class Test {
	
	static String [][]tokens = new String[100][2];
	static SharedData sharedData;
	window w;
	
	public Test(SharedData sharedData, window w){
        Test.sharedData = sharedData;
        this.w = w;
    }
	
	public  void main (String arg[])
	{
		try {
			sharedData.working.acquire();
			
			start();
		
			sharedData.working.release();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		int choose;
			while(true) {
				
				//w.displayMessage(Thread.currentThread().getName() + " is waiting sql");
				System.out.print("��ѡ��ϵͳ��\n 1��������������ϵͳ  2���������ݹ���ϵͳ  0���˳�\n");
				w.displayMessage("��ѡ��ϵͳ��\n 1��������������ϵͳ  2���������ݹ���ϵͳ  0���˳�\n");
				
				 while(w.flag ) {System.out.println("\r");};
		            
	            Scanner input = new Scanner(w.Rmessage);
	            w.flag = true;
				
				choose = input.nextInt();
				
				if(choose == 1)
					Procedure.menu();
				else if(choose == 2)
					sqlInput();
				else if(choose == 0)
					break;
				else 
					System.out.print("����������������룺\n");			
					w.displayMessage("����������������룺\n");
			}
	}
	
	public void sqlInput() {
		System.out.println("��ӭ����¼���뿪ʼʹ�á�");
		w.displayMessage("��ӭ����¼���뿪ʼʹ�á�");
		/*
		 * Ӧ����һ����֤����Ա���û��Ĺ��ܡ�
		 */
		System.out.println("������sql���");
		w.displayMessage("������sql���");
		int flag=1;
		while(true) {

			System.out.println(Thread.currentThread().getName() + " is waiting sql");
            w.displayMessage(Thread.currentThread().getName() + " is waiting sql");
            while(w.flag ) {System.out.println("\r");};
            
            Scanner input = new Scanner(w.Rmessage);
            w.flag = true;
			
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
//				System.out.println("ִ�н���SingleSqlParserFactory.generateParser����");
//				System.out.println(parameter_list);
				if(!parameter_list.isEmpty()) {
//					�﷨���ת��		
					String garameterString = new String();
					garameterString = TransToString(parameter_list);
					System.out.println(garameterString);
					Assemble assemble = new Assemble(sharedData, w);
					assemble.Execute(garameterString);
				}
			}

			System.out.print("��ѡ���Ƿ��������\n 1������   0���˳�\n");
			w.displayMessage("��ѡ���Ƿ��������\n 1������   0���˳�\n");
			//w.displayMessage("!!!!");
			System.out.println(Thread.currentThread().getName() + " is waiting control");
            w.displayMessage(Thread.currentThread().getName() + " is waiting control");
            while(w.flag ) {System.out.println("\r");};
			Scanner input1 = new Scanner(w.Rmessage);
			w.flag = true;
			flag = input1.nextInt();
			
			if(flag==0)
				break;
			input.close();
			input1.close();
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
