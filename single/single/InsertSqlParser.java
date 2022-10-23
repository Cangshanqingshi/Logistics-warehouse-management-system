package com.sitinspring.common.sqlparser.single;

import java.util.*;

public class  InsertSqlParser {
	public static ArrayList<String> InsertAnalyze(String[][] tokenFlow) {
    	ArrayList<String> grammerList = null;
    	grammerList = new ArrayList<>();//insert列表规则，“4（insert）；（表名）；（属性名）；（各属性内容）”
    	System.out.println("开始检测insert相关语法");
    	int tokenLength = 0;
    	for(int i = 0; i < tokenFlow.length; i++) {
    		if(tokenFlow[i][0] == null) {
    			tokenLength = i+1;
    			break;
    		}
    	}
    	boolean right = false;
    	
    	if(tokenFlow[0][1].equals("insert")) {
    		grammerList.add("4");
    		grammerList.add(";");
			if(tokenFlow[1][1].equals("into")) {
				if(tokenFlow[2][0] == "2"||tokenFlow[2][0] == "3") {
					grammerList.add(tokenFlow[2][1]);
					grammerList.add(";");
					if(tokenFlow[3][1].equals("values")) {//无需指定要插入数据的列名
						grammerList.add(null);
						grammerList.add(";");
						if(tokenFlow[4][1].equals("(")) {
							if(!tokenFlow[5][1].equals("'")) {
								right = false;
							}
							else {
								for(int j = 0; j < tokenLength; j++) {
									
									if(tokenFlow[5+j][0] != "2"&&tokenFlow[5+j][0] != "3") {
										if(tokenFlow[5+j][1].equals(",")) {
											if(tokenFlow[5+j+1][1].equals("'")) {
												continue;
											}
											else {
												right = false;
												break;
											}
										}
										else if(tokenFlow[5+j][1].equals(")")&&tokenFlow[5+j+1][1].equals(";")){
											
											right = true;
											break;
										}
										else if(tokenFlow[5+j][1].equals("'")) {
											continue;
										}
										else {
											right = false;
											break;
										}
									}
									else {
										if(tokenFlow[5+j][0].equals("2")||tokenFlow[5+j][0].equals("3")){
											if(tokenFlow[5+j-1][1].equals("'")&&tokenFlow[5+j+1][1].equals("'")&&(tokenFlow[5+j+2][1].equals(",")||tokenFlow[5+j+2][1].equals(")"))) {

												grammerList.add(tokenFlow[5+j][1]);
											}
											else {
												right = false;


											}
										}
										
									}
								}
							}
							
						}
					}
					else if (tokenFlow[3][1].equals("(")) {//需要指定列名
						
						int parentheses_length = 0;
						int parentheses_number = 0;
						for(int j = 0; j < tokenLength; j++) {
							
							if(tokenFlow[4+j][0] != "2") {
								if(tokenFlow[4+j][1].equals(",")) {
									if(tokenFlow[4+j+1][0].equals("2")) {
										continue;
									}
									else {
										right = false;
										break;
									}
								}
								if(tokenFlow[4+j][1].equals(")")) {
									
									parentheses_length = j +1;
									break;
								}
								else {
									right = false;
									break;
								}
							}
							else {
								parentheses_number++;
								grammerList.add(tokenFlow[4+j][1]);
							}
						}
						grammerList.add(";");
						if(tokenFlow[4+parentheses_length][1].equals("values")) {
							if(tokenFlow[5+parentheses_length][1].equals("(")) {
								if(!tokenFlow[6+parentheses_length][1].equals("'")) {
									right = false;
								}
								else {
									for(int j = 0; j < tokenLength; j++) {
										
	    								if(tokenFlow[6+parentheses_length+j][0] != "2"&&tokenFlow[6+parentheses_length+j][0] != "3") {
	    									if(tokenFlow[6+parentheses_length+j][1].equals(",")) {
	    										if(tokenFlow[6+parentheses_length+j+1][1].equals("'")) {
	    											continue;
	    										}
	    										else {
	    											right = false;
	    											break;
	    										}
	    									}
	    									else if(tokenFlow[6+parentheses_length+j][1].equals(")")&&tokenFlow[6+parentheses_length+j+1][1].equals(";")){
	    										
	    										if(parentheses_number == 0) {
	    											
	    											right = true;
	    										}
	    										else {
	    											right = false;
	    										}
	    										break;
	    									}
	    									else if(tokenFlow[6+parentheses_length+j][1].equals("'")) {
	    										continue;
	    									}
	    									else {
	    										right = false;
	    										break;
	    									}
	    								}
	    								else {
	    									if(tokenFlow[6+parentheses_length+j-1][1].equals("'")&&tokenFlow[6+parentheses_length+j+1][1].equals("'")&&(tokenFlow[6+parentheses_length+j+2][1].equals(",")||tokenFlow[6+parentheses_length+j+2][1].equals(")"))) {
	    										parentheses_number--;
	    										
	    										grammerList.add(tokenFlow[6+parentheses_length+j][1]);
	    									}
	    									else {
	    										right = false;


											}
	    								}
	    							}
								}
								
							}
						}
					}
					else {
						right = false;
					}
				}
				else {
					right = false;
				}
			}
			else {
				right = false;
			}
		}
		else {
			right = false;
		}
    	
    	if(right) {
    		System.out.println("语法正确");
    	}
    	else {
    		System.out.println("语法错误");
    		grammerList = null;
    	}
    	return grammerList;
    }
	
	
}
