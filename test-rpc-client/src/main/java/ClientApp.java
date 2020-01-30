import com.test.api.ITea;

public class ClientApp {
    public static void main(String[] args) {
        ITea tea = new ServiceProxyFactory("localhost", 9090).getService(ITea.class);
        System.out.println(tea.produceTea(2));
    }
}





