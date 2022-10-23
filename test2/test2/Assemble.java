package test2;

import java.awt.desktop.SystemSleepEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class Assemble {
	private static HSSFWorkbook workbook;
    private static File file = new File("src/resources/data.xls");
    private static FileOutputStream outStream = null;
    private static FileLock lock = null;
    
    public void Execute(String result) {
    	String[] result1= result.split(";");
		int sheetIndex = 0;
		System.out.println(result1[2]);    		
		
		switch(Integer.valueOf(result1[0])) {
			case 1:
				if(result1[2].equals("goodsinfo"))sheetIndex = 1;
	    		else if(result1[2].equals("edgesinfo"))sheetIndex = 2;
	    		else if(result1[2].equals("表名3"))sheetIndex = 3;
	    		else if(result1[2].equals("表名4"))sheetIndex = 4;
				String[] typeStrings = result1[4].split(",");
				int[] types = new int[typeStrings.length];
				for(int i=0; i<types.length; i++)
					types[i] = Integer.valueOf(typeStrings[i]);
//				StringBuffer Index_Select = QuerySpecifiedIndex(sheetIndex, result1[3], Integer.valueOf(result1[4]), result1[5] );
				String Index1 = SelectIndex(sheetIndex, result1[3], types, result1[5], result1[6]);
//				String Index1 = Index_Select.toString();
				ShowData(sheetIndex, result1[1], Index1);
				break;
			case 2:
				if(result1[1].equals("goodsinfo"))sheetIndex = 1;
	    		else if(result1[1].equals("edgesinfo"))sheetIndex = 2;
	    		else if(result1[1].equals("表名3"))sheetIndex = 3;
	    		else if(result1[1].equals("表名4"))sheetIndex = 4;
				StringBuffer Index_Delete = QuerySpecifiedIndex(sheetIndex, result1[2], Integer.valueOf(result1[3]), result1[4] );
				String Index2 = Index_Delete.toString();
				DeleteData(sheetIndex, Index2);
				break;
			case 3:
				if(result1[1].equals("goodsinfo"))sheetIndex = 1;
	    		else if(result1[1].equals("edgesinfo"))sheetIndex = 2;
	    		else if(result1[1].equals("表名3"))sheetIndex = 3;
	    		else if(result1[1].equals("表名4"))sheetIndex = 4;
				StringBuffer Index_Modify = QuerySpecifiedIndex(sheetIndex, result1[4], 3, result1[5] );
				String Index3 = Index_Modify.toString();
				ModifyData(sheetIndex, result1[2], result1[3], Index3);
				break;
			case 4:
				if(result1[1].equals("goodsinfo"))sheetIndex = 1;
	    		else if(result1[1].equals("edgesinfo"))sheetIndex = 2;
	    		else if(result1[1].equals("表名3"))sheetIndex = 3;
	    		else if(result1[1].equals("表名4"))sheetIndex = 4;
				InsertData(sheetIndex, result1[2], result1[3]);
				break;
		}
	}
    
    private static String SelectIndex(int sheetIndex, String name, int[] type, String condition,String relation) {

    	String[] names = name.split(",");
    	String[] conditions = condition.split(",");
    	String[] relations = relation.split(",");
    	
    	String Indexs = new String();
    	List<String> indexsList = new ArrayList<>();
    	int[] orIndex = new int[100];
    	int t=0;
    	List<List<String>> indexsLists = new ArrayList<List<String>>();
    	for(int i=0; i<conditions.length; i++) {
    		List<String> index_tempList = new ArrayList<>();
    		StringBuffer Index_Modify = QuerySpecifiedIndex(sheetIndex, names[i], type[i], conditions[i] );
    		String indexString = Index_Modify.toString();
    		String[] indexStrings = indexString.split(",");
    		for(int j=0; j<indexStrings.length; j++) {
    			index_tempList.add(indexStrings[j]);
    		}
    		indexsLists.add(index_tempList);
    	}
    	if(!relations[0].equals("null")) {
    		for(int i=0; i<relations.length; i++) {
	    		if(relations[i].equals("1")) {
	    			indexsLists.get(i+1).retainAll(indexsLists.get(i));
	    		}
	    		else {
	    			orIndex[t] = i;
	    			t++;
	    		}
	    	}
    	}
    	indexsList = indexsLists.get(indexsLists.size()-1);
    	for(int i=0; i<t; i++) {
    		indexsList.removeAll(indexsLists.get(orIndex[i]));
    		indexsList.addAll(indexsLists.get(orIndex[i]));
    	}
    	for(int i=0; i<indexsList.size(); i++) {
    		Indexs += indexsList.get(i);
    		if(i!=indexsList.size())
    			Indexs += ",";
    	}
    	return Indexs;
	}
    
    private static boolean readDataFromFile() {
        try {
            workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
        } catch (IOException e) {
            System.out.print(".");
//                e.printStackTrace();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    private static void saveFile() {
        try {
            file.createNewFile();
            outStream = FileUtils.openOutputStream(file);   // 将excel存盘
            FileChannel channel = outStream.getChannel();
            try {
                //方法一
                lock = channel.lock();
                workbook.write(outStream);                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != lock) {
                try {
                    lock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    private static StringBuffer QuerySpecifiedIndex(int sheetIndex, String name, int type, String condition) {
    	StringBuffer Index = new StringBuffer();
    	while (true) {
            //System.out.println();
            //System.out.println("===============  指定列号查询 ===============");
            //System.out.println("输入0返回上一层：");
            //System.out.println();
            try {
                while(true) {
                	boolean readFileSuccess = readDataFromFile();
                    if (!readFileSuccess) continue;
                    //String[] inputs_query = name.split(",");
                    //String[] inputs_condition = condition.split(",");
                    
                    
                    int column_queryIndex = -1 ;
                    
                    HSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
                    HSSFRow titleRow = sheet.getRow(0);
                    int lastCellNum = titleRow.getLastCellNum();
                    if(name.equals("null")) {
                    	int RowNum = sheet.getLastRowNum();
                    	//int CellNum = titleRow.getLastCellNum();
                    	for(int i = 0; i < RowNum; i++ ) {
                    		Index.append(i + ",");
                    	}
                    	Index.deleteCharAt(Index.length()-1);
                    	return Index;
                    }
                    for (int i = 0; i < lastCellNum; i++) {                   
                        for (int k = 0; k < 1; k++) {
                            HSSFCell titleCell = titleRow.getCell(i);
                            String title = titleCell.getStringCellValue();
                            if (name.equals(title)) {
                                column_queryIndex = i;
                            }
                        }
                    }
                    int lastRowNum = sheet.getLastRowNum();
                    //boolean judge_if = false;
                    for (int i = 1; i < lastRowNum; i++) {
                        boolean judge = true;
                        HSSFRow row = sheet.getRow(i);
                        switch(type) {
                        	case 1:
                        		for (int j = 0; j < lastCellNum; j++) {
                                	if (j == column_queryIndex) {
                                		HSSFCell cell1 = row.getCell(j);
                                		String value1 = cell1.getStringCellValue();
                                		int value2 = Integer.valueOf(value1);
                                		int condition1 = Integer.valueOf(condition);
                                		if(value2 < condition1) {
                                			judge = true;
                                		}
                                		else {
                                			judge = false;
                                		}
                                	}
                        			if(!judge){
                        				break;
                        			}
                                }
                        		break;
                        	case 2:
                        		for (int j = 0; j < lastCellNum; j++) {
                                	if (j == column_queryIndex) {
                                		HSSFCell cell1 = row.getCell(j);
                                		String value1 = cell1.getStringCellValue();
                                		int value2 = Integer.valueOf(value1);
                                		int condition1 = Integer.valueOf(condition);
                                		if(value2 <= condition1) {
                                			judge = true;
                                		}
                                		else {
                                			judge = false;
                                		}
                                	}
                        			if(!judge){
                        				break;
                        			}
                                }
                        		break;
                        	case 3:
                        		for (int j = 0; j < lastCellNum; j++) {
                                	if (j == column_queryIndex) {
                                		HSSFCell cell1 = row.getCell(j);
                                		String value1 = cell1.getStringCellValue();
                                		if(value1.contentEquals(condition)) {
                                			judge = true;
                                		}
                                		else {
                                			judge = false;
                                		}
                                	}
                        			if(!judge){
                        				break;
                        			}
                                }
                        		break;
                        	case 4:
                        		for (int j = 0; j < lastCellNum; j++) {
                                	if (j == column_queryIndex) {
                                		HSSFCell cell1 = row.getCell(j);
                                		String value1 = cell1.getStringCellValue();
                                		int value2 = Integer.valueOf(value1);
                                		int condition1 = Integer.valueOf(condition);
                                		if(value2 > condition1) {
                                			judge = true;
                                		}
                                		else {
                                			judge = false;
                                		}
                                	}
                        			if(!judge){
                        				break;
                        			}
                                }
                        		break;
                        	case 5:
                        		for (int j = 0; j < lastCellNum; j++) {
                                	if (j == column_queryIndex) {
                                		HSSFCell cell1 = row.getCell(j);
                                		String value1 = cell1.getStringCellValue();
                                		int value2 = Integer.valueOf(value1);
                                		int condition1 = Integer.valueOf(condition);
                                		if(value2 >= condition1) {
                                			judge = true;
                                		}
                                		else {
                                			judge = false;
                                		}
                                	}
                        			if(!judge){
                        				break;
                        			}
                                }
                        		break;
                        	case 6:
                        		for (int j = 0; j < lastCellNum; j++) {
                                	if (j == column_queryIndex) {
                                		HSSFCell cell1 = row.getCell(j);
                                		String value1 = cell1.getStringCellValue();
                                		if(value1.contains(condition)) {
                                			judge = true;
                                		}
                                		else {
                                			judge = false;
                                		}
                                	}
                        			if(!judge){
                        				break;
                        			}
                                }
                        		break;
                        }
                        
                        if(judge) {
                        	Index.append(i + ",");
                        }
                    }
                    Index.deleteCharAt(Index.length()-1);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
    	return Index;
    }
    
    
    
    private static void ShowData(int sheetIndex, String name, String Index) {
    	try {
    		while(true) {
    			String[] Index1 = Index.split(",");
    			String[] name1 = name.split(",");
    			HSSFSheet sheet = workbook.getSheetAt(sheetIndex-1);
    			HSSFRow titleRow = sheet.getRow(0);
    			int lastCellNum = titleRow.getLastCellNum();
    			
    			int[] columnIndex = new int[name1.length];
    			
    			
    			if(name1[0].equals("all")) {
    				int CellNum = titleRow.getLastCellNum();
    				for(int i = 0; i < Index1.length; i++) {
        				HSSFRow ShowRow = sheet.getRow(Integer.valueOf(Index1[i]));   				
        				for(int j = 0; j < CellNum; j++) {
        					HSSFCell ShowCell = ShowRow.getCell(j);
        					String value = ShowCell.getStringCellValue();
        					System.out.print(value + " ");
        				}
        				System.out.println();
        			}
    				return;
    			}
    			for(int i = 0; i < lastCellNum; i++) {
    				HSSFCell titleCell = titleRow.getCell(i);
    				String title = titleCell.getStringCellValue();
    				for(int j = 0; j < name1.length; j++) {
    					if(title.equals(name1[j])) {
    						columnIndex[j] = i;
    					}
    				}
    			}
    			
    			for(int j = 0; j < columnIndex.length; j++) {
					HSSFCell titleCell = titleRow.getCell(columnIndex[j]);
					String value = titleCell.getStringCellValue();
					System.out.print(value + " ");
				}
				System.out.println();
    			for(int i = 0; i < Index1.length; i++) {
    				HSSFRow ShowRow = sheet.getRow(Integer.valueOf(Index1[i]));   				
    				for(int j = 0; j < columnIndex.length; j++) {
    					HSSFCell ShowCell = ShowRow.getCell(columnIndex[j]);
    					String value = ShowCell.getStringCellValue();
    					System.out.print(value + " ");
    				}
    				System.out.println();
    			}
    			return;
    		}
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    private static void DeleteData(int sheetIndex, String RowIndex_String ) {
    	try {
    		while(true) {
    			boolean readFileSuccess = readDataFromFile();
    			String[] Index1 = RowIndex_String.split(",");
                if (!readFileSuccess) continue;
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex-1);
                for(int i = 0; i < Index1.length; i++) {
                	int RowIndex = Integer.valueOf(Index1[i]);
                	sheet.shiftRows(RowIndex+1-i,sheet.getLastRowNum(),-1);
                }
                saveFile();
                System.out.println("删除成功！");
                return;
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    private static void ModifyData(int sheetIndex, String name, String element, String RowIndex_Modify) {
    	try {
    		while(true) {
    			boolean readFileSuccess = readDataFromFile();
                if (!readFileSuccess) continue;
                
                String[] Index1 = RowIndex_Modify.split(",");
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex-1);
                HSSFRow titleRow = sheet.getRow(0);
                
                
                String[] name1 = name.split(",");
                String[] element1 = element.split(",");
                int[] column_Index = new int[name1.length];
                for(int i = 0; i < titleRow.getLastCellNum(); i++) {
                	for(int j = 0; j < name1.length; j++) {
                		HSSFCell cell = titleRow.getCell(i);
                		String value = cell.getStringCellValue();
                		if(value.equals(name1[j])) {
                			column_Index[j] = i;
                		}
                	}
                }
                for(int i = 0; i < Index1.length; i++) {
                	HSSFRow modifyRow = sheet.getRow(Integer.valueOf(Index1[i]));
                	for(int j = 0; j < column_Index.length; j++) {
                    	HSSFCell modifyCell = modifyRow.getCell(column_Index[j]);
                    	modifyCell.setCellValue(element1[j]);
                    }
                }   
                System.out.println("修改成功！");
                saveFile();              
                return;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}   	
    }
    
    
    private static void InsertData(int sheetIndex, String name, String element) {
    	try {
    		while(true) {
    			boolean readFileSuccess = readDataFromFile();
                if (!readFileSuccess) continue;
                String[] element1 = element.split(",");
                String[] name1 = name.split(",");
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex - 1);
                HSSFRow row = sheet.getRow(0);
                int lastRowNum = sheet.getLastRowNum();
                int lastCellNum = row.getLastCellNum();
                HSSFRow nextRow = sheet.createRow(lastRowNum + 1);
                int[] columnIndex = new int[name1.length];
                
                for(int i = 0; i < lastCellNum; i++) {
    				HSSFCell titleCell = row.getCell(i);
    				String title = titleCell.getStringCellValue();
    				for(int j = 0; j < name1.length; j++) {
    					if(title.equals(name1[j])) {
    						columnIndex[j] = i;
    					}
    				}
    			}
                /*
                for (int i = 0; i < lastCellNum; i++) {
                	HSSFCell addCell = nextRow.createCell(i);
                	addCell.setCellValue(element1[i]);
                	
                }
                */
                for (int i = 0; i < lastCellNum; i++) {
                    HSSFCell addCell = nextRow.createCell(i);
                    boolean judge = false;
                    for(int j = 0; j < columnIndex.length; j++) {
                    	if(i == columnIndex[j]) {
                    		addCell.setCellValue(element1[j]);
                    		judge = true;                   		
                    	}
                    }
                    if(!judge) {
                		addCell.setCellValue("null");
                	}
                    //String value = addCell.getStringCellValue();
            		//System.out.print(value + " ");
                }
                saveFile();
                System.out.println("增加成功！");
                
                return;
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}    	
    }

    
    
    
}
