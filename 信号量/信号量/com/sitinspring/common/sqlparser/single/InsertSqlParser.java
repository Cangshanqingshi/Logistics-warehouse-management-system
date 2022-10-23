package com.sitinspring.common.sqlparser.single;
/** *//**
*
* ���������������
*/
//correct-test
public class InsertSqlParser extends BaseSingleSqlParser{
 
	public InsertSqlParser(String originalSql) {
		super(originalSql);
		
	}
	//insert into table_name (name,age,sex) values ("С��","28","Ů");
	@Override
	protected void initializeSegments() {
		segments.add(new SqlSegment("(insert into)(.+?)([(])","[,]"));
		segments.add(new SqlSegment("([(])(.+?)([)] values [(])","[,]"));
		segments.add(new SqlSegment("([)] values [(])(.+)([)] ENDOFSQL)","[,]"));
		// values
	}
	
	public String getParsedSql()
	{
		String retval=super.getParsedSql();
		retval=retval+")";
		return retval;
	}
		
}