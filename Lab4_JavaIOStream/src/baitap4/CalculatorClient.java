
package baitap4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalculatorClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : CalculatorServer.PORT;

        try (Socket socket = new Socket(host, port);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("=== Connected to Remote Calculator Server (" + host + ":" + port + ") ===");
            System.out.println("Syntax: CALC <op> <num1> <num2> (Example: CALC + 100 200)");
            System.out.println("Type 'QUIT' to exit.");

            String request;
            System.out.print("> ");
            while ((request = console.readLine()) != null) {
                out.println(request);
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server closed connection.");
                    break;
                }
                System.out.println("Server: " + response);

                if (request.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
                System.out.print("> ");
            }
        } catch (IOException e) {
            System.err.println("Client network error: " + e.getMessage());
        }
    }
}
