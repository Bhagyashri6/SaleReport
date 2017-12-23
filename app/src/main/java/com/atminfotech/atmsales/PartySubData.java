package com.atminfotech.atmsales;

/**
 * Created by ABC on 06/16/2017.
 */

public class PartySubData {
    private String BillNO;
    private String BillDate;
    private String Days;
    private String Amount;
    private String Balance;

    public PartySubData() {
    }

    public PartySubData(String billNO, String billDate, String days, String amount, String balance) {
        BillNO = billNO;
        BillDate = billDate;
        Days = days;
        Amount = amount;
        Balance = balance;
    }

    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
