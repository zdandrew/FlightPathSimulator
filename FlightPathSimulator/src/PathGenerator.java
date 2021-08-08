import java.io.IOException;
import java.util.*;

public class PathGenerator {
    private String departureAirport;
    private String arrivalAirport;
    private HashMap<AirportKey, AirportNode> adjList;
    private ReadCSV data;

    public PathGenerator(String departureAirport,
                         String arrivalAirport, String file) throws IOException {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.data = new ReadCSV(Main.startTime, Main.startTime.plusDays(Main.numDays), file);

        populateAdjList();
    }

    private void populateAdjList() {
        List<Flight> flights = data.getFlights();
        adjList = new HashMap<>();

        Set<String> airportCodes = new HashSet<>();
        airportCodes.addAll(data.getDepartureAirports());
        airportCodes.addAll(data.getArrivalAirports());

        // add dummy start and end node for every airport code
        for (String airportCode : airportCodes) {
            adjList.put(new AirportKey(airportCode, 0), new AirportNode(airportCode, 0));
            adjList.put(new AirportKey(airportCode, Main.maxInterval), new AirportNode(airportCode, Main.maxInterval));
        }

        // populate edges into adjList
        for (Flight flight : flights) {
            String departureAirportCode = flight.getDepartureAirport();
            String arrivalAirportCode = flight.getArrivalAirport();

            int startInterval = flight.getStartInterval();
            int endInterval = flight.getEndInterval();

            if (endInterval > Main.maxInterval) continue;

            if (!adjList.containsKey(new AirportKey(departureAirportCode, startInterval))) {
                adjList.put(new AirportKey(departureAirportCode, startInterval),
                        new AirportNode(departureAirportCode, startInterval));
            }
            if (!adjList.containsKey(new AirportKey(arrivalAirportCode, endInterval))) {
                adjList.put(new AirportKey(arrivalAirportCode, endInterval),
                        new AirportNode(arrivalAirportCode, endInterval));
            }
            AirportNode fromNode = adjList.get(new AirportKey(departureAirportCode, startInterval));
            AirportNode toNode = adjList.get(new AirportKey(arrivalAirportCode, endInterval));
            fromNode.addEdge(flight);
        }

        // add extra edges from all airports to their future versions
        // skip vertices that don't need to exist
        for (String airportCode : airportCodes) {
            for (int interval = 0; interval <= Main.maxInterval; interval++) {
                if (!adjList.containsKey(new AirportKey(airportCode, interval))) continue;

                AirportNode fromNode = adjList.get(new AirportKey(airportCode, interval));
                for (int futureInterval = interval + 1; futureInterval <= Main.maxInterval; futureInterval++) {
                    if (!adjList.containsKey(new AirportKey(airportCode, futureInterval))) continue;
                    AirportNode toNode = adjList.get(new AirportKey(airportCode, futureInterval));

                    fromNode.addEdge(new Flight(fromNode.getStartInterval(), toNode.getStartInterval(),
                            null, fromNode.getAirportCode(), toNode.getAirportCode(), 0, 0));
                }
            }

        }

    }

    public void getCheapestFlightPath() {
    	AirportKey source = new AirportKey(departureAirport, 0);
        HashSet<AirportKey> finished = new HashSet<>();
        HashMap<AirportKey, Flight> parents = new HashMap<>();
        HashMap<AirportKey, Integer> finalDistances = new HashMap<>();
        HashMap<AirportKey, Integer> currDistances = new HashMap<>();
        PriorityQueue<AirportKeyDist> pqDistances
                = new PriorityQueue<>(Comparator.comparingInt(AirportKeyDist::getDist));

        for (AirportKey key : adjList.keySet()) {
            pqDistances.add(new AirportKeyDist(key, Integer.MAX_VALUE));
            currDistances.put(key, Integer.MAX_VALUE);
        }

        // add source vertex
        pqDistances.add(new AirportKeyDist(source, 0));
        currDistances.put(source, 0);
        parents.put(source, null);

        // iterate through nodes
        while (finished.size() < adjList.keySet().size()) {
            AirportKeyDist minDistNode = pqDistances.poll();
            AirportKey minKey = minDistNode.getKey();

            // throw away duplicates if already processed
            if (finished.contains(minKey)) continue;

            // add final distance
            finished.add(minKey);
            finalDistances.put(minKey, minDistNode.getDist());

            // do not update neighbors if node is unreachable
            if (minDistNode.getDist() == Integer.MAX_VALUE) continue;

            // update neighbors
            for (Flight edge : adjList.get(minKey).getEdges()) {
                AirportKey neighborKey = new AirportKey(edge.getArrivalAirport(), edge.getEndInterval());
                int currDistance = currDistances.get(neighborKey);
                int newDistance = finalDistances.get(minKey) + edge.getCost();

                if (newDistance < currDistance && !finished.contains(neighborKey)) {
                    currDistances.put(neighborKey, newDistance);
                    pqDistances.add(new AirportKeyDist(neighborKey, newDistance));
                    parents.put(neighborKey, edge);
                }
            }
        }

        // print out flights to take
        AirportKey currLoc = new AirportKey(arrivalAirport, Main.maxInterval);
        if (finalDistances.get(currLoc) == Integer.MAX_VALUE) {
            System.out.println("Destination is unreachable.");
        } else {
            System.out.println("Flights to Take:");
            int minCostToArrival = finalDistances.get(currLoc);
            Stack<Flight> flightStack = new Stack<>();
            while (!currLoc.equals(new AirportKey(departureAirport, 0))) {
                Flight edge = parents.get(currLoc);
                if (!edge.getDepartureAirport().equals(edge.getArrivalAirport())) {
                    flightStack.push(edge);
                }

                currLoc = new AirportKey(edge.getDepartureAirport(), edge.getStartInterval());
            }

            while (!flightStack.isEmpty()) {
                System.out.println(flightStack.pop());
            }

            System.out.println("Min cost of travel to " + arrivalAirport + ": " + minCostToArrival);
        }
    }

    private class AirportKeyDist {
        private AirportKey key;
        private int dist;

        public AirportKeyDist(AirportKey key, int dist) {
            this.key = key;
            this.dist = dist;
        }

        public AirportKey getKey() {
            return key;
        }

        public int getDist() {
            return dist;
        }

        public String toString() {
            return key + " " + dist;
        }
    }

    private class AirportKey {
        private String airportCode;
        private int interval;

        public AirportKey(String airportCode, int interval) {
            this.airportCode = airportCode;
            this.interval = interval;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AirportKey that = (AirportKey) o;
            return interval == that.interval &&
                    Objects.equals(airportCode, that.airportCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(airportCode, interval);
        }

        public String toString() {
            return airportCode + interval;
        }
    }

}
