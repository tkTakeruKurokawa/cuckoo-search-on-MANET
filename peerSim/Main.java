import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args){
        String filename = "cuckoo_compare.csv";
        double minBattery = 100.0;
        double maxBattery = 0.0;
        double minCapacity = 100.0;
        double maxCapacity = 0.0;
        double aveBattery = 0.0;
        double aveCapacity = 0.0;

        try (BufferedReader in = new BufferedReader(new FileReader(new File(filename)))){
            String line;
            while((line = in.readLine()) != null){
                String[] string = line.split(" ", 0);
                if(string[0].equals("Average")){
                    System.out.println(string[2]);
                    if(string[1].equals("Battery:")){
                        double value = Double.parseDouble(string[2]);
                        if(maxBattery<value)
                            maxBattery = value;
                        if(minBattery>value)
                            minBattery = value;
                        aveBattery+=value;
                    }
                    if(string[1].equals("Capacity:")){
                        double value = Double.parseDouble(string[2]);
                        if(maxCapacity<value)
                            maxCapacity = value;
                        if(minCapacity>value)
                            minCapacity = value;
                        aveCapacity+=value;
                    }
                }

            }
            System.out.println("Average Battery: " + aveBattery/100.0);
            System.out.println("Average Capacity: " + aveCapacity/100.0);
            System.out.println("Max Battery: " + maxBattery);
            System.out.println("Min Battery: " + minBattery);
            System.out.println("Max Capacity: " + maxCapacity);
            System.out.println("Min Capacity: " + minCapacity);


        } catch (FileNotFoundException e){ 
            e.printStackTrace();
            System.exit(-1); // 0 以外は異常終了
        } catch (IOException e){ 
            e.printStackTrace();
            System.exit(-1);
        }
    }   
}

