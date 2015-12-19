package models;

public class Province {
	private int province_id;
	private String province;
	public Province(String province,int province_id){
		this.province=province;
		this.province_id=province_id;
	}
	public void setProvinceid(int province_id){
		this.province_id=province_id;
	}
	public int getProvinceid(){
		return this.province_id;
	}
	public void setProvince(String province){
		this.province=province;
	}
	public String getProvince(){
		return this.province;
	}
}
