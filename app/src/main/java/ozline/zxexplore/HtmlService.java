package ozline.zxexplore;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class HtmlService {

    public static String request_post(String path,String data,String cookies) throws  IOException{
        URL url;
        try {
            url = new URL(path);
        }catch (Exception e){
            System.out.println("Wrong!");
            return "-1";
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST"); //POST提交
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(20000); //20秒延时
        conn.setInstanceFollowRedirects(true);//自动重定向
        conn.setRequestProperty("Cookie",cookies);
        conn.setRequestProperty("Connection","Keep-Alive");
        conn.setRequestProperty("Charset","UTF-8");
        conn.connect();//开始连接
        DataOutputStream out = new DataOutputStream(conn.getOutputStream()); //传递数据
        out.writeBytes(data);
        out.flush();
        out.close(); //结束传递

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));//得到数据
        String line;
        String re="";
        while((line = reader.readLine())!=null){
            re=re+line;
        }

        reader.close(); //结束连接
        conn.disconnect();
        return re;
    }

    public static  String request_get(String path,String cookies) throws IOException {
        URL url;
        try {
            url = new URL(path);
        }catch (Exception e) {
            System.out.println("Wrong!");
            return "-1";
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(20000); //20秒超时
        conn.setRequestProperty("Cookie",cookies);
        conn.setDoInput(true);
        int code = conn.getResponseCode();
        if(code==200){
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while((len=is.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            is.close();
            return baos.toString();
        }else return "-1";
    }


}

//参考源码:https://www.cnblogs.com/yc-755909659/p/4193947.html