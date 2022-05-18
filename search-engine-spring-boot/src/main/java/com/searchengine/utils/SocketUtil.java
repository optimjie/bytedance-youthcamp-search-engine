package com.searchengine.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
/**
 * Java socket客户端
 * 与python同行调用深度学习模型
 *
 *
 */
public class SocketUtil {
    // 服务端IP地址
    public static final String HOST = "127.0.0.1";
    // 服务端端口
    public static final int PORT = 50000;

    /**
     * 文本转图片
     * @return
     */
    public List<String> sentence2Img(String sentence) throws Exception{
        List<String> urlList = new ArrayList<>();

        // 发送给服务端的参数 <状态码，数据>
        Map<String, String> paramMap = new HashMap<String, String>();
        //图片转换服务
        paramMap.put("code", "100");
        paramMap.put("sentence", sentence);
        System.out.println("sentence :"+paramMap.get("sentence"));
        // map转成json字符串，需要引入fastjson依赖
        String param = JSON.toJSONString(paramMap);

        // 接收服务端返回的信息
        String socketServerMsg = getSocketServerMsg(HOST, PORT, param);
        System.out.println("服务端信息：" + socketServerMsg);
        // json字符串转成map，需要引入fastjson依赖
        Map resultMap = (Map) JSON.parse(socketServerMsg);
//        for (Object map : resultMap.entrySet()) {
//            System.out.println(((Map.Entry) map).getKey() + ":" + ((Map.Entry) map).getValue());
//        }

        String code=String.valueOf(resultMap.get("code"));
        System.out.println("服务码  : "+code);
        int status=(int) resultMap.get("status");
        System.out.println("状态码  : "+status);

        switch (code) {
            //文字搜图服务
            case "100":
                System.out.println("文字搜图服务");
                switch (status) {
                    //成功
                    case 10:
                        System.out.println("执行成功");
                        break;
                    //失败
                    case 20:
                        System.out.println("执行失败");
                        break;
                    //默认
                    default:
                        System.out.println("默认执行");
                }
                break;
            default:
                System.out.println("无对应服务");

        }

        for (Object url : resultMap.values()) {
            if (!(url.toString().equals("10") || url.toString().equals("100"))){
                System.out.println(url.toString());
                urlList.add(url.toString());
            }
        }

        return urlList;
    }

    /**
     * 以图搜图
     * @return
     */
    public List<String> img2Img(String filepath) throws Exception{
        List<String> urlList = new ArrayList<>();

        // 发送给服务端的参数 <状态码，数据>
        Map<String, String> paramMap = new HashMap<String, String>();
        //图片转换服务
        paramMap.put("code", "200");
        paramMap.put("filepath", filepath);
        System.out.println("sentence :"+paramMap.get("filepath"));
        // map转成json字符串，需要引入fastjson依赖
        String param = JSON.toJSONString(paramMap);

        // 接收服务端返回的信息
        String socketServerMsg = getSocketServerMsg(HOST, PORT, param);
        System.out.println("服务端信息：" + socketServerMsg);
        // json字符串转成map，需要引入fastjson依赖
        Map resultMap = (Map) JSON.parse(socketServerMsg);
//        for (Object map : resultMap.entrySet()) {
//            System.out.println(((Map.Entry) map).getKey() + ":" + ((Map.Entry) map).getValue());
//        }

        String code=String.valueOf(resultMap.get("code"));
        System.out.println("服务码  : "+code);
        int status=(int) resultMap.get("status");
        System.out.println("状态码  : "+status);

        switch (code) {
            //以图搜图服务
            case "200":
                System.out.println("以图搜图服务");
                switch (status) {
                    //成功
                    case 10:
                        System.out.println("执行成功");
                        break;
                    //失败
                    case 20:
                        System.out.println("执行失败");
                        break;
                    //默认
                    default:
                        System.out.println("默认执行");
                }
                break;
            default:
                System.out.println("无对应服务");

        }

        for (Object url : resultMap.values()) {
            if (!(url.toString().equals("10") || url.toString().equals("200"))){
                System.out.println(url.toString());
                urlList.add(url.toString());
            }
        }
        return urlList;
    }


    public static String getSocketServerMsg(String host, int port, String message) throws Exception {
        // 与服务端建立连接
        Socket socket = new Socket(host, port);
        // 获得输出流
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter pWriter = new PrintWriter(outputStream);
        pWriter.write(message);
        pWriter.flush();
        // 通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();
        // 获得输入流
        InputStream inputStream = socket.getInputStream();

        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        inputStream.close();
        outputStream.close();
        socket.close();

        return sb.toString();
    }

}
