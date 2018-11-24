package ozline.zxexplore;
//智学网数据处理

import android.text.Editable;
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
    private static String js_userid;
    private static String allowopen;
    private static String token;
    private static String js_token;
    private static String childrenID;

    public static String userid;
    public static String[] class_name; //[0]=数量
    public static String[] class_id; //[0]=数量
    public static String[] exam_name;
    public static String[] exam_id;
    public static String[] student_name;
    public static String[] student_id;

    //智学网cookie

    public static boolean dontallowkeep() throws IOException {
        allowopen = HtmlService.request_get("https://www.ozlinex.com/zxcheck.txt",zx_cookie);
        Log.i("内部匿名类",allowopen);
        if(!allowopen.contains("OK")){
            return true;
        }
        return false;
    }

    public static boolean login(Editable username, Editable passwd, Editable en_passwd) throws JSONException,IOException{
        String re;
        String lt;
        String execution;
        String st;

        js_userid = HtmlService.request_post("http://www.zhixue.com/weakPwdLogin/?from=web_login","loginName="+username+"&password="+passwd+"&code=","");
        JSONObject jsonObject = new JSONObject(js_userid);
        userid = jsonObject.getString("data");
        Log.i("智学网登录","用户ID:"+userid); //userid
        re = HtmlService.request_post("http://www.zhixue.com/login.jsp","decPwd=1&userId="+userid+"&password="+passwd+"&backUrl=./login.html&nextpage=./redirectIndex","");

        re = HtmlService.request_get("http://open.changyan.com/sso/login/?sso_from=zhixuesso"+
                "&service=http://www.zhixue.com:80/ssoservice.jsp"+
                "&callback=jQuery191035885313783137995_1537716415173"+
                "&_=1537716415174","");
        re = re.replaceAll(" ","");
        lt = getSubString(re, "\"lt\":\"", "\",\"exec"); //lt
        execution = getSubString(re,"\"execution\":\"", "\"}"); //execution
        Log.i("智学网登录","lt值:"+lt+"\n"+"execution值:"+execution);
        re = HtmlService.request_get("http://open.changyan.com/sso/login/?sso_from=zhixuesso"+
                "&service=http://www.zhixue.com:80/ssoservice.jsp"+
                "&callback=jQuery191035885313783137995_1537716415173"+
                "&username="+userid+"&password="+passwd+"&sourceappname=tkyh,tkyh&key=id&_eventId=submit&lt="+lt+"&execution="+execution+
                "&_=1537716415175","");
        re = re.replaceAll(" ","");
        st = getSubString(re,"\"st\":\"", "\"}");
        Log.i("智学网登录","st值:"+st);
        re = HtmlService.request_post("http://www.zhixue.com/ssoservice.jsp","ation=login"+
                "&username="+userid+
                "&password="+passwd+
                "&ticket="+st,"");
        re=re.replaceAll(" ","");
        if(re.indexOf("success")!=-1){
            //此处是智学网APP弱密码登录，PASSWORD是加密后的密码，不知道有什么方法解决，估计只能抓包了
            //好在密码不会怎么变动哈哈哈哈
            js_token=HtmlService.request_post("http://www.zhixue.com/container/app/weakCheckLogin?",
                    "password="+en_passwd+"&loginName="+username+"&description=%7B%27encrypt%27%3A%5B%27password%27%5D%7D&deviceMac=50%3A8F%3A4C%3AFA%3AAA%3A3B&deviceId=866146033672761","");
            JSONObject jtoken = new JSONObject(js_token);
            JSONObject jstoken = jtoken.getJSONObject("result");
            token=jstoken.getString("token");
            childrenID=jstoken.getString("id");
            Log.i("智学网登录","hsy的token:"+token);
            Log.i("智学网登录","登录成功!");
            return true;
            //0ddb91708f0c52e8cf55d84f hsy
            //08c185208d0e57efc854 ozline
        }else{
            Log.i("智学网登录","登录失败!");
            Log.i("智学网登录","返回数据:"+re);
            return false;
        }
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
    //get_mark是智学网网页端的API，现在已经添加验证了，只能查询本班及已添加好友的成绩，估摸着要作废

    public static String get_mark1(String examid,String studentid) throws JSONException,IOException{
        js_mark = HtmlService.request_get("http://app.zhixue.com/study/social/getGuessScore?&examId="+examid+"&guessUserId="+studentid+"&token="+token+"&childrenId="+childrenID,"");
        JSONObject frist = new JSONObject(js_mark);
        JSONObject second = frist.getJSONObject("result");
        JSONArray thrid = second.getJSONArray("studentPKDTOs");
        JSONObject student = thrid.getJSONObject(1);
        JSONArray marks = student.getJSONArray("subjectList");

        //JSONArray first = new JSONArray(js_mark);
        //JSONObject second  = first.getJSONObject(1); //取出第二个人的数据
        //JSONArray marks = second.getJSONArray("subjectList");
        String marklist = "";
        for(int i=0;i< marks.length();i++){ //科目数量
            JSONObject mark = marks.getJSONObject(i);
            marklist=marklist+mark.getString("subjectName")+"   "+mark.getString("score")+"\n";

        }
        return marklist;
    }


    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    public static String remove(String resource,char ch)
    {
        StringBuffer buffer=new StringBuffer();
        int position=0;
        char currentChar;

        while(position<resource.length())
        {
            currentChar=resource.charAt(position++);
            if(currentChar!=ch) buffer.append(currentChar); } return buffer.toString();
    }

}

