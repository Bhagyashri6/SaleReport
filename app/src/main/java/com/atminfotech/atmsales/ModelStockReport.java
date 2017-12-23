package com.atminfotech.atmsales;

public class ModelStockReport {

    private String itemcode;
    private String itemname;
    private String model;
    private String brand;
    private String subBrand;
    private String gender;
    private String catogery;
    private double amount;

    public ModelStockReport() {
    }

    public ModelStockReport(String itemcode, String itemname, String model, String brand, String subBrand, String gender, String catogery, double amount) {
        this.itemcode = itemcode;
        this.itemname = itemname;
        this.model = model;
        this.brand = brand;
        this.subBrand = subBrand;
        this.gender = gender;
        this.catogery = catogery;
       /* if(amount.equalsIgnoreCase("anytype{}") || amount.equalsIgnoreCase(""))
            this.amount="-";
        else if(Integer.valueOf(amount)<=0){this.amount="Expired";}*/
        this.amount = amount;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSubBrand() {
        return subBrand;
    }

    public void setSubBrand(String subBrand) {
        this.subBrand = subBrand;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCatogery() {
        return catogery;
    }

    public void setCatogery(String catogery) {
        this.catogery = catogery;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
/* public ModelStockReport(String Date, String brand, String name, String qty, String values) {

        this.purDate = Date;
        this.brand = brand;
        this.qty = qty;
        this.name = name;
        if(values.equalsIgnoreCase("anytype{}") || values.equalsIgnoreCase(""))
            this.values="-";
                    else if(Integer.valueOf(values)<=0){this.values="Expired";}
         this.values = values;

    }*/

   /* public String getPurDate() {return purDate;    }

    public void setPurDate(String purDate) {
        this.purDate = purDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return purDate+" "+brand+" "+name;
    }*/
}

