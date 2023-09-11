package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.CustomerBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO= (CustomerDAO) DAOFactory.getDaoFactory().getDo(DAOFactory.DoType.Customer);

    @Override
    public boolean saveCustomer(CustomerDTO cusDto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(cusDto.getId(),cusDto.getName(),cusDto.getAddress()));
    }

    @Override
    public List<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
        ResultSet rst = customerDAO.getAll();
        List<CustomerDTO> allCustomer=new ArrayList<>();
        while (rst.next()) {
            CustomerDTO customer = new CustomerDTO();
            customer.setId(rst.getString("id"));
            customer.setName(rst.getString("name"));
            customer.setAddress(rst.getString("address"));
            allCustomer.add(customer);
        }
        rst.close();
        return allCustomer;
    }

    @Override
    public boolean updateCustomer(CustomerDTO cusDto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(cusDto.getId(),cusDto.getName(),cusDto.getAddress()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }
    /*@Override
    public boolean saveCustomer(CustomerDTO cusDto) throws SQLException {
        return customerDAO.save(new Customer(cusDto.getId(),cusDto.getName(),cusDto.getAddress()));
    }

    @Override
    public List<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
        ResultSet rst = customerDAO.getAll();
        List<CustomerDTO> allCustomer=new ArrayList<>();
        while (rst.next()) {
            CustomerDTO customer = new CustomerDTO();
            customer.setId(rst.getString("id"));
            customer.setName(rst.getString("name"));
            customer.setAddress(rst.getString("address"));
            allCustomer.add(customer);
        }
        rst.close();
        return allCustomer;
    }

    @Override
    public boolean updateCustomer(CustomerDTO cusDto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(cusDto.getId(),cusDto.getName(),cusDto.getAddress()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }*/


}
