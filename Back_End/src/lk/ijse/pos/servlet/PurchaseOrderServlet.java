package lk.ijse.pos.servlet;

import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.dto.PurchaseOrderDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/purchase-order"})
public class PurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try( Connection connection = dbcp.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement("SELECT code FROM item");
            ResultSet resultSet = pstm.executeQuery();


            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            ItemDTO itemDTO = new ItemDTO();

            while (resultSet.next()) {
                itemDTO.setCode(resultSet.getString(1));
                JsonObjectBuilder code = Json.createObjectBuilder();
                code.add("code", itemDTO.getCode());

                arrayBuilder.add(code.build());
            }

            PreparedStatement cusPstm = connection.prepareStatement("SELECT id FROM customer");
            ResultSet cusDetails = cusPstm.executeQuery();

            CustomerDTO customerDTO = new CustomerDTO();
            while (cusDetails.next()) {
                customerDTO.setId(cusDetails.getString(1));
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("id", customerDTO.getId());

                arrayBuilder.add(customer.build());
            }

            resp.getWriter().print(arrayBuilder.build());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String oId = jsonObject.getString("oId");
        double cash = Double.parseDouble(jsonObject.getString("cash"));
        double balance = Double.parseDouble(jsonObject.getString("balance"));
        Date date = Date.valueOf(jsonObject.getString("date"));
        String cusId = jsonObject.getString("cusId");

        PurchaseOrderDTO orderDTO = new PurchaseOrderDTO(oId, cash, balance, date, cusId);

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try (Connection connection = dbcp.getConnection()){

            connection.setAutoCommit(false);

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO orderDetails VALUES (?,?,?,?,?)");
            pstm.setObject(1, orderDTO.getoId());
            pstm.setObject(2, orderDTO.getCash());
            pstm.setObject(3, orderDTO.getBalance());
            pstm.setObject(4, orderDTO.getDate());
            pstm.setObject(5, orderDTO.getCusId());

            if (!(pstm.executeUpdate() > 0)) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new SQLException("Order Details Error");
            } else {

                JsonArray itemData = jsonObject.getJsonArray("itemData");

                for (JsonValue item : itemData) {
                    JsonObject jsonObject1 = item.asJsonObject();
                    String itemCode1 = jsonObject1.getString("code");
                    int itemQty = Integer.parseInt(jsonObject1.getString("qty"));

                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT qty FROM item WHERE code=?");
                    preparedStatement.setObject(1, itemCode1);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    int newValue = 0;
                    while (resultSet.next()) {
                        int qtyOnHand = resultSet.getInt(1);
                        newValue = qtyOnHand - itemQty;
                    }

                    PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE item SET qty=? WHERE code=?");
                    preparedStatement1.setObject(1, newValue);
                    preparedStatement1.setObject(2, itemCode1);

                    if (!(preparedStatement1.executeUpdate() > 0)){
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw new SQLException("Item Update Error");
                    }

                    PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO orderItems VALUES (?,?,?)");
                    preparedStatement2.setObject(1, orderDTO.getoId());
                    preparedStatement2.setObject(2, itemCode1);
                    preparedStatement2.setObject(3, itemQty);

                    if (!(preparedStatement2.executeUpdate() > 0)){
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw new SQLException("Order Items Error");
                    }
                }

                connection.commit();
                connection.setAutoCommit(true);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state", "OK");
                objectBuilder.add("message", "Successfully Order Added.....");
                objectBuilder.add("Data", " ");
                resp.getWriter().print(objectBuilder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String id = jsonObject.getString("id");

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try (Connection connection = dbcp.getConnection()){

            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(id);

            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM customer WHERE id=?");
            pstm.setObject(1, customerDTO.getId());

            ResultSet resultSet = pstm.executeQuery();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            while (resultSet.next()) {
                String id1 = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);

                objectBuilder.add("id", id1);
                objectBuilder.add("name", name);
                objectBuilder.add("address", address);
            }

            resp.getWriter().print(objectBuilder.build());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String code = jsonObject.getString("code");
        String name = jsonObject.getString("item_name");
        String qty = jsonObject.getString("item_contity");
        String price = jsonObject.getString("unit_price");

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");


        try (Connection connection = dbcp.getConnection()){


            ItemDTO itemDTO = new ItemDTO(code,name,qty,price);
            itemDTO.setCode(code);

            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
            pstm.setObject(1, itemDTO.getCode());

            ResultSet resultSet = pstm.executeQuery();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            while (resultSet.next()) {
                String code1 = resultSet.getString(1);
                String name1 = resultSet.getString(2);
                String qty1 = String.valueOf(resultSet.getInt(3));
                String price1 = String.valueOf(resultSet.getDouble(4));

                objectBuilder.add("code", code1);
                objectBuilder.add("item_name", name1);
                objectBuilder.add("item_contity", qty1);
                objectBuilder.add("unit_price", price1);
            }

            resp.getWriter().print(objectBuilder.build());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
