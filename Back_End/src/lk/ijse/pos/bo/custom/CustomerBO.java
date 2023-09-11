package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBO;
import lk.ijse.pos.dto.CustomerDTO;


import java.sql.SQLException;
import java.util.List;

public interface  CustomerBO extends SuperBO {
    public boolean saveCustomer(CustomerDTO cusDto) throws SQLException, ClassNotFoundException;
    public List<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException;
    public boolean updateCustomer(CustomerDTO cusDto) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String id)throws SQLException, ClassNotFoundException;
}
