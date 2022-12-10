package logic;

public class ClientConnected {
	// Class variables *************************************************
	private String ipAddress;
	private String host;
	private String status;
	//******************************************************************
	
	/**
	 * class for saving each client that connect to the server, only have getters and setters 
	 * 
	 */
	
	
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	public ClientConnected(String ipAddress, String host, String status) {
		super();
		this.ipAddress = ipAddress;
		this.host = host;
		this.status = status;
	}
	


}
