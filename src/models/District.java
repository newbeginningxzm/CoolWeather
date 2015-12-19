package models;

public class District {
	private String city;
	private int city_id;
	private String district;
	private String district_code;
	public District(String city,int city_id,String district,String district_code){
		this.city=city;
		this.city_id=city_id;
		this.district=district;
		this.district_code=district_code;
	}
	public void setCity(String city){
		this.city=city;
	}
	public String getCity(){
		return this.city;
	}
	public void setCityid(int city_id){
		this.city_id=city_id;
	}
	public int getCityid(){
		return this.city_id;
	}
	public void setDistrict(String district){
		this.district=district;
	}
	public String getDistrit(){
		return this.district;
	}
	public void setDistrictcode(String district_code){
		this.district_code=district_code;
	}
	public String getDistrictcode(){
		return this.district_code;
	}
}
