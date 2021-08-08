import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class AirportNode {
    private String airportCode;
    private int interval;
    private ArrayList<Flight> edges;

    public AirportNode(String airportCode, int interval) {
        this.airportCode = airportCode;
        this.interval = interval;
        this.edges = new ArrayList<Flight>();
    }

    public LocalDateTime getStartInterval() {
        return Main.startTime.plusHours(interval * Main.intervalSize);
    }

    public LocalDateTime getEndInterval() {
        return Main.startTime.plusHours((interval + 1) * Main.intervalSize);
    }

    public ArrayList<Flight> getEdges() {
        return edges;
    }

    public String getAirportCode() {
        return airportCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportNode that = (AirportNode) o;
        return interval == that.interval &&
                Objects.equals(airportCode, that.airportCode);
    }

    public void addEdge(Flight flight) {
        edges.add(flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportCode, interval);
    }

    public String toString() {
        return "Airport Code: " + airportCode + " Interval: " + interval;
    }

}
