package com.group6.fep.dao;

import com.group6.fep.dto.AddressDTO;
import com.group6.fep.dto.OrderByShopDTO;
import com.group6.fep.dto.OrderDTO;
import com.group6.fep.dto.OrderDetailDTO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.group6.fep.utils.DBUtil;

public class OrderDAO {

    //check status of order
    //chua dung den
//    public OrderDTO getOrder(String emailBuyer, int status) throws ClassNotFoundException, SQLException {
//        Connection conn = DBUtil.getConnection();
//        PreparedStatement stm = conn.prepareStatement("SELECT [order_id], delivery_id, "
//                + "payment_id, email_buyer, order_date, status, address, ward_id, district_id, "
//                + "city_id FROM [order] WHERE email_buyer = ? AND [status] = ?");
//        stm.setString(1, emailBuyer);
//        stm.setInt(2, status);
//        ResultSet rs = stm.executeQuery();
//        while (rs.next()) {
//            AddressDAO adr = new AddressDAO();
//            OrderByShopDAO ob = new OrderByShopDAO();
//            AddressDTO address = new AddressDTO(rs.getString("address"), adr.get(rs.getString("ward_id"), adr.WARD),
//                    adr.get(rs.getString("district_id"), adr.DISTRICT), adr.get(rs.getString("city_id"), adr.CITY));
//            OrderDTO order = new OrderDTO(rs.getInt("order_id"), rs.getInt("delivery_id"),
//                    rs.getInt("payment_id"), rs.getString("email_buyer"), rs.getDate("order_date"),
//                    address, ob.getOrderByShop(rs.getInt("order_id")), getTotal(rs.getString("email_buyer")));
//            return order;
//        }
//        return null;
//    }
    public float getTotal(String emailBuyer) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT SUM(p.price) as total FROM\n"
                + "(SELECT order_id FROM [order] WHERE email_buyer = ?) o \n"
                + "LEFT JOIN [order_by_shop] os ON o.order_id = os.order_id\n"
                + "LEFT JOIN [order_detail] od ON od.order_by_shop_id = os.order_by_shop_id\n"
                + "LEFT JOIN product p ON p.product_id = od.product_id");
        stm.setString(1, emailBuyer);
        ResultSet rs = stm.executeQuery();
        rs.next();
        return Float.parseFloat(String.format("%.0f", rs.getFloat("total")));
    }

    //addCart
//    public void addCart(String emailBuyer, int productId) throws ClassNotFoundException, SQLException {
//        Connection conn = DBUtil.getConnection();
//        PreparedStatement stm = conn.prepareStatement("SELECT order_id FROM [order] "
//                + "WHERE email_buyer = ? AND [status] = 0");
//        stm.setString(1, emailBuyer);
//        ResultSet rs = stm.executeQuery();
//        if (!rs.next()) {
//            createOrder(emailBuyer);
//            addCart(emailBuyer, productId);
//        } else {
//            OrderByShopDAO obs = new OrderByShopDAO();
//            obs.addOrderByShop(rs.getInt("order_id"), productId);
//        }
//    }
    //deleteAProduct
//    public void deleteAProduct(String emailBuyer, int productId) throws ClassNotFoundException, SQLException {
//        Connection conn = DBUtil.getConnection();
//        PreparedStatement stm = conn.prepareStatement("SELECT order_id FROM [order] "
//                + "WHERE email_buyer = ? AND [status] = 0");
//        stm.setString(1, emailBuyer);
//        ResultSet rs = stm.executeQuery();
//        if (!rs.next()) {
//            createOrder(emailBuyer);
//            addCart(emailBuyer, productId);
//        } else {
//            OrderByShopDAO obs = new OrderByShopDAO();
//            obs.addOrderByShop(rs.getInt("order_id"), productId);
//        }
//    }
    //create order when order null
    public void createOrder(OrderDTO order) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("INSERT INTO [order] (delivery_id, "
                + "payment_id, email_buyer, address, ward_id, "
                + "district_id, city_id, pay_id, user_name, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        stm.setInt(1, order.getDeliveryId());
        stm.setInt(2, order.getPaymentId());
        stm.setString(3, order.getEmailBuyer());
        stm.setString(4, order.getAddress().getHouseNumber());
        stm.setString(5, order.getAddress().getWardId());
        stm.setString(6, order.getAddress().getDistrictId());
        stm.setString(7, order.getAddress().getCityId());
        stm.setString(8, order.getPayId());
        stm.setString(9, order.getUserName());
        stm.setString(10, order.getPhone());
        stm.executeUpdate();
        for (OrderByShopDTO obs : order.getOrderByShopList()) {
            new OrderByShopDAO().createOrderByShop(getMaxId(), obs);
        }
    }

    public int getMaxId() throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT MAX(order_id) as id FROM [order]");
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        return -1;
    }

    //chua dung den
//    public void updateOrder(String emailBuyer, int productId) throws ClassNotFoundException, SQLException {
//        Connection conn = DBUtil.getConnection();
//        PreparedStatement stm = conn.prepareStatement("SELECT * FROM [order] o \n"
//                + "LEFT JOIN [order_by_shop] os ON o.order_id = os.order_id\n"
//                + "LEFT JOIN [order_detail] od ON od.order_by_shop_id = os.order_by_shop_id\n"
//                + "WHERE email_buyer = ? AND [status] = 0 AND product_id = ?");
//        stm.setString(1, emailBuyer);
//        stm.setInt(2, productId);
//        ResultSet rs = stm.executeQuery();
//        if (rs.next()) {
//            //chay updateOrderDetail
//        } else {
//            OrderByShopDAO obs = new OrderByShopDAO();
//            ProductDAO p = new ProductDAO();
//            obs.createOrderByShop(productId, p.getSellerEmailByProductId(productId));
//        }
//    }
    //check Order when order_detail null
    public void checkOrder(int orderId) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT [order_by_shop_id] "
                + "FROM [order] WHERE orde_id = ? ");
        stm.setInt(1, orderId);
        ResultSet rs = stm.executeQuery();
        System.out.println(rs.next());
        OrderByShopDAO obs = new OrderByShopDAO();
        while (rs.next()) {
            obs.checkOrderByShop(rs.getInt("order_by_shop_id"));
        }
    }

    //history order by buyer
    public List<OrderDTO> getOrder(String emailBuyer) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT order_id, delivery_id, "
                + "payment_id, email_buyer, order_date, address, ward_id, district_id, "
                + "city_id, pay_id, user_name, phone FROM [order] "
                + "WHERE email_buyer = ? ORDER BY order_date DESC");
        stm.setString(1, emailBuyer);
        ResultSet rs = stm.executeQuery();
        List<OrderDTO> list = new ArrayList<>();
        AddressDAO ad = new AddressDAO();
        while (rs.next()) {
            list.add(new OrderDTO(rs.getInt("order_id"), rs.getInt("delivery_id"),
                    rs.getInt("payment_id"), rs.getString("email_buyer"),
                    rs.getDate("order_date"), new AddressDTO(rs.getString("address"),
                    ad.get(rs.getString("ward_id"), 3), ad.get(rs.getString("district_id"), 2),
                    ad.get(rs.getString("city_id"), 1)), new OrderByShopDAO().getOrderByShop(rs.getInt("order_id")),
                    rs.getString("pay_id"), rs.getString("user_name"), rs.getString("phone")));
        }
        return list;
    }

    public int getTotalOrder(List<OrderByShopDTO> list) {
        int total = 0;
        for (OrderByShopDTO os : list) {
            total = os.getTotal();
        }
        return total;
    }

    public OrderDTO getOrder(int orderId) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT order_id, delivery_id, "
                + "payment_id, email_buyer, order_date, address, ward_id, district_id, "
                + "city_id, pay_id, user_name, phone FROM [order] "
                + "WHERE order_id = ?");
        stm.setInt(1, orderId);
        ResultSet rs = stm.executeQuery();
        AddressDAO ad = new AddressDAO();
        if (rs.next()) {
            OrderDTO o = new OrderDTO(rs.getInt("order_id"), rs.getInt("delivery_id"),
                    rs.getInt("payment_id"), rs.getString("email_buyer"),
                    rs.getDate("order_date"), new AddressDTO(rs.getString("address"),
                    ad.get(rs.getString("ward_id"), 3), ad.get(rs.getString("district_id"), 2),
                    ad.get(rs.getString("city_id"), 1)), new OrderByShopDAO().getOrderByShop(rs.getInt("order_id")),
                    rs.getString("pay_id"), rs.getString("user_name"), rs.getString("phone"));
            return o;
        }
        return null;
    }

    public int getTotalIncome(int month) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT SUM(od.price) FROM \n"
                + "(SELECT order_id FROM [order] WHERE MONTH([order_date]) = ?) o \n"
                + "LEFT JOIN order_by_shop os ON o.order_id = os.order_id\n"
                + "LEFT JOIN order_detail od ON od.order_by_shop_id = os.order_by_shop_id");
        stm.setInt(1, month);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) % 3 / 100;
        }
        return -1;
    }

    public int getTotalIncome() throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT SUM(od.price) FROM \n"
                + "(SELECT order_id FROM [order]) o\n"
                + "LEFT JOIN order_by_shop os ON o.order_id = os.order_id\n"
                + "LEFT JOIN order_detail od ON od.order_by_shop_id = os.order_by_shop_id");
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) * 3 / 100;
        }
        return -1;
    }

    public int countOrder() throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT max([order_id])\n"
                + "  FROM [FEP_DB].[dbo].[order]\n"
                + "  where status = 1");
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public int getTotalOrder(int month) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("select count(order_id) from dbo.[order]\n"
                + "where month(order_date) = ?");
        stm.setInt(1, month);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public float getOrderByCategory(String emailSeller, int category) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT CAST((SELECT CAST(("
                + "SELECT SUM(quantity) FROM order_detail WHERE product_id in\n"
                + "(SELECT product_id FROM product WHERE category_id = ? AND "
                + "email_seller = ? )) AS float) / CAST((SELECT SUM(quantity) "
                + "FROM product WHERE category_id = ? AND email_seller = ?) AS float)*100) "
                + "AS DECIMAL(8, 6)) as cr");
        stm.setInt(1, category);
        stm.setString(2, emailSeller);
        stm.setInt(3, category);
        stm.setString(4, emailSeller);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getFloat("cr");
        }
        return -1;
    }

    public int getOrderByPrice(String emailSeller, int min, int max) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT SUM(od.quantity) as [sum] FROM\n"
                + "(SELECT product_id FROM product WHERE email_seller = ? ) u\n"
                + "LEFT JOIN order_detail od ON u.product_id = od.product_id\n"
                + "LEFT JOIN order_by_shop os ON os.order_by_shop_id = od.order_by_shop_id\n"
                + "LEFT JOIN [order] o ON o.order_id = os.order_id\n"
                + "WHERE od.price >= ? AND od.price <= ?");
        stm.setString(1, emailSeller);
        stm.setInt(2, min);
        stm.setInt(3, max);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt("sum");
        }
        return -1;
    }

    public ArrayList<Integer> getOrderByPrice(String emailSeller) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> arr = new ArrayList<>();
        int[] price = {10000, 100000, 500000, 1000000, 2000000, 5000000};
        for (int i = 0; i < price.length - 1; i++) {
            arr.add(getOrderByPrice(emailSeller, price[i], price[i + 1]));
        }
        return arr;
    }

    public ArrayList<Integer> getTotalOrderCurrentMonths(int monthNumber) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> arr = new ArrayList<>();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        for (int i = currentMonth - 1; i >= currentMonth - monthNumber; i--) {
            arr.add(getTotalOrder(i));
        }
        return arr;
    }

    public ArrayList<Integer> getTotalIncomeCurrentMonths(int monthNumber) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> arr = new ArrayList<>();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        for (int i = currentMonth - 1; i >= currentMonth - monthNumber; i--) {
            arr.add(getTotalIncome(i));
        }
        return arr;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String currentPath;
        try {
            System.out.println(new OrderDAO().getOrderByCategory("LinhTKSS170602@fpt.edu.vn", 1));
        } catch (Exception ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
