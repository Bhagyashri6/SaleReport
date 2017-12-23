package com.atminfotech.atmsales;

/**
 * Created by ABC on 06/16/2017.
 */

public class PartyData {
    private String partyName;
    private String Total;

    public PartyData(String partyName, String total) {
        this.partyName = partyName;
        Total = total;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
}
