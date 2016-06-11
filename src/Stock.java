import java.io.Serializable;
import java.sql.Date;

public class Stock implements Serializable{
	private String pname;
	private double pUnitPrice;
	private int id, pQty;
	private String pDate;
	public Stock(int id, String pname, double pUnitPrice, int pQty, String pDate) {
		this.id = id;
		this.pname = pname;
		this.pUnitPrice = pUnitPrice;
		this.pQty = pQty;
		this.pDate = pDate;
	}
	public int getID(){
		return id;
	}
	public void setID(int id){
		this.id = id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public double getpUnitPrice() {
		return pUnitPrice;
	}
	public void setpUnitPrice(double pUnitPrice) {
		this.pUnitPrice = pUnitPrice;
	}
	public int getpQty() {
		return pQty;
	}
	public void setpQty(int pQty) {
		this.pQty = pQty;
	}
	public String getpDate() {
		return pDate;
	}
	public void setpDate(String pDate) {
		this.pDate = pDate;
	}
	
}
	