package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
        	Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            while (true){
            	System.out.println("Please Enter Job");
                String job = myObj.nextLine();
                int jobs = Integer.parseInt(job);
                // Open connection to Load Balancer.
                Socket loadBalancerSocket = new Socket("localhost", 12350);
                
                // Start a new thread to send request.
                Thread requestSender = new Thread(new RequestSender(loadBalancerSocket, jobs));
                requestSender.start();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RequestSender implements Runnable{
    private Socket loadBalancerSocket;
    private int job = 0;
    RequestSender(Socket loadBalancerSocket, int time){
        this.loadBalancerSocket = loadBalancerSocket;
        this.job = job;
    }
    @Override
    public void run() {
        try {
            BufferedWriter lbWriter = new BufferedWriter(new OutputStreamWriter(loadBalancerSocket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader lbReader = new BufferedReader(new InputStreamReader(loadBalancerSocket.getInputStream(), StandardCharsets.UTF_8));

            int jobInMin = job;

            // Send to Load Balancer.
            lbWriter.write(jobInMin + "\n");
            lbWriter.flush();

            // Get worker's response, sent via Load Balancer.
            String result = lbReader.readLine();
            
            System.out.println(result+"\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
