package com.arturdevmob.currencyrates.business.core.models;

public class Currency {
    private String charCode;
    private String name;
    private double rate;
    private String imageUrl;

    public Currency(String charCode, String name, double rate, String imageUrl) {
        this.charCode = charCode;
        this.name = name;
        this.rate = rate;
        this.imageUrl = imageUrl;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
