import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Submission for Insight data engineering fellow coding challenge.
 * @author Aakash Bhatia.
 */
public class slidingWindow {
    /**
     * method that reads input files and writes out comparison values in a text file.
     * @param input1 actual values, input2 predicted values, output comparison.txt, windowSize.
     */
	public void readCSV(String input1, String input2, String output, int windowSize) {
	    /**
	     * HashMap of HashMap instance to store actual value of stocks at any given hour
	     */
		Map<Integer,HashMap<String,BigDecimal>> actual = filetoMap(input1);
	    /**
	     * HashMap of HashMap instance to store actual value of stocks at any given hour
	     */
		Map<Integer,HashMap<String,BigDecimal>> predicted = filetoMap(input2);
	    /**
	     * Checking if amount of input data is not zero.
	     */
		if (actual.size() == 0 || predicted.size() == 0) {
			System.out.println("Input files contain no data");
			return;
		}
	    /**
	     * Checking if window size value is not greater than the amount of input data available.
	     */
		if (windowSize > actual.size() || windowSize > predicted.size()) {
			System.out.println("Window size cannot be greater than time span of input data");
			return;
		}
	    /**
	     * Writing contents to the output file 'comparison.txt'.
	     */
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(output))) {
			int start = 1; int step = windowSize - 1; int end = start + step;
			while (actual.get(end) != null) {
				double error = 0;
				int count = 0;
			    /**
			     * Looping over a window to calculate the average error.
			     */
				for (int i = start; i <= end; i++) {
					HashMap<String, BigDecimal> map1 = predicted.get(i);
					HashMap<String, BigDecimal> map2 = actual.get(i);
					//System.out.println(i);
					if (map1 != null && map2 != null) {
						Iterator it = map1.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry entry = (Map.Entry) it.next();
						    /**
						     * Checking if the actual values contains the stock id from predicted value.
						     */
							if (map2.get(entry.getKey()) != null) {
								BigDecimal val1 = (BigDecimal) entry.getValue();
								BigDecimal val2 = (BigDecimal) map2.get(entry.getKey());
								error = error + Math.abs(val1.subtract(val2).doubleValue());
								count++;
							}
						}
					}
				}
				StringBuilder sb = new StringBuilder();
			    /**
			     * If no value within a certain window is found "NA" would be printed instead of the actual error value.
			     */
				if (count == 0) {
					sb.append(String.valueOf(start)).append("|").append(String.valueOf(end)).append("|").append("NA").append("\n");
				} else {
					sb.append(String.valueOf(start)).append("|").append(String.valueOf(end)).append("|").append(String.format("%.2f", error/count)).append("\n");
				}
				bw.write(sb.toString());
				start++;
				end++;
			}
		} catch (IOException e) {
			System.out.println("Please enter valid output file name");
		}
	}
    /**
     * @throws FileNotFoundException.
     */
	public static void main(String[] args) throws FileNotFoundException {
		final long start =  System.currentTimeMillis();
		if (args.length != 4) {
			System.out.println("Following arguments are needed: actual_values, predicted_values, output_path, window_size");
			return;
		}
		slidingWindow output = new slidingWindow();
		int wind = 0; String in;
	    /**
	     * Tries to read the window size and store it in a variable wind.
	     */
		try (BufferedReader br = new BufferedReader(new FileReader(args[2]))) {
			while ((in = br.readLine()) != null) {
				wind = Integer.parseInt(in);
			}
		} catch (IOException e) {
			System.out.println("Windows file not found");
		}
		output.readCSV(args[0], args[1], args[3], wind);
		final long end =  System.currentTimeMillis();
		System.out.println("Total execution time in milli-seconds:" +(end - start));
	}
    /**
     * method that returns a HashMap for any input file.
     * HashMap maps time hour to another map mapping stock id to stock value.
     * @param inputFile.
     * @return HashMap of a HashMap.
     */
	private Map<Integer,HashMap<String,BigDecimal>> filetoMap(String inputFile) {
	    /**
	     * Instance to create a HashMap of a HashMap.
	     */
		Map<Integer,HashMap<String,BigDecimal>> stockmap = new HashMap<Integer, HashMap<String, BigDecimal>>();
	    /**
	     * Appends content of a line to a string.
	     */
		String in = "";
	    /**
	     * Instance variable to account for the time hour.
	     */
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			while ((in = br.readLine()) != null) {
				String[] line = in.split("\\|");
			    /**
			     * Checking if 3 values are present in a line.
			     */
				if (line.length != 3) continue;
			    /**
			     * Assigning first element as the time hour.
			     */
				int timeHour;
				try {
					timeHour = Integer.parseInt(line[0]);
				} catch (Exception e) {
					timeHour = 0;
				}
			    /**
			     * Assigning second element as the stock name.
			     */
				String stockname;
				try {
					stockname = line[1].trim();
				} catch (Exception e) {
					stockname = "";
				}
			    /**
			     * Assigning third element as the stock value (Used BigDecimal for scalability).
			     */
				BigDecimal stockvalue;
				try {
					stockvalue = new BigDecimal(line[2].trim());
				} catch (Exception e) {
					stockvalue = BigDecimal.ZERO;
				}
			    /**
			     * Inserting elements into the HashMap.
			     */
				if (!stockmap.containsKey(timeHour)) {
					HashMap<String, BigDecimal> map = new HashMap<String,BigDecimal>();
					map.put(stockname, stockvalue);
					stockmap.put(timeHour, map);					
				} else {
					stockmap.get(timeHour).put(stockname, stockvalue);
				}
			}
		} catch (IOException e) {
			System.out.println("Input file not found");
		}
		return stockmap;
	}
}
