

public class Sale {
	private int saleID;
	private int bID;
	private int cID;
	private double total;
	private double discount;
	private double profit;



	public Sale (int saleID, int bID, int cID,double total, double discount, double profit) {
	this.saleID = saleID;
	this.bID = bID;
	this.cID = cID;
	this.total = total;
	this.discount = discount;
	this.profit = profit;
	}



	@Override
	public String toString() {
		return "Sale [saleID=" + saleID + ", bID=" + bID + ", cID=" + cID + ", total=" + total + ", discount="
				+ discount + ", profit=" + profit + "]";
	}



	public int getSaleID() {
		return saleID;
	}



	public void setSaleID(int saleID) {
		this.saleID = saleID;
	}



	public int getbID() {
		return bID;
	}



	public void setbID(int bID) {
		this.bID = bID;
	}



	public int getcID() {
		return cID;
	}



	public void setcID(int cID) {
		this.cID = cID;
	}



	public double getTotal() {
		return total;
	}



	public void setTotal(int total) {
		this.total = total;
	}



	public double getDiscount() {
		return discount;
	}



	public void setDiscount(int discount) {
		this.discount = discount;
	}



	public double getProfit() {
		return profit;
	}



	public void setProfit(int profit) {
		this.profit = profit;
	}
}