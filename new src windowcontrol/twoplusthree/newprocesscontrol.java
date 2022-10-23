package twoplusthree;

import java.util.Scanner;

import test1.SharedData;

public class newprocesscontrol {
	public static void main(String[] args) {
		SharedData sharedData = new SharedData();
		System.out.print("您打算开几个进程\n");
		Scanner input1 = new Scanner(System.in);
		sharedData.n = input1.nextInt();
		for(int i = 0; i < sharedData.n; i++) {
			new Thread(new process(sharedData)).start();
		}
	}
}
