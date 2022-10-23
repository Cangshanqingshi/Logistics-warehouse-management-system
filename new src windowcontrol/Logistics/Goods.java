package Logistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Goods {
	public int ID;
	public int weight;
	public String sourceArea;
	public String destinationArea;
	public String expectedArrDate;
	public int customerID;
	public int customerGrade;
	public int isUrgent;//0:no,1:yes
	public int logMethod;//1:speed;2:normal;3:price
	public double priority;
	public int hasSend;
	
    private final float methodWeight = 0.2f;
    private final float clientGradeWeight = 0.3f;
    private final float dateWeight = 0.4f;
    private static long todayTimestamp = new Date().getTime();
    private static long oneDayTimestamp = 24 * 60 * 60 * 1000;
    public Goods(int ID, int weight, String sourceArea, String destinationArea, int customerID, int customerGrade,int isUrgent, String expectedArrDate, int hasSend) {
        this.ID = ID;
        this.weight = weight;
        this.sourceArea = sourceArea;
        this.destinationArea = destinationArea;
        this.customerID = customerID;
        this.customerGrade = customerGrade;
        this.isUrgent = isUrgent;
        this.expectedArrDate = expectedArrDate;
        this.logMethod = method(weight, sourceArea, destinationArea,isUrgent);
        this.priority = prior(this.logMethod, customerGrade, expectedArrDate);
        this.hasSend = hasSend;
    }

    public double getPriority(){
        return priority;
    }

    private int method(int weight, String source, String destination, int isUrgent) {
    	if (isUrgent == 1 ) {
    		return 1;
    	}
    	if (weight > 200) {
    		return 3;
    	}
    	return 2;
    }
    
    private double prior(int logMethod, int custGrade, String dateString) {
        double date = Math.ceil((todayTimestamp - SimpleDateFormat(dateString)) / oneDayTimestamp);
        return logMethod * methodWeight + custGrade * clientGradeWeight + dateWeight/date;
    }

    private long SimpleDateFormat(String strDate) {
        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(strDate);
            return date.getTime();
        } catch (ParseException px) {
            px.printStackTrace();
        }

        return 0;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "ID=" + ID +
                ", weight=" + weight + 
                ", belongingArea='" + sourceArea + '\'' +
                ", sendingArea='" + destinationArea + '\'' +
                ", logisticsMethod=" + logMethod +
                ", customerID=" + customerID +
                ", customerGrade=" + customerGrade +
                ", expectedArrivingDate='" + expectedArrDate + '\'' +
                ", isUrgent=" + isUrgent +
                ", priority=" + priority +
                ", hasSend=" + hasSend +
                '}';
    }
	
}
