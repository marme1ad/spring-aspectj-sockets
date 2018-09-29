package com.stackoverflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Controller
public class MyController {

    private static Logger logger = LoggerFactory.getLogger(MyController.class);

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    static {
        try {
            serverSocket = new ServerSocket(18080);
            clientSocket = new Socket("127.0.0.1", 18080);
        } catch (IOException e) {
            logger.error("Error occurred during serverSocket init!", e);
        }
    }

    @Autowired
    MyService helloService;

    @RequestMapping(value = "/client/{message}", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity client(@PathVariable("message") String message) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println(message);

        return ResponseEntity.ok("Message sent successfully: " + message);
    }

    @RequestMapping(value = "/server", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity server() {
        try {
            serverSocket.setSoTimeout(1000);
            Socket socket = serverSocket.accept();
            new ServerThread(socket).start();
        } catch (IOException e) {
            // during initial acceptance timeout exception will be thrown
            logger.warn("Exception during socket acceptance", e);
        }

        return ResponseEntity.ok("Hello, World!");
    }

    private class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(input), StandardCharsets.UTF_8));
                PrintWriter writer = new PrintWriter(output, true);

                while (true) {
                    String line = reader.readLine();
                    helloService.doCall(line);
                    writer.println("Socket event: " + line);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

}
