/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group6.fep.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group6.fep.dao.CategoryDAO;
import com.group6.fep.dto.AddressDTO;
import com.group6.fep.dto.OrderDetailDTO;
import com.group6.fep.dto.ProductDTO;
import com.group6.fep.dto.UserDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

/**
 *
 * @author kanek
 */
public class GhnApi {

    private static String shopId = "3237368";
    private static Gson gson = new Gson();
    public static final String TOKEN = "d67c06a0-2e7c-11ed-b824-262f869eb1a7";

    public static String getCityId(String cityName) throws IOException {

        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/master-data/province")
                .setHeaders(getBasicHeaders())
                .execute()
                .returnContent()
                .asString();
        return getIdFromResponse(response, "ProvinceName", "ProvinceID", cityName);
    }

    public static String getIdFromResponse(String res, String keyName, String keyId, String valueName) {
        JsonObject jobj = gson.fromJson(res, JsonObject.class);
        JsonArray data = jobj.get("data").getAsJsonArray();
        for (JsonElement el : data) {
            JsonObject province = el.getAsJsonObject();
            if (province.get(keyName).toString().toLowerCase().contains(valueName.toLowerCase())) {
                return province.get(keyId).toString();
            }
        }
        return null;
    }

    public static Header[] getBasicHeaders() {
        Header headerToken = new BasicHeader("token", TOKEN);
        Header headerContentType = new BasicHeader("Content-Type", "application/json");
        Header[] headers = {headerToken, headerContentType};
        return headers;
    }

    public static String getDistrictId(String cityId, String districtName) throws IOException {
//        System.out.println("cityId: " + cityId);
        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/master-data/district")
                .setHeaders(getBasicHeaders())
                .bodyString("{\n"
                        + "    \"province_id\":" + cityId + "\n"
                        + "}", ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();

        return getIdFromResponse(response, "DistrictName", "DistrictID", districtName);
    }

    public static String getWardId(String districtId, String wardName) throws IOException {
//        System.out.println("districtId: " + districtId);
        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id")
                .setHeaders(getBasicHeaders())
                .bodyString("{\n"
                        + "    \"district_id\":" + districtId + "\n"
                        + "}", ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        String data = getIdFromResponse(response, "WardName", "WardCode", wardName);

        return data == null ? null : data.replaceAll("\"", "");
    }

    public static String[] getFullAddressId(String cityName, String districtName, String wardName) throws IOException {
        String districtId = getDistrictId(cityName, districtName);
        String cityId = getCityId(cityName);
//        System.out.println("districtId: " + districtId);
        System.out.println(ContentType.APPLICATION_JSON);
        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id")
                .setHeaders(getBasicHeaders())
                .bodyString("{\n"
                        + "    \"district_id\":" + districtId + "\n"
                        + "}", ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        String wardId = "";
        if (getIdFromResponse(response, "WardName", "WardCode", wardName) != null) {
            wardId = getIdFromResponse(response, "WardName", "WardCode", wardName).replace("\"", "");
        }
        String[] result = {cityId, districtId, wardId};
        return result;
    }

    public static String getServices(String fromDistrictId, String toDistrictId) throws IOException {
        String body = "{\n"
                + "    \"shop_id\": 3237368,\n"
                + "    \"from_district\": " + fromDistrictId + ",\n"
                + "    \"to_district\": " + toDistrictId + "\n"
                + "}";
        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services")
                .setHeaders(getBasicHeaders())
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        JsonObject jobj = gson.fromJson(response, JsonObject.class);
        return jobj.get("data").toString();
    }

    public static int getShipingFee(String fromCityName, String toCityName, String fromDistrictName, String toDistrictName, String toWardName, String serviceId) throws IOException {
        String fromCityId = getCityId(fromCityName);
        String toCityId = getCityId(toCityName);
        String fromDistrictId = getDistrictId(fromCityId, fromDistrictName);
        String toDistrictId = getDistrictId(toCityId, toDistrictName);
        String toWardId = getWardId(toDistrictId, toWardName);
        String body = "{\n"
                + "    \"from_district_id\": " + fromDistrictId + ",\n"
                + "    \"service_type_id\":" + serviceId + ",\n"
                + "    \"to_district_id\":" + toDistrictId + ",\n"
                + "    \"to_ward_code\":" + toWardId + ",\n"
                + "    \"height\": 50,\n"
                + "    \"length\":20,\n"
                + "    \"weight\":200,\n"
                + "    \"width\":20,\n"
                + "    \"insurance_value\":0\n"
                + "}";

        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee")
                .setHeaders(getBasicHeaders())
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        JsonObject jobj = gson.fromJson(response, JsonObject.class);
        return jobj.getAsJsonObject("data").get("service_fee").getAsInt();
    }

    public static String getOrderDetail(String orderID) throws IOException {
        String body = "{\n"
                + "    \"order_code\": \" " + orderID + "\"\n"
                + "}";
        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail")
                .setHeaders(getBasicHeaders())
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        JsonObject jobj = gson.fromJson(response, JsonObject.class);
        return jobj.getAsJsonObject("data").toString();
    }
    public static void cancelOrder(String orderCode) throws IOException{
        String body = "{\"order_codes\":[\"" + orderCode + "\"]}";
        Header[] headers = new Header[3];
        Header[] basicHeaders = getBasicHeaders();
        headers[0] = basicHeaders[0];
        headers[1] = basicHeaders[1];
        headers[2] = new BasicHeader("ShopId", shopId);
        String response = Request.Post("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel")
                .setHeaders(basicHeaders)
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        JsonObject jobj = gson.fromJson(response, JsonObject.class);
    }
    public static String createOrder(UserDTO user, UserDTO seller, List<OrderDetailDTO> orderDetails, String cod) throws IOException, SQLException, ClassNotFoundException {
        AddressDTO userAddress = user.getAddress();
        AddressDTO sellerAddress = seller.getAddress();
        Header[] headers = new Header[3];
        Header[] basicHeaders = getBasicHeaders();
        headers[0] = basicHeaders[0];
        headers[1] = basicHeaders[1];
        headers[2] = new BasicHeader("ShopId", shopId);
        String body = "{\n"
                + "    \"payment_type_id\": 2,\n"
                + "    \"note\": \"Tintest 123\",\n"
                + "    \"from_name\":\"" + seller.getFirstName() + " " + seller.getLastName() + "\",\n"
                + "    \"from_phone\":\"" + seller.getPhone() + "\",\n"
                + "    \"from_address\":\"" + sellerAddress.getHouseNumber() + "\",\n"
                + "    \"from_ward_name\":\"" + sellerAddress.getWardName() + "\",\n"
                + "    \"from_district_name\":\"" + sellerAddress.getDistrictName() + "\",\n"
                + "    \"from_province_name\":\"" + sellerAddress.getCityName() + "\",\n"
                + "    \"required_note\": \"KHONGCHOXEMHANG\",\n"
                + "    \"to_name\": \"" + user.getFirstName() + " " + user.getLastName() + "\",\n"
                + "    \"to_phone\": \"" + user.getPhone() + "\",\n"
                + "    \"to_address\": \"" + userAddress.getHouseNumber() + "\",\n"
                + "    \"to_ward_name\": \"" + userAddress.getWardName() + "\",\n"
                + "    \"to_district_name\": \"" + userAddress.getDistrictName() + "\",\n"
                + "    \"to_province_name\":\"" + userAddress.getCityName() + "\",\n"
                + "    \"cod_amount\": " + cod + ",\n"
                + "    \"content\": \"Theo New York Times\",\n"
                + "    \"weight\": 200,\n"
                + "    \"length\": 1,\n"
                + "    \"width\": 19,\n"
                + "    \"height\": 10,\n"
                + "    \"service_id\": 53320,\n"
                + "    \"service_type_id\":2,\n"
                + "    \"items\": [\n";
        CategoryDAO cateDAO = new CategoryDAO();
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetailDTO orderDetail = orderDetails.get(i);
            ProductDTO product = orderDetail.getProduct();
            body += "        {\n"
                    + "            \"name\":\"" + product.getName() + "\",\n"
                    + "            \"code\":\"" + Integer.toString(orderDetail.getProductId()) + "\",\n"
                    + "            \"quantity\": " + Integer.toString(orderDetail.getQuantity()) + ",\n"
                    + "            \"price\": 0,\n"
                    + "            \"length\": 12,\n"
                    + "            \"width\": 12,\n"
                    + "            \"height\": 12,\n"
                    + "            \"category\": \n"
                    + "            {\n"
                    + "                \"level1\":\"" + cateDAO.find(product.getCateId()).getName() + "\"\n"
                    + "            }\n"
                    + "        }\n";
            if(i != orderDetails.size() -1 ){
                body += " , ";
            }
        }
        body += "    ]\n"
                + "    \n"
                + "}";
        System.out.println(body);
        String response = Request.Post("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create")
                .setHeaders(headers)
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        JsonObject jobj = gson.fromJson(response, JsonObject.class);
        return jobj.getAsJsonObject("data").get("order_code").toString();

    }

    public static void main(String[] args) {
        try {
            //        try {
//            System.out.println(getCityId("Hồ Chí Minh"));
//            System.out.println(getDistrictId("202", "Quận 12"));
//            System.out.println(getWardId("1454", "Thạnh Lộc"));
            System.out.println(GhnApi.getShipingFee(
                    "Hồ Chí Minh", "Hồ Chí Minh",
                    "Quận 12", "Quận " + "12",
                    "Thạnh Lộc", "2"));
            //        } catch (IOException ex) {
            //            Logger.getLogger(GhnApi.class.getName()).log(Level.SEVERE, null, ex);
            //        }
        } catch (IOException ex) {
            Logger.getLogger(GhnApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
