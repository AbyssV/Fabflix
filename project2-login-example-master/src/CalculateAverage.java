import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
public class CalculateAverage {
		
	public static void calculate() throws FileNotFoundException
	{
        int count=0;
        
        long total_big = 0;
        long total_small = 0;

        File inputFile = new File (
                         "/Users/weijingkaihui/Downloads/cs122b_p2/cs122b-winter18-team-17/scaled2-slave.txt");
        Scanner scan = new Scanner(inputFile);

        while(scan.hasNext()) {

        		count++;
            long num1 = scan.nextLong();
            long num2 = scan.nextLong();

            total_big = total_big + num1;
            total_small = total_small + num2;
            
            //System.out.println(" num1  "+num1);
            //System.out.println(" num2  "+num2);

        } 
        
        long average_big = total_big/count;
        long average_small = total_small/count;
        
        System.out.println(" count  "+count);
        System.out.println(" average_big  "+average_big);
        System.out.println(" average_small  "+average_small);
    }
	
	public static void main(String[] args) throws FileNotFoundException {

		calculate();
    }

}
