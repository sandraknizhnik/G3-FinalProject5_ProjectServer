package entity;

import java.io.Serializable;
import java.util.Random;

public class Item implements Serializable{
	//lets us to send as a message
	private static final long serialVersionUID = 4989431237771663829L;
	private String name;
    private String imgSrc;
    private double price;
    private String color;
    private String code;
    
    public Item(String name, String imgSrc, double price, String color, String code) {
		this.name = name;
		this.imgSrc = imgSrc;
		this.price = price;
		this.color = color;
		this.code = code;
	}

	public String getName() {
        return name;
    }

    public void setCode() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        this.code = String.format("%06d", number);
    }
    
    public String getCode() {
        return code;
    }

    public String getImgSrc() {
        return imgSrc;
    }


    public double getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }
    
    
}