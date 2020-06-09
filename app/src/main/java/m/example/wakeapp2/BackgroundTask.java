package m.example.wakeapp2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import m.example.wakeapp2.group_log_reg.LoginActivity;

public class BackgroundTask extends AsyncTask<String, String, String> {
    Context context;


    public BackgroundTask(Context ctx){
        this.context=ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String regURL1 = "http://www.projektbudzik.pl/php/phpuserreg.php";
        String logURL11 = "http://www.projektbudzik.pl/php/phpusergroup.php";
        String logURL1 = "http://www.projektbudzik.pl/php/phpuserlog.php";
        String regURL2 = "http://www.projektbudzik.pl/php/phpgroupreg.php";
        String logURL2 = "http://www.projektbudzik.pl/php/phpgrouplog.php";
        String alarmsURL = "http://www.projektbudzik.pl/php/phplistalarm.php";
        String deviceURL = "http://www.projektbudzik.pl/php/phplistdevice.php";
        String userURL = "http://www.projektbudzik.pl/php/phplistuser.php";
        String changeRole = "http://www.projektbudzik.pl/php/phpchangerole.php";
        String addDevice = "http://www.projektbudzik.pl/php/phpadddevice.php";
        String removeDevice = "http://www.projektbudzik.pl/php/phpremovedevice.php";
        String removeAlarm = "http://www.projektbudzik.pl/php/phpremovealarm.php";
        String addAlarm = "http://www.projektbudzik.pl/php/phpaddalarm.php";
        String editlarm = "http://www.projektbudzik.pl/php/phpeditalarm.php";
        String getAlarm = "http://www.projektbudzik.pl/php/phpaktualnebudzenie.php";

        if(type.equals("userreg")){
            String username = strings[1];
            String pass = strings[2];
            String email = strings[3];
            try {
                URL url = new URL(regURL1);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream= httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter( (outputStreamWriter));
                    String insert_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"
                            +URLEncoder.encode("pass", "UTF-8")+"="+ URLEncoder.encode(pass, "UTF-8")+"&"
                            +URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8");

                    bufferedWriter.write(insert_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String result;
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line= bufferedReader.readLine())!=null){
                        stringBuilder.append(line).append("\n");
                    }

                    result = stringBuilder.toString();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return  result;
                }catch (IOException e){
                    e.printStackTrace();
                }


            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("userlog"))        {

            String login_name = strings[1];
            String login_pass = strings[2];
            try {
                URL url = new URL(logURL1);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(login_name,"UTF-8")+"&"+
                            URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(login_pass,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,StandardCharsets.UTF_8));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("getAlarms"))        {

            String UserRole = strings[1];
            String Email = strings[2];
            String GroupId = strings[3];
            try {
                URL url = new URL(alarmsURL);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    String data = URLEncoder.encode("UserRole","UTF-8")+"="+URLEncoder.encode(UserRole,"UTF-8")+"&"+
                            URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+"&"+
                            URLEncoder.encode("GroupId","UTF-8")+"="+URLEncoder.encode(GroupId,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,StandardCharsets.UTF_8));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("grouplog"))        {
            String grouplogin_name = strings[1];
            String grouplogin_pass = strings[2];
            String grouplogin_email = strings[3];
            try {
                URL url = new URL(logURL2);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(grouplogin_name,"UTF-8")+"&"+
                            URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(grouplogin_pass,"UTF-8")+"&"+
                            URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(grouplogin_email,"UTF-8");



                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"UTF-8"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("userGroup"))        {
            String grouplogin_name = strings[1];


            try {
                URL url = new URL(logURL11);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(grouplogin_name,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"UTF-8"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("groupreg"))        {
            String group_name = strings[1];
            String group_pass = strings[2];
            String Email = strings[3];
            try {
                URL url = new URL(regURL2);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(group_name,"UTF-8")+"&"+
                            URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(group_pass,"UTF-8")+"&"+
                            URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"UTF-8"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

        }
        else if(type.equals("getDevices"))        {

            String UserRole = strings[1];
            String Email = strings[2];
            String GroupId = strings[3];
            try {
                URL url = new URL(deviceURL);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("UserRole","UTF-8")+"="+URLEncoder.encode(UserRole,"UTF-8")+"&"+
                            URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+"&"+
                            URLEncoder.encode("GroupId","UTF-8")+"="+URLEncoder.encode(GroupId,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("getUsers"))        {

            String GroupId = strings[1];
            try {
                URL url = new URL(userURL);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("GroupId","UTF-8")+"="+URLEncoder.encode(GroupId,"UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("changeRole"))        {
            String Email = strings[1];
            String UserRole = strings[2];
            try {
                URL url = new URL(changeRole);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+"&"+
                            URLEncoder.encode("UserRole","UTF-8")+"="+URLEncoder.encode(UserRole,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("addDevice"))        {
            String dName = strings[1];
            String dType = strings[2];
            String dMAC = strings[3];
            String dUser = strings[4];
            String dGroupId = strings[5];
            try {
                URL url = new URL(addDevice);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("dName","UTF-8")+"="+URLEncoder.encode(dName,"UTF-8")+"&"+
                            URLEncoder.encode("dType","UTF-8")+"="+URLEncoder.encode(dType,"UTF-8")+"&"+
                            URLEncoder.encode("dMAC","UTF-8")+"="+URLEncoder.encode(dMAC,"UTF-8")+"&"+
                            URLEncoder.encode("dUser","UTF-8")+"="+URLEncoder.encode(dUser,"UTF-8")+"&"+
                            URLEncoder.encode("dGroupId","UTF-8")+"="+URLEncoder.encode(dGroupId,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("removeDevice"))        {
            String dId = strings[1];
            try {
                URL url = new URL(removeDevice);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("dId","UTF-8")+"="+URLEncoder.encode(dId,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("removeAlarm"))        {
            String aId = strings[1];
            try {
                URL url = new URL(removeAlarm);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("aId","UTF-8")+"="+URLEncoder.encode(aId,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("addAlarm"))        {
            String aDateStart = strings[1];
            String aTime = strings[2];
            String aDeviceId = strings[3];
            String aCreate_by = strings[4];
            String aSequence = strings[5];
            String aDateEnd = strings[6];
            try {
                URL url = new URL(addAlarm);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("aDateStart","UTF-8")+"="+URLEncoder.encode(aDateStart,"UTF-8")+"&"+
                            URLEncoder.encode("aTime","UTF-8")+"="+URLEncoder.encode(aTime,"UTF-8")+"&"+
                            URLEncoder.encode("aDeviceId","UTF-8")+"="+URLEncoder.encode(aDeviceId,"UTF-8")+"&"+
                            URLEncoder.encode("aCreate_by","UTF-8")+"="+URLEncoder.encode(aCreate_by,"UTF-8")+"&"+
                            URLEncoder.encode("aSequence","UTF-8")+"="+URLEncoder.encode(aSequence,"UTF-8")+"&"+
                            URLEncoder.encode("aDateEnd","UTF-8")+"="+URLEncoder.encode(aDateEnd,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("addAlarms"))        {
            String aDateStart = strings[1];
            String aTime = strings[2];
            String aDeviceId = strings[3];
            String aCreate_by = strings[4];

            try {
                URL url = new URL(addAlarm);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("aDateStart","UTF-8")+"="+URLEncoder.encode(aDateStart,"UTF-8")+"&"+
                            URLEncoder.encode("aTime","UTF-8")+"="+URLEncoder.encode(aTime,"UTF-8")+"&"+
                            URLEncoder.encode("aDeviceId","UTF-8")+"="+URLEncoder.encode(aDeviceId,"UTF-8")+"&"+
                            URLEncoder.encode("aCreate_by","UTF-8")+"="+URLEncoder.encode(aCreate_by,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;
                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    Log.e("Alarm", response);
                    return response;

                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("editAlarm"))        {

            String aDateStart = strings[1];
            String aTime = strings[2];
            String aDeviceId = strings[3];
            String aId = strings[6];
            String aSequence = strings[4];
            String aDateEnd = strings[5];

            try {
                URL url = new URL(editlarm);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("aDateStart","UTF-8")+"="+URLEncoder.encode(aDateStart,"UTF-8")+"&"+
                            URLEncoder.encode("aTime","UTF-8")+"="+URLEncoder.encode(aTime,"UTF-8")+"&"+
                            URLEncoder.encode("aDeviceId","UTF-8")+"="+URLEncoder.encode(aDeviceId,"UTF-8")+"&"+
                            URLEncoder.encode("aId","UTF-8")+"="+URLEncoder.encode(aId,"UTF-8")+"&"+
                            URLEncoder.encode("aSequence","UTF-8")+"="+URLEncoder.encode(aSequence,"UTF-8")+"&"+
                            URLEncoder.encode("aDateEnd","UTF-8")+"="+URLEncoder.encode(aDateEnd,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;

                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("getAlarm"))        {

            String aMAC = strings[1];

            try {
                URL url = new URL(getAlarm);
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("aMAC","UTF-8")+"="+URLEncoder.encode(aMAC,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream,"iso-8859-1"));
                    String response = "" ;

                    String line;
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        response+= line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {

        String toastMsg = "";

        try {
            JSONObject jObj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            boolean error = jObj.getBoolean("error");

            if (!error) {
                String msg = jObj.getString("msg");
                toastMsg = msg;

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                toastMsg = errorMsg;
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();

        }
        if (toastMsg.length() > 3) {
            Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
        }
    }
}