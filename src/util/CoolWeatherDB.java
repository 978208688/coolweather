package util;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import db.CoolWeatherOpenHelper;

public class CoolWeatherDB {
	/*����һ������ģʽ��*/
	/*
	 * ���ݿ���
	 **/
  public static final String DB_NAME = "cool_weather";
  /*
   * �汾��
   **/
  public static final int VERSION = 1;
  private static CoolWeatherDB coolWeatherDB;
  
  private SQLiteDatabase db;//�����ݵĲ�������������
  
  /*
   * �����췽��˽�л�
   */
  
  private CoolWeatherDB(Context context){
	  CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
	  db = dbHelper.getWritableDatabase();//�õ���д�����ݿ�
	  
  }
  /*
   * ��ȡCoolWeatherDB��ʵ��
   */
  public synchronized static CoolWeatherDB getInstance(Context context){
	     
	    if(coolWeatherDB == null){
	    	return new CoolWeatherDB(context);
	    }
	    	
	    
	    return coolWeatherDB;
  }
  /*
   *��provinceʵ���洢�����ݿ�  
   */
  public void saveProvince(Province province){
      if(province != null){
    	  ContentValues values =new ContentValues();
    	  values.put("province_name", province.getProvinceName());
    	  values.put("province_code", province.getProvinceCode());
    	  
    	  db.insert("province", null, values);
      }
	
  }
  
  /*
   * �����ݿ��ж�ȡ����
   */
  public List<Province> loadProvince(){
	  List <Province>list = new ArrayList<Province>();
	 Cursor cursor = db.query("province", null, null, null, null, null, null);
	  
	  if(cursor.moveToFirst()){
		  do{ 
		
			  Province province = new Province();
			  province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			  province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			  province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			
			  list.add(province);
		  }while(cursor.moveToNext());
	  }
	  
	  if(cursor != null){
		  cursor.close();
	  }
	 
	  return list;
  }
  
  public void saveCity(City city){
	  if(city != null){
		  ContentValues values = new ContentValues();
		  values.put("city_name", city.getCityName());
		  values.put("city_code", city.getCityCode());
		  values.put("province_id",city.getProvinceId());
		  db.insert("city", null, values);
	  }
  }
  public List<City>loadCity(int provinceid){
	  List<City>list = new ArrayList<City>();

	  Cursor cursor = db.query("city", null,"province_id = ?", new String[]{String.valueOf(provinceid)}, null, null, null);
	  if(cursor.moveToFirst()){
		  do{
			   City city = new City();
			   
			   city.setCityName(cursor.getString (cursor.getColumnIndex("city_name")));
			   city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			   city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			   city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
			  
			   list.add(city);
			   
		  }while(cursor.moveToNext());
	  }
	  if(cursor != null){
		  cursor.close();
	  }
	  
	 return list;
  }
  
  public void saveCounty(County county){
	  if(county != null){
		  ContentValues values = new  ContentValues();
		  values.put("city_id", county.getCityId());//����ԭ�򣬽�city_id��һ�д�����countyID  values.put("city_id", county.getId());
		  values.put("county_name", county.getCountyName());
		  values.put("county_code", county.getCountyCode());
		  db.insert("county", null, values);
	  }
  }
  public List<County> loadCounty(int cityId){
	  
	  List<County> list = new ArrayList<County>();
	  Cursor cursor = db.query("county", null, "city_id= ?", new String[]{String.valueOf(cityId)}, null, null, null);
	 
	  if(cursor.moveToFirst()){
		  do{
			  
			  County county = new County();
			  county.setId(cursor.getInt(cursor.getColumnIndex("id")));
			  county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
			  county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
			  county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
			  list.add(county);
		  }while(cursor.moveToNext());
	  }
	  if(cursor != null){
		  cursor.close();
	  }
	  
	  return list;
  }
  
  
}
