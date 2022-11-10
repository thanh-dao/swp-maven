/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group6.fep.dao;

import com.google.gson.Gson;
import static com.group6.fep.dao.ProductDAO.ASC;
import com.group6.fep.dto.AddressDTO;
import com.group6.fep.dto.UserDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import com.group6.fep.utils.Constants;
import com.group6.fep.utils.DBUtil;

public class UserDAO {

    
    public HashMap<String, Integer> getTop10UserBuyByShop(String sellerEmail, int month, boolean trend, boolean isquantity)
            throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        String query = "SELECT TOP 10 SUM(od.";
        if (isquantity) {
            query += "quantity";
        } else {
            query += "price";
        }
        query += ") as [param] , o.email_buyer FROM\n"
                + "(SELECT email FROM [user] WHERE email = ?) u\n"
                + "LEFT JOIN product p ON p.email_seller = u.email\n"
                + "LEFT JOIN order_detail od ON od.product_id = p.product_id\n"
                + "LEFT JOIN order_by_shop os ON os.order_by_shop_id = od.order_by_shop_id\n"
                + "LEFT JOIN [order] o ON o.order_id = os.order_id\n"
                + "WHERE o.email_buyer is not null\n"
                + "GROUP BY o.email_buyer "
                + "order by param asc";
//        if (trend == ProductDAO.ASC) {
//            query += "asc";
//            System.out.println("");
//        } else {
//            query += "desc";
//        }
        System.out.println(query);
        PreparedStatement stm = conn.prepareStatement(query);
        stm.setString(1, sellerEmail);
        HashMap<String, Integer> list = new HashMap<>();
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            list.put(rs.getString("email_buyer"), rs.getInt("param"));
        }
        return list;
    }
    
    
    /**
     * find a user in database
     *
     * @param email
     * @return UserDTO
     */
    public UserDTO findUser(String email) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("select [email]\n"
                + "      ,[avatar]\n"
                + "      ,[first_name]\n"
                + "      ,[last_name]\n"
                + "      ,[phone]\n"
                + "      ,[yob]\n"
                + "      ,[address]\n"
                + "      ,[ward_id]\n"
                + "      ,[district_id]\n"
                + "      ,[city_id]\n"
                + "      ,[role_id]"
                + "  from [user] where email = ?");
        stm.setString(1, email);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            String storedAvatar = rs.getString("avatar");
            UserDTO user = new UserDTO(
                    rs.getString(1), storedAvatar.contains("http") ? storedAvatar : Constants.IMAGE_RELATIVE_DIRECTORY + "/" + storedAvatar, rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getDate(6),
                    new AddressDAO().getFullAddress(
                            rs.getString("address"),
                            rs.getString("city_id"),
                            rs.getString("district_id"),
                            rs.getString("ward_id")
                    ),
                    rs.getInt(11)
            );
            return user;
        }

        return null;
    }

    /**
     * add user to database
     *
     * @param email
     * @param givenName
     * @param avatarLink
     * @return UserDTO
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public UserDTO addUser(String email, String givenName, String avatarLink) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        String[] splitedFullName = givenName.split(" ");
        String firstName = splitedFullName[0];
        String lastName = "";
        for (int i = 1; i < splitedFullName.length; i++) {
            lastName += splitedFullName[i];
            if (i != splitedFullName.length - 1) {
                lastName += " ";
            }
        }
        PreparedStatement stm = conn.prepareStatement("insert into [user]"
                + "(email, avatar, last_name, first_name, role_id)"
                + " values (?, ?, ?, ?, 0)");
        stm.setString(1, email);
        stm.setString(2, avatarLink);
        stm.setString(3, lastName);
        stm.setString(4, firstName);
        stm.executeUpdate();
        return new UserDTO(email, Constants.IMAGE_RELATIVE_DIRECTORY + "/" + avatarLink, firstName, lastName);
    }

    public UserDTO getUserByProductId(int productId) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT email, avatar, first_name, last_name, "
                + "phone, address, w.name, d.name, c.name FROM [user] u \n"
                + "LEFT JOIN ward w ON u.ward_id = w.ward_id\n"
                + "LEFT JOIN district d ON u.district_id = d.district_id\n"
                + "LEFT JOIN city c ON u.city_id = c.city_id\n"
                + "WHERE email = (SELECT email_seller FROM product WHERE product_id = ? )");
        stm.setInt(1, productId);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            String storedAvatar = rs.getString("avatar");
            UserDTO user = new UserDTO(
                    rs.getString("email"), storedAvatar.contains("http") ? storedAvatar : Constants.IMAGE_RELATIVE_DIRECTORY + "/" + storedAvatar, rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("phone"),
                    new AddressDAO().getFullAddress(rs.getString("address"), rs.getString(7),
                            rs.getString(8), rs.getString(9))
            );
            return user;
        }
        return null;
    }

    public int countUser() throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("select count(email) from [dbo].[user]");
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public LinkedHashMap<String, String> getTop10SellerByMonth(int month) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT DISTINCT TOP 10 SUM(od.quantity) , os.email_seller FROM \n"
                + "(SELECT order_id FROM [order] WHERE MONTH([order_date]) = ?) o\n"
                + "LEFT JOIN order_by_shop os ON o.order_id = os.order_id\n"
                + "LEFT JOIN order_detail od ON od.order_by_shop_id = os.order_by_shop_id\n"
                + "GROUP BY os.email_seller\n"
                + "order by SUM(od.quantity) desc");
        stm.setInt(1, month);
        ResultSet rs = stm.executeQuery();
        LinkedHashMap<String, String> arr = new LinkedHashMap();
        while (rs.next()) {
            arr.put(rs.getString(2), rs.getString(1));
        }
        return arr;
    }

    public String getUserAvatar(String email) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("select avatar from [user] where email = ? ");
        stm.setString(1, email);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return Constants.IMAGE_RELATIVE_DIRECTORY + "/" + rs.getString(1);
        }
        return null;
    }

    public List<UserDTO> getUserByRole(boolean status) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT [email]\n"
                + "      ,[avatar]\n"
                + "      ,[first_name]\n"
                + "      ,[last_name]\n"
                + "      ,[phone]\n"
                + "      ,[role_id]"
                + "  FROM [user] WHERE role_id = ?");
        stm.setInt(1, status ? 2 : 0);
        ResultSet rs = stm.executeQuery();
        List<UserDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new UserDTO(
                    rs.getString("email"), rs.getString("avatar"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("phone"), rs.getInt("role_id")
            ));
        }
        return list;
    }

    public boolean updateUserRole(String email, boolean status) throws ClassNotFoundException, SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("UPDATE [user] \n"
                + "SET role_id = ? WHERE email = ?");
        stm.setInt(1, status ? 2 : 0);
        stm.setString(2, email);
        return stm.executeUpdate() == 1;
    }

    public String getUserJson(List<UserDTO> userList) throws ClassNotFoundException, SQLException {
        Gson gson = new Gson();
        HashMap<String, String> hashmap = new HashMap<>();
        String a = "[";
        for (UserDTO r : userList) {
            hashmap.put("email", String.valueOf(r.getEmail()));
            hashmap.put("name", r.getFirstName() + r.getLastName());
            hashmap.put("avatar", r.getAvatarLink());
            hashmap.put("phone", r.getPhone() == null ? "Chưa cập nhật" : r.getPhone());
            hashmap.put("roleId", String.valueOf(r.getRoleId()));
            a += gson.toJson(hashmap) + ",";
        }
        return a + "]";
    }

    public boolean updateUserAvatar(String dir, String email) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("update [user] "
                + " set [avatar] = ? "
                + " where [email] = ?"
        );
        stm.setString(1, dir);
        stm.setString(2, email);
        return stm.executeUpdate() == 1;
    }

    // Update user information
    public boolean updateUser(UserDTO user) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("update [user] "
                + " set [first_name] = ? , \n"
                + " [last_name] = ? , "
                + " [phone] = ? , "
                + " [yob] = ? , "
                + " [address] = ? , "
                + " [ward_id] = ? , "
                + " [district_id] = ? , "
                + " [city_id] = ?  "
                + " where [email] = ?"
        );
        AddressDTO address = user.getAddress();
        stm.setString(1, user.getFirstName());
        stm.setString(2, user.getLastName());
        stm.setString(3, user.getPhone());
        stm.setDate(4, (Date) user.getYob());
        stm.setString(5, address.getHouseNumber());
        stm.setString(6, address.getWardId());
        stm.setString(7, address.getDistrictId());
        stm.setString(8, address.getCityId());
        stm.setString(9, user.getEmail());
        return stm.executeUpdate() == 1;
    }

    public AddressDTO getAddressUser(String userEmail) throws ClassNotFoundException, SQLException {
        Connection conn;
        conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement("SELECT w.name as ward, "
                + "d.name as district, c.name as city FROM (SELECT ward_id, "
                + "district_id, city_id FROM [user] WHERE email = ? ) u\n"
                + "LEFT JOIN ward w ON w.ward_id = u.ward_id\n"
                + "LEFT JOIN district d ON d.district_id = u.district_id\n"
                + "LEFT JOIN city c ON c.city_id = u.city_id");
        stm.setString(1, userEmail);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            AddressDTO address = new AddressDTO(null, rs.getString("ward"), rs.getString("district"),
                            rs.getString("city"));
            return address;
        }
        return null;
    }

    public static void main(String[] args) {
        UserDAO uDAO = new UserDAO();
        try {
//            uDAO.getTop10SellerByMonth(9).forEach((k, v) -> {
//                System.out.println(k + "  " + v);
//            });
//System.out.println(uDAO.getUserByRole(false).size());
//System.out.println(uDAO.updateUserRole("ThinhPQSE151077@fpt.edu.vn", true));
//            System.out.println(uDAO.getUserJson(uDAO.getAllUser()));
//            System.out.println(uDAO.getUserByProductId(149));
//            System.out.println(uDAO.addUser("thanhddse151068@fpt.edu.vn", "Dao Duc Thanh", "jajaaja"));
//            System.out.println(uDAO.findUser("ThinhPQSE151077@fpt.edu.vn"));
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            uDAO.getTop10UserBuyByShop("thinhpqse151077@fpt.edu.vn", 11, ProductDAO.DESC, true).forEach((k,v) -> {
//                System.out.println(k + " " + v);
//            });
//            uDAO.getTop10UserBuyByShop("thanhddse151068@fpt.edu.vn", 11, ProductDAO.DESC, false).forEach((k, v) -> {
//                System.out.println(k + " " + v);
//            });
            System.out.println(uDAO.getUserByRole(false).size());
            System.out.println(uDAO.getUserByRole(true).size());
        } catch (Exception ex) {
        }
    }
}
