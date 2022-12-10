package logic;


public class Subscriber {
	// Class variables *************************************************
	private String id;
	private String firstName;
	private String lastName;
	private String phone_number;
	private String email;
	private String credit_card_number;
	private String subscriberNumber; 
	//******************************************************************
	
	/**
	 * class for saving each subscriber info , 
	 * only have getters and setters and toString
	 * 
	 */
	
	
	
	public Subscriber(String id, String FName, String LName, String phone_number,String email,String credit_card_number,String Subscriber_number) {
		this.id = id;
		this.firstName=FName;
		this.lastName=LName;
		this.phone_number=phone_number;
		this.email=email;
		this.credit_card_number=credit_card_number;
		this.subscriberNumber=Subscriber_number;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
		//System.out.println("ID set to "+id);
	}
	/**
	 * @return the lName
	 */
	public String getLastname() {
		return lastName;
	}
	/**
	 * @param name the lName to set
	 */
	public void setLastName(String name) {
		lastName = name;
		//System.out.println("Last name set to "+name);
	}
	/**
	 * @return the pName
	 */
	public String getFirstname() {
		return firstName;
	}
	/**
	 * @param name the pName to set
	 */
	public void setFirsname(String name) {
		firstName = name;
		//System.out.println("Private name set to "+name);
	}
	
	
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCredit_card_number() {
		return credit_card_number;
	}
	public void setCredit_card_number(String credit_card_number) {
		this.credit_card_number = credit_card_number;
	}
	public String getSubscriber_number() {
		return subscriberNumber;
	}
	public void setSubscriber_number(String subscriber_number) {
		subscriberNumber = subscriber_number;
	}
	@Override
	public String toString() {
		return "Subscriber Details:\n id=" + id + "\n first name=" + firstName + "\n last name=" + lastName + "\n phone_number=" + phone_number
				+ "\n email=" + email + "\n credit card number=" + credit_card_number + "\n Subscriber number="
				+ subscriberNumber + " ";
	}
	
}
