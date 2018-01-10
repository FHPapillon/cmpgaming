package com.cmp.fragalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.cmp.fragalyzer.types.PlayerStats;
import com.cmp.fragalyzer.types.StatScope;

import java.util.Set;

public class Starter {

	public static void main(String[] args) {
		LogReader lr = new LogReader();
		String basePath = "E://FHT//logs";
		Ranker ranker = new Ranker(basePath);
		HeatmapDataProvider hdp = new HeatmapDataProvider(basePath);
		lr.readBattleLogs(basePath);
		rank(lr.getRounds(), ranker, hdp, StatScope.ROUND);
		rank(lr.getBattles(), ranker, hdp, StatScope.BATTLE);
		ranker.rank(lr.getLogEntries(),"",StatScope.OVERALL);

	}

	private static void rank(HashMap<String, ArrayList<LogEntry>> events, Ranker ranker, HeatmapDataProvider hdp, StatScope scope) {
		
		
		// Getting a Set of Key-value pairs
		Set<Entry<String, ArrayList<LogEntry>>> entrySet =events.entrySet();

		String round;
		ArrayList<LogEntry> logEntries;

		// Obtaining an iterator for the entry set
		Iterator<Map.Entry<String, ArrayList<LogEntry>>> it = entrySet.iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<LogEntry>> me = it.next();
			/*
			 * fw = new FileWriter(basePath + "//stats_" +
			 * me.getKey().replaceAll("[^A-Za-z0-9]", "") + ".json");
			 * fw.append(System.getProperty("line.separator")); // e.g.
			 * fw.write("[");
			 */
			round = me.getKey();
			logEntries = me.getValue();
			ranker.rank(logEntries, round, scope);
			hdp.dumpHeatMapDataPointsAndEventList(logEntries, round, scope);
		}
	}

}