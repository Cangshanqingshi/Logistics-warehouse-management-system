package Logistics;

public class Edges{
	public int ID;
	public double Price;
	public double TimeConsume;
	public double Overall;// 综合衡量价格和时间
	public String Startpoint;
	public String Endpoint;
	
    public Edges(int ID,String Startpoint, String Endpoint, double Price, double TimeConsume) {
        this.ID = ID;
    	this.Price = Price;
        this.TimeConsume = TimeConsume;
        this.Startpoint = Startpoint;
        this.Endpoint = Endpoint;
        this.Overall = calculate(Price,TimeConsume);
    }

    private double calculate(double price2, double timeConsume2) {
		return Math.sqrt(price2*timeConsume2);
	}

    @Override
    public String toString() {
        return "Edges{" +
        		"ID=" + ID +
                ", Startpoint='" + Startpoint + '\'' +
                ", Endpoint='" + Endpoint + '\'' +
                ", Price=" + Price + 
                ", TimeConsume=" + TimeConsume + 
                ", Overall=" + Overall +
                '}';
    }
	
}
