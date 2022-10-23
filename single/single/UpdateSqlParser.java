package com.sitinspring.common.sqlparser.single;

import java.util.ArrayList;
import java.util.List;

public class UpdateSqlParser {
	//update语法检测
    public static ArrayList<String> UpdateAnalyze(String[][] tokenFlow){
    	ArrayList<String> grammerList = null;
    	grammerList = new ArrayList<>();//update列表规则，“3（update）；（表名）；（属性名）；（属性修改后的内容）；（条件的属性）；（符号）；（条件的内容）”
    	System.out.println("开始检测update相关语法");
    	int tokenLength = 0;
    	for(int i = 0; i < tokenFlow.length; i++) {
    		if(tokenFlow[i][0] == null) {
    			tokenLength = i+1;
    			break;
    		}
    	}
    	ArrayList<String> lift_rationlity = null;
    	lift_rationlity = new ArrayList<>();
    	ArrayList<String> right_rationlity = null;
    	right_rationlity = new ArrayList<>();
    	ArrayList<String> whereList = null;
    	whereList = new ArrayList<>();
    	boolean right = false;
    	boolean where_isuse = false;
    	int where_start = 0;
    	if(tokenFlow[0][1].equals("update")&&tokenFlow[1][0].equals("2")&&tokenFlow[2][1].equals("set")) {
    		grammerList.add("3");
    		grammerList.add(";");
    		grammerList.add(tokenFlow[1][1]);
    		grammerList.add(";");
    		if(!tokenFlow[3][0].equals("2")) {
    			right = false;
    		}
    		for(int i = 0; i < tokenLength; i++) {
    			if(tokenFlow[3+i][0].equals("2")||tokenFlow[3+i][0].equals("3")) {
    				if(!(tokenFlow[3+i+1][1].equals("=")||tokenFlow[3+i+1][1].equals("'"))) {
    					right = false;
    					break;
    				}
    				continue;
    			}
    			else {
    				if(tokenFlow[3+i][1].equals("=")) {  					
    					if(tokenFlow[3+i-1][0].equals("2")&&tokenFlow[3+i+1][1].equals("'")&&(tokenFlow[3+i+2][0].equals("2")||tokenFlow[3+i+2][0].equals("3"))&&tokenFlow[3+i+3][1].equals("'")&&(tokenFlow[3+i+4][1].equals(",")||tokenFlow[3+i+4][1].equals("where")||tokenFlow[3+i+4][1].equals(";"))) {
    						lift_rationlity.add(tokenFlow[3+i-1][1]);
    						right_rationlity.add(tokenFlow[3+i+2][1]);
    						continue;
    					}
        				else {
        					right = false;
        					break;	
						}
        			}
    				else if(tokenFlow[3+i][1].equals("'")){
       					continue;
    				}
    				else if(tokenFlow[3+i][1].equals(",")){
    					if(tokenFlow[3+i+1][0].equals("2")) {
    						continue;
    					}
    					else {
    						right = false;
							break;
						}
    					
    				}
    				else if (tokenFlow[3+i][1].equals("where")) {
    					where_start = 3+i;
    					where_isuse = true;
        				break;
					}
    				else if (tokenFlow[3+i][1].equals(";")) {
        				right = true;
    					break;
					}
    				else {
    					right = false;
    					break;
    				}
    			}
    			
    		}
    		if(right||where_isuse) {
    			for(int i = 0; i < lift_rationlity.size(); i++) 
        			grammerList.add(lift_rationlity.get(i));
        		grammerList.add(";");
        		for(int i = 0; i < lift_rationlity.size(); i++) 
        			grammerList.add(right_rationlity.get(i));
        		grammerList.add(";");
        		if(where_start == 0) {
        			grammerList.add("null");
        			grammerList.add(";");
        			grammerList.add("0");
        			grammerList.add(";");
        			grammerList.add("null");
        			right = true;
        			System.out.println("语法正确");
        			return grammerList;
        		}
        		whereList = (ArrayList<String>) whereSelect(tokenFlow, where_start+1);
        		if(whereList.size() > 0) {
        			right = true;
        			grammerList.addAll(whereList);
        		}
        		else {
        			right = false;
        		}
    		}	
    	}
    	else{
    		right = false;
    	}
    	if(right) {
    		System.out.println("语法正确");
    	}
    	else {
    		System.out.println("语法错误");
    		grammerList.clear();
    	}	
    	return grammerList;
    }
    
    //判断where后内容是否符合语法
    public static List<String> whereSelect(String[][] tokens, int i) {
    	List<String> whereList = new ArrayList<String>();//输出
		List<String> natureList = new ArrayList<String>();//左属性
		List<String> operatorList = new ArrayList<String>();//运算符
		List<String> valueList = new ArrayList<String>();//条件内容
		List<String> relationList = new ArrayList<String>();//关系条件
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
					if(tokens[i][0].equals("3")||tokens[i][0].equals("2")||tokens[i][1].equals("%")||tokens[i][1].equals("_")) {
						if(tokens[i][0].equals("2")||tokens[i][0].equals("3"))
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
    
    //转换运算符号为数字
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
