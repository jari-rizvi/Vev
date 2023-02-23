package com.teamx.vevae.DummyModel;

import java.util.ArrayList;

public class StoreHome {

    String CategoryName;
    ArrayList<Categories>  dataArrayList;

    public StoreHome(String categoryName, ArrayList<Categories> dataArrayList) {
        CategoryName = categoryName;
        this.dataArrayList = dataArrayList;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public ArrayList<Categories> getDataArrayList() {
        return dataArrayList;
    }

    public void setDataArrayList(ArrayList<Categories> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }
}
