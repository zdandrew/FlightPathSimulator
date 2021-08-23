# FlightPathSimulator
Finds the cheapest airfare possible for multi-flight trips using graph algorithms and information retrieval. Created by Jay Vishwarupe and Andrew Zhang for NETS150.

Name of project: Flight path generator

Description of project / how it works:
Often, when planning a journey, fliers plan with the goal of minimizing cost. While a direct flight might be faster
and more convenient, it is rarely the cheapest option. Unfortunately, it can be overwhelming for consumers to try
considering all possible connecting flights to find their cheapest option. Meanwhile, existing flight aggregators
Expedia have a financial incentive to only show the flights that are the most profitable to their bottom line.

We attempt to solve this problem with our flight path generator which searches every possible connecting strategy
and provides the cheapest flights to get your destination within a specified number of days. Our program takes your
departure date, your start and end airports, as well as the number of days you're willing to travel for to ensure
we're able to minimize your cost.

Topics used:
Our project dives deep into graphs / graph algorithms and information retrieval.

For the first topic, we generate a graph to represent all the flights and airports. The challenging part of this
problem was how we could account for the departure times. Effectively, what that meant was that arriving at a
particular airport at different times would give you access to different edges (since you cannot take a flight that
departs before you arrive). After brainstorming a variety of solutions, we came up with an approach to generate
duplicate versions of each airport where each duplicate is associated with a time interval. For example, the airport
with code "AKL" would have a version with time interval from 12:00 AM inclusive to 3:00 AM exclusive, a version with
3:00 AM inclusive to 6:00 AM exclusive, and so on. If we decide to look for all flight paths that get us to our
destination within 5 days, we would generate (24 * 5) / 3 = 40 duplicate nodes each with their unique time intervals
(note that intervals from different days are distinct). Then, we would transform all the the flights into edges in
this graph where the departure and arrival airports correspond to the relevant versions of the airport nodes with the
time interval. This allowed us to ensure every airport only had outgoing edges for flights within its time interval.
To ensure our code also considered all flights out of that airport in different intervals, we added "dummy"
edges/flights from that interval version of "AKL" to all future versions of "AKL" which allows us to use Dijkstra to
get access to all relevant future flights from that airport without too many manual changes to the algorithm.

For information retrieval, we translated CSV data into Java objects that the rest of our code could manipulate. This
required us to explore how to interpret and process CSV data in the context of Java. It also required exploring new
libraries to codify certain properties of flights. One example of this is that we learned to use the LocalDateTime,
LocalDate, and LocalTime classes to represent specific instances of time and make comparisons to determine which
connecting flights could be taken when.
