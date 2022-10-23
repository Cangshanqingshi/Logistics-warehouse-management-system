package test1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class Main2 {
    private static HSSFWorkbook workbook;
    private static File file = new File("src/resources/data.xls");
    private static FileOutputStream outStream = null;
    private static FileLock lock = null;

    public static void main(String[] args) {
        System.out.println("----------------�ļ���ȡ��----------------");
        while (true) {
            boolean readFileSuccess = readDataFromFile();
            if (!readFileSuccess) continue;

            System.out.println();
            System.out.println("��ѡ����");
            int sheetNumbers = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNumbers; i++) {
                String sheetName = workbook.getSheetAt(i).getSheetName();
                System.out.println(i + 1 + ". " + sheetName);
            }
            System.out.println();
            System.out.println("0. �˳�ϵͳ");
            try {
                Scanner input = new Scanner(System.in);
                int inputValue = input.nextInt();
                if (inputValue == 0) {
                    System.out.println("--------------ϵͳ�ǳ�-------------");
                    return;
                } else if (inputValue < sheetNumbers || inputValue > 0) {
                    sheetSelector(inputValue - 1);
                } else {
                    System.out.println("����������������룡");
                }
            } catch (Exception e) {
                System.out.println("����������������룡");
            }
        }
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

    private static void sheetSelector(int sheetIndex) {
        while (true) {
            boolean readFileSuccess = readDataFromFile();
            if (!readFileSuccess) continue;

            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            System.out.println();
            System.out.println("================  ���" + sheet.getSheetName() + "  ===============");
            System.out.println("1. ��������");
            System.out.println("2. ɾ������");
            System.out.println("3. �޸�����");
            System.out.println("4. ��ѯ����");
            System.out.println();
            System.out.println("0. ������һ��");
            Scanner input = new Scanner(System.in);
            int inputValue;
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                        dataAdd(sheetIndex);
                        break;
                    case 2:
                        dataDelete(sheetIndex);
                        break;
                    case 3:
                        dataModify(sheetIndex);
                        break;
                    case 4:
                        dataQuery(sheetIndex);
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

    private static void dataAdd(int sheetIndex) {
        while (true) {
            boolean readFileSuccess = readDataFromFile();
            if (!readFileSuccess) continue;
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            System.out.println();
            System.out.println("===============  �������� ===============");
            HSSFRow row = sheet.getRow(0);
            int lastCellNum = row.getLastCellNum();
            ArrayList inputs = new ArrayList<>();
            getInputs(row, lastCellNum, inputs);

            while (true) {
                readFileSuccess = readDataFromFile();
                if (!readFileSuccess) continue;

                sheet = workbook.getSheetAt(sheetIndex);
                int lastRowNum = sheet.getLastRowNum();
                HSSFRow nextRow = sheet.createRow(lastRowNum + 1);
                for (int i = 0; i < lastCellNum; i++) {
                    HSSFCell addCell = nextRow.createCell(i);
                    if (i == 0) {
                        addCell.setCellValue(String.valueOf(lastRowNum + 1));
                    } else if (i == lastCellNum - 1) {
                        addCell.setCellValue("1");
                    } else {
                        addCell.setCellValue(String.valueOf(inputs.get(i - 1)));
                    }
                }
                saveFile();
                System.out.println();
                System.out.println("��ӳɹ���");
                return;
            }
        }
    }

    private static void dataDelete(int sheetIndex) {
        while (true) {
            System.out.println();
            System.out.println("===============  ɾ������ ===============");
            System.out.println("��������Ҫɾ������������, ����0������һ�㣺");
            System.out.println();
            Scanner input = new Scanner(System.in);
            try {
                int inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    default:
                        while (true) {
                            boolean readFileSuccess = readDataFromFile();
                            if (!readFileSuccess) continue;
                            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                            printRowByIndex(sheet, inputValue);
                            HSSFRow deleteRow = sheet.getRow(inputValue);
                            int lastCellNum = deleteRow.getLastCellNum();
                            HSSFCell deleteCell = deleteRow.getCell(lastCellNum - 1);
                            deleteCell.setCellValue("0");
                            saveFile();
                            System.out.println("ɾ���ɹ���");
                            return;
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void dataModify(int sheetIndex) {
        while (true) {
            System.out.println();
            System.out.println("===============  �޸����� ===============");
            System.out.println("��������Ҫ�޸ĵ���������, ����0������һ�㣺");
            System.out.println();
            Scanner input = new Scanner(System.in);
            try {
                int inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    default:
                        while (true) {
                            boolean readFileSuccess = readDataFromFile();
                            if (!readFileSuccess) continue;

                            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                            if (inputValue > sheet.getLastRowNum()) {
                                System.out.println("���ݲ����ڣ�");
                                return;
                            }
                            printRowByIndex(sheet, inputValue);
                            HSSFRow titleRow = sheet.getRow(0);
                            int lastCellNum = titleRow.getLastCellNum();
                            ArrayList inputs = new ArrayList<String>();
                            getInputs(titleRow, lastCellNum, inputs);

                            while (true) {
                                readFileSuccess = readDataFromFile();
                                if (!readFileSuccess) continue;
                                sheet = workbook.getSheetAt(sheetIndex);
                                HSSFRow modifyRow = sheet.getRow(inputValue);

                                for (int i = 1; i < lastCellNum; i++) {
                                    HSSFCell modifyCell = modifyRow.getCell(i);
                                    if (i == lastCellNum - 1) {
                                        modifyCell.setCellValue("1");
                                    } else {
                                        modifyCell.setCellValue(String.valueOf(inputs.get(i - 1)));
                                    }
                                }
                                saveFile();
                                System.out.println("�޸ĳɹ���");
                                return;
                            }
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void getInputs(HSSFRow titleRow, int lastCellNum, ArrayList inputs) {
        for (int i = 1; i < lastCellNum - 1; i++) {
            HSSFCell cell = titleRow.getCell(i);
            String value = cell.getStringCellValue();
            System.out.println(value + "��");
            Scanner input = new Scanner(System.in);
            try {
                String InputValue = input.next();
                inputs.add(InputValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void dataQuery(int sheetIndex) {
        while (true) {

            System.out.println();
            System.out.println("===============  ��ѯ���� ===============");
            System.out.println("1. ��ѯ��������");
            System.out.println("2. ��ѯ��������");
            System.out.println("3. ��ѯָ����");
            System.out.println("4. ����������ѯ");
            System.out.println();
            System.out.println("0. ������һ��");
            Scanner input = new Scanner(System.in);
            try {
                int inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    case 1:
                        QueryAll(sheetIndex);
                        break;
                    case 2:
                        QueryLineName(sheetIndex);
                        break;
                    case 3:
                        QuerySpecifiedColumn(sheetIndex);
                        break;
                    case 4:
                        QueryByIndex(sheetIndex);
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

    private static void QueryAll(int sheetIndex) {
        while (true) {
            boolean readFileSuccess = readDataFromFile();
            if (!readFileSuccess) continue;

            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
                HSSFRow row = sheet.getRow(i);
                int lastCellNum = row.getLastCellNum();

                HSSFCell flagCell = row.getCell(lastCellNum - 1);
                String flag = flagCell.getStringCellValue();
                if (flag.equals("0")) continue;

                for (int j = 0; j < lastCellNum - 1; j++) {
                    HSSFCell cell = row.getCell(j);
                    String value = cell.getStringCellValue();

                    System.out.print(value + " ");
                }
                System.out.println();
            }
            break;
        }
    }

    private static void QueryLineName(int sheetIndex) {
        while (true) {
            boolean readFileSuccess = readDataFromFile();
            if (!readFileSuccess) continue;
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            HSSFRow row = sheet.getRow(0);
            int lastCellNum = row.getLastCellNum();
            System.out.println();
            for (int i = 0; i < lastCellNum - 1; i++) {
                HSSFCell cell = row.getCell(i);
                String value = cell.getStringCellValue();
                System.out.println(i + 1 + ". " + value);
            }
            break;
        }
    }

    private static void QuerySpecifiedColumn(int sheetIndex) {
        while (true) {
            System.out.println();
            System.out.println("===============  ָ���кŲ�ѯ ===============");
            System.out.println("����0������һ�㣺");
            System.out.println();
            Scanner input = new Scanner(System.in);
            try {
                String inputValue = input.nextLine();
                switch (inputValue) {
                    case "0":
                        return;
                    default:
                        while (true) {
                            boolean readFileSuccess = readDataFromFile();
                            if (!readFileSuccess) continue;
                            String[] inputs = inputValue.split(",");
                            int[] columnIndex = new int[inputs.length];

                            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                            HSSFRow titleRow = sheet.getRow(0);
                            int lastCellNum = titleRow.getLastCellNum();
                            for (int i = 0; i < lastCellNum; i++) {
                                for (int k = 0; k < inputs.length; k++) {
                                    inputs[k] = inputs[k].replace(" ", "");
                                    HSSFCell titleCell = titleRow.getCell(i);
                                    String title = titleCell.getStringCellValue();
                                    if (inputs[k].trim().equals(title)) {
                                        columnIndex[k] = i;
                                    }
                                }
                            }


                            if(!cheakIsRepeat(columnIndex)){
                                System.out.println("�������");
                                break;
                            }

                            int lastRowNum = sheet.getLastRowNum();
                            for (int i = 0; i < lastRowNum; i++) {
                                HSSFRow row = sheet.getRow(i);
                                HSSFCell flagCell = row.getCell(lastCellNum - 1);
                                String flag = flagCell.getStringCellValue();
                                if (flag.equals("0")) continue;

                                for (int j = 0; j < lastCellNum; j++) {
                                    for (int aColumnIndex : columnIndex) {
                                        if (j == aColumnIndex) {
                                            HSSFCell cell = row.getCell(j);
                                            String value = cell.getStringCellValue();
                                            System.out.print(value + " ");
                                        }
                                    }
                                }
                                System.out.println();
                            }
                            break;
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void QueryByIndex(int sheetIndex) {
        while (true) {
            System.out.println();
            System.out.println("===============  ������ѯ ===============");
            System.out.println("����0������һ�㣺");
            System.out.println();
            Scanner input = new Scanner(System.in);
            int inputValue;
            try {
                inputValue = input.nextInt();
                switch (inputValue) {
                    case 0:
                        return;
                    default:
                        while (true) {
                            boolean readFileSuccess = readDataFromFile();
                            if (!readFileSuccess) continue;
                            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                            printRowByIndex(sheet, inputValue);
                            break;
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void printRowByIndex(HSSFSheet sheet, int inputValue) {
        System.out.println();
        HSSFRow titleRow = sheet.getRow(0);
        int lastCellNum = titleRow.getLastCellNum();
        for (int i = 0; i < lastCellNum - 1; i++) {
            HSSFCell cell = titleRow.getCell(i);
            String value = cell.getStringCellValue();
            System.out.print(value + " ");
        }
        System.out.println();

        int lastRowNum = sheet.getLastRowNum();
        if (inputValue > lastRowNum || inputValue < 0) {
            return;
        }

        HSSFRow row = sheet.getRow(inputValue);

        HSSFCell flagCell = row.getCell(lastCellNum - 1);
        String flag = flagCell.getStringCellValue();
        if (flag.equals("0")) return;

        for (int i = 0; i < lastCellNum - 1; i++) {
            HSSFCell cell = row.getCell(i);
            String value = cell.getStringCellValue();
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static boolean cheakIsRepeat(int[] array) {
        HashSet<Integer> hashSet = new HashSet<Integer>();
        for (int anArray : array) {
            hashSet.add(anArray);
        }
        return hashSet.size() == array.length;
    }

    private static void saveFile() {
        try {
            file.createNewFile();
            outStream = FileUtils.openOutputStream(file);   // ��excel����
            FileChannel channel = outStream.getChannel();
            try {
                //����һ
                lock = channel.lock();
                workbook.write(outStream);
//                Thread.sleep(10000); //for test

                //������
                //lock = channel.tryLock();
                //if(lock != null){
                //  do something..
                //}
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
}