package test1;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================��ӭ����������+�ǻ�������ѯϵͳ===============");
        while (true) {
            System.out.println("----------��ѡ�����----------");
            System.out.println("1.��������ϵͳ");
            System.out.println("2.���ݹ���ϵͳ");
            System.out.println("3.���ݿ����");
            System.out.println();
            System.out.println("0.�˳�ϵͳ");
            try {
                Scanner input = new Scanner(System.in);
                int inputValue = input.nextInt();
                if (inputValue == 0) {
                    System.out.println("--------------ϵͳ�ǳ�-------------");
                    return;
                } else if (inputValue == 1) {
                    new Main1().main(args);
                } else if (inputValue == 2) {
                    new Main2().main(args);
                } else if (inputValue == 3) {
                    new Main3().main(args);
                } else {
                    System.out.println("����������������룡");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("����������������룡");
            }
        }
    }
}
