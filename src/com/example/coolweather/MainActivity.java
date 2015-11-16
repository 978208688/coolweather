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
	//�洢ʡ����Ϣ
	private List<Province>provinceList = new ArrayList<Province>();
	//�洢�м���Ϣ
	private List<City>cityList = new ArrayList<City>();
	//�洢�ؼ���Ϣ
	private List<County>countyList = new ArrayList<County>();
	
	ArrayAdapter<String> listAdapter ;
	
	private List<String>datalist = new ArrayList<String>();//�����ı�list����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		LEVEL_CURRENT  = LEVEL_PROVINCE;
		titletext = (TextView)findViewById(R.id.title_text);
		titletext.setText("�й�");
		init();

	}

	private void init() {
		// TODO �Զ����ɵķ������
		listview = (ListView)findViewById(R.id.listView1);
		listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,datalist);
		coolweatherdb = CoolWeatherDB.getInstance(MainActivity.this);
		listview.setOnItemClickListener(this);
		listview.setAdapter(listAdapter);
		
		queryProvince();
	}

	private void queryProvince(){//��ѯʡ����Ϣ
		showProgressDialog();
		provinceList = coolweatherdb.loadProvince();//�����ݿ��ѯʡ��Ϣ
		if(provinceList.isEmpty()){//������������ݿ���û����Ϣ�� ������Ҫ�����ϲ�ѯ
			address ="http://www.weather.com.cn/data/list3/city.xml";
			queryFormService(address,"province");			
		}else{
			
			closeProgressDialog();
			for(Province province:provinceList){//�����ݿ��в�ѯ
				
				datalist.add(province.getProvinceName());
			}
			listAdapter.notifyDataSetChanged();	
             
		}
			
	}
	private void queryCity(String provinceCode ,int provinceId){//��ѯ������Ϣ
		
		showProgressDialog();
		cityList = coolweatherdb.loadCity(provinceId);
		
		if(cityList.isEmpty()){
			address = "http://www.weather.com.cn/data/list3/city"+provinceCode+".xml";
			queryFormService(address,"city");
		}else{
			closeProgressDialog();
			titletext.setText(selectProvince.getProvinceName());
			datalist.clear();
			for(City city:cityList){//�����ݿ��в�ѯ
				
				datalist.add(city.getCityName());
				
			}
			
			listAdapter.notifyDataSetChanged();	

		}
		
	}
    
	private void queryCounty(String cityCode, int cityid){
		
		showProgressDialog();
		countyList = coolweatherdb.loadCounty(cityid);
		if(countyList.isEmpty()){//������ݿ���û������
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
		// TODO �Զ����ɵķ������
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				// TODO �Զ����ɵķ������
			
					runOnUiThread(new Runnable() {
					    
						@Override
						public void run() {
							// TODO �Զ����ɵķ������
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
				// TODO �Զ����ɵķ������
				//Toast.makeText(MainActivity.this, "���ӳ�ʱ", Toast.LENGTH_LONG).show();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO �Զ����ɵķ������
						closeProgressDialog();
						Toast.makeText(MainActivity.this, "����ʧ��", Toast.LENGTH_LONG).show();
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
		// TODO �Զ����ɵķ������
	
        if(LEVEL_CURRENT ==LEVEL_PROVINCE){
        	
        	selectProvince = provinceList.get(position);
        	provinceid = selectProvince.getId();
        	queryCity(selectProvince.getProvinceCode(), provinceid);
        	LEVEL_CURRENT = LEVEL_CITY;
        	
        	return ;
        
        }
        
		if(LEVEL_CURRENT == LEVEL_CITY){//�����ǰ����ʾ�ĳ����б�
			
			selectCity = cityList.get(position);//�õ���ǰѡ�еĳ���
			cityId = selectCity.getId();
			queryCounty(selectCity.getCityCode(),cityId);
		     
		}
	}
	private void showProgressDialog(){
		if(progressdialog == null){
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("���ڼ���...");
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
