package utils;

import java.io.*;
import java.util.*;
public class FileUtils {
	private static final String key="16e31731cde49ba74c5b4888bae69120";
	private static final String province_file="E:\\Learning Android\\WeatherData\\Provinces.txt";
	private static final String CHAR_SET="utf-8";
	public static void writeData(String path,String data){
		BufferedWriter write=null;
		try{
			FileOutputStream out=new FileOutputStream(path);
			write=new BufferedWriter(new OutputStreamWriter(out,CHAR_SET));
			write.write(data);
		}catch(Exception e){
			
		}finally{
			if(write!=null){
				try{
					write.close();
				}catch(Exception e){
					
				}
			}
		}
	}
	public static String readData(String path){
		StringBuilder data=new StringBuilder();
		BufferedReader in=null;
		try{
			in=new BufferedReader(new InputStreamReader(new FileInputStream(path),CHAR_SET));
			String line=null;
			while((line=in.readLine())!=null){
				data.append(line);
			}
		
		}catch(Exception e){
			
		}finally{
			try{
				if(in!=null)
					in.close();
			}catch(Exception e){
				
			}
			
		}
		data.deleteCharAt(0);
		return data.toString();
	}
}
