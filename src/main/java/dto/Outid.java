package dto;

public class Outid {
	private String id;
	private String memo;
	private String createdate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	@Override
	public String toString() {
		return "OutId [id=" + id + ", memo=" + memo + ", createdate=" + createdate + "]";
	}
	
	
}
