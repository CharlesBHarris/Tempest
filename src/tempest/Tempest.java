package tempest;
 

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.AbstractMap;


public class Tempest {
	
	
	/**
	 * Reads the file filename and returns a String with the raw text in it.
	 * 
	 * @param filename  the file name String
	 *  
	 * @return String  contains the raw text
	 * 
	 * @throws IOException  will be caught and stack trace printed
	 */
	public static String readFile(String filename) throws IOException {
		BufferedReader fileReader = null;
		String output = "";
		String line = null;
		try {
			fileReader = new BufferedReader(new FileReader(filename));
			while ((line = fileReader.readLine()) != null) {
				output += line + " ";
			}
			return output;
		} catch (IOException exception) {
			System.out.printf("IOException: Please check that the file \"tempest.txt\" "
					          + "is in the project root directory\n");
			exception.printStackTrace();
			return output;
		} finally {
			if (fileReader != null) {
				fileReader.close();
			}
		}
	}

	
	/**
	 * Takes the raw text input fileString and returns a String[] containing the
	 * parsed words. Replaces all [];:",.!? characters with whitespace and then
	 * shrinks all repeated whitespace to a single character. Then splits the
	 * string into the return String[].
	 * 
	 * @param fileString   raw string input of our text
	 * 
	 * @return String[]    a string array of parsed words
	 */
	public static String[] parseFile(String fileString) {
		String replacedFileString = fileString.toLowerCase()
				                              .replaceAll("[\\[\\],.?!;:\"]", " ");
		return replacedFileString.split("\\s+");
	}
 
	
	/**
	 * Returns a Map<String, Integer> containing the (word, count) pairs
	 * obtained by counting the unique elements of words.
	 * 
	 * @param words   a String[] with our words to count
	 * 
	 * @return Map<String, Integer>   contains (word, count) pairs
	 */
	public static Map<String, Integer> getCounter(String[] words) {
		Map<String, Integer> counter = new HashMap<String, Integer>();
		Integer tempInt = 0;
		for (int i = 0; i < words.length; i++) {
			if ((tempInt = counter.putIfAbsent(words[i], 1)) != null) {
				counter.replace(words[i], tempInt, tempInt + 1);
			}
		}
		return counter;
	}

	
	/**
	 * Returns the top N words, ordered by word-count, of the (word, count)
	 * pairs in counter. It first loads slider with N pairs, sorts slider by
	 * word-count, and then iterates through the remainder of counter. For each
	 * iteration it compares the word-count with successive elements in slider
	 * and inserts the new pair where it would fit in the ordering. Lesser
	 * elements are shifted back and the last element overwritten, maintaining
	 * an ordering of N pairs.
	 * 
	 * @param counter   a Map<String, Integer> holding the (word, count) pairs
	 * 
	 * @param N    the number of words returned
	 * 
	 * @return slider   an ArrayList<Map.Entry<String, Integer>> containing the N
	 *                  top (word, count) pairs
	 */
	public static ArrayList<Map.Entry<String, Integer>> getTopNWords(Map<String, Integer> counter, Integer N) {
		ArrayList<Map.Entry<String, Integer>> slider = new ArrayList<>();
		for (String key : counter.keySet()) {
			Map.Entry<String, Integer> tempPair = new AbstractMap.SimpleEntry<String, Integer>(key, counter.get(key));
			if (slider.size() < N) {
				slider.add(tempPair);
				if (slider.size() == N) {
					slider.sort(new MapComparator());
				}
			} else {
				for (int j = N - 1; j >= 0; j--) {
					Boolean condition = tempPair.getValue() <= slider.get(j).getValue();
					if (j == N - 1) {
						if (condition) {
							break;
						} else {
							continue;
						}
					} else {
						if (condition) {
							slider.set(j + 1, tempPair);
							break;
						} else {
							slider.set(j + 1, slider.get(j));
							if (j == 0) {
								slider.set(j, tempPair);
								break;
							}
						}
					}
				}
			}
		}
		return slider;
	}

	
	/**
	 * Simple print function for ArrayList<Map.Entry<String, Integer>>
	 * 
	 * @param slider  an ArrayList<Map.Entry<String, Integer>> that holds (word, count) pairs
	 */
	public static void printTopNWords(ArrayList<Map.Entry<String, Integer>> slider) {
		System.out.printf("\nThe %s most common words are:%n%n", slider.size());
		for (int i = 0; i < slider.size(); i++) {
			System.out.printf("%s  (%s) %n", slider.get(i).getKey(), slider.get(i).getValue());
		}
		System.out.printf("\n");
	}

	
	/**
	 * Our main function. Contains the magic variables N = 10 and filename =
	 * "tempest.txt" for this particular case. Contains 5 function calls to
	 * implement in order: read file, parse file to a word list, obtain the
	 * count data, determine the top N words, and print results.
	 * 
	 * @param args   command line inputs (not used)
	 * 
	 * @throws IOException  if thrown, caught in called method readFile(String)
	 */
	public static void main(String[] args) throws IOException {
		// magic variables
		Integer N = 10;
		String filename = "tempest.txt";
		
		// function calls
		String tempest = readFile(filename);
		String[] parsedTempest = parseFile(tempest);
		Map<String, Integer> counter = getCounter(parsedTempest);
		ArrayList<Map.Entry<String, Integer>> output = getTopNWords(counter, N);
		printTopNWords(output);
	}
}
