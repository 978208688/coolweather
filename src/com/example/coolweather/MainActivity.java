package com.example.coolweather;


 
import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import util.CoolWeatherDB;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener{
    
	
	
	private TextView titletext;
	private ProgressDialog progressdialog;
	private int cityId;
	private final int LEVEL_CITY =2;
	private final int LEVEL_COUNTY =3;
	private final int  LEVEL_PROVINCE =4;
	private  int LEVEL_CURRENT=0;
	private Province selectProvince;
	private City selectCity;
	int provinceid;
	private County selectCounty;
	CoolWeatherDB coolweatherdb;
	private ListView listview;
	String address;
	//存储省级信息
	private List<Province>provinceList = new ArrayList<Province>();
	//存储市级信息
	private List<City>cityList = new ArrayList<City>();
	//存储县级信息
	private List<County>countyList = new ArrayList<County>();
	
	ArrayAdapter<String> listAdapter ;
	
	private List<String>datalist = new ArrayList<String>();//如果想改变list数据
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		LEVEL_CURRENT  = LEVEL_PROVINCE;
		titletext = (TextView)findViewById(R.id.title_text);
		titletext.setText("中国");
		init();

	}

	private void init() {
		// TODO 自动生成的方法存根
		listview = (ListView)findViewById(R.id.listView1);
		listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,datalist);
		coolweatherdb = CoolWeatherDB.getInstance(MainActivity.this);
		listview.setOnItemClickListener(this);
		listview.setAdapter(listAdapter);
		
		queryProvince();
	}

	private void queryProvince(){//查询省级信息
		showProgressDialog();
		provinceList = coolweatherdb.loadProvince();//从数据库查询省信息
		if(provinceList.isEmpty()){//返回真代表数据库中没有信息， 我们需要从网上查询
			address ="http://www.weather.com.cn/data/list3/city.xml";
			queryFormService(address,"province");			
		}else{
			
			closeProgressDialog();
			for(Province province:provinceList){//从数据库中查询
				
				datalist.add(province.getProvinceName());
			}
			listAdapter.notifyDataSetChanged();	
             
		}
			
	}
	private void queryCity(String provinceCode ,int provinceId){//查询城市信息
		
		showProgressDialog();
		cityList = coolweatherdb.loadCity(provinceId);
		
		if(cityList.isEmpty()){
			address = "http://www.weather.com.cn/data/list3/city"+provinceCode+".xml";
			queryFormService(address,"city");
		}else{
			closeProgressDialog();
			titletext.setText(selectProvince.getProvinceName());
			datalist.clear();
			for(City city:cityList){//从数据库中查询
				
				datalist.add(city.getCityName());
				
			}
			
			listAdapter.notifyDataSetChanged();	

		}
		
	}
    
	private void queryCounty(String cityCode, int cityid){
		
		showProgressDialog();
		countyList = coolweatherdb.loadCounty(cityid);
		if(countyList.isEmpty()){//如果数据库中没有数据
			address = "http://www.weather.com.cn/data/list3/city"+cityCode+".xml";
			queryFormService(address, "county");
		}else{
			closeProgressDialog();
			titletext.setText(selectCity.getCityName());
			LEVEL_CURRENT = LEVEL_COUNTY;
			datalist.clear();
			for(County county : countyList){
				datalist.add(county.getCountyName());
				
			}
			datalist.remove(0);
			listAdapter.notifyDataSetChanged();
		}
		
		
	}
	private void queryFormService(String address,final String type) {
		// TODO 自动生成的方法存根
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				// TODO 自动生成的方法存根
			
					runOnUiThread(new Runnable() {
					    
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							boolean result = false;
						    if(type.equals("province")){
						        result = Utility.handleProvinceResponse(coolweatherdb, response);
						       if(result)
						    	   queryProvince();
		
							   
						    }else if(type.equals("city")){
						    	  result = Utility.handleCityResponse(coolweatherdb, response,provinceid);
						    	  
							       if(result)
							            queryCity(selectProvince.getProvinceCode(), provinceid);
							      
						  
							}
						    else if(type.equals("county")){
						    	
								     result = Utility.handleCountyResponse(coolweatherdb, response,cityId );
								     if(result){
								    	 queryCounty(selectCity.getCityCode(), cityId);
								     }
								    	
								     
							}
						        
						
					         
								
						}
					});
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				//Toast.makeText(MainActivity.this, "链接超时", Toast.LENGTH_LONG).show();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
	
        if(LEVEL_CURRENT ==LEVEL_PROVINCE){
        	
        	selectProvince = provinceList.get(position);
        	provinceid = selectProvince.getId();
        	queryCity(selectProvince.getProvinceCode(), provinceid);
        	LEVEL_CURRENT = LEVEL_CITY;
        	
        	return ;
        
        }
        
		if(LEVEL_CURRENT == LEVEL_CITY){//如果当前是显示的城市列表
			
			selectCity = cityList.get(position);//得到当前选中的城市
			cityId = selectCity.getId();
			queryCounty(selectCity.getCityCode(),cityId);
		     
		}
	}
	private void showProgressDialog(){
		if(progressdialog == null){
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("正在加载...");
			progressdialog.setCanceledOnTouchOutside(false);
		}
		progressdialog.show();
	}
	private void closeProgressDialog(){
		if(progressdialog != null){
			
			progressdialog.dismiss();
		}
	}

}
