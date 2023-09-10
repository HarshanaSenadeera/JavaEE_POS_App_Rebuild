package lk.ijse.pos.servlet;

import lk.ijse.pos.dto.CustomerDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String servletPath = req.getServletPath();

        System.out.println(servletPath);

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try( Connection connection = dbcp.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement("select * from customer");

            ResultSet set = pstm.executeQuery();

            JsonObjectBuilder customerObject = Json.createObjectBuilder();
            JsonArrayBuilder customerArray = Json.createArrayBuilder();

            while (set.next()){
                CustomerDTO customerDTO = new CustomerDTO( set.getString(1), set.getString(2), set.getString(3));

                customerObject.add("id",customerDTO.getId());
                customerObject.add("name",customerDTO.getName());
                customerObject.add("address",customerDTO.getAddress());

                customerArray.add(customerObject.build());
            }

            resp.getWriter().print(customerArray.build());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CustomerDTO customerDTO = new CustomerDTO(req.getParameter("cusID"),req.getParameter("cusName"),req.getParameter("cusAddress"));

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");


        try(Connection connection = dbcp.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement("insert into customer values (?,?,?)");

            pstm.setObject(1,customerDTO.getId());
            pstm.setObject(2,customerDTO.getName());
            pstm.setObject(3,customerDTO.getAddress());

            if(pstm.executeUpdate()>0){

                JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
                messageBuilder.add("state","OK");
                messageBuilder.add("message","Successfully Added");
                messageBuilder.add("Data"," ");
                resp.getWriter().print(messageBuilder.build());

            }
        } catch ( RuntimeException e) {
            JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
            errorBuilder.add("state","Error");
            errorBuilder.add("message",e.getLocalizedMessage());
            errorBuilder.add("data"," ");
            resp.setStatus(500);

            resp.getWriter().print(errorBuilder.build());

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

        CustomerDTO customerDTO = new CustomerDTO(customerObject.getString("id"),customerObject.getString("name"),customerObject.getString("address"));

        ServletContext servletContext = getServletContext();

        try(Connection connection = ((BasicDataSource) servletContext.getAttribute("dbcp")).getConnection();) {

            PreparedStatement pstm = connection.prepareStatement("update customer set name=?,address=? where id=?");

           pstm.setObject(1,customerDTO.getName());
           pstm.setObject(2,customerDTO.getAddress());
           pstm.setObject(3,customerDTO.getId());

            if(pstm.executeUpdate()>0){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state","OK");
                objectBuilder.add("message","Successfully Updated...!");
                objectBuilder.add("data"," ");
                resp.getWriter().print(objectBuilder.build());
            }else {
                throw new RuntimeException("Cant Update...!");
            }

        } catch (RuntimeException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state","Error");
            objectBuilder.add("message",e.getLocalizedMessage());
            objectBuilder.add("data"," ");
            resp.setStatus(500);
            resp.getWriter().print(objectBuilder.build());

        } catch (SQLException e) {

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state","Error");
            objectBuilder.add("message",e.getLocalizedMessage());
            objectBuilder.add("data"," ");
            resp.getWriter().print(objectBuilder.build());
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String id = req.getParameter("id");

        BasicDataSource dbcp = (BasicDataSource) getServletContext().getAttribute("dbcp");

        try (Connection connection = dbcp.getConnection()){


            PreparedStatement pstm = connection.prepareStatement("delete from customer where id=?");

            pstm.setObject(1,id);

            if(pstm.executeUpdate()>0){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state","OK");
                objectBuilder.add("message","Delete Successfully....!");
                objectBuilder.add("data"," ");


                resp.getWriter().print(objectBuilder.build());
            }
        } catch ( RuntimeException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state","Error");
            objectBuilder.add("message",e.getLocalizedMessage());
            objectBuilder.add("data"," ");
            resp.setStatus(500);

        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state","Error");
            objectBuilder.add("message",e.getLocalizedMessage());
            objectBuilder.add("data"," ");

            resp.getWriter().print(objectBuilder.build());
        }


    }


}
