package tempest;


import java.util.Comparator;
import java.util.Map;


/**
 * Implementation of the compare method for Map.Entry<String, Integer>.
 * 
 * @author charlesharris
 */
public class MapComparator implements Comparator<Map.Entry<String, Integer>> {
	@Override
	public int compare(Map.Entry<String, Integer> map1, Map.Entry<String, Integer> map2) {
		int value1 = map1.getValue();
		int value2 = map2.getValue();
		if (value1 < value2) {
			return 1;
		} else if (value1 > value2) {
			return -1;
		} else {
			return 0;
		}
	}
}
