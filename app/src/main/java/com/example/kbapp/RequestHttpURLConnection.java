//문자 받아온거 flask 서버에 보내는 코드 (post)
package com.example.kbapp;

import android.content.ContentValues;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.io.InputStream;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Map;
//import static com.group6.myapplication.MainActivity.email;
//import static com.group6.myapplication.MainActivity.password;

public class RequestHttpURLConnection {
    private static final String TAG2 = "RequestHttpURLConnection";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String request(String _url, String _params){

        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;
        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();

        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (_params == null)
            sbParams.append("null params");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else{
            /**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             * */


            try{
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                String json = "";

                // build jsonObject
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("message",_params);

                json=jsonObject.toString();


                // [2-1]. urlConn 설정.
                urlConn.setConnectTimeout(150000);
                urlConn.setReadTimeout(50000);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setDefaultUseCaches(false);
                urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                //urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                //urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencode");
                urlConn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
                urlConn.setRequestProperty("Accept", "application/json");

                //urlConn.setRequestProperty("apikey", ""); // ""안에 apikey를 입력
                //urlConn.setRequestProperty("message", _params); // ""안에 apikey를 입력


                // [2-2]. parameter 전달 및 데이터 읽어오기.
                String strParams = _params; //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
                OutputStream os = urlConn.getOutputStream();
                //os.write("{\"message\" : \"hello\"}".getBytes("UTF-8")); // 출력 스트림에 출력.
                os.write(json.getBytes("UTF-8"));
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.


                // [2-3]. 연결 요청 확인.
                // 실패 시 null을 리턴하고 메서드를 종료.

                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    return null;
                }
                // [2-4]. 읽어온 결과물 리턴.
                // 요청한 URL의 출력물을 BufferedReader로 받는다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                // 출력물의 라인과 그 합에 대한 변수.
                String line;
                String page = "";

                // 라인을 받아와 합친다.
                while ((line = reader.readLine()) != null){
                    page += line;
                }

                return page;

            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
        }


        return null;
    }
}


