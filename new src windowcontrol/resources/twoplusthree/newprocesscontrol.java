package twoplusthree;

import test1.SharedData;

public class newprocesscontrol {
	public static void main(String[] args) {
		SharedData sharedData = new SharedData();
		
		new Thread(new process(sharedData)).start();
        new Thread(new process(sharedData)).start();
	}
}
