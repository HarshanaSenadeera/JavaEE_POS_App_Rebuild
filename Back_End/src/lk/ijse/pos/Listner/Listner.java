package lk.ijse.pos.Listner;


import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class Listner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        BasicDataSource pool=new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUrl("jdbc:mysql://localhost:3306/rebuilddatabase");
        pool.setPassword("1234");
        pool.setUsername("root");
        pool.setMaxTotal(10);
        pool.setInitialSize(10);
        servletContext.setAttribute("dbcp",pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {
            ((BasicDataSource)servletContextEvent.getServletContext().getAttribute("dbcp")).close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

