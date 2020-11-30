package WorkerNode;

import workernode.WorkerTask;
import java.net.ServerSocket;
import java.net.Socket;

public class WorkerNode{
    public static void main(String[] args) {
        try {
            
            ServerSocket workerSocket = new ServerSocket(Integer.valueOf(args[0]));
            System.out.println("Worker node is started: ");
            while(true){
                // Accept connection from Load Balancer.
                Socket loadBalancerSocket = workerSocket.accept();
                
                // Start a new thread to service this request.
                Thread workerTask = new Thread(new WorkerTask(loadBalancerSocket));
                workerTask.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
