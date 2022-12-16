package entity;

import java.io.Serializable;

public class ItemRespose implements Serializable {

	private static final long serialVersionUID = -6590085116090254282L;
	
	private Item[] items;

	public Item[] getItems() {
		return items;
	}

	public ItemRespose(Item[] items) {
		super();
		this.items = items;
	}
	
}
