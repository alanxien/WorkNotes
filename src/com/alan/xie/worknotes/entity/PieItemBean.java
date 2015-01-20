package com.alan.xie.worknotes.entity;

public class PieItemBean {
	private String itemType;
    private float itemValue;

    public PieItemBean(String itemType, float itemValue) {
        this.itemType = itemType;
        this.itemValue = itemValue;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public float getItemValue() {
        return itemValue;
    }

    public void setItemValue(float itemValue) {
        this.itemValue = itemValue;
    }
}
