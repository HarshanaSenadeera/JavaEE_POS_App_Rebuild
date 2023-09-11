package lk.ijse.pos.servlet;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.CustomerBO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.util.ResponseUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {

    private CustomerBO customerBO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        customerBO= (CustomerBO) BOFactory.getBoFactory().getBoType(BOFactory.BoType.Customer);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {

            List<CustomerDTO> allCustomer = customerBO.getAllCustomer();

            JsonArrayBuilder allCus= Json.createArrayBuilder();

            for (CustomerDTO cus :allCustomer) {
                JsonObjectBuilder customerBuilder=Json.createObjectBuilder();
                customerBuilder.add("id",cus.getId());
                customerBuilder.add("name",cus.getName());
                customerBuilder.add("address",cus.getAddress());
                allCus.add(customerBuilder.build());
            }

            resp.setContentType("application/json");
            resp.getWriter().print(ResponseUtil.genJson("Success","Loaded",allCus.build()));

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

            String cusId=req.getParameter("cusID");
            String cusName=req.getParameter("cusName");
            String cusAddress= req.getParameter("cusAddress");


            if (customerBO.saveCustomer(new CustomerDTO(cusId,cusName,cusAddress))){

                resp.getWriter().print(ResponseUtil.genJson("Success","Successfully Added"));
            }else {
                resp.getWriter().print(ResponseUtil.genJson("error","Customer Added Fail"));
            }

        }catch (SQLException e){

            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader=Json.createReader(req.getReader());
        JsonObject customerObject=reader.readObject();

        String cusId=customerObject.getString("id");
        String cusName = customerObject.getString("name");
        String cusAddress =customerObject.getString("address");
        CustomerDTO customerDTO=new CustomerDTO(cusId,cusName,cusAddress);

        try {

            if (customerBO.updateCustomer(customerDTO)){

                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Updated..!"));
            }else{
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Updated Failed..!"));
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

        String cusId=req.getParameter("id");
        try {

            if (customerBO.deleteCustomer(cusId)){

                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Deleted..!"));
            }else{
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Delete Failed..!"));
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
