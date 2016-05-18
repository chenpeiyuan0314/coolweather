package com.coolweather.app.db;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.coolweather.app.util.Utility;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * Province建表语句
	 */
	public static final String CREATE_PROVINCE = "CREATE TABLE Province (" +
			"id INTEGER primary key autoincrement, " +
			"province_name text, " +
			"province_code text)";
	/**
	 * City建表语句
	 */
	public static final String CREATE_CITY = "CREATE TABLE City (" +
			"id INTEGER primary key autoincrement," +
			"city_name text," +
			"city_code text," +
			"province_id INTEGER)";
	/**
	 * County建表语句
	 */
	public static final String CREATE_COUNTY = "CREATE TABLE County (" +
			"id INTEGER primary key autoincrement," +
			"county_name text," +
			"county_code text," +
			"city_id INTEGER)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
		
		Log.d(Tag, "Create Database!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*
		Log.d(Tag, "Upgrade Database!");
		*/
	}

	private static final String Tag = CoolWeatherOpenHelper.class.getName();
	private Context context;
}
