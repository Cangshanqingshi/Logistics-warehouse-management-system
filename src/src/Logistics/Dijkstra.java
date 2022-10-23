package Logistics;

import java.util.List;

public class Dijkstra {
	
	private static final double INF = 9999999999.0;
	
    public static double[] minStep(double[][] weight, int start, int end, List<String> regions) {
        //����һ������ͼ��Ȩ�ؾ��󣬺�һ�������start����0��ţ�������������У�
        //����һ��double[] ���飬��ʾ��start���������·������
        int n = regions.size();      //�������
        double[] shortPath = new double[n];  //����start��������������·��
        String[] path = new String[n];  //����start�������������·�����ַ�����ʾ
        for (int i = 0; i < n; i++)
            path[i] = regions.get(start) + "-->" + regions.get(i);
        int[] visited = new int[n];  //��ǵ�ǰ�ö�������·���Ƿ��Ѿ����,1��ʾ�����
        //��ʼ������һ�������Ѿ����
        shortPath[start] = 0;
        visited[start] = 1;

        for (int count = 1; count < n; count++) {   //Ҫ����n-1������
            int k = -1;       //ѡ��һ�������ʼ����start�����δ��Ƕ���
            double dmin = INF;
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
                System.out.println("��" + regions.get(start) + "������" + regions.get(i) + "�����·��Ϊ��" + path[i] +"��·������Ϊ��" + shortPath[i]);
            }
            System.out.println("=====================================");
        } else {
            System.out.println("��" + regions.get(start) + "������" + regions.get(end) + "�����·��Ϊ��"+ path[end] + "��·������Ϊ��" + shortPath[end]);
        }
        return shortPath;
    }
    
}