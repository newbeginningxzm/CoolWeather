package com.paragon.coolweather;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.HttpCallbackListener;
import utils.HttpUtils;
import utils.HttpUtilsWithListener;
import utils.JsonUtils;
import models.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener{
	private String distirct_code;
	private String district_name;
	private TextView city;
	private TextView district;
//	private TextView now_weather;
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
	private ProgressDialog progress;
	private Button refresh;
	private Button setDistrict;
	private District selected_district;
	private Map<String,String> weather;
	private String test;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		setViews();
		if(pref.getBoolean("district_selected", false)){
			selected_district=new District(pref.getString("city", ""),0, pref.getString("district", ""), pref.getString("district_code", ""));
			city.setText(selected_district.getCity());
			district.setText(selected_district.getDistrict());
			refreshWeather(selected_district.getDistrict());
		}else{
			Intent intent=new Intent(WeatherActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		refresh.setOnClickListener(this);
		setDistrict.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.select_district:
			Intent intent=new Intent(WeatherActivity.this,MainActivity.class);
			intent.putExtra("from_weather", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			if(selected_district!=null)
			refreshWeather(selected_district.getDistrictcode());
			break;
		default:
			break;
		}
	}
	private void refreshWeather(final String district_code){
		String weather_site="http://v.juhe.cn/weather/index";
		String key="16e31731cde49ba74c5b4888bae69120";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("cityname", district_code);
		params.put("key", key);
		showProgressDialog();
		HttpUtilsWithListener.getData(weather_site, params, "GET", new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				String data=response;
				if (data!=null){
					final Map<String,String> weather=JsonUtils.getWeatherData(response);
					saveWeather(weather);
					runOnUiThread(new Runnable() {
						public void run() {
							setWeather(weather);
							closeProgressDialog();
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						public void run() {
							weather=loadWeather();
							setWeather(weather);
							closeProgressDialog();
							Toast.makeText(WeatherActivity.this, "获取最新天气信息失败，请检查网络信息！", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(WeatherActivity.this, "获取最新天气信息失败，请检查网络信息！", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		closeProgressDialog();
	}
	private void setViews(){
		pref=PreferenceManager.getDefaultSharedPreferences(this);
		editor=pref.edit();
		refresh=(Button)findViewById(R.id.refresh_weather);
		setDistrict=(Button)findViewById(R.id.select_district);
		city=(TextView)findViewById(R.id.city);
		district=(TextView)findViewById(R.id.district);
//		now_weather=(TextView)findViewById(R.id.now_weather);
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
	private void saveWeather(Map<String,String> weather){
		editor.putStringSet("weather_info", weather.keySet());
		editor.putBoolean("weather_loaded", true);
		for(String key:weather.keySet()){
			editor.putString(key, weather.get(key));
		}
		editor.commit();
	}
	private Map<String,String> loadWeather(){
		boolean loaded=pref.getBoolean("weather_loaded", false);
		if(loaded){
			Map<String,String> weather=new HashMap<String,String>();
			Set<String> weather_info=pref.getStringSet("weather_info",null);
			for(String info:weather_info){
				weather.put(info, pref.getString(info, null));
			}
		}
		return weather;
	}
	private void setWeather(Map<String,String> weather){
		Set<String> fieldName=new HashSet<String>();
		Set<String> weatherParams=null;
		try{
			Field[] fields=this.getClass().getDeclaredFields();
			for(int i=0;i<fields.length;i++){
				fieldName.add(fields[i].getName());
			}
			if(weather.size()>0){
				weatherParams=weather.keySet();
				for(String param:weatherParams){
					if(fieldName.contains(param)){
						Field f=this.getClass().getDeclaredField(param);
						f.setAccessible(true);
						TextView text=(TextView)f.get(this);
						text.setText(weather.get(param));
					}	
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    private void showProgressDialog(){
				if(progress==null){
			    	progress=new ProgressDialog(this);
					progress.setCanceledOnTouchOutside(false);
			    	progress.setMessage("正在加载数据！");
				}
		    	progress.show();
    	
    }
    private void closeProgressDialog(){
    	if(progress!=null&&progress.isShowing())
    	progress.dismiss();
    }
}

