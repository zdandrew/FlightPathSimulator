import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private LocalDateTime startTime;
    private int startInterval;
    private int endInterval;
    private LocalDateTime endTime;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;
    private int cost;
    private int duration;

    public Flight(LocalDateTime startTime, LocalDateTime endTime, String airline,
                  String departureAirport, String arrivalAirport, int cost, int duration) {
        this.startTime = startTime;
        this.endTime = endTime.plusHours(Main.intervalSize);
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.cost = cost;
        this.duration = duration + 60 * Main.intervalSize;
        if (startTime != null && endTime != null) {
        	startInterval = (int) (Duration.between(Main.startTime, startTime).toHours()
                    / Main.intervalSize);
            endInterval = (int) (Duration.between(Main.startTime, endTime).toHours()
                    / Main.intervalSize);
        }
        
    }
    @Override
    public String toString() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    	return "Start: " + startTime.format(formatter) + " End: " + endTime.format(formatter)
                + " Airline: " + airline + " From: " + departureAirport
                + " To: " + arrivalAirport + " Cost: " + cost;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public int getCost() {
        return cost;
    }

    public int getStartInterval() {
    	return startInterval;
    }
    
    public int getEndInterval() {
    	return endInterval;
    }
}
