package com.sitinspring.common.sqlparser.single;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexicalAnalyzer {
    /*
    * 1表示关键字
    * 2表示标识符
    * 3表示常数
    * 4表示运算符
    * 5表示界符
    * 6表示字符串
    * */

    //关键字
    static String []keyWord={"int","varchar","char","update", "set", "select","alert","table","and","or","where","from","oder","by","group","having","right",
    		"joins","inners","drop","delete","insert","create","dorp","database","in","not","any","like"};
    //运算符
    static String []operation={">","<","=",">=","<="};
    //界符
    static String []symbol={",",";","(",")","[","]","'","_","%","*"};
    
    static ArrayList<String> keyWords=null;
    static ArrayList<String> operations=null;
    static ArrayList<String> symbols=null;

    //指向当前所读到字符串的位置的指针
    static int p,lines,q;
	
    //判断是否出现词法错误
    static boolean err_flag=false;
    
	static String [][]tokens = new String[100][2];
	
    public static void main(String []args) {
    	
        init();
 		Scanner input = new Scanner(System.in);
// 		List<String> sqL = new ArrayList<>();
 		String sql = input.nextLine();
		while(sql.lastIndexOf(";")!=sql.length()-1) {			
			sql = sql + " " + input.nextLine();			
		}
		sql = sql.toLowerCase();
		analyze(sql);
//		for(int i = 0;i < q; i++) {
//			System.out.print(tokens[i][0]+" "+tokens[i][1]+"\n");
//		}
    }

    //初始化把数组转换为ArrayList
    public static void init(){
        keyWords=new ArrayList<>();
        operations=new ArrayList<>();
        symbols=new ArrayList<>();
        Collections.addAll(keyWords, keyWord);
        Collections.addAll(operations, operation);
        Collections.addAll(symbols, symbol);
    }

    public static String[][] analyze(String str){

        p=0;
        q=0;
        char ch;
        str=str.trim();
        for (;p<str.length();p++){
            ch=str.charAt(p);
            if (Character.isDigit(ch)){
                digitCheck(str);
            }else if (Character.isLetter(ch)||ch=='_'){
                letterCheck(str);
            }else if (ch=='"'){
                stringCheck(str);
            }
            else if (ch==' '){
                continue;
            }else {
                symbolCheck(str);
            }
        }
        if(err_flag) {
        	tokens = null;
        }
        return tokens;
    }

    /*数字的识别
    * 1、识别退出：
    *   1.1、遇到空格符
    *   1.2、遇到运算符或者界符
    * 2、错误情况：
    *   2.1、两个及以上小数点
    *   2.2、掺杂字母
    * */
    public static void digitCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        //判断数字的小数点是否有且是否大于1
        int flag=0;
        boolean err=false;
        char ch;
        for (;p<str.length();p++) {
            ch = str.charAt(p);
            if (ch==' '||(!Character.isLetterOrDigit(ch)&&ch!='.')) {
                break;
            }else if (err){
                token+=ch;
            }
            else {
                token+=ch;
                if (ch == '.') {
                    if (flag == 1) {
                        err = true;
                    } else {
                        flag++;
                    }
                }else if (Character.isLetter(ch)){
                    err=true;
                }
            }
        }
        if (token.charAt(token.length()-1)=='.'){
            err=true;
        }
        if (err){
        	err_flag = true;
        	System.out.println("词法分析错误");
            System.out.println(lines+"line"+": "+token+" is wrong");
        }else {
        	tokens[q][0] = "3";
        	tokens[q][1] = token;
        	q++;
//            System.out.println("("+3+","+token+")");
        }
        if (p!=str.length()-1||(p==str.length()-1&&!Character.isDigit(str.charAt(p)))){
            p--;
        }
    }

    //标识符，关键字的识别
    public static void letterCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        for (;p<str.length();p++){
            ch=str.charAt(p);
            if (!Character.isLetterOrDigit(ch)&&ch!='_'){
                break;
            }else{
                token+=ch;
            }
        }
        if (keyWords.contains(token)){
        	tokens[q][0] = "1";
        	tokens[q][1] = token;
        	q++;
//            System.out.println("("+1+","+token+")");
        }else {
        	tokens[q][0] = "2";
        	tokens[q][1] = token;
        	q++;
//            System.out.println("("+2+","+token+")");
        }
        if (p!=str.length()-1||(p==str.length()-1&&(!Character.isLetterOrDigit(str.charAt(p))&&str.charAt(p)!='_'))){
            p--;
        }
    }

    //符号的识别
    public static void symbolCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        if (symbols.contains(token)){
        	tokens[q][0] = "5";
        	tokens[q][1] = token;
        	q++;
//            System.out.println("("+5+","+token+")");
            p--;
        }else {
            if (operations.contains(token)){
                if (p<str.length()){
                    ch=str.charAt(p);
                    if (operations.contains(token+ch)){
                        token+=ch;
                        p++;
                        if (p<str.length()){
                            ch=str.charAt(p);
                            if (operations.contains(token+ch)){
                                token+=ch;
                            	tokens[q][0] = "4";
                            	tokens[q][1] = token;
                            	q++;
//                                System.out.println("("+4+","+token+")");
                            }else{
                                p--;
                            	tokens[q][0] = "4";
                            	tokens[q][1] = token;
                            	q++;
//                                System.out.println("("+4+","+token+")");
                            }
                        }else{
                        	tokens[q][0] = "4";
                        	tokens[q][1] = token;
                        	q++;
//                            System.out.println("("+4+","+token+")");
                        }
                    }else {
                        p--;
                    	tokens[q][0] = "4";
                    	tokens[q][1] = token;
                    	q++;
//                        System.out.println("("+4+","+token+")");
                    }
                }
            }else {
                p--;
                err_flag=true;
                System.out.println("词法分析错误");
                System.out.println(lines+"line"+": "+token+" is wrong");
            }
        }
    }

    //字符串检查
    public static void stringCheck(String str){
        String token= String.valueOf(str.charAt(p++));
        char ch;
        for (;p<str.length();p++){
            ch=str.charAt(p);
            token+=ch;
            if (ch=='"'){
                break;
            }
        }
        if (token.charAt(token.length()-1)!='"'){
        	err_flag=true;
        	System.out.println("词法分析错误");
            System.out.println(lines+"line"+": "+token+" is wrong");
        }else {
        	tokens[q][0] = "6";
        	tokens[q][1] = token;
        	q++;
//            System.out.println("("+6+","+token+")");
        }
    }
}


