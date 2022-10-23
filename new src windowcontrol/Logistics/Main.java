package Logistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import test2.Assemble;

public class Main {
	
	private static final double INF = 9999999.0;
	private static final int MaxNode = 1000;
	
    private static final int goodsIdIndex = 0;
    private static final int goodsWeightIndex = 1;
    private static final int sourceAreaIndex = 2;
    private static final int destinationAreaIndex = 3;
    private static final int customerIDIndex = 4;
    private static final int customerGradeIndex = 5;
    private static final int isUrgentIndex = 6;
    private static final int expectedArrivingDateIndex = 7;
    private static final int hasSendFlagIndex = 8;

    private static final int edgesIDIndex = 0;
    private static final int StartpointIndex = 1;
    private static final int EndpointIndex = 2;
    private static final int PriceIndex = 3;
    private static final int TimeConsumeIndex = 4;
    
    private static ArrayList<Goods> goodsList = new ArrayList<Goods>();//物品总表
    private static ArrayList<Edges> edgesList = new ArrayList<Edges>();//边的总表 
    private static List<String> belongingRegionList = new ArrayList<String>();
    
    private static double[][] Graph1 = new double[MaxNode][MaxNode];
    private static double[][] Graph2 = new double[MaxNode][MaxNode];
    private static double[][] Graph3 = new double[MaxNode][MaxNode];
    
	public void menu() {
	    System.out.println("------------物流管理-----------");
        System.out.println("读取数据中...");
            
            while (true) {
//            	//测试用
//            	System.out.println("G1：");
//            	for(int i = 0; i < belongingRegionList.size(); i++) {
//            		System.out.print(belongingRegionList.get(i) + "\t" + "\t");
//            	}
//            	System.out.println();
//                for(int i = 0; i < belongingRegionList.size(); i++) {
//                	System.out.print(belongingRegionList.get(i) + "\t" + "\t");
//            		for(int j = 0; j < belongingRegionList.size(); j++) {
//        	        	System.out.print(Graph1[i][j] + "\t");
//            		}
//            		System.out.println();
//            	}
//            	System.out.println("节点有：" + belongingRegionList);
    			
            	System.out.println("1.进行发货");
    			System.out.println("2.物流单号查询");
    			System.out.println("3.输出发货顺序");
                System.out.println("0.退出系统");
                
                Scanner input = new Scanner(System.in);
                int inputValue;
                
                try {
                    inputValue = input.nextInt();
                    switch (inputValue) {
                        case 0:
                            return;
                        case 1:
                            reigion_sendGoods();
                            break;
                        case 2:
                            checkGoods();
                            break;
                        case 3:
                            methodOutput();
                            break;
                        default:
                            System.out.println("输入错误！请重新输入指令！");
                            break;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("输入错误！请重新输入！");
                }
            }
    }
	
	private static void listInit() {
        File file1 = new File("src/resources/goodsInfo.xls");
        File file2 = new File("src/resources/edgesInfo.xls");
        try {
        	readDataFromFile1(file1);
        	readDataFromFile2(file2);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	private static void readDataFromFile1(File file) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
        // 读取默认第一个工作表sheet
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Set<String> belongingRegionSet = new HashSet<>();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row.getCell(goodsIdIndex)==null)
            	break;
            HSSFCell cell0 = row.getCell(goodsIdIndex);
            HSSFCell cell1 = row.getCell(goodsWeightIndex);
            HSSFCell cell2 = row.getCell(sourceAreaIndex);
            HSSFCell cell3 = row.getCell(destinationAreaIndex);
            HSSFCell cell4 = row.getCell(customerIDIndex);
            HSSFCell cell5 = row.getCell(customerGradeIndex);
            HSSFCell cell6 = row.getCell(isUrgentIndex);
            HSSFCell cell7 = row.getCell(expectedArrivingDateIndex);
            HSSFCell cell8 = row.getCell(hasSendFlagIndex);
            int goodsId = Integer.parseInt(cell0.getStringCellValue());
            int goodsWeight = Integer.parseInt(cell1.getStringCellValue());
            String sourceArea = cell2.getStringCellValue();
            belongingRegionSet.add(sourceArea);
            String destinationArea = cell3.getStringCellValue();
            int customerID = Integer.parseInt(cell4.getStringCellValue());
            int customerGrade = Integer.parseInt(cell5.getStringCellValue());
            int isUrgent = Integer.parseInt(cell6.getStringCellValue());
            String expectedArrivingDate = cell7.getStringCellValue();
            int hasSend = Integer.parseInt(cell8.getStringCellValue());

            Goods goods = new Goods(goodsId, goodsWeight, sourceArea, destinationArea, customerID, customerGrade, isUrgent, expectedArrivingDate, hasSend);
            goodsList.add(goods);
        }
        belongingRegionList.addAll(belongingRegionSet);
	}
   
	private static void readDataFromFile2(File file) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
        // 读取默认第一个工作表sheet
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row.getCell(edgesIDIndex)==null)
            	break;
            HSSFCell cell0 = row.getCell(edgesIDIndex);
            HSSFCell cell1 = row.getCell(StartpointIndex);
            HSSFCell cell2 = row.getCell(EndpointIndex);
            HSSFCell cell3 = row.getCell(PriceIndex);
            HSSFCell cell4 = row.getCell(TimeConsumeIndex);
            int ID = Integer.parseInt(cell0.getStringCellValue());
            String Startpoint = cell1.getStringCellValue();
            String Endpoint = cell2.getStringCellValue();
            double Price = Double.parseDouble(cell3.getStringCellValue());
            double TimeConsume = Double.parseDouble(cell4.getStringCellValue());
            Edges edges = new Edges(ID, Startpoint, Endpoint, Price, TimeConsume);
            edgesList.add(edges);
        }
        //initGraph
        for(int i = 0; i < belongingRegionList.size(); i++) {
    		for(int j = 0; j < belongingRegionList.size(); j++) {
	        	Graph1[i][j] = INF;
	    		Graph2[i][j] = INF;
	    		Graph3[i][j] = INF;
    		}
    	}

    	for(int i = 0; i < edgesList.size(); i++) {
    		int m = belongingRegionList.indexOf(edgesList.get(i).Startpoint);
    		int n = belongingRegionList.indexOf(edgesList.get(i).Endpoint);
    		Graph1[m][n] = edgesList.get(i).Price;
    		Graph1[n][m] = edgesList.get(i).Price;
    		Graph2[m][n] = edgesList.get(i).TimeConsume;
    		Graph2[n][m] = edgesList.get(i).TimeConsume;
    		Graph3[m][n] = edgesList.get(i).Overall;
    		Graph3[n][m] = edgesList.get(i).Overall;
    	}
	}
   
	private void reigion_sendGoods() {
		listInit();
        while (true) {
            System.out.println("----------请输入地区----------");

            for (int i = 0; i < belongingRegionList.size(); i++) {
                System.out.println(i + 1 + "." + belongingRegionList.get(i));
            }
            System.out.println();
            System.out.println("0.返回");

            try {
                Scanner input = new Scanner(System.in);//输入序号
                int inputValue = input.nextInt();
                if (inputValue == 0) {
                    return;
                } else if (inputValue <= belongingRegionList.size() && inputValue>0) {
                    method_sendGoods(belongingRegionList.get(inputValue-1).toString());
                } else {
                    System.out.println("输入错误！请重新输入地区！");
                }
            } catch (Exception e) {
                System.out.println("输入错误！请重新输入！");
            }
        }
	}
	
	private void method_sendGoods(String chosenArea) {
        while (true) {

            System.out.println("----------请输入方案----------");
			System.out.println("1.速度优先方案");
			System.out.println("2.综合最优方案");
			System.out.println("3.价格最优方案");
            System.out.println("0.返回");
            
            Scanner input = new Scanner(System.in);
            int inputValue;
            
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                    	sendGoods(chosenArea, 1);
                        break;
                    case 2:
                    	sendGoods(chosenArea, 2);
                        break;
                    case 3:
                    	sendGoods(chosenArea, 3);
                        break;
                    default:
                        System.out.println("输入错误！请重新输入指令！");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("输入错误！请重新输入！");
            }

        }
    }
	
	private void sendGoods(String chosenArea, int logMethod) {
        while (true) {
            System.out.println("请输入1发货，输入0返回：");
            Scanner input = new Scanner(System.in);
            int inputValue;
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                    	if (goodsList.stream()
                                .filter(goods -> goods.sourceArea.equals(chosenArea))
                                .filter(goods -> goods.logMethod == logMethod)
                                .filter(goods -> goods.hasSend == 0)
                                .count() != 0) {
	                        goodsList.stream()
	                                .filter(goods -> goods.sourceArea.equals(chosenArea))
	                                .filter(goods -> goods.logMethod == logMethod)
	                                .max(Comparator.comparing(Goods::getPriority))
	                                .stream()
	                                .forEach(el -> {
	                                    System.out.println(el);
	                                    minStep(belongingRegionList.indexOf(el.sourceArea), belongingRegionList.indexOf(el.destinationArea), logMethod);
	                                });
	                        
	                        System.out.println(goodsList.stream()
	                                .filter(goods -> goods.sourceArea.equals(chosenArea))
	                                .filter(goods -> goods.logMethod == logMethod)
	                                .max(Comparator.comparing(Goods::getPriority)).get()
	                                .customerID+"");
	        				StringBuffer Index_Modify = Assemble.QuerySpecifiedIndex(1, "id", 3, goodsList.stream()
	                                .filter(goods -> goods.sourceArea.equals(chosenArea))
	                                .filter(goods -> goods.logMethod == logMethod)
	                                .max(Comparator.comparing(Goods::getPriority)).get()
	                                .customerID+"");
	        				String Index3 = Index_Modify.toString();
	        				System.out.println(Index3.length());
	        				Assemble.ModifyData(1, "hassend", "1", Index3);
	                        remove(goodsList, goodsList.stream()
	                                .filter(goods -> goods.sourceArea.equals(chosenArea))
	                                .filter(goods -> goods.logMethod == logMethod)
	                                .max(Comparator.comparing(Goods::getPriority)).get());
                    	}else {
                        	System.out.println("该地区已无货物需要发送~");
                    	}
                    	break;
                    default:
                    	System.out.println("输入错误！请重新输入指令！");
                    	break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("输入错误！请重新输入！");
            }
        }		
	}
	
	private void checkGoods() {
		listInit();
        while (true) {
            System.out.println("----------请输入物品ID----------");
            System.out.println();
            System.out.println("0.返回");

            try {
                Scanner input = new Scanner(System.in);//输入ID
                int inputValue = input.nextInt();
                int findFlag = 0;
                if (inputValue == 0) {
                    return;
                } else { 
                	for (int i = 0; i < goodsList.size(); i++) {
                		if (inputValue == goodsList.get(i).ID)  {
                			System.out.println(goodsList.get(i).toString());
                			minStep(belongingRegionList.indexOf(goodsList.get(i).sourceArea)
                					, belongingRegionList.indexOf(goodsList.get(i).destinationArea)
                					, goodsList.get(i).logMethod);
                			findFlag = 1;
                			break;
                		}
                	}
                	if (findFlag==0) {
                		System.out.println("找不到该ID对应的物流物品，请重新输入！");

                	}
                }
            } catch (Exception e) {
                System.out.println("输入错误！请重新输入！");
            }
        }
	}

	private void methodOutput() {
        
		listInit();
		
		while (true) {
            System.out.println("----------请输入模式序号----------");
			System.out.println("1.速度优先方案");
			System.out.println("2.综合最优方案");
			System.out.println("3.价格最优方案");
            System.out.println("0.返回");
            
            Scanner input = new Scanner(System.in);
            int inputValue;
            
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                    	printGoodsInThisMethod(1);
                        break;
                    case 2:
                    	printGoodsInThisMethod(2);
                        break;
                    case 3:
                    	printGoodsInThisMethod(3);
                        break;
                    default:
                        System.out.println("输入错误！请重新输入方案！");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("输入错误！请重新输入！");
            }
        }
	}
	
	private void printGoodsInThisMethod(int logMethod) {
		while (true) {

            System.out.println("----------请输入地区序号----------");

            for (int i = 0; i < belongingRegionList.size(); i++) {
                System.out.println(i + 1 + "." + belongingRegionList.get(i));//可能需要调，应调用其ID
            }
            System.out.println();
            System.out.println("0.返回");

            try {
                Scanner input = new Scanner(System.in);//输入序号
                int inputValue = input.nextInt();
                if (inputValue == 0) {
                    return;
                } else if (inputValue <= belongingRegionList.size() && inputValue>0) {
                	String sourceArea = belongingRegionList.get(inputValue-1).toString();
                    goodsList.stream()
                            .filter(goods -> goods.sourceArea.equals(sourceArea))
                            .filter(goods -> goods.logMethod == logMethod)
                            .sorted((o1, o2) -> {
                                double diff = o1.priority - o2.priority;
                                if (diff == 0) {
                                    return 0;
                                }
                                return diff > 0 ? -1 : 1;
                            })
                            .forEach(System.out::println);
                    System.out.println();
                } else {
                    System.out.println("输入错误！请重新输入地区！");
                }
            } catch (Exception e) {
                System.out.println("输入错误！请重新输入！");
            }
        }
    }
	
    private void minStep(int start, int end, int logMethod) {
        double[][] Graphx = new double[belongingRegionList.size()][belongingRegionList.size()];
    		if (logMethod == 1) Graphx = Graph2; //如果为1，选择速度优先的方案
    		if (logMethod == 2) Graphx = Graph3; //如果为2，选择综合最优方案；
    		if (logMethod == 3)	Graphx = Graph1;//如果为3，选择价格最优方案；
            Dijkstra.minStep(Graphx, start, end, belongingRegionList);
            System.out.println();
    } 
	
//    private static void minStep(int start, int end, int logMethod) {
//        File file = new File("src/main/resources/regionDistance.xls");
//        try {
//            int[][] Graph = new int[belongingRegionList.size()][belongingRegionList.size()];
//            HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
//            // 读取默认第一个工作表sheet
//            HSSFSheet sheet = workbook.getSheetAt(logMethod - 1);
//            for (int i = 1; i < belongingRegionList.size() + 1; i++) {
//                HSSFRow row = sheet.getRow(i);
//                for (int j = 1; j < belongingRegionList.size() + 1; j++) {
//                    HSSFCell cell = row.getCell(j);
//                    int value = (int) cell.getNumericCellValue();
//                    Graph[i - 1][j - 1] = value;
//                }
//            }
//            Dijkstra.minStep(Graph, start, end, belongingRegionList);
//            System.out.println();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
	
    public void remove(ArrayList<Goods> list, Goods target) {
        Iterator<Goods> iter = list.iterator();
        while (iter.hasNext()) {
            Goods item = iter.next();
            if (item.equals(target)) {
                iter.remove();
            }
        }
    }
    
}
