import com.test.api.Request;
import com.test.api.Response;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ServiceProxyFactory implements InvocationHandler {
    private String host;
    private int port;

    public ServiceProxyFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = new Socket(host, port);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        Request request = new Request();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        objectOutputStream.writeObject(request);

        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Response response = (Response) objectInputStream.readObject();
        return response.getData();
    }
}
