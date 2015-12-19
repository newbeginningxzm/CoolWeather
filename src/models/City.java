package models;

public class City {
	private String province;
	private int province_id;
	private String city;
	private int city_id;
	public City(String province,int province_id,String city,int city_id){
		this.province=province;
		this.province_id=province_id;
		this.city=city;
		this.city_id=city_id;
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
}
