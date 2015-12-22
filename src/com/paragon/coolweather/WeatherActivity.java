package com.paragon.coolweather;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.HttpUtils;
import utils.JsonUtils;
import models.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	private String distirct_code;
	private String district_name;
	private TextView city;
	private TextView district;
	private TextView now_temp;
	private TextView now_wind;
	private TextView now_humidity;
	private TextView refresh_time;
	private TextView today_hitemp;
	private TextView today_lowtemp;
	private TextView today_weather;
	private TextView tomorrow_hitemp;
	private TextView tomorrow_lowtemp;
	private TextView tomorrow_weather;
	private TextView tomorrow_title;
	private TextView dat_hitemp;
	private TextView dat_lowtemp;
	private TextView dat_weather;
	private TextView dat_title;
	private District selected_district;
	private Map<String,String> weather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		setTextViews();
		selected_district=(District)getIntent().getSerializableExtra("selected_district");
		if(selected_district!=null){
			city.setText(selected_district.getCity());
			district.setText(selected_district.getDistrict());
//			weather=JsonUtils.getWeatherData(selected_district.getDistrictcode());
			district.setText(JsonUtils.getWeather(selected_district.getDistrictcode()));
//			setWeather(weather);
		}
	}
	private Map<String,String> queryWeather(String district_code){
		return JsonUtils.getWeatherData(district_code);
	}
	private void setTextViews(){
		city=(TextView)findViewById(R.id.city);
		district=(TextView)findViewById(R.id.district);
		now_temp=(TextView)findViewById(R.id.now_temp);
		now_wind=(TextView)findViewById(R.id.now_wind);
		now_humidity=(TextView)findViewById(R.id.now_humidity);
		refresh_time=(TextView)findViewById(R.id.refresh_time);
		today_hitemp=(TextView)findViewById(R.id.today_hitemp);
		today_lowtemp=(TextView)findViewById(R.id.today_lowtemp);
		today_weather=(TextView)findViewById(R.id.today_weather);
		tomorrow_hitemp=(TextView)findViewById(R.id.tomorrow_hitemp);
		tomorrow_lowtemp=(TextView)findViewById(R.id.tomorrow_lowtemp);
		tomorrow_weather=(TextView)findViewById(R.id.tomorrow_weather);
		tomorrow_title=(TextView)findViewById(R.id.tomorrow_title);
		dat_hitemp=(TextView)findViewById(R.id.dat_hitemp);
		dat_lowtemp=(TextView)findViewById(R.id.dat_lowtemp);
		dat_weather=(TextView)findViewById(R.id.dat_weather);
		dat_title=(TextView)findViewById(R.id.dat_title);
	}
//	private void setWeather(Map<String,String> weather){
//		Set<String> fieldName=new HashSet<String>();
//		Set<String> weatherParams=null;
//		try{
//			Field[] fields=this.getClass().getDeclaredFields();
//			for(int i=0;i<fields.length;i++){
//				fieldName.add(fields[i].getName());
//			}
//			if(weather.size()>0){
//				weatherParams=weather.keySet();
//				for(String param:weatherParams){
//					if(fieldName.contains(param)){
//						Field f=this.getClass().getDeclaredField(param);
//						f.setAccessible(true);
//						Method m=f.getClass().getDeclaredMethod("setText", new Class[]{CharSequence.class});
//						m.setAccessible(true);
//						m.invoke(this,weather.get(param));
//					}	
//				}	
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
}

