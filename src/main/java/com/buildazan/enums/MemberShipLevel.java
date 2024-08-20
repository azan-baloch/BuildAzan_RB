package com.buildazan.enums;

public enum MemberShipLevel {
	NONE(0, 0, "NONE"),
    BASIC(30, 499, "BASIC"),
    STANDARD(30, 999, "STANDARD"),
    PREMIUM(30, 1999, "PREMIUM");
	
	private final int days;
	private final int price;
	private final String name;

	MemberShipLevel(int days, int price, String name) {
		this.days = days;
		this.price = price;
		this.name = name;
	}
	
	public int getDays() {
		return days;
	}
	
	public int getPrice() {
		return price;
	}

	public String getName(){
		return name;
	}
    
}
