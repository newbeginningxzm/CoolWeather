package com.paragon.coolweather;

import java.util.*;

import utils.HttpCallbackListener;
import utils.HttpUtilsWithListener;
import utils.JsonUtils;

import models.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private ListView list;
	private ArrayAdapter<String> adapter;
	private TextView title;
	private List<String> dataList=new ArrayList<String>();
	private int current_level=0;
	private final int PROVINCE_LEVEL=0;
	private final int CITY_LEVEL=1;
	private final int DISTRICT_LEVEL=2;
	private CoolWeatherDB db;
	private String source_data;
	private List<Province>provinces=new ArrayList<Province>();
	private List<City>cities=new ArrayList<City>();
	private List<District> districts=new ArrayList<District>();
	private Province selected_province;
	private City selected_city;
	private District selected_district;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
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
    	if(source_data==null)
    		showProgressDialog();
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
						result=JsonUtils.JsonToDistrict(JsonUtils.getDistrictsdata(source_data), db);
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
							result=JsonUtils.JsonToCity(JsonUtils.getCitysData(source_data), db);
							break;
						case DISTRICT_LEVEL:
							result=JsonUtils.JsonToDistrict(JsonUtils.getDistrictsdata(source_data), db);
							break;
						default:
							break;
						}
					}
				}
				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, "加载失败，请检查数据连接！", Toast.LENGTH_LONG).show();
						}
					});
				}
			});
    }
    private void queryProvinces(){
    	provinces=db.getProvinces();
    	if(provinces.size()>0){
			dataList.clear();
    		for(Province province:provinces){
    			dataList.add(province.getProvince());
    		}
    		adapter.notifyDataSetInvalidated();
    		list.setSelection(0);
    		title.setText("中国");
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
    		adapter.notifyDataSetInvalidated();
    		list.setSelection(0);
    		title.setText(selected_province.getProvince());
    		current_level=CITY_LEVEL;
    	}else{
    		queryFromServer(CITY_LEVEL);
    	}
    }
    private void queryDistrict(){
    	districts=db.getDistricts(selected_city.getCityid());
    	if(districts.size()>0){
    		for(District district:districts){
    			dataList.add(district.getDistrit());
    		}
    		adapter.notifyDataSetInvalidated();
    		list.setSelection(0);
    		title.setText(selected_city.getCity());
    		current_level=DISTRICT_LEVEL;
    	}else{
    		queryFromServer(DISTRICT_LEVEL);
    	}
    }
    private void showProgressDialog(){
    	
    }
}
