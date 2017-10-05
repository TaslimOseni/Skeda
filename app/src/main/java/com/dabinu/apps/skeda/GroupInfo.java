package com.dabinu.apps.skeda;

import java.util.ArrayList;



public class GroupInfo{

    private String name;
    private ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildInfo> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<ChildInfo> productList) {
        this.list = productList;
    }

}
