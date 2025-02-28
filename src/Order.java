

import java.util.List;
class Order {
    private String orderId;
    private String customerId;
    private String timeStamp;
    private List<Item> itemList;

    public Order(String orderId, String customerId, String timeStamp, List<Item> itemList) {
        this.orderId = orderId.trim();
        this.customerId = customerId.trim();
        this.timeStamp = timeStamp.trim();
        this.itemList = itemList;
    }

	public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getTimeStamp() { return timeStamp; }
    public List<Item> getItemList() { return itemList; }
    
    public double calculateTotalCost() {
    	//之后再改
        return 1;
    }
}