package ozline.zxexplore;
//智学网数据处理

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

import static java.lang.String.valueOf;

public class zhixue {

    private static String zx_cookie; //用户Cookie cookie刷新时间没有研究过
    private static String js_classid; //原始班级ID
    private static String js_examid; //原始考试I
    private static String js_studentsid;
    private static String js_mark;

    public static String[] class_name; //[0]=数量
    public static String[] class_id; //[0]=数量
    public static String[] exam_name;
    public static String[] exam_id;
    public static String[] student_name;
    public static String[] student_id;

    //智学网cookie
    public static void setZx_cookie(String a){ //设定Cookie
        zhixue.zx_cookie=a;
    }

    public static boolean get_class() throws JSONException, IOException { //初始化班级数据
        js_classid = HtmlService.request_get("http://www.zhixue.com/zhixuebao/zhixuebao/friendmanage/", zx_cookie); //获取数据+JSON初始化
        JSONObject jsonObject = new JSONObject(js_classid);
        JSONArray classids = jsonObject.getJSONArray("clazzs");

        class_name = new String[classids.length()+1]; //固定大小
        class_id = new String[classids.length()+1];


        class_name[0]= valueOf(classids.length());
        class_id[0]= valueOf(classids.length());
        for(int i=0;i< classids.length();i++){
            JSONObject classid = classids.getJSONObject(i);
            class_name[i+1]=classid.getString("name"); //保存数据
            class_id[i+1]=classid.getString("id");
        }

        if(Integer.valueOf(class_id[0])>0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean get_exam() throws JSONException,IOException{ //初始化考试数据  只读取前10场考试
        js_examid= HtmlService.request_get("http://www.zhixue.com/zhixuebao/zhixuebao/main/getUserExamList/?pageIndex=0&actualPosition=0", zx_cookie); //获取数据+JSON初始化
        JSONObject jsonObject = new JSONObject(js_examid);
        JSONArray examids = jsonObject.getJSONArray("examList");

        exam_name = new String[examids.length()+1]; //固定大小
        exam_id = new String[examids.length()+1];

        exam_name[0]= valueOf(examids.length());
        exam_id[0]= valueOf(examids.length());

        for(int i=0;i< examids.length();i++){
            JSONObject examid = examids.getJSONObject(i);
            exam_name[i+1]=examid.getString("examName"); //保存数据
            exam_id[i+1]=examid.getString("examId");
        }

        if(Integer.valueOf(exam_id[0])>0){
            return true;
        }else{
            return false;
        }

    }

    public static boolean get_students(int id) throws JSONException,IOException{ //获取学生数据，需要提供班级的序列号
        js_studentsid= HtmlService.request_get("http://www.zhixue.com/zhixuebao/zhixuebao/getClassStudent/?classId="+class_id[id]+"&d=1516895547816", zx_cookie);
        JSONArray studentids = new JSONArray(js_studentsid);

        Log.i("内部监听", String.valueOf(studentids.length()));

        student_name = new String[studentids.length()+1]; //固定大小
        student_id = new String[studentids.length()+1];

        student_name[0]= valueOf(studentids.length());
        student_id[0]= valueOf(studentids.length());

        for(int i=0;i< studentids.length();i++){
            JSONObject studentid = studentids.getJSONObject(i);
            student_name[i+1]=studentid.getString("name"); //保存数据
            student_id[i+1]=studentid.getString("id");
        }

        if(Integer.valueOf(student_id[0])>0){
            return true;
        }else{
            return false;
        }

    }

    public static String get_mark(String examid,String studentid) throws JSONException,IOException{
        js_mark = HtmlService.request_get("http://www.zhixue.com/zhixuebao/zhixuebao/personal/studentPkData/?examId="+examid+"&pkId="+studentid,zx_cookie);
        JSONArray first = new JSONArray(js_mark);
        JSONObject second  = first.getJSONObject(1); //取出第二个人的数据
        JSONArray marks = second.getJSONArray("subjectList");
        String marklist = "";
        for(int i=0;i< marks.length();i++){ //科目数量
            JSONObject mark = marks.getJSONObject(i);
            marklist=marklist+mark.getString("subjectName")+"   "+mark.getString("score")+"\n";

        }
        return marklist;
    }
}