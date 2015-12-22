package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONObject;

public class HttpUtilsWithListener {
	 public static final String DEF_CHATSET = "UTF-8";
	    public static final int DEF_CONN_TIMEOUT = 10000;
	    public static final int DEF_READ_TIMEOUT = 10000;
	    public static void getData(final String strUrl, final Map<String, Object> params,final String method,final HttpCallbackListener listener){

	    	new Thread(new Runnable() {
				@Override
				public void run() {
			    	HttpURLConnection conn = null;
			    	BufferedReader reader = null;
			        String rs = null;
			        String site=null;
				    String errcode=null;
					// TODO Auto-generated method stub
					 try {
				            StringBuffer sb = new StringBuffer();
				            if(method==null || method.equals("GET")){
				                site= strUrl+"?"+urlencode(params);
				            }
				            URL url = new URL(site);
				            conn = (HttpURLConnection) url.openConnection();
				            if(method==null || method.equals("GET")){
				                conn.setRequestMethod("GET");
				            }else{
				                conn.setRequestMethod("POST");
				                conn.setDoOutput(true);
				            }
				            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
				            conn.setReadTimeout(DEF_READ_TIMEOUT);
				            conn.setInstanceFollowRedirects(false);
				            conn.connect();
				            if (params!= null && method.equals("POST")) {
				                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
				                    out.writeBytes(urlencode(params));
				                }
				            }
				            InputStream is = conn.getInputStream();
				            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
				            String strRead = null;
				            while ((strRead = reader.readLine()) != null) {
				                sb.append(strRead);
				            }
				            rs = sb.toString();
				            if(listener!=null)
				            	listener.onFinish(rs);
				        } catch (IOException e) {
				            e.printStackTrace();
				            try{
				            	if(rs.toString()!=null)
				            	errcode=new JSONObject(rs.toString()).getString("error_code");
				            }catch(Exception ee){
				            	ee.printStackTrace();
				            }
				            if(listener!=null)
				            	listener.onError(e);
				        } finally {
				        	try{
					            if (reader != null) {
					            	
					                reader.close();
					            }
					            if (conn != null) {
					                conn.disconnect();
					            }
				        	}catch(IOException e){
				        		e.printStackTrace();
				        	}
				        }
				}
			}).start();
	    }
	 
	    //将map型转为请求参数型
	    public static String urlencode(Map<String, ?> data) {
	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<String, ?> i : data.entrySet()) {
	            try {
	                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
}
