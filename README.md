## Table of Contents
1. Software and running instructions
1. Approach to the problem.
1. Data structures used.
1. Complexity analysis

## Software and running instructions
1. The source code is called slidingWindow written in Java 10 and resides in the src folder.
1. Compilation instructions are written in the run.sh.
1. Following imports are made : java.io.BufferedReader, java.io.BufferedWriter, java.io.FileNotFoundException, java.io.FileReader,java.io.FileWriter, java.io.IOException, java.math.BigDecimal, java.util.HashMap, java.util.Iterator, java.util.Map;

## Approach to the problem
The given problem can be divided in two parts. First part is where we parse the input file to store in a hash map followed by the second part where we perform calculations using the sliding window approach.

1. Reading the input file:

    1. Created a private function that reads any input file and returns a HashMap within a HashMap. First HashMap uses the time as the        key to map to the corresponding values of stock at that time. The inner HashMap uses the id of the stock as a key to map to its        corresponding value.
    1. While reading the input file line to line, the outer map was checked if it already contains the time hour key
    1. If the outer HashMap did not contain the time hour key, a new HashMap was created to insert the stock value.
    1. If the outer HashMap did contain the time hour key, the same HashMap was fetched using get() and stock value was added into          the inner HashMap.
    1. Lastly, two double HashMaps were created, one to store the actual value and the other to store the predicted value.

1. Performing calculations:

    1. A sliding window approach was used to compute the error at any given time. Considering the case where window size was          4, the first error was calculated by computing the average error across first four hours between actual and predicted value.            Similarly the second error was calculated by computing the average error cross second to fifth time hour. In case value for a            window is not available NA would be printed out in the average error.
    1. To compute the difference, iteration was performed on the predicted stock value HashMap. So for every predicted stock value,            corresponding stock id was fetched from the same time hour from the actual stock value. This saved us iterating from the longer          actual stock value data set as prediction file is shorter than the actual file.
    1. A count was also initiated whenever a new window calculation was performed to account for varying number of data points where we        have both the actual stock value and predicted stock value.
    1. Lastly, the value was appended to an instance of the string builder and written to a file.
    
## Data structures used
1. HashMap to map time hour value to another HashMap that maps corresponding stock id to its value (Double HashMap).

## Complexity analysis
Consider having N time hours and M stock ID values corresponding to each time hour

1. Parsing the file and storing the value for each time hour in the HashMap takes O(N) time for the outer map and O(M) time for the        inner map in the worst case for look up and adding to a map takes O(1) time.
1. While calculating the error, values are again looked up from the HashMap which is O(N) for the outer map and O (M) for the inner map    where M is the lesser of length of actual values present to that of predicted values present.
1. Writing the starting time, ending time of a window along with its average error takes approximately O(N) time.
1. Hence overall time complexity is Max(O(N), O(M)).
1. Space Complexity is O (N * P) where N is the number of time hours and P is the length of stock id values for N time hours.
