package com.sitinspring.common.sqlparser.single;
 
import java.util.ArrayList;
import java.util.List;
 
public abstract class BaseSingleSqlParser {
	
	//ԭʼSql���
	protected String originalSql;

	//Sql���Ƭ��
	protected List<SqlSegment> segments;
	
	
	/** *//**
	��* ���캯��������ԭʼSql��䣬�������֡�
	��* @param originalSql
	��*/
	public BaseSingleSqlParser(String originalSql)
	{
		System.out.println("������BaseSingleSqlParser�Ĺ��캯��");
		this.originalSql=originalSql;
		segments=new ArrayList<SqlSegment>();
		initializeSegments();
		//splitSql2Segment();
	}
	
	/** *//**
	��* ��ʼ��segments��ǿ������ʵ��
	��*
	��*/
	protected abstract void initializeSegments();
	
	
	/** *//**
	��* ��originalSql���ֳ�һ����Ƭ��
	��*
	��
	 * @return */
	protected List<List<String>> splitSql2Segment()
	{
		List<List<String>> list=new ArrayList<List<String>>(); 
		
		//System.out.println("������BaseSingleSqlParser��splitSql2Segment���������ڷָ�sqlΪ��ͬ��ģ��");
		
		for(SqlSegment sqlSegment:segments)    // int[] aaa = 1,2,3;   int a:aaa 
		{
			sqlSegment.parse(originalSql);
			list.add(sqlSegment.getBodyPieces());
		}	
		return list;
	}
	
	/** *//**
	��* �õ�������ϵ�Sql���
	��* @return
	��*/
	public String getParsedSql() 
	{
		StringBuffer sb=new StringBuffer();
		for(SqlSegment sqlSegment:segments)
		{
			sb.append(sqlSegment.getParsedSqlSegment()+"n");
		}
		String retval=sb.toString().replaceAll("n+", "n");
		return retval;
	}
	
}
