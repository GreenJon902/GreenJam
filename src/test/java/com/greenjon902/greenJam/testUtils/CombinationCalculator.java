package com.greenjon902.greenJam.testUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinationCalculator {
	public static <K, V> HashMap<K, V>[] calculate(Map<K, V[]> map) {
		return recur(map, new ArrayList<>(map.keySet().stream().toList()));
	}

	private static <K, V> HashMap<K, V>[] recur(Map<K, V[]> map, List<K> keys) {
		if (keys.isEmpty()) {
			return new HashMap[] {new HashMap<>()};
		}
		K key = keys.remove(0);

		HashMap<K, V>[] maps = recur(map, keys);
		V[] options = map.get(key);

		HashMap<K, V>[] newMaps = new HashMap[maps.length * options.length];
		int i = 0;
		for (HashMap<K, V> oldMap : maps) {
			for (V option : options) {

				HashMap<K, V> newMap = (HashMap<K, V>) oldMap.clone();
				newMap.put(key, option);
				newMaps[i] = newMap;

				i++;
			}
		}

		return newMaps;
	}
}
