package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {

	/**
	 * 解析和处理省级数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 解析和处理城市数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if(!TextUtils.isEmpty(response)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 解析和处理县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId
	 * @return
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if(!TextUtils.isEmpty(response)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 解析城市信息并保存到数据库
	 * @param file
	 * @param context
	 * @return
	 */
	public static void handleCityXml(final CoolWeatherDB coolWeatherDB, Context context) throws Exception {
		//final CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(context);
		Log.d(Tag, "XML:[" + XML + "]");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(new InputSource(context.getClass().getClassLoader().getResourceAsStream(XML)), new DefaultHandler() {
			private long provinceId = -1;
			private long cityId = -1;
			
			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				String name = attributes.getValue(1);
				String code = attributes.getValue(2);
				if("province".equals(qName)) {
					Province province = new Province();
					province.setProvinceName(name);
					province.setProvinceCode(code);
					
					provinceId = coolWeatherDB.saveProvince(province);
				}
				if("city".equals(qName)) {
					City city = new City();
					city.setCityName(name);
					city.setCityCode(code);
					city.setProvinceId((int)provinceId);
					
					cityId = coolWeatherDB.saveCity(city);
				}
				if("county".equals(qName)) {
					County county = new County();
					county.setCountyName(name);
					county.setCountyCode(code);
					county.setCityId((int)cityId);
					
					coolWeatherDB.saveCounty(county);
				}
			}
			
		});
	}
	
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
	 * @param content
	 * @param response
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将服务器返回的所有天气信息存储到SharePreferences文件中
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
	
	private static final String XML = "assets/city.xml";
	private static final String Tag = Utility.class.getName();
}
