package ozline.zxexplore;


import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;


public class table extends AppCompatActivity{
    static {
        System.loadLibrary("native-lib");
    }

    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;
    private Button button;
    private TextView text;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableview);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        button = findViewById(R.id.button2);
        text = findViewById(R.id.textView3);


        //考试
        data_list = new ArrayList<String>();
        for(int i=0;i < Integer.valueOf(zhixue.exam_name[0]);i++){
            data_list.add(zhixue.exam_name[i+1]);
        }
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);

        //班级
        data_list = new ArrayList<String>();
        for(int i=0;i < Integer.valueOf(zhixue.class_name[0]);i++){
            data_list.add(zhixue.class_name[i+1]);
        }
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arr_adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    zhixue.get_students(position+1);
                    data_list = new ArrayList<String>();
                    for(int i=0;i < Integer.valueOf(zhixue.student_name[0]);i++){
                        data_list.add(zhixue.student_name[i+1]);
                    }
                    arr_adapter= new ArrayAdapter<String>(table.this, android.R.layout.simple_spinner_item, data_list);
                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(arr_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    text.setText(zhixue.exam_name[Integer.valueOf(spinner.getSelectedItemPosition()+1)]+"\n\n"+ zhixue.class_name[Integer.valueOf(spinner2.getSelectedItemPosition()+1)]+"   "+zhixue.student_name[Integer.valueOf(spinner3.getSelectedItemPosition()+1)]+"\n\n"+ zhixue.get_mark1(zhixue.exam_id[Integer.valueOf(spinner.getSelectedItemPosition()+1)],zhixue.student_id[Integer.valueOf(spinner3.getSelectedItemPosition()+1)]));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
