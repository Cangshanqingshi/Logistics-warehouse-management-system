package com.sitinspring.common.sqlparser.single;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;

public class CreateSqlParser {
	public static ArrayList<String> CreateAnalyze(String[][] token){
    	ArrayList<String> grammerList = null;
    	grammerList = new ArrayList<>();//create列表规则，“5（create）；（表名）；（属性名）”
//    	System.out.println("开始检测create相关语法");
    	int tokenLength = 0;
    	for(int i = 0; i < token.length; i++) {
    		if(token[i][0] == null) {
    			tokenLength = i+1;
    			break;
    		}
    	}
    	ArrayList<String> rationality_name = null;//属性名
    	rationality_name = new ArrayList<>();
    	ArrayList<String> data_type = null;//数据类型
    	data_type = new ArrayList<>();
    	ArrayList<String> data_size = null;//数据类型大小
    	data_size = new ArrayList<>();
    	boolean right = false;//判断句子语法是否正确
    	if(token[0][1].equals("create")&&token[1][1].equals("table")&&token[2][0].equals("2")&&token[3][1].equals("(")&&token[4][0].equals("2")) {
    		grammerList.add("5");
    		grammerList.add(";");
    		grammerList.add(token[2][1]);
    		grammerList.add(";");
    		for(int i = 4; i < tokenLength-1; i++) {
    			
    			if(token[i][0].equals("2")) {
    				rationality_name.add(token[i][1]);
    				
    				if((token[i-1][1].equals("(")||token[i-1][1].equals(","))&&token[i+1][0].equals("1")) {
    					
    					data_type.add(token[i+1][1]);
    					if(token[i+2][1].equals(",")) {
    						
    						data_size.add("null");
    						continue;
    					}
    					else if(token[i+2][1].equals(")")) {
    						
    						if(token[i+3][1].equals(";")) {
    							data_size.add("null");
    							right = true;
    							break;
    						}
    						else {
    							right = false;
    							break;
    						}
    						
    					}
    					else if (token[i+2][1].equals("(")) {
    						
							if(token[i+3][0].equals("3")&&token[i+4][1].equals(")")) {
								
								data_size.add(token[i+3][1]);
								if(token[i+5][1].equals(")")&&token[i+6][1].equals(";")) {
									
									right = true;
	    							break;
								}
								else if (token[i+5][1].equals(",")) {
									continue;
								}
								else {
									right = false;
									break;
								}
							}
							else {
								right = false;
								break;
							}
						}
    					else {
    						right = false;
    						break;
    					}
    				}
    				else {
    					right = false;
						break;
    				}
    			}
    			if(token[i][1].equals(";")) {
    				right = true;
    				break;
    			}
    		}
    	}
    	else {
    		right = false;
    	}
    	grammerList.addAll(rationality_name);
    	
    	
    	if(right) {
    		System.out.println("语法正确");
    	}
    	else {
    		grammerList.clear();
    		System.out.println("语法错误");
    	}
    	
    	return grammerList;
	}	
}
