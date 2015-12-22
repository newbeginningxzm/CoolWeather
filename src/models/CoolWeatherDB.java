package models;

import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	private static final String DB_NAME="cool_weather";
	private static final String TABLE_PROVINCE="Province";
	private static final String TABLE_CITY="City";	
	private static final String TABLE_DISTRICT="District";	
	private Context mContext;
	private static final int VERSION=1;
	private static CoolWeatherDB coolweatherDB;
	private SQLiteDatabase db;
	private CoolWeatherDB(Context context){
		mContext=context;
		CoolWeatherOpenHelper helper=new CoolWeatherOpenHelper(mContext, DB_NAME, null, VERSION);
		db=helper.getWritableDatabase();
	}
	public static synchronized CoolWeatherDB getInstance(Context context){
		if(coolweatherDB==null)
		coolweatherDB=new CoolWeatherDB(context);
		return coolweatherDB;
	}
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues item=new ContentValues();
			item.put("province", province.getProvince());
			item.put("province_id", province.getProvinceid());
			db.insert(TABLE_PROVINCE, null, item);
		}
	}
	public List<Province> getProvinces(){
		List<Province> provinces=new ArrayList<Province>();
		Cursor cr=db.query(TABLE_PROVINCE, null, null, null, null, null, null);
		if(cr.moveToFirst()){
			do{
				String province_name=cr.getString(cr.getColumnIndex("province"));
				int province_id=cr.getInt(cr.getColumnIndex("province_id"));
				Province province=new Province(province_name, province_id);
				provinces.add(province);
			}while(cr.moveToNext());
		}
		return provinces;
	}
	public void saveCity(City city){
		if(city!=null){
			ContentValues item=new ContentValues();
			item.put("province", city.getProvince());
			item.put("province_id", city.getProvinceid());
			item.put("city", city.getCity());
			item.put("city_id", city.getCityid());	
			db.insert(TABLE_CITY, null, item);
		}
	}
	public List<City>getCitys(int province_id){
		List<City> citys = new ArrayList<City>();
		Cursor cr=db.query(TABLE_CITY, null, "province_id=?", new String[]{String.valueOf(province_id)}, null, null, null);
		if(cr.moveToFirst()){
			do{
				String province=cr.getString(cr.getColumnIndex("province"));
				int provinceid=province_id;
				String city_=cr.getString(cr.getColumnIndex("city"));
				int city_id=cr.getInt(cr.getColumnIndex("city_id"));
				City city=new City(province, provinceid, city_, city_id);
				citys.add(city);
			}while(cr.moveToNext());
		}
		return citys;
	}
	public void saveDistrict(District district){
		if(district!=null){
			ContentValues item=new ContentValues();
			item.put("city", district.getCity());
			item.put("city_id", district.getCityid());
			item.put("district", district.getDistrict());
			item.put("district_code", district.getDistrictcode());
			db.insert(TABLE_DISTRICT, null, item);	
		}
	}
	public List<District> getDistricts(int cityid){
		List<District> districts=new ArrayList<District>();
		Cursor cr=db.query(TABLE_DISTRICT, null, "city_id=?", new String[]{String.valueOf(cityid)}, null, null, null);
		if(cr.moveToFirst()){
			do{
				String city=cr.getString(cr.getColumnIndex("city"));
				int city_id=cityid;
				String district_=cr.getString(cr.getColumnIndex("district"));
				String district_code=cr.getString(cr.getColumnIndex("district_code"));
				District district=new District(city, city_id, district_, district_code);
				districts.add(district);
			}while(cr.moveToNext());
		}
		return districts;
	}
}
