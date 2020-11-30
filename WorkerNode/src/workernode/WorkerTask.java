package workernode;

import WorkerNode;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WorkerTask implements Runnable {
  
    private Socket loadBalancerSocket;
    WorkerTask(Socket loadBalancerSocket){
        this.loadBalancersocket = loadBalancerSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader lbReader = new BufferedReader(new InputStreamReader(loadBalancerSocket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter lbWriter = new BufferedWriter(new OutputStreamWriter(loadBalancerSocket.getOutputStream(), StandardCharsets.UTF_8));
            
            String job = lbReader.readLine();
            String number = lbReader.readLine();
            System.out.println("Thread Running for : "+  job + " Seconds");
           
            int miliSec = Integer.parseInt(job) * 1000;
            Thread.sleep(10);
                        
            lbWriter.write("Closed"+number+"\n");
            
            lbWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
