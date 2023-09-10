package lk.ijse.pos.servlet;

import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/item"})
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonObjectBuilder itemObject = Json.createObjectBuilder();
        JsonArrayBuilder itemArray = Json.createArrayBuilder();

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try(Connection connection = dbcp.getConnection();) {

            PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet set = pstm.executeQuery();

            while (set.next()){

                ItemDTO itemDTO = new ItemDTO(set.getString(1),set.getString(2),set.getInt(3),set.getDouble(4));

                itemObject.add("code",itemDTO.getCode());
                itemObject.add("item_name",itemDTO.getName());
                itemObject.add("item_contity",itemDTO.getQty());
                itemObject.add("unit_price",itemDTO.getPrice());

                itemArray.add(itemObject.build());
            }

            resp.getWriter().print(itemArray.build());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        String code = req.getParameter("itemCode");
        String name = req.getParameter("itemName");
        int qty = Integer.parseInt(req.getParameter("itemQty"));
        double price = Double.parseDouble(req.getParameter("itemPrice"));

        ItemDTO itemDTO = new ItemDTO(code,name,qty,price);

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");


        try(Connection connection = dbcp.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement("insert into item values (?,?,?,?)");

           pstm.setObject(1,itemDTO.getCode());
           pstm.setObject(2,itemDTO.getName());
           pstm.setObject(3,itemDTO.getQty());
           pstm.setObject(4,itemDTO.getPrice());

            if(pstm.executeUpdate()>0){
                JsonObjectBuilder itemObject = Json.createObjectBuilder();
                itemObject.add("state","OK");
                itemObject.add("message","Save item successfully Done...!");
                itemObject.add("data"," ");

                resp.getWriter().print(itemObject.build());
            }
        } catch ( RuntimeException e) {
            JsonObjectBuilder itemObject = Json.createObjectBuilder();
            itemObject.add("state","Error");
            itemObject.add("message",e.getLocalizedMessage());
            itemObject.add("data"," ");
            resp.setStatus(500);

            resp.getWriter().print(itemObject.build());
        } catch (SQLException e) {

            JsonObjectBuilder errorSQL = Json.createObjectBuilder();
            errorSQL.add("state","Error");
            errorSQL.add("message",e.getLocalizedMessage());
            errorSQL.add("data"," ");

            resp.getWriter().print(errorSQL.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customerObject = reader.readObject();

        String code = customerObject.getString("code");
        String name = customerObject.getString("item_name");
        int qty = Integer.parseInt(customerObject.getString("item_contity"));
        double price = Double.parseDouble(customerObject.getString("unit_price"));

        ItemDTO itemDTO = new ItemDTO(code,name,qty,price);

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try(Connection connection = dbcp.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement("update item set item_name=?,item_contity=?,unit_price=? where code=?");

           pstm.setObject(1,itemDTO.getName());
           pstm.setObject(2,itemDTO.getQty());
           pstm.setObject(3,itemDTO.getPrice());
           pstm.setObject(4,itemDTO.getCode());

            if(pstm.executeUpdate()>0){
                JsonObjectBuilder messageObject = Json.createObjectBuilder();
                messageObject.add("state","OK");
                messageObject.add("message","Successfully Updated");
                messageObject.add("data"," ");

                resp.getWriter().print(messageObject.build());
            }
        } catch ( RuntimeException e) {

            JsonObjectBuilder errorMessage = Json.createObjectBuilder();
            errorMessage.add("state","Error");
            errorMessage.add("message",e.getLocalizedMessage());
            errorMessage.add("data"," ");
            resp.setStatus(500);

            resp.getWriter().print(errorMessage.build());

        } catch (SQLException e) {

            JsonObjectBuilder errorSQL = Json.createObjectBuilder();
            errorSQL.add("state","Error");
            errorSQL.add("message",e.getLocalizedMessage());
            errorSQL.add("data"," ");


            resp.getWriter().print(errorSQL.build());
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try (Connection connection = dbcp.getConnection()){

            PreparedStatement pstm = connection.prepareStatement("delete from item where code=?");

            pstm.setObject(1,req.getParameter("code"));

            if(pstm.executeUpdate()>0){

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state","OK");
                objectBuilder.add("message","Delete successfully Done");
                objectBuilder.add("data"," ");

                resp.getWriter().print(objectBuilder.build());
            }

        } catch ( RuntimeException e) {

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state","Error");
            objectBuilder.add("message",e.getLocalizedMessage());
            objectBuilder.add("data"," ");

            resp.setStatus(500);
            resp.getWriter().print(objectBuilder.build());
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state","OK");
            objectBuilder.add("message","Delete successfully Done");
            objectBuilder.add("data"," ");

            resp.getWriter().print(objectBuilder.build());
        }
    }

}
