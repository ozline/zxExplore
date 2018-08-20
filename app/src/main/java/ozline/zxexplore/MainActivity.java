package ozline.zxexplore;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    private WebView webview;
    private Button button;
    private Button button2;
    private EditText username;
    private EditText password;
    zhixue zx = new zhixue();
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //---浏览器---
        webview =  findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setUserAgentString("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webview.loadUrl("http://www.zhixue.com");
        webview.setWebViewClient(new WebViewClient(){
                @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
            }
        });

        //---按钮---
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button3);
        username = findViewById(R.id.Editid);
        password = findViewById(R.id.Editpw);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie("www.zhixue.com");
                zx.setZx_cookie(cookieStr);
                try {
                    if(zx.get_class()  && zx.get_exam()){
                        Intent intent=new Intent(MainActivity.this, table.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        MainActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String js = "javascript:document.getElementById('txtUserName').value = '" +username.getText()+
                        "';document.getElementById('txtPassword').value='" +password.getText()+
                        "';document.getElementById('signup_button').click();"; //加入默认值
                webview.loadUrl(js);
            }
        });

    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
