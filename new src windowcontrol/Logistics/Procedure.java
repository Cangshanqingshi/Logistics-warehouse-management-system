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
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import test2.Assemble;

public class Procedure {
	
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
    private static final int goodDeleteFlagIndex = 9;

    private static final int edgesIDIndex = 0;
    private static final int StartpointIndex = 1;
    private static final int EndpointIndex = 2;
    private static final int PriceIndex = 3;
    private static final int TimeConsumeIndex = 4;
    private static final int edgeDeleteFlagIndex = 5;
    
    private static ArrayList<Goods> goodsList = new ArrayList<Goods>();//��Ʒ
    private static ArrayList<Edges> edgesList = new ArrayList<Edges>();//��
    private static List<String> belongingRegionList = new ArrayList<String>();
    
    private static double[][] Graph1 = new double[MaxNode][MaxNode];
    private static double[][] Graph2 = new double[MaxNode][MaxNode];
    private static double[][] Graph3 = new double[MaxNode][MaxNode];
    
	public static void menu() {
	    System.out.println("------------��������-----------");
            
            while (true) {
//            	//������
//            	System.out.println("G1��");
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
//            	System.out.println("�ڵ��У�" + belongingRegionList);
    			
            	System.out.println("1.�����ȼ����з���");
    			System.out.println("2.�������Ų�ѯ");
    			System.out.println("3.��������");
                System.out.println("0.�˳�ϵͳ");
                
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
                            System.out.println("�����������������ָ�");
                            break;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("����������������룡");
                }
            }
    }
	
	private static void listInit() {
        File file = new File("src/resources/data.xls");
        edgesList.clear();
        goodsList.clear();
        belongingRegionList.clear();
        try {
        	readDataFromFile1(file);
        	//System.out.println("��⵽�����������У�" + goodsList.size() + " � ");
        	readDataFromFile2(file);
        	//System.out.println("��⵽�������У�" + edgesList.size() + " ���� ");
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	private static void readDataFromFile1(File file) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
        // ��ȡĬ�ϵ�һ��������sheet
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Set<String> belongingRegionSet = new HashSet<>();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row.getCell(goodsIdIndex)==null)
            	break;
            HSSFCell cellJudge = row.getCell(goodDeleteFlagIndex);
            if (Integer.parseInt(cellJudge.getStringCellValue()) == 0)
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
        // ��ȡĬ�ϵ�һ��������sheet
        HSSFSheet sheet = workbook.getSheetAt(1);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row.getCell(edgesIDIndex)==null)
            	break;
            HSSFCell cellJudge = row.getCell(edgeDeleteFlagIndex);
            if (Integer.parseInt(cellJudge.getStringCellValue()) == 0)
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
   
	private static void reigion_sendGoods() {
		listInit();
        while (true) {
            System.out.println("----------��ѡ�񷢻�����----------");

            for (int i = 0; i < belongingRegionList.size(); i++) {
                System.out.println(i + 1 + "." + belongingRegionList.get(i));
            }
            System.out.println();
            System.out.println("0.����");

            try {
                Scanner input = new Scanner(System.in);//�������
                int inputValue = input.nextInt();
                if (inputValue == 0) {
                    return;
                } else if (inputValue <= belongingRegionList.size() && inputValue>0) {
                    sendGoods(belongingRegionList.get(inputValue-1).toString());
                } else {
                    System.out.println("����������������������");
                }
            } catch (Exception e) {
                System.out.println("����������������룡");
            }
        }
	}
	
	private static void sendGoods(String chosenArea) {
        while (true) {
            System.out.println("������1���͸õ������ȼ���ߵĻ������0���أ�");
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
                                .filter(goods -> goods.hasSend == 0)
                                .count() != 0) 
                    	{
                    		int ID = goodsList.stream()
	                                .filter(goods -> goods.sourceArea.equals(chosenArea))
	                                .filter(goods -> goods.hasSend == 0)
	                                .max(Comparator.comparing(Goods::getPriority))
	                                .get().ID;
	                        int logMethod = goodsList.stream()
	                                .filter(goods -> goods.ID == ID)
	                                .max(Comparator.comparing(Goods::getPriority))
	                                .get().logMethod;
	                        goodsList.stream()
	                        		 .filter(goods -> goods.ID == ID)
		                             .max(Comparator.comparing(Goods::getPriority))
	                                 .stream()
	                                 .forEach(el -> {
	                                    System.out.println(el);
	                                    minStep(belongingRegionList.indexOf(el.sourceArea)
	                                    		, belongingRegionList.indexOf(el.destinationArea)
	                                    		, logMethod);
	                                });
	                        	                       
	        				StringBuffer Index_Modify = Assemble.QuerySpecifiedIndex(1, "id", 3, goodsList.stream()
	        																		.filter(goods -> goods.ID == ID)
													                                .max(Comparator.comparing(Goods::getPriority))
													                                .get()
													                                .ID+"");
	        				String Index3 = Index_Modify.toString();
	        				Assemble.ModifyData(1, "has_send", "1", Index3);
	                        remove(goodsList, goodsList.stream()
	                                .filter(goods -> goods.sourceArea.equals(chosenArea))
	                                .filter(goods -> goods.logMethod == logMethod)
	                                .filter(goods -> goods.hasSend == 0)
	                                .max(Comparator.comparing(Goods::getPriority)).get());
                    	}else {
                        	System.out.println("�õ������޻�����Ҫ����~");
                    	}
                    	break;
                    default:
                    	System.out.println("�����������������ָ�");
                    	break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("����������������룡");
            }
        }		
	}
	
	private static void checkGoods() {
		listInit();
        while (true) {
            System.out.println("----------��������ƷID----------");
            System.out.println();
            System.out.println("0.����");

            try {
                Scanner input = new Scanner(System.in);//����ID
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
                		System.out.println("�Ҳ�����ID��Ӧ��������Ʒ�����������룡");

                	}
                }
            } catch (Exception e) {
                System.out.println("����������������룡");
            }
        }
	}

	private static void methodOutput() {
        
		listInit();
		
		while (true) {
            System.out.println("----------������ģʽ���----------");
			System.out.println("1.�ٶ����ȷ���");
			System.out.println("2.�ۺ����ŷ���");
			System.out.println("3.�۸����ŷ���");
            System.out.println("0.����");
            
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
                        System.out.println("����������������뷽����");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("����������������룡");
            }
        }
	}
	
	private static void printGoodsInThisMethod(int logMethod) {
		while (true) {

            System.out.println("----------������������----------");

            for (int i = 0; i < belongingRegionList.size(); i++) {
                System.out.println(i + 1 + "." + belongingRegionList.get(i));//������Ҫ����Ӧ������ID
            }
            System.out.println();
            System.out.println("0.����");

            try {
                Scanner input = new Scanner(System.in);//�������
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
                    System.out.println("����������������������");
                }
            } catch (Exception e) {
                System.out.println("����������������룡");
            }
        }
    }
	
    private static void minStep(int start, int end, int logMethod) {
        double[][] Graphx = new double[belongingRegionList.size()][belongingRegionList.size()];
    		if (logMethod == 1) Graphx = Graph2; //���Ϊ1��ѡ���ٶ����ȵķ���
    		if (logMethod == 2) Graphx = Graph3; //���Ϊ2��ѡ���ۺ����ŷ�����
    		if (logMethod == 3)	Graphx = Graph1;//���Ϊ3��ѡ��۸����ŷ�����
            Dijkstra.minStep(Graphx, start, end, belongingRegionList);
            System.out.println();
    } 
	
    public static void remove(ArrayList<Goods> list, Goods target) {
        Iterator<Goods> iter = list.iterator();
        while (iter.hasNext()) {
            Goods item = iter.next();
            if (item.equals(target)) {
                iter.remove();
            }
        }
    }
    
}
