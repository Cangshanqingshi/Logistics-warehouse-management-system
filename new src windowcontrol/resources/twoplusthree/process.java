package twoplusthree;

import com.sitinspring.common.sqlparser.single.Test;

import test1.SharedData;
import test1.window;

public class process implements Runnable{
	static SharedData sharedData;
	static window w;
	
	public process(SharedData sharedData){
        process.sharedData = sharedData;
    }
	
	@Override
	public void run(){
		String[] arg = null;
		process.w = new window(Thread.currentThread().getName());
		Test test = new Test(sharedData, w);
		test.main(arg);
	}
}
