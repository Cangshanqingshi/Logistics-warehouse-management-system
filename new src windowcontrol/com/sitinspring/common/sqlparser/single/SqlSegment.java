package com.sitinspring.common.sqlparser.single;
 
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/** *//**
* Sql���Ƭ��
* @since 2018-4-4 
*/
public class SqlSegment {
	private static final String Crlf = "|";
	@SuppressWarnings("unused")
	private static final String FourSpace = "����";
	/** *//**
	��* Sql���Ƭ�ο�ͷ����
	��*/
	private String start;
	/** *//**
	��* Sql���Ƭ���м䲿��
	��*/
	private String body;
	/** *//**
	��* Sql���Ƭ�ν�������
	��*/
	private String end;
	/** *//**
	��* ���ڷָ��м䲿�ֵ�������ʽ
	��*/
	private String bodySplitPattern;
	/** *//**
	��* ��ʾƬ�ε�������ʽ
	��*/
	private String segmentRegExp;
	/** *//**
	��* �ָ���BodyСƬ��
	��*/
	
	private List<String> bodyPieces;
	/** *//**
	��* ���캯��
	��* @param segmentRegExp ��ʾ���SqlƬ�ε�������ʽ
	��* @param bodySplitPattern ���ڷָ�body��������ʽ
	��*/
	public SqlSegment(String segmentRegExp,String bodySplitPattern)
	{
		start="";
		body="";
		end="";
		this.segmentRegExp=segmentRegExp;
		this.bodySplitPattern=bodySplitPattern;
		this.bodyPieces = new ArrayList<String>();
	
	}
	
	/** *//**
	��* ��sql�в��ҷ���segmentRegExp�Ĳ��֣�����ֵ��start,body,end������������
	��* @param sql
	��*/
	public void parse(String sql)
	{
		//���в��ԣ����Էֿ�����Ƿ���ȷ���ô���Ϊ��ȷ�ģ���Ҫ����һ���ֿ��˼��Σ������Ϊʲô��
//-----------------------------------------------------------------------------------------------------------
		
//		System.out.println();
//		System.out.println("��ʼ��sql���зֿ�");
//		System.out.println("�ֿ�");
//-----------------------------------------------------------------------------------------------------------
		
		Pattern pattern=Pattern.compile(segmentRegExp,Pattern.CASE_INSENSITIVE);
		Matcher matcher=pattern.matcher(sql);
		while(matcher.find())
		{
			start=matcher.group(1);
			body=matcher.group(2);
			end=matcher.group(3);
			System.out.println(matcher.group(0));
			System.out.println("start");			
			System.out.println(start);
			System.out.println("body");
			System.out.println(body);
			System.out.println("end");
			System.out.println(end);
			System.out.println();
			parseBody();
			
		}
		
	}
	
	/** *//**
	��* ����body����
	��*
	��*/
	private void  parseBody()
	{
		//System.out.println("��body");
		List<String> ls=new ArrayList<String>();
		Pattern p = Pattern.compile(bodySplitPattern,Pattern.CASE_INSENSITIVE);
		// �������ǰ��ո�
		body=body.trim();
		System.out.println(body);
		Matcher m = p.matcher(body);	
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
//---------------------------------------------------------------------------------------------------------------------------
	//������group��0����ʲô������ô�����滻�ģ�
//		if(result)
//		{
//			System.out.println(m.group(0));
//		}
//---------------------------------------------------------------------------------------------------------------------------
		while (result) 
		{
			m.appendReplacement(sb,Crlf);
			result = m.find();
		}
		m.appendTail(sb);
//---------------------------------------------------------------------------------------------------------------------------
//		System.out.println(sb.toString());
//---------------------------------------------------------------------------------------------------------------------------		
		// �ٰ��ո����
		ls.add(start);
		String[] arr=sb.toString().split("[|]");
		int arrLength=arr.length;
		for(int i=0;i<arrLength;i++)
		{
//			System.out.println(arr[i]);
//			String temp=FourSpace+arr[i];
//			System.out.println(temp);
			ls.add(arr[i]);
		}
		bodyPieces = ls;
	
	}
	
	/** *//**
	��* ȡ�ý����õ�SqlƬ��
	��* @return
	��*/
	public String getParsedSqlSegment()
	{
		StringBuffer sb=new StringBuffer();
		sb.append(start+Crlf);
		for(String piece:bodyPieces)
		{
			sb.append(piece+Crlf);
		}
		return sb.toString();
	}
 
	
	public String getStart() {
		return start;
	}
 
	public void setStart(String start) {
		this.start = start;
	}
 
	public String getBody() {
		return body;
	}
 
	public void setBody(String body) {
		this.body = body;
	}
 
	public String getEnd() {
		return end;
	}
 
	public void setEnd(String end) {
		this.end = end;
	}
 
	public String getBodySplitPattern() {
		return bodySplitPattern;
	}
 
	public void setBodySplitPattern(String bodySplitPattern) {
		this.bodySplitPattern = bodySplitPattern;
	}
 
	public String getSegmentRegExp() {
		return segmentRegExp;
	}
 
	public void setSegmentRegExp(String segmentRegExp) {
		this.segmentRegExp = segmentRegExp;
	}
 
	public List<String> getBodyPieces() {
		return bodyPieces;
	}
 
	public void setBodyPieces(List<String> bodyPieces) {
		this.bodyPieces = bodyPieces;
	}
	
}
