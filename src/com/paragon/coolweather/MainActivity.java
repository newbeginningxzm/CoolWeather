package com.paragon.coolweather;

import java.util.*;

import utils.HttpCallbackListener;
import utils.HttpUtilsWithListener;
import utils.JsonUtils;

import models.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private ListView list;
	private ArrayAdapter<String> adapter;
	private TextView title;
	private ProgressDialog progress;
	private List<String> dataList=new ArrayList<String>();
	private final int PROVINCE_LEVEL=0;
	private final int CITY_LEVEL=1;
	private final int DISTRICT_LEVEL=2;
	private int current_level=PROVINCE_LEVEL;
	private CoolWeatherDB db;
	private String source_data;
	private List<Province>provinces=new ArrayList<Province>();
	private List<City>cities=new ArrayList<City>();
	private List<District> districts=new ArrayList<District>();
	private Province selected_province;
	private City selected_city;
	private District selected_district;
	boolean district_selected=false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        title=(TextView)findViewById(R.id.title_text);
        list=(ListView)findViewById(R.id.list_view);
        db=CoolWeatherDB.getInstance(MainActivity.this);
        adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dataList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (current_level) {
				case PROVINCE_LEVEL:
					selected_province=provinces.get(position);
					queryCity();
					break;
				case CITY_LEVEL:
					selected_city=cities.get(position);
					queryDistrict();
					break;
				case DISTRICT_LEVEL:
					selected_district=districts.get(position);
					district_selected=true;
					Intent intent=new Intent(MainActivity.this,WeatherActivity.class);
//					intent.putExtra("district_code", selected_district.getDistrictcode());
//					intent.putExtra("district_name", selected_district.getDistrictcode());
					intent.putExtra("selected_district", selected_district);
					intent.putExtra("district_selected", district_selected);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
        queryProvinces();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void queryFromServer(final int type){
    	String site="http://v.juhe.cn/weather/citys";
    	String key="16e31731cde49ba74c5b4888bae69120";
    	Map<String ,Object>params=new HashMap<String,Object>();
    	params.put("key", key);
		showProgressDialog();
    	if(source_data==null||"".equals(source_data))
    		HttpUtilsWithListener.getData(site, params, "GET", new HttpCallbackListener() {
    	    	boolean result=false;
				@Override
				public void onFinish(String response) {
					// TODO Auto-generated method stub
					source_data=response;
					switch (type) {
					case PROVINCE_LEVEL:
						result=JsonUtils.JsonToProvince(JsonUtils.getProvincesData(source_data), db);
						break;
					case CITY_LEVEL:
						result=JsonUtils.JsonToCity(JsonUtils.getCitysData(source_data), db);
						break;
					case DISTRICT_LEVEL:
						final String districtData=JsonUtils.getDistrictsdataWitdID(source_data,selected_city.getCityid(),selected_city.getCity());
						result=JsonUtils.JsonToDistrict(districtData, db);
						break;
					default:
						break;
					}
					if(result){
						runOnUiThread(new Runnable() {
							public void run() {
								closeProgressDialog();
								switch (type) {
								case PROVINCE_LEVEL:
									queryProvinces();
									break;
								case CITY_LEVEL:
									queryCity();
									
									break;
								case DISTRICT_LEVEL:
									queryDistrict();
									break;
								default:
									break;
								}
							}
						});
						
					}
				}
				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(MainActivity.this, "加载失败，请检查数据连接！", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
    	else{
	    	boolean result=false;
	    	closeProgressDialog();
    		switch (type) {
			case PROVINCE_LEVEL:
				result=JsonUtils.JsonToProvince(JsonUtils.getProvincesData(source_data), db);
				break;
			case CITY_LEVEL:
				result=JsonUtils.JsonToCity(JsonUtils.getCitysData(source_data), db);
				break;
			case DISTRICT_LEVEL:
				final String Districtsdata=JsonUtils.getDistrictsdataWitdID(source_data,selected_city.getCityid(),selected_city.getCity());
				result=JsonUtils.JsonToDistrict(Districtsdata, db);
				final boolean r=result;
				break;
			default:
				break;
			}
    		if(result){
				switch (type) {
				case PROVINCE_LEVEL:
					queryProvinces();
					break;
				case CITY_LEVEL:
					queryCity();
					break;
				case DISTRICT_LEVEL:
					queryDistrict();
					break;
				default:
					break;
				}
			}
    	}
    }
    private void queryProvinces(){
    	provinces=db.getProvinces();
    	if(provinces.size()>0){
			dataList.clear();
    		for(Province province:provinces){
    			dataList.add(province.getProvince());
    		}
    		runOnUiThread(new Runnable() {
				@Override
				public void run() {
		    		adapter.notifyDataSetChanged();
		    		list.setSelection(0);
		    		title.setText("中国");
				}
			});
    		current_level=PROVINCE_LEVEL;
    	}else{
    		queryFromServer(PROVINCE_LEVEL);
    	}
    }
    private void queryCity(){
    	cities=db.getCitys(selected_province.getProvinceid());
    	if(cities.size()>0){
    		dataList.clear();
    		for(City city:cities){
    			dataList.add(city.getCity());
    		}
    		runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
		    		adapter.notifyDataSetChanged();
		    		list.setSelection(0);
		    		title.setText(selected_province.getProvince());
				}
			});
    		current_level=CITY_LEVEL;
    	}else{
    		queryFromServer(CITY_LEVEL);
    	}
    }
    private void queryDistrict(){
    	districts=db.getDistricts(selected_city.getCityid());
    	final StringBuilder d=new StringBuilder();
    	if(districts.size()>0){
//    		for(District district:districts){
//			d.append(district.getCity()+":"+district.getCityid()+":"+district.getDistrit()+":"+district.getDistrictcode());
//			d.append("\r\n");
//		}
    		dataList.clear();
    		for(District district:districts){
    			dataList.add(district.getDistrict());
    		}
    		runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					if(dataList.size()>0)
		    		adapter.notifyDataSetChanged();
		    		list.setSelection(0);
		    		title.setText(selected_city.getCity());				}
			});
    		current_level=DISTRICT_LEVEL;
//    		runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					title.setText(d.toString());
//				}
//			});
//    		current_level=DISTRICT_LEVEL;
    	}else{
    		queryFromServer(DISTRICT_LEVEL);
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
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	switch (current_level) {
		case PROVINCE_LEVEL:
			finish();
			break;
		case CITY_LEVEL:
//			if(provinces.size()>0){
//				dataList.clear();
//				for(Province province:provinces){
//					dataList.add(province.getProvince());
//				}
//				adapter.notifyDataSetChanged();
//			}else
				queryProvinces();
			break;
		case DISTRICT_LEVEL:
//			if(cities.size()>0){
//				dataList.clear();
//				for(City city:cities){
//					dataList.add(city.getCity());
//				}
//				adapter.notifyDataSetChanged();
//			}else
				queryCity();
			break;
		default:
			break;
		}
    }
}
