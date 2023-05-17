package Controller;

public class Server {
    static Server server = new Server();

    public static Server getInstance(){
        if (server==null){
            server = new Server();
        }
        return server;
    }
}
