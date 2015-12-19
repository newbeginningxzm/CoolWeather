package models;

import java.util.*;
import java.util.Map;

import utils.HttpUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	private static final String SOURCE="http://v.juhe.cn/weather/citys";
	private static final String key="16e31731cde49ba74c5b4888bae69120";
	private static final String CREATE_PROVINCE="create table Province(" +
			"id integer primary key autoincrement" +
			"province text"+
			"province_id integer";
	private static final String CREATE_CITY="create table City" +
			"id integer primary key autoincrement" +
			"province text"+
			"province_id integer"+
			"city text" +
			"city_id integer";
	private static final String CREATE_COUNTRY="create table District" +
			"id integer primary key autoincrement" +
			"city text" +
			"city_id integer"+
			"district text" +
			"district_code text" ;
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
