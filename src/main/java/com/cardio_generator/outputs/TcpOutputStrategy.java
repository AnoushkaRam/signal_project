package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class TcpOutputStrategy implements OutputStrategy {
    //This class implements the OutputStrategy interface to provide functionality for outputting data over TCP.

    private ServerSocket serverSocket; // Server socket to accept client connections
    private Socket clientSocket; // Socket for communicating with the client
    private PrintWriter out; // PrintWriter for sending data to the client

    public TcpOutputStrategy(int port) {
        //Constructs a TcpOutputStrategy with the specified port.
        //@param port The port on which the TCP server will listen for client connections.
        try {
            // Create a server socket bound to the specified port
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    // Wait for a client to connect
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    // Initialize PrintWriter to send data to the client
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        //outputs patient data over TCP.
        // @param patientId The ID of the patient.
        // @param timestamp The timestamp of the data.
        // @param label     The label associated with the data.
        // @param data      The data to be sent.
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message); // Send the message to the client
        }
    }
}
