package com.coolweather.app.util;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
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
	
	private static final String XML = "assets/city.xml";
	private static final String Tag = Utility.class.getName();
}
