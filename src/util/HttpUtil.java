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
			// TODO �Զ����ɵķ������
			HttpURLConnection connection = null;
			try {
				URL url = new URL(address);
				connection = (HttpURLConnection)url.openConnection();//����������������
				connection.setRequestMethod("GET");//GET��ʾ�ӷ�������ȡ���ݣ�post��ʾ�ϴ�����
				connection.setReadTimeout(80000);//��ȡ��ʱ
				connection.setConnectTimeout(8000);//���ӳ�ʱ
				InputStream in = connection.getInputStream();//�õ����������ص���
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
