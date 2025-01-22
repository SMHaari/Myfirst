package hello_java;
import java.io.*;
import java.net.*;

public class ChatApplicationClientSide {

	    private static final String SERVER_ADDRESS = "localhost";
	    private static final int PORT = 12345;

	    public static void main(String[] args) {
	        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
	             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
	            
	            System.out.println("Connected to the chat server");
	            new Thread(() -> {
	                try {
	                    String response;
	                    while ((response = input.readLine()) != null) {
	                        System.out.println(response);
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }).start();

	            String userInput;
	            while ((userInput = keyboard.readLine()) != null) {
	                output.println(userInput);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


