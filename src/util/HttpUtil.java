package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
   public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
	   new Thread (new Runnable(){

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			HttpURLConnection connection = null;
			try {
				URL url = new URL(address);
				connection = (HttpURLConnection)url.openConnection();//将服务器发送请求
				connection.setRequestMethod("GET");//GET表示从服务器获取数据，post表示上传数据
				connection.setReadTimeout(80000);//读取超时
				connection.setConnectTimeout(8000);//连接超时
				InputStream in = connection.getInputStream();//得到服务器返回的流
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuilder reponse = new StringBuilder();
			    String line;
				while((line=br.readLine()) != null){
					
					reponse.append(line);
				}
				if(listener != null){
				
					listener.onFinish(reponse.toString());
				}
			} catch (Exception e) {
				// TODO: handle exception
				if(listener != null){
					listener.onError(e);
				}
			}
			finally{
				if(connection != null){
					connection.disconnect();
				}
			
			}
		}
		   
	   }).start();
	   
	   
   }
}
