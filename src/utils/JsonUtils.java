package utils;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import models.City;
import models.CoolWeatherDB;
import models.District;
import models.Province;

public class JsonUtils {
	private static String site="http://v.juhe.cn/weather/citys";
	private static final String key="16e31731cde49ba74c5b4888bae69120";
	private static final String CHAR_SET="utf-8";
	private static ArrayList<String> location_known=new ArrayList<String>();
	private static HashMap<String, String>location_id=new HashMap<String, String>();
	public synchronized static boolean JsonToProvince(String data,CoolWeatherDB db){
		if(data!=null&&"".equals(data)){
			try{
				JSONArray provinces=new JSONObject(data).getJSONArray("provinces");
				for(int i=0;i<provinces.length();i++){
					JSONObject obj=provinces.getJSONObject(i);
					Province province=new Province(obj.getString("province"), obj.getInt("province_id"));
					if(db!=null)
						db.saveProvince(province);
				}
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	public synchronized static boolean JsonToCity(String data,CoolWeatherDB db){
		if(data!=null){
			try{
				JSONArray citys=new JSONObject(data).getJSONArray("citys");
				for(int i=0;i<citys.length();i++){
					JSONObject obj=citys.getJSONObject(i);
					City city=new City(obj.getString("province"), obj.getInt("province"), obj.getString("city"), obj.getInt("city_id"));
					if(db!=null)
						db.saveCity(city);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	public synchronized static boolean JsonToDistrict(String data,CoolWeatherDB db){
		if(data!=null){
			try{
				JSONArray citys=new JSONObject(data).getJSONArray("districts");
				for(int i=0;i<citys.length();i++){
					JSONObject obj=citys.getJSONObject(i);
					District district=new models.District(obj.getString("city"), obj.getInt("city_id"), obj.getString("district"), obj.getString("district_code"));
					if(db!=null)
						db.saveDistrict(district);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	public static String getProvincesData(String data){
		StringBuilder province=new StringBuilder();
		if(data!=null){
			int id=1;
			try{
				location_known.clear();
				JSONArray provinces=new JSONObject(data).getJSONArray("result");
				province.append("{\"provinces\":[");
				for(int i=0;i<provinces.length();i++){
					JSONObject obj=provinces.getJSONObject(i);
					String temp=obj.getString("province");
					if(location_known.contains(temp)){
						continue;
					}else{
						location_known.add(temp);
						province.append("{\"province\":");
						province.append("\"").append(temp).append("\",");
						province.append("\"province_id\":");
						province.append("\"").append(id++).append("\"},");
					}
				}
				province.deleteCharAt(province.length()-1);
				province.append("]}");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return province.toString();
	}
	public static String getCitysData(String data){
		StringBuilder citys=new StringBuilder();
		if(data!=null){
			int id=1;
			try{
				citys.append("{\"citys\":[");
				String provinces_data=getProvincesData(data);
				List<String> location_known=new ArrayList<String>();
				location_known.clear();
				location_id.clear();
				JSONArray provinces=new JSONObject(provinces_data).getJSONArray("provinces");
				initID(provinces, "province", "province_id", location_id);
				JSONArray objs=new JSONObject(data).getJSONArray("result");
				for(int i=0;i<objs.length();i++){
					JSONObject obj=objs.getJSONObject(i);
					String city_name=obj.getString("city");
					String province_name=obj.getString("province");
					if(location_known.contains(city_name)){
						continue;
					}else{
						location_known.add(city_name);
						citys.append("{\"province\":");
						citys.append("\"").append(province_name).append("\",");
						citys.append("\"province_id\":");
						citys.append("\"").append(location_id.get(province_name)).append("\",");
						citys.append("\"city\":");
						citys.append("\"").append(city_name).append("\",");
						citys.append("\"city_id\":");
						citys.append("\"").append(id++).append("\"},");
					}
				}
				citys.deleteCharAt(citys.length()-1);
				citys.append("]}");
				return data;
			}catch(Exception e){
				
			}
		}
		return citys.toString();
	}
	public static String getDistrictsdata(String data){
		StringBuilder districts=new StringBuilder();
		if(data!=null){
			try{
				location_known.clear();
				location_id.clear();
				String city_data=getCitysData(data);
				JSONArray citys=new JSONObject(city_data).getJSONArray("citys");
				initID(citys, "city", "city_id", location_id);
				JSONArray district=new JSONObject(data).getJSONArray("result");
				districts.append("{\"districts\":[");
				for(int i=0;i<district.length();i++){
					JSONObject obj=district.getJSONObject(i);
					String city_name=obj.getString("city");
					String district_name=obj.getString("district");
					String district_code=obj.getString("id");
					if(location_known.contains(district_name)){
						continue;
					}else{
						location_known.add(district_name);
						districts.append("{\"city\":");
						districts.append("\"").append(city_name).append("\",");
						districts.append("\"city_id\":");
						districts.append("\"").append(location_id.get(city_name)).append("\",");
						districts.append("\"district\":");
						districts.append("\"").append(district_name).append("\",");
						districts.append("\"district_code\":");
						districts.append("\"").append(district_code).append("\"},");
					}
				}
				districts.deleteCharAt(districts.length()-1);
				districts.append("]}");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return districts.toString();
	}
	public static void initID(JSONArray array,String item_name,String item_id,Map<String,String> result){
		result.clear();
		try{
			for(int i=0;i<array.length();i++){
				JSONObject temp=array.getJSONObject(i);
				String name=temp.getString(item_name);
				String id=temp.getString(item_id);
				result.put(name, id);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
