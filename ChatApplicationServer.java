package hello_java;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatApplicationServer {


	    private static final int PORT = 12345;
	    private static Set<Socket> clientSockets = new HashSet<>();

	    public static void main(String[] args) {
	        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	            System.out.println("Chat server started on port " + PORT);
	            while (true) {
	                Socket clientSocket = serverSocket.accept();
	                synchronized (clientSockets) {
	                    clientSockets.add(clientSocket);
	                }
	                new ClientHandler(clientSocket).start();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private static class ClientHandler extends Thread {
	        private Socket socket;

	        public ClientHandler(Socket socket) {
	            this.socket = socket;
	        }

	        public void run() {
	            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
	                
	                out.println("Welcome to the chat!");
	                String message;
	                while ((message = in.readLine()) != null) {
	                    System.out.println("Received: " + message);
	                    broadcastMessage(message);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                synchronized (clientSockets) {
	                    clientSockets.remove(socket);
	                }
	            }
	        }

	        private void broadcastMessage(String message) {
	            synchronized (clientSockets) {
	                for (Socket s : clientSockets) {
	                    try {
	                        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
	                        out.println(message);
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    }
	}


