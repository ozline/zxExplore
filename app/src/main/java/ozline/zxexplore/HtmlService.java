package ozline.zxexplore;
import android.os.StrictMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HtmlService {
    public static  String request_get(String path,String cookies) throws IOException {
        URL url;
        try {
            url = new URL(path);
        }catch (Exception e) {
            System.out.println("Wrong!");
            return "-1";
        }
        System.out.println("OK!");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); //5秒超时
        conn.setRequestProperty("Cookie",cookies);
        conn.setDoInput(true);
        int code = conn.getResponseCode();
        if(code==200){
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while((len=is.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            is.close();
            return baos.toString();
        }else return "-1";
    }

    public static String getHtml(String path) throws Exception {
        // 通过网络地址创建URL对象
        URL url = new URL(path);
        // 根据URL
        // 打开连接，URL.openConnection函数会根据URL的类型，返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设定URL的请求类别，有POST、GET 两类
        conn.setRequestMethod("GET");
        //设置从主机读取数据超时（单位：毫秒）
        conn.setConnectTimeout(5000);
        //设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(5000);
        // 通过打开的连接读取的输入流,获取html数据
        InputStream inStream = conn.getInputStream();
        // 得到html的二进制数据
        byte[] data = readInputStream(inStream);
        // 是用指定的字符集解码指定的字节数组构造一个新的字符串
        String html = new String(data, "utf-8");
        return html;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}

//参考源码:https://www.cnblogs.com/yc-755909659/p/4193947.html