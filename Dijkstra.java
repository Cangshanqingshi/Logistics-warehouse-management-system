package test1;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra {

    public static void main(String[] args) {
        //��·��ͨ
        int m = 10000;
        int[][] weight1 = {//�ڽӾ���
                {0, 3, 2000, 7, m},
                {3, 0, 4, 2, m},
                {m, 4, 0, 5, 4},
                {7, 2, 5, 0, 6},
                {m, m, 4, 6, 0}
        };

        int[][] weight2 = {
                {0, 10, m, 30, 100},
                {m, 0, 50, m, m},
                {m, m, 0, m, 10},
                {m, m, 20, 0, 60},
                {m, m, m, m, 0}
        };

        List<String> regions = new ArrayList<String>(){{
            add("A");
            add("B");
            add("C");
            add("D");
            add("E");
        }};

        int start = 0;
        int[] shortPath = minStep(weight1, start, -1, regions);
//        for (int i = 0; i < shortPath.length; i++)
//            System.out.println("��" + start + "������" + i + "����̾���Ϊ��" + shortPath[i]);
    }

    public static int[] minStep(int[][] weight, int start, int end, List<String> regions) {
        //����һ������ͼ��Ȩ�ؾ��󣬺�һ�������start����0��ţ�������������У�
        //����һ��int[] ���飬��ʾ��start���������·������
        int n = weight.length;      //�������
        int[] shortPath = new int[n];  //����start��������������·��
        String[] path = new String[n];  //����start�������������·�����ַ�����ʾ
        for (int i = 0; i < n; i++)
            path[i] = regions.get(start) + "-->" + regions.get(i);
        int[] visited = new int[n];   //��ǵ�ǰ�ö�������·���Ƿ��Ѿ����,1��ʾ�����

        //��ʼ������һ�������Ѿ����
        shortPath[start] = 0;
        visited[start] = 1;

        for (int count = 1; count < n; count++) {   //Ҫ����n-1������
            int k = -1;        //ѡ��һ�������ʼ����start�����δ��Ƕ���
            int dmin = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && weight[start][i] < dmin) {
                    dmin = weight[start][i];
                    k = i;
                }
            }

            //����ѡ���Ķ�����Ϊ��������·�����ҵ�start�����·������dmin
            shortPath[k] = dmin;
            visited[k] = 1;

            //��kΪ�м�㣬������start��δ���ʸ���ľ���
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
                    weight[start][i] = weight[start][k] + weight[k][i];
                    path[i] = path[k] + "-->" + regions.get(i);
                }
            }
        }
        if (end == -1) {
            for (int i = 0; i < n; i++) {
                System.out.println("��" + regions.get(start) + "������" + regions.get(i) + "�����·��Ϊ��" + path[i] + "����̾���Ϊ��" + shortPath[i]);
            }
            System.out.println("=====================================");
        } else {
            System.out.println("��" + regions.get(start) + "������" + regions.get(end) + "�����·��Ϊ��" + path[end] + "����̾���Ϊ��" + shortPath[end]);
        }
        return shortPath;
    }
}
