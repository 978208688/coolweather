package util;

import model.City;
import model.County;
import model.Province;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
  public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolweatherdb,
String response){
	  
	  if(!TextUtils.isEmpty(response)){
		  String [] allprovinces = response.split(",");
		  for(String it_province:allprovinces){
			 Province province = new Province();
			 String [] stringarr = it_province.split("\\|");
			 province.setProvinceCode(stringarr[0]);
			 province.setProvinceName(stringarr[1]);
			  //将数据写入数据库
			 
			 coolweatherdb.saveProvince(province);
			
		  }
		  return true;
		
	  }
	  return false;
  }
   
  public synchronized static boolean handleCityResponse(CoolWeatherDB coolweatherdb,String response,int provinceId){
	  if(!TextUtils.isEmpty(response)){
		  String [] allCity = response.split(",");
		  
		  for(String it_City : allCity){
			  City city = new City();
			  String [] Stringarr = it_City.split("\\|");
			  city.setCityCode(Stringarr[0]);
			  city.setCityName(Stringarr[1]);
			  city.setProvinceId(provinceId);
			  
			  //将数据写入数据库
			  coolweatherdb.saveCity(city);
		  }
		  return true;
		  
	  }
	  return false;
  }
  
  public synchronized static boolean handleCountyResponse(CoolWeatherDB coolweatherdb,String response,int cityId){
	  if(!TextUtils.isEmpty(response)){
		  String [] allCounty = response.split(",");
		  for(String it_County : allCounty){
			  County county = new County();
			  String [] Stringarr = it_County.split("\\|");
			  county.setCityId(cityId);
			  county.setCountyCode(Stringarr[0]);
			  county.setCountyName(Stringarr[1]);
			  //将数据写入数据库
			  coolweatherdb.saveCounty(county);
		  }
		  return true;
	  }
	  return false;
  }
}
