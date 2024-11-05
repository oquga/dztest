import Controllers.DeleteStudentHandler;
import Controllers.PostStudentHandler;
import Controllers.GetStudentHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/get", new GetStudentHandler());
        server.createContext("/post", new PostStudentHandler());
        server.createContext("/delete", new DeleteStudentHandler());

        // Start the server
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }
}