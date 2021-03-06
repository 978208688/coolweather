package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
    public static final String CREATE_PROVINCE = "create table province("
    /******创建省表******/                         +"id integer primary key autoincrement,"
    		                                     +"province_name text,"+"province_code text)";
    public static final String CREATE_CITY = "create table city("
     /*****创建城市表*****/		              +"id integer primary key autoincrement,"
    		              					  +"city_name text ,city_code text,province_id integer)";
    public static final String CREATE_COUNTY ="create table county("+
    /******创建县表******/		              	  "id integer primary key autoincrement,"
    		              					   +"county_name text, county_code text, city_id integer)";
    
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自动生成的方法存根
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根
		
	}

}
