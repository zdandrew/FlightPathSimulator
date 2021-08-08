import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class ReadCSV {
	private List<Flight> records = new ArrayList<Flight>();
	private Set<String> fromAirports = new HashSet<String>();
	private Set<String> toAirports = new HashSet<String>();
	private int cnt;
	
	// Read in the csv data that falls into the date range and create flight objects to reflect the data
	public ReadCSV(LocalDateTime date1, LocalDateTime date2, String file) throws IOException {
		cnt = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].length() != 10) {
                	values[0] = "0" + values[0];
                }
                LocalDateTime entryDate = toDateTime(values[0], values[2]);
                // Check if the current entry falls into the date range
                if (entryDate.isAfter(date1) && entryDate.isBefore(date2)) {
                	LocalDateTime start = toDateTime(values[0], values[2]);
                	LocalDateTime end = toDateTime(values[0], values[4]);
                	String airline = "" + values[9];
                	String from = "" + values[1];
                	
                	String to = "" + values[3];
                	if (from.equals("") || to.equals("")) {
                		continue;
                	}
                	fromAirports.add(from);
                	toAirports.add(to);
                	int price = -1;
                	if (values[10] != null) {
                		price = Integer.parseInt(values[10]);
                	}
                	String durationStr = values[5]; 
                	Scanner sc = new Scanner(durationStr);
                	String hr = sc.next();
                	String min = "0";
                	if (sc.hasNext()) {
                		min = sc.next();
                    	hr = hr.substring(0, hr.length() - 1);
                    	min = min.substring(0, min.length() - 1);
                	} else {
                		min = hr.substring(0, hr.length() - 1);
                		hr = "0";
                	}
                	
                	int hrMin = Integer.parseInt(hr) * 60;
                	int minMin = Integer.parseInt(min);
                	sc.close();
                	
                	//Create the flight object using the data extracted above.
                	Flight obj = new Flight(start, end, airline, from, to, price, hrMin + minMin);
                	cnt++;
                	records.add(obj);
                }
                
            }
        }
        System.out.println("from: " + fromAirports.toString());
        System.out.println("to: " + toAirports.toString());
        System.out.println("number of entries: " + cnt);
       
	}

	// Input is the date and time
	public static LocalDateTime toDateTime(String day, String s) {
		day = day.replace('/', '-');
		LocalDate date = LocalDate.parse(day, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		if (s.length() == 0) {
			s = "12:00 AM";
		}
		if (s.length() < 8) {
			s = "0" + s;
		}
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
	    LocalTime time = LocalTime.parse(s, inputFormatter);
		LocalDateTime dt = LocalDateTime.of(date, time);

		return dt;
	}
	
	public List<Flight> getFlights() {
		return records;
	}
	
	public Set<String> getDepartureAirports() {
		return fromAirports;
	}
	
	public Set<String> getArrivalAirports() {
		return toAirports;
	}
}
