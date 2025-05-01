import java.io.*;
import java.net.InetAddress;
import java.util.*;

public class MultiHostNetworkChecker 
{

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        List<String> hosts = new ArrayList<>();

        System.out.println("Enter hostnames or IPs (type 'done' to finish):");

        while (true) 
	{
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) break;
            if (!input.isEmpty()) hosts.add(input);
        }

        String logFile = "network_status_log.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) 
	{

            for (String host : hosts) 
	    {
                long startTime = System.currentTimeMillis();

                try 
		{
                    InetAddress inet = InetAddress.getByName(host);
                    boolean reachable = inet.isReachable(3000); // 3 second timeout
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    String result = String.format("Host: %s | Status: %s | Time: %dms",
                            host, reachable ? "Reachable" : "Unreachable", elapsedTime);

                    System.out.println(result);
                    writer.write(result);
                    writer.newLine();

                } 
		catch (IOException e) 
		{
                    String errorMsg = "Host: " + host + " | Error: " + e.getMessage();
                    System.out.println(errorMsg);
                    writer.write(errorMsg);
                    writer.newLine();
                }
            }

            System.out.println("\n✅ Results logged to: " + logFile);

        } 
	catch (IOException e) 
	{
            System.err.println("Logging failed: " + e.getMessage());
        }

        scanner.close();
    }
}
