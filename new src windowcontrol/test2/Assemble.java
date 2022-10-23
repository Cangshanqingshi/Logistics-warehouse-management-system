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
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import test1.SharedData;
import test1.window;


public class Assemble {
	private static HSSFWorkbook workbook;
    private static File file = new File("src/resources/data.xls");
    private static FileOutputStream outStream = null;
    private static FileLock lock = null;
    
    static SharedData sharedData;
    window w;
    
    public Assemble(SharedData sharedData2, window w){
		Assemble.sharedData = sharedData2;
		this.w = w;
	}
    
    public void Execute(String result) {
    	String[] result1= result.split(";");
		int sheetIndex = 0;	
		
		switch(Integer.valueOf(result1[0])) {
			case 1:
				try {
	                // 第一个(cnt==0)读者进行P操作
	                sharedData.queue.acquire();
	                sharedData.mutex.acquire();
	                if(sharedData.readerCnt == 0){
	                    sharedData.rw.acquire();
	                }
	                sharedData.readerCnt++;
	                sharedData.mutex.release();
	                sharedData.queue.release();
	                // 开始读 >_<
	                
					if(result1[2].equals("goodsinfo"))sheetIndex = 1;
		    		else if(result1[2].equals("edgesinfo"))sheetIndex = 2;
		    		else if(sheetIndex == 0) {
		    			System.out.println("该表不存在，请重新输入。");
		    			break;
		    		}
					String[] typeStrings_select = result1[4].split(",");
					int[] types_select = new int[typeStrings_select.length];
					for(int i=0; i<types_select.length; i++)
						types_select[i] = Integer.valueOf(typeStrings_select[i]);
					String Index1 = SelectIndex(sheetIndex, result1[3], types_select, result1[5], result1[6]);
					if(Index1.equals("null")) {
						break;
					}
					ShowData(sheetIndex, result1[1], Index1);
					
					// 最后一个(cnt==0)读者进行V操作
	                sharedData.mutex.acquire();
	                sharedData.readerCnt--;
	                if(sharedData.readerCnt == 0){
	                    sharedData.rw.release();
	                }
	               
	                w.displayMessage("查询完毕，输入任意值结束当前任务");
	                w.displayMessage(Thread.currentThread().getName() + " is stoping");
	                while(w.flag) {System.out.println("\r");};
	                Scanner input = new Scanner(w.Rmessage);
	                w.flag = true;
	                input.close();
	                
	                sharedData.mutex.release();
	                // 睡一觉zzz
	                Thread.sleep(new Random().nextInt(2));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
	                // 核心逻辑
	                sharedData.queue.acquire();
	                sharedData.rw.acquire();
	                
					if(result1[1].equals("goodsinfo"))sheetIndex = 1;
		    		else if(result1[1].equals("edgesinfo"))sheetIndex = 2;
		    		else if(sheetIndex == 0) {
		    			System.out.println("该表不存在，请重新输入。");
		    			break;
		    		}
					StringBuffer Index_Delete = QuerySpecifiedIndex(sheetIndex, result1[2], Integer.valueOf(result1[3]), result1[4] );
					String Index2 = Index_Delete.toString();
					DeleteData(sheetIndex, Index2);
					
					w.displayMessage("删除完毕，输入任意值结束当前任务");
	                w.displayMessage(Thread.currentThread().getName() + " is stoping");
	                while(w.flag) {System.out.println("\r");};
	                Scanner input = new Scanner(w.Rmessage);
	                w.flag = true;
	                input.close();
					
					sharedData.rw.release();
	                sharedData.queue.release();
	                // 睡一觉zzz
	                Thread.sleep(new Random().nextInt(2));
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
				break;
			case 3:
				try {
	                // 核心逻辑
	                sharedData.queue.acquire();
	                sharedData.rw.acquire();
	                
					if(result1[1].equals("goodsinfo"))sheetIndex = 1;
		    		else if(result1[1].equals("edgesinfo"))sheetIndex = 2;
		    		else if(sheetIndex == 0) {
		    			System.out.println("该表不存在，请重新输入。");
		    			break;
		    		}	
					String[] typeStrings_update = result1[5].split(",");
					int[] types_update = new int[typeStrings_update.length];
					for(int i=0; i<types_update.length; i++)
						types_update[i] = Integer.valueOf(typeStrings_update[i]);
					String Index_update = SelectIndex(sheetIndex, result1[4], types_update, result1[6], result1[7]);
					ModifyData(sheetIndex, result1[2], result1[3], Index_update);
					//System.out.println("修改成功！");
	                //System.out.println();
	                w.displayMessage("修改完毕，输入任意值结束当前任务");
					w.displayMessage(Thread.currentThread().getName() + " is stoping");
	                while(w.flag) {System.out.println("\r");};
	                Scanner input = new Scanner(w.Rmessage);
	                w.flag = true;
	                input.close();
					
					sharedData.rw.release();
	                sharedData.queue.release();
	                // 睡一觉zzz
	                Thread.sleep(new Random().nextInt(2));
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
				break;
			case 4:
				try {
	                // 核心逻辑
	                sharedData.queue.acquire();
	                sharedData.rw.acquire();
	                
					if(result1[1].equals("goodsinfo"))sheetIndex = 1;
		    		else if(result1[1].equals("edgesinfo"))sheetIndex = 2;
		    		else if(sheetIndex == 0) {
		    			System.out.println("该表不存在，请重新输入。");
		    			break;
		    		}
					InsertData(sheetIndex, result1[2], result1[3]);
					
					sharedData.rw.release();
	                sharedData.queue.release();
	                // 睡一觉zzz
	                Thread.sleep(new Random().nextInt(2));
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
				break;
			case 5:
				try {
	                // 核心逻辑
	                sharedData.queue.acquire();
	                sharedData.rw.acquire();
	                
					createTalbe(result1[1], result1[2]);
					sharedData.rw.release();
	                sharedData.queue.release();
	                // 睡一觉zzz
	                Thread.sleep(new Random().nextInt(2));
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
				break;
		}
	}
    
    private String SelectIndex(int sheetIndex, String name, int[] type, String condition,String relation) {

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
    		String Index_Modify1 = Index_Modify.toString();
    		if(Index_Modify1.equals("null")) {
    			System.out.println("字段名 "+names[i]+" 不存在");
    			w.displayMessage("字段名 "+names[i]+" 不存在");
    			return Indexs = "null";
    		}
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
    
    
    public static StringBuffer QuerySpecifiedIndex(int sheetIndex, String name, int type, String condition) {
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
                    
                    boolean judge1 = false;
                    
                    if(name.equals("null")) {
                    	int RowNum = sheet.getLastRowNum();
                    	for(int i = 0; i < RowNum; i++ ) {
                    		HSSFRow row = sheet.getRow(i+1);
                    		HSSFCell flagCell = row.getCell(lastCellNum - 1);
                            String flag = flagCell.getStringCellValue();
                            if (flag.equals("0")) continue;
                    		Index.append(i+1 + ",");
                    	}
                    	Index.deleteCharAt(Index.length()-1);
                    	return Index;
                    }
                    
                    for(int i = 0; i < lastCellNum; i++){
                    	HSSFCell titleCell = titleRow.getCell(i);
                    	String title = titleCell.getStringCellValue();
                    	if(name.equals(title)) {
                    		judge1 = true;
                    	}
                    }
                    if(!judge1) {
                    	return Index.append("null");
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
                    for (int i = 1; i <= lastRowNum; i++) {
                        boolean judge = true;
                        HSSFRow row = sheet.getRow(i);
                        HSSFCell flagCell = row.getCell(lastCellNum - 1);
                        String flag = flagCell.getStringCellValue();
                        if (flag.equals("0")) continue;
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
                                		int value2 = Integer.valueOf(value1.trim());
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
    
    
    
    private void ShowData(int sheetIndex, String name, String Index) {
    	try {
    		while(true) {
    			if(Index.isEmpty()){
    				System.out.println("没有符合条件的数据。");
    				System.out.println();
    				w.displayMessage("没有符合条件的数据。");
    				return;
    			}  			
    			
    			String[] Index1 = Index.split(",");
    			String[] name1 = name.split(",");
    			HSSFSheet sheet = workbook.getSheetAt(sheetIndex-1);
    			HSSFRow titleRow = sheet.getRow(0);
    			int lastCellNum = titleRow.getLastCellNum();
    			
    			int[] columnIndex = new int[name1.length];
    			int[] columnIndexJudge = new int[name1.length];
    			
    			
    			if(name1[0].equals("all")) {
    				int CellNum = titleRow.getLastCellNum();
    				
    				HSSFRow showTitleRow = sheet.getRow(0); 
    				for(int j = 0; j < CellNum; j++) {
    					HSSFCell ShowCell = showTitleRow.getCell(j);
    					String value = ShowCell.getStringCellValue();
    					System.out.print(value + " ");
    					w.displayMessage(value + " ");
    				}
    				System.out.println();
    				
    				for(int i = 0; i < Index1.length; i++) {
        				HSSFRow ShowRow = sheet.getRow(Integer.valueOf(Index1[i])); 
        				for(int j = 0; j < CellNum; j++) {
        					HSSFCell ShowCell = ShowRow.getCell(j);
        					String value = ShowCell.getStringCellValue();
        					System.out.print(value + " ");
        					w.displayMessage(value + " ");
        				}
        				System.out.println();
        			}
    				System.out.println();
    				return;
    			}
    			for(int i = 0; i < lastCellNum; i++) {
    				HSSFCell titleCell = titleRow.getCell(i);
    				String title = titleCell.getStringCellValue();
    				for(int j = 0; j < name1.length; j++) {
    					if(title.equals(name1[j])) {
    						columnIndex[j] = i;
    						columnIndexJudge[j] = 1;
    					}
    				}
    			}
    			for(int i = 0; i < name1.length; i++) {
    				if(columnIndexJudge[i] == 0) {
    					System.out.println("字段名 " + name1[i] + " 不存在");
    					w.displayMessage("字段名 " + name1[i] + " 不存在");
    					return;
    				}
    			}
    			
    			for(int j = 0; j < columnIndex.length; j++) {
					HSSFCell titleCell = titleRow.getCell(columnIndex[j]);
					String value = titleCell.getStringCellValue();
					System.out.print(value + " ");
				}
    			for(int i = 0; i < Index1.length; i++) {
    				HSSFRow ShowRow = sheet.getRow(Integer.valueOf(Index1[i]));   				
    				for(int j = 0; j < columnIndex.length; j++) {
    					HSSFCell ShowCell = ShowRow.getCell(columnIndex[j]);
    					String value = ShowCell.getStringCellValue();
    					System.out.print(value + " ");
    					w.displayMessage(value + " ");
    				}
    				System.out.println();
    			}
    			System.out.println();
    			return;
    		}
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    private void DeleteData(int sheetIndex, String RowIndex_String ) {
    	try {
    		while(true) {
    			boolean readFileSuccess = readDataFromFile();
    			String[] Index1 = RowIndex_String.split(",");
                if (!readFileSuccess) continue;
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex-1);
                for(int i = 0; i < Index1.length; i++) {
                	int RowIndex = Integer.valueOf(Index1[i]);
                	HSSFRow deleteRow = sheet.getRow(RowIndex);
                    int lastCellNum = deleteRow.getLastCellNum();
                    HSSFCell deleteCell = deleteRow.getCell(lastCellNum - 1);
                    deleteCell.setCellValue("0");
                }
                saveFile();
                //System.out.println("删除成功！");
                //System.out.println();
                return;
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public	static void ModifyData(int sheetIndex, String name, String element, String RowIndex_Modify) {
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
                
                saveFile();              
                return;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}   	
    }
    
    
    private void InsertData(int sheetIndex, String name, String element) {
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
                    	if(i == lastCellNum - 1) {
                    		addCell.setCellValue("1");
                    	}
                    	else if(i == columnIndex[j]) {
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
                //System.out.println("增加成功！");
                //System.out.println();
                return;
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}    	
    }

    
    private void createTalbe(String tableName, String filedName) {
    	try {
    		while(true) {
    			boolean readFileSuccess = readDataFromFile();
                if (!readFileSuccess) continue;
                
                String[] filedNames = filedName.split(",");
                HSSFSheet createSheet = workbook.createSheet(tableName);
                HSSFRow firstRow = createSheet.createRow(0);
                
                for(int i = 0; i < filedNames.length; i++) {
                	HSSFCell createCell = firstRow.getCell(i);
                	createCell.setCellValue(filedNames[i]);
                }
                //System.out.println("增加成功！");
                //System.out.println();
                saveFile();
                return;
    		}
    		
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
}
