
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.util.*;

public class OrderList {
    private List<Order> orderList;

    public OrderList() {
        this.orderList = new ArrayList<>();
    }

    public List<Order> getOrderList() {
    	//这里可以加exceptioin
        return orderList;
    }

    
    public void loadOrderListFromFile(String filePath, Map<String, Item> menu) {
        
    }

    
    public Order findOrderByOrderId(String orderId) {
    	
        return null;
    }

    /**
     * 按客户 ID 查找订单
     */
    public List<Order> findOrderByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getCustomerId().equals(customerId)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    /**
     * 添加新订单
     * @throws InvalidOrderException 
     */
    public void addOrder(Order order) throws InvalidOrderException {
        if (order == null) {
            throw new InvalidOrderException();
        }
        orderList.add(order);
    }

    /**
     * 按订单 ID 删除订单
     */
    public boolean removeOrderByOrderId(String orderId) {
        return false;
    }

    /**
     * 按客户 ID 删除订单，并返回被删除的订单列表
     */
    public List<Order> removeOrderByCustomerId(String customerId) {
       
        return null;
    }

    /**
     * 生成订单日志（打印所有订单）
     */
    public void generateLog() {
    }
}
 
