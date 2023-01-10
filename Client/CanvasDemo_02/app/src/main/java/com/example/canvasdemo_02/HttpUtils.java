package com.example.canvasdemo_02;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

    private String PATH = "http://192.168.1.110:8080/register";
    private static URL url;
    public HttpUtils() {
        // TODO Auto-generated constructor stub
    }

    public String getPATH() {
        return PATH;
    }

    public  void setPATH(String PATH) {
        this.PATH = PATH;
    }

//    static{
//        try {
//            url = new URL(PATH);
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    /**
     * @param params 填写的url的参数
     * @param encode 字节编码
     * @return
     */
    public static String sendPostMessage(String urlPath,JSONObject params, String encode) throws Exception {
        //System.out.println(params);
        try {
            url = new URL(urlPath);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter out = null;
        try{
//        StringBuffer buffer = new StringBuffer();
//        try {//把请求的主体写入正文！！
//            if(params != null&&!params.isEmpty()){
//                //迭代器
//                for(Map.Entry<String, String> entry : params.entrySet()){
//                    buffer.append(entry.getKey()).append("=").
//                            append(URLEncoder.encode(entry.getValue(),encode)).
//                            append("&");
//                }
//            }
//            System.out.println(buffer.toString());
//            //删除最后一个字符&，多了一个;主体设置完毕
//            buffer.deleteCharAt(buffer.length()-1);
//            byte[] mydata = buffer.toString().getBytes();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setDoInput(true);//表示从服务器获取数据
            connection.setDoOutput(true);//表示向服务器写数据
            //获得上传信息的字节大小以及长度

            connection.setRequestMethod("POST");
            //是否使用缓存
            connection.setUseCaches(false);
            //表示设置请求体的类型是文本类型
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//            connection.setRequestProperty("Content-Length", String.valueOf(mydata.length));
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            //获得输出流，向服务器输出数据
            out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            //获得服务器响应的结果和状态码
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                return changeInputeStream(connection.getInputStream(),encode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "-2";
    }
    /**
     * 将一个输入流转换成字符串
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputeStream(InputStream inputStream, String encode) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if(inputStream != null){
            try {
                while((len = inputStream.read(data))!=-1){
                    data.toString();

                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(),encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else
            result = "null";
        return result;
    }

}
