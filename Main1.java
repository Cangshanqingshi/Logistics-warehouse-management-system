package test1;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main1 {
    private static final int goodsIdIndex = 0;
    private static final int goodsNameIndex = 1;
    private static final int belongingRegionIndex = 2;
    private static final int sendingRegionIndex = 3;
    private static final int goodsTypeIndex = 4;
    private static final int customerLevelIndex = 5;
    private static final int receiveDateIndex = 6;
    private static List<String> belongingRegionList = new ArrayList<>();
    private static ArrayList<Goods> goodsList = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println("------------��ӭ������������ϵͳ-----------");
        System.out.println("��ȡ������...");

        // ��Ҫ������excel�ļ�
        File file = new File("src/resources/data.xls");
        try {
            readDataFromFile(file);

            while (true) {

                System.out.println("----------��ѡ�����----------");

                for (int i = 0; i < belongingRegionList.size(); i++) {
                    System.out.println(i + 1 + "." + belongingRegionList.get(i));
                }
                System.out.println();
                System.out.println("0.�˳�ϵͳ");

                try {
                    Scanner input = new Scanner(System.in);
                    int inputValue = input.nextInt();
                    if (inputValue == 0) {
                        System.out.println("--------------ϵͳ�ǳ�-------------");
                        return;
                    } else if (inputValue < belongingRegionList.size()) {
                        regionSelector(inputValue - 1);
                    } else {
                        System.out.println("����������������룡");
                    }
                } catch (Exception e) {
                    System.out.println("����������������룡");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDataFromFile(File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
        // ��ȡĬ�ϵ�һ��������sheet
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Set<String> belongingRegionSet = new HashSet<>();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell cell0 = row.getCell(goodsIdIndex);
            HSSFCell cell1 = row.getCell(goodsNameIndex);
            HSSFCell cell2 = row.getCell(belongingRegionIndex);
            HSSFCell cell3 = row.getCell(sendingRegionIndex);
            HSSFCell cell4 = row.getCell(goodsTypeIndex);
            HSSFCell cell5 = row.getCell(customerLevelIndex);
            HSSFCell cell6 = row.getCell(receiveDateIndex);
            int goodsId = Integer.parseInt(cell0.getStringCellValue());
            String goodsName = cell1.getStringCellValue();
            String belongingRegion = cell2.getStringCellValue();
            belongingRegionSet.add(belongingRegion);
            String sendingRegion = cell3.getStringCellValue();
            int goodsType = Integer.parseInt(cell4.getStringCellValue());
            int clientGrade = Integer.parseInt(cell5.getStringCellValue());
            String receiveDate = cell6.getStringCellValue();
            Goods goods = new Goods(goodsId, goodsName, belongingRegion, sendingRegion, goodsType, clientGrade, receiveDate);
            goodsList.add(goods);
        }
        belongingRegionList.addAll(belongingRegionSet);
    }

    private static void regionSelector(int belongingRegionIndex) {
        String belongRegion = belongingRegionList.get(belongingRegionIndex);
        System.out.println("----------����" + belongRegion + "----------");
        while (true) {
            System.out.println("1.�������ȼ���������Ʒ");
            System.out.println("2.�۸���С��������");
            System.out.println("3.ʱ�������������");
            System.out.println("4.�ۺ��������ŷ���");
            System.out.println("5.������������");
            System.out.println();
            System.out.println("0.������һҳ");
            Scanner input = new Scanner(System.in);
            int inputValue;
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                        prioritySort(belongRegion);
                        break;
                    case 2:
                        projectSeletor(belongRegion, 1);
                        break;
                    case 3:
                        projectSeletor(belongRegion, 2);
                        break;
                    case 4:
                        projectSeletor(belongRegion, 3);
                        break;
                    case 5:
                        projectSeletor(belongRegion, 4);
                        break;
                    default:
                        System.out.println("����������������룡");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("����������������룡");
            }
        }
    }

    private static void projectSeletor(String belongingRegion, int projectId) {
        while (true) {
            System.out.println("1.�����ȼ�����÷�������Ʒ");
            System.out.println("2.������·��");
            System.out.println("3.������ƷID����");
            System.out.println();
            System.out.println("0.������һҳ");
            Scanner input = new Scanner(System.in);
            int inputValue;
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                        printGoodsOfThisProject(belongingRegion, projectId);
                        break;
                    case 2:
                        minStep(belongingRegionList.indexOf(belongingRegion), -1, projectId);
                        break;
                    case 3:
                        sendGoods(belongingRegion, projectId);
                        break;
                    default:
                        System.out.println("����������������룡");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("����������������룡");
            }
        }
    }

    private static void sendGoods(String belongingRegion, int projectId) {
        while (true) {
            System.out.println("��������ƷID����������0������һ�㣺");
            Scanner input = new Scanner(System.in);
            int inputValue;
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    default:
                        goodsList.stream()
                                .filter(goods -> goods.belongingArea.equals(belongingRegion))
                                .filter(goods -> goods.type == projectId)
                                .filter(goods -> goods.ID == inputValue)
                                .forEach(el -> {
                                    System.out.println(el);
                                    minStep(belongingRegionList.indexOf(belongingRegion), belongingRegionList.indexOf(el.sendingArea), projectId);
                                });
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("����������������룡");
            }
        }
    }

    // if end == -1, then print all. else print specified route
    private static void minStep(int start, int end, int projectId) {
        File file = new File("src/resources/regionDistance.xls");
        try {
            int[][] Graph = new int[belongingRegionList.size()][belongingRegionList.size()];
            HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(file));
            // ��ȡĬ�ϵ�һ��������sheet
            HSSFSheet sheet = workbook.getSheetAt(projectId - 1);
            for (int i = 1; i < belongingRegionList.size() + 1; i++) {
                HSSFRow row = sheet.getRow(i);
                // ��ȡ��ǰ�����Ԫ���к�
                for (int j = 1; j < belongingRegionList.size() + 1; j++) {
                    HSSFCell cell = row.getCell(j);
                    int value = (int) cell.getNumericCellValue();
                    Graph[i - 1][j - 1] = value;
                }
            }
            Dijkstra.minStep(Graph, start, end, belongingRegionList);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void printGoodsOfThisProject(String belongingRegion, int goodsType) {
        //        NodeTree root = new NodeTree(goodsList.get(0)); �����νṹ�洢���ø��ڵ�
        goodsList.stream()
                .filter(goods -> goods.belongingArea.equals(belongingRegion))
                .filter(goods -> goods.type == goodsType)
                .sorted((o1, o2) -> {
                    double diff = o1.priority - o2.priority;
                    if (diff == 0) {
                        return 0;
                    }
                    return diff > 0 ? -1 : 1;
                })
                .forEach(System.out::println);
//                .forEach(el-> {
//                    root.insert(root, el); //����Ԫ��
//                });
        System.out.println();
//        root.preOrder(root); //�������
    }

    private static void prioritySort(String belongingRegion) {
        goodsList.stream()
                .filter(goods -> goods.belongingArea.equals(belongingRegion))
                .sorted((o1, o2) -> {
                    double diff = o1.priority - o2.priority;
                    if (diff == 0) {
                        return 0;
                    }
                    return diff > 0 ? -1 : 1;
                })
                .forEach(System.out::println);
        System.out.println();
    }
}
