package lk.ijse.pos.servlet;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.ItemBO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.util.ResponseUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet(urlPatterns = {"/pages/item"})
public class ItemServlet extends HttpServlet {

    private ItemBO itemBO;

    @Override
    public void init() throws ServletException {
        itemBO = (ItemBO) BOFactory.getBoFactory().getBoType(BOFactory.BoType.Item);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
//

            List<ItemDTO> allItem = itemBO.getAllItem();

            JsonArrayBuilder allItems= Json.createArrayBuilder();

            for (ItemDTO dto:allItem) {
                JsonObjectBuilder itemBuilder=Json.createObjectBuilder();

                itemBuilder.add("code",dto.getCode());
                itemBuilder.add("item_name",dto.getName());
                itemBuilder.add("item_contity",dto.getQty());
                itemBuilder.add("unit_price",dto.getPrice());
                allItems.add(itemBuilder.build());
            }

            resp.setContentType("application/json");
            resp.getWriter().print(ResponseUtil.genJson("Success", "Loaded", allItems.build()));

        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {

            String code=req.getParameter("itemCode");
            String description=req.getParameter("itemName");
            String qtyOnHand= req.getParameter("itemQty");
            String unitPrice= req.getParameter("itemPrice");


            if (itemBO.saveItem(new ItemDTO(code, description, qtyOnHand, unitPrice))){

                resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
            }

        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }catch (SQLException e){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        JsonReader reader=Json.createReader(req.getReader());
        JsonObject itemObject=reader.readObject();

        String code=itemObject.getString("code");
        String description = itemObject.getString("item_name");
        String qtyOnHand = itemObject.getString("Item_contity");
        String unitPrice = itemObject.getString("unit_price");


        try {

            if (itemBO.updateItem(new ItemDTO(code,description,qtyOnHand,unitPrice))){
                resp.getWriter().print(ResponseUtil.genJson("Success", "Item Updated..!"));
            }else{
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Item Updated Failed..!"));
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }catch (SQLException e){

            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code=req.getParameter("code");

        try {

            if (itemBO.deleteItem(code)){

                resp.getWriter().print(ResponseUtil.genJson("Success", "Item Deleted..!"));
            }else{
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Item Delete Failed..!"));
            }

        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }catch (SQLException e){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }

}
