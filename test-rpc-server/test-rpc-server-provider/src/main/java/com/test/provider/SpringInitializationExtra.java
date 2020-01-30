package com.test.provider;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SpringInitializationExtra implements ApplicationContextAware, InitializingBean {

    ExecutorService service = Executors.newCachedThreadPool();

    //key:服务类如TeaImpl的接口的全类名，com.test.api.ITea    value:服务类对应的spring实例
    private Map<String,Object> springBeanMap=new ConcurrentHashMap<String, Object>();

    public void afterPropertiesSet() throws Exception {
        ServerSocket serverSocket = new ServerSocket(9090);
        while (true) {
            //一个serverSocket可accept出多个客户端Socket
            Socket socket = serverSocket.accept();
            //每次往线程池加一个客户socket对应的线程任务
            service.execute(new UserRequestHandler(socket,springBeanMap));
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //此处map字符串“teaImpl”将作为key，通过下列操作，将key替换为com.test.api.ITea
        Map<String,Object> map= applicationContext.getBeansWithAnnotation(RpcService.class);

        for(Map.Entry<String,Object> entry :map.entrySet()){
            String key=entry.getKey();//teaImpl
            Object springBean=entry.getValue();//spring bean

            RpcService rpcServiceAnnotation= springBean.getClass().getAnnotation(RpcService.class);
            String newKey=rpcServiceAnnotation.value().getName();
            springBeanMap.put(newKey,springBean);
        }

    }
}



