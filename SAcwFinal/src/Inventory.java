

public class Inventory {
    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private int discount;
    private int loyalty;
	
	
	  public Inventory(int inventoryID, String productName, int quantity, double price, int discount, int loyalty) {
	        this.productId = inventoryID;
	        this.productName = productName;
	        this.quantity = quantity;
	        this.price = price;
	        this.discount = discount;
	        this.loyalty = loyalty;
	    }

	  @Override
	public String toString() {
		return "Inventory ID: " + productId + ", Name: " + productName+ "";
	}

	public Inventory(int inventoryID, String productName, double price, int discount, int loyalty) {
	        this.productId = inventoryID;
	        this.productName = productName;
	        this.price = price;
	        this.discount = discount;
	        this.loyalty = loyalty;
	    }
	  
	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getDiscount() {
		return discount;
	}


	public void setDiscount(int discount) {
		this.discount = discount;
	}


	public int getLoyalty() {
		return loyalty;
	}


	public void setLoyalty(int loyalty) {
		this.loyalty = loyalty;
	}
}
