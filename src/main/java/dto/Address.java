package dto;

public class Address {
	private int addressCode;
	private int customerCode;
	private String address;
	private String createdate;
	public int getAddressCode() {
		return addressCode;
	}
	public void setAddressCode(int addressCode) {
		this.addressCode = addressCode;
	}
	public int getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(int customerCode) {
		this.customerCode = customerCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	@Override
	public String toString() {
		return "Address [addressCode=" + addressCode + ", customerCode=" + customerCode + ", address=" + address
				+ ", createdate=" + createdate + "]";
	}
	
	
}
