package com.test.provider;

import com.test.api.Request;
import com.test.api.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class UserRequestHandler implements Runnable {

    private Socket socket;

    private Map<String, Object> springBeanMap;

    public UserRequestHandler(Socket socket, Map<String, Object> springBeanMap) {
        this.socket = socket;
        this.springBeanMap = springBeanMap;
    }

    public void run() {
        try {
            //解析前端数据并解析
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Request request = (Request) objectInputStream.readObject();
            Object methodResult = sendMsg(request,springBeanMap);

            //返回解析的数据给前端
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Response response = new Response();
            response.setRetcode(200);
            response.setRetmsg("操作成功");
            response.setData(methodResult);
            objectOutputStream.writeObject(response);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析request，且根据springBeanMap获得spring bean，然后调用spring bean的服务，返回服务的值
    private Object sendMsg(Request request,Map<String,Object> springBeanMap) throws Exception{
        //1.request.getClassName()
        Object springBean= springBeanMap.get(request.getClassName());
        //2.request.getParams()
        Object[] params = request.getParams();
        Class[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        //3.request.getMethodName()
        Method method = springBean.getClass().getMethod(request.getMethodName(), paramTypes);
        return method.invoke(springBean, params);
    }
}
