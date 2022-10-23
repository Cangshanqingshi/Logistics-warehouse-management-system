package com.sitinspring.common.sqlparser.single;

import java.util.ArrayList;
import java.util.List;

/** *//**
*
* 单句删除语句解析器
*/
//正确
public class DeleteSqlParser {
 
	
	public DeleteSqlParser(String originalSql) {
		
		
	}
	
	//delete from table_name where ();
	
	public static List<String> DeleteAnalyze(String[][] tokens){
		System.out.println("调用了DeleteSqlParser的DeleteAnalyze方法");
		List<String> gramarList = new ArrayList<>();
		int i=0;
		if(tokens[i][1].equals("delete")&&tokens[i+1][1].equals("from")) {
			i=i+2;
			gramarList.add("2");
			gramarList.add(";");
			if(tokens[i][0].equals("2")) {
				gramarList.add(tokens[i][1]);
				i++;
				if(tokens[i][1].equals("where")) {//分析where语句
					gramarList.add(";");
					i++;
					List<String> whereList = new ArrayList<String>();
					whereList = whereDelete(tokens, i);//存储where语句得到的信息
					if(!whereList.isEmpty()) {//若信息不为空，则将其添加到总语法信息中
						gramarList.addAll(whereList);
					}
					else {
						gramarList.clear();
					}
				}
				else if(tokens[i][1].equals(";")) {
					gramarList.add(";");
					gramarList.add("null");
					gramarList.add(";");
					gramarList.add("0");
					gramarList.add(";");
					gramarList.add("null");
					gramarList.add(";");
					gramarList.add("null");
				}
			}
			else {
				System.out.println("语法错误");
				gramarList.clear();
			}
		}
		else {
			System.out.println("语法错误");
			gramarList.clear();
		}
		return gramarList;
	}
	
	public static List<String> whereDelete(String[][] tokens, int i) {
		List<String> whereList = new ArrayList<String>();
		List<String> natureList = new ArrayList<String>();
		List<String> operatorList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		List<String> relationList = new ArrayList<String>();
		boolean err_flag = false;
		while(!tokens[i][1].equals(";")) {
			if(tokens[i][0].equals("2")&&tokens[i+1][0].equals("4")&&tokens[i+2][1].equals("'")&&tokens[i+4][1].equals("'")&&(tokens[i+3][0].equals("3")||tokens[i+3][0].equals("2"))){
				natureList.add(tokens[i][1]);
				operatorList.add(code(tokens[i+1][1]));
				valueList.add(tokens[i+3][1]);
				i=i+5;
			}
			else if(tokens[i][0].equals("2")&&tokens[i+1][1].equals("like")&&tokens[i+2][1].equals("'")) {
				natureList.add(tokens[i][1]);
				operatorList.add(code(tokens[i+1][1]));
				i=i+3;
				String likeString = new String();
				while(!tokens[i][1].equals("'")) {
					if(tokens[i][0].equals("2")||tokens[i][1].equals("%")||tokens[i][1].equals("_")) {
						likeString += tokens[i][1];
						i++;
					}
					else {
						err_flag = true;
						break;
					}
				}
				if(tokens[i][1].equals("'")) {
					valueList.add(likeString);
					i++;
				}
			}
			else {
				err_flag = true;
			}
			if(tokens[i][1].equals("and")||tokens[i][1].equals("or")) {
				relationList.add(code(tokens[i][1]));
				i++;
			}
			else if(!tokens[i][1].equals(";")) {
				err_flag = true;
			}
			if(err_flag) {
				System.out.println("语法错误");
				break;
			}	
		}
		if(!err_flag) {
			whereList.addAll(natureList);
			whereList.add(";");
			whereList.addAll(operatorList);
			whereList.add(";");
			whereList.addAll(valueList);
			whereList.add(";");
			if(relationList.isEmpty())
				whereList.add("null");
			whereList.addAll(relationList);
		}
		return whereList;
	}
	
	public static String code(String t) {
		switch (t) {
		case "and": {
			return "1";
		}
		case "or": {
			return "2";
		}
		case "<": {
			return "1";
		}
		case "<=": {
			return "2";
		}
		case "=": {
			return "3";
		}
		case ">": {
			return "4";
		}
		case ">=": {
			return "5";
		}
		case "like": {
			return "6";
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + t);
		}
	}
}
 
 
