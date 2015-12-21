package com.paragon.coolweather;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private TextView;
	private District selected_district;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		city=(TextView)findViewById(R.id.city);
		district=(TextView)findViewById(R.id.district);
		selected_district=(District)getIntent().getSerializableExtra("selected_district");
		if(selected_district!=null){
			city.setText(selected_district.getCity());
			district.setText(selected_district.getDistrit());
		}
	}
	private Map<String,String> queryWeather(String district_code){
		return JsonUtils.getWeatherData(district_code);
	}
	private void setWeather(Map<String,String> weather){
		Set<String> fieldName=new HashSet<String>();
		Set<String> weatherParams=null;
		try{
			Field[] fields=this.getClass().getDeclaredFields();
			for(int i=0;i<fields.length;i++){
				fieldName.add(fields[i].getName());
			}
			if(weather.size()>0)
			weatherParams=weather.keySet();
			for(String param:weatherParams){
				if(fieldName.contains(param)){
					Field f=this.getClass().getDeclaredField(param);
					f.set(this, weather.get(param));
				}

			}
		}catch(Exception e){
			
		}

		
	}
}

