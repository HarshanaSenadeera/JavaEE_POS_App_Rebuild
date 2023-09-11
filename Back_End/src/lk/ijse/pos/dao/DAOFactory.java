package lk.ijse.pos.dao;


import lk.ijse.pos.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.pos.dao.custom.impl.ItemDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    public DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        if(daoFactory==null){
            daoFactory=new DAOFactory();
        }
        return daoFactory;
    }
    public enum DoType{
        Item, Customer
    }
    public SuperDAO getDo(DoType doType){
        switch (doType){
            case Customer:
                return (SuperDAO) new CustomerDAOImpl();
            case Item:
                return (SuperDAO) new ItemDAOImpl();
        }
        return null;
    }
}
