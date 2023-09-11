package lk.ijse.pos.bo;

import lk.ijse.pos.bo.custom.impl.CustomerBOImpl;
import lk.ijse.pos.bo.custom.impl.ItemBOIml;


public class BOFactory {
    private static BOFactory boFactory;

    public BOFactory() {
    }

    public static BOFactory getBoFactory() {
        if(boFactory==null){
            boFactory=new BOFactory();
        }
        return boFactory;
    }
    public enum BoType{
        Item, Customer
    }
    public SuperBO getBoType(BoType boType){
        switch (boType){
            case Customer:
                return (SuperBO) new CustomerBOImpl();
            case Item:
                return (SuperBO) new ItemBOIml();
        }
        return null;
    }
}
