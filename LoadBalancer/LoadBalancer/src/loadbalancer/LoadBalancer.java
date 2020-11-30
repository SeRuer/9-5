package LoadBalancer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LoadBalancer {
    private static void startLoadBalancer() {
        try {
            // List of WorkerInfo objects. WorkerInfo class has two fields: host, port.
            ArrayList<WorkerInfo> workers = new ArrayList<>();
            BufferedReader workerFile = new BufferedReader(new FileReader(new File("worker_list.txt")));

            // Populate worker list from worker_list.txt.
            while (workerFile.read() != -1) {
                String[] info = workerFile.readLine().split(",");
                workers.add(new WorkerInfo(info[0], Integer.valueOf(info[1])));
            }

            // WorkerLoads object consists of a list of worker loads, one int load for each worker.
            WorkerLoads workerLoads = new WorkerLoads(workers.size());

            // Open Load Balancer Socket. This socket acts as a single entry point for all incoming request from Clients.
            ServerSocket balancerSocket = new ServerSocket(12350);
            int currentWorker = 0;
            while (!Thread.interrupted()) {

                // Accept a new client connection.
                Socket clientSocket = balancerSocket.accept();
                  
                currentWorker = (currentWorker + 1) % workers.size();
                System.out.println("Selected worker " + currentWorker + ".");

                // Open connection to selected worker.
                Socket workerSocket = new Socket(workers.get(currentWorker).getHost(), workers.get(currentWorker).getPort());

                // Start a new thread to serve this request.
                Thread lbRequestServer = new Thread(new clientSocket, workerSocket, workerLoads, currentWorker));
                start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        startLoadBalancer();
    }
}


class WorkerLoads {
    private ArrayList<Integer> workerLoads = new ArrayList<>();

    WorkerLoads(int num_servers) {
        // Initialize loads of all workers to 0.
        for (int i = 0; i < num_servers; i++)
            workerLoads.add(0);
    }

    int getLoad(int index){
        return workerLoads.get(index);
    }

    synchronized void incrementLoad(int index){
        workerLoads.set(index, workerLoads.get(index) + 1);
    }

    synchronized void decrementLoad(int index){
    	System.out.println("DecrementLoad For "+ index);
        workerLoads.set(index, workerLoads.get(index) - 1);
    }

}