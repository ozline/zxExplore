package ozline.zxexplore;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.CookieHandler;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    private Button button;
    private EditText username;
    private EditText password;
    private EditText en_password;
    private TextView text;
    zhixue zx = new zhixue();
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        button = findViewById(R.id.button);
        username = findViewById(R.id.Editid);
        password = findViewById(R.id.Editpw);
        en_password = findViewById(R.id.editpw);
        text = findViewById(R.id.textView);

        java.net.CookieManager manager = new java.net.CookieManager();
        CookieHandler.setDefault(manager);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    text.setText("正在验证权限....");
                    if(zx.dontallowkeep()){
                        finish();
                    }
                    text.setText("正在登录账号....");
                    zx.login(username.getText(),password.getText(),en_password.getText());
                    text.setText("正在拉取班级及考试信息.....");
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

    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

}
