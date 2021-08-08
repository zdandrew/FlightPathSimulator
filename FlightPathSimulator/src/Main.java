import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    static LocalDateTime startTime;
    static int intervalSize;
    static int maxInterval;
    static int numDays;

    public static void main(String[] args) throws IOException  {
    	Scanner sc = new Scanner(System.in);

    	// level of precision in intervals generated (must divide 24)
    	intervalSize = 3;

        System.out.println("What day/time would you like to leave? Enter in MM-DD-YYYY format.");
        String startDate = sc.nextLine();

        System.out.println("Within how many days should you reach your destination?");
        int days = Integer.parseInt(sc.nextLine());
        System.out.println("We will look for flights that arrive to your destination within " + days + " days.");
        maxInterval = (24 / intervalSize) * days - 1;
        numDays = days;

        System.out.println("Enter the code of your departure airport: ");
        String departure = sc.nextLine();

        System.out.println("Enter the code of your arrival airport: ");
        String arrival = sc.nextLine();

        LocalDate d = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        startTime = LocalDateTime.of(d, LocalTime.of(0,0));
        PathGenerator p = new PathGenerator(departure, arrival, "NZ airfares.csv");
        p.getCheapestFlightPath();
        
        
        System.out.println("----------------------------------------");
        System.out.println();
        
        d = LocalDate.parse("09-19-2019", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        startTime = LocalDateTime.of(d, LocalTime.of(0,0));
        PathGenerator test = new PathGenerator("AKL", "WLG", "dummyAirlineData.csv");
        test.getCheapestFlightPath();
    }

}
