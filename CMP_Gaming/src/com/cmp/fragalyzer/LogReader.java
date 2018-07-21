package com.cmp.fragalyzer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;

import com.cmp.fragalyzer.types.DataPoint;

public class LogReader {

	private ArrayList<LogEntry> logEntries;

	private HashMap<String, ArrayList<LogEntry>> rounds;
	private HashMap<String, ArrayList<LogEntry>> battles;
	private ArrayList<LogEntry> battle;
	private String basePath;

	private int roundNumber;
	private String mapname;
	private int mapnumber;
	private String roundname;
	public LogReader() {
		super();
		rounds = new HashMap<>();
		battles = new HashMap<>();
		logEntries = new ArrayList<>();
		setRoundNumber(0);
		setMapnumber(1);
		setMapname("");
		setRoundname("");
		setBattle(new ArrayList<>());
		setLog(new StringBuffer());
	}
	
	private StringBuffer log;

	private void dumpHeatMapDataPointsAndEventList() {
		HashMap<String, DataPoint> heatmapHM = new HashMap<>();
		String heatmapCoord;
		// Create from the now created Entries a human-readable list of events
		FileWriter fw = null;
		DataPoint dpo;
		LogEntry logEntry;
		try {
			fw = new FileWriter(basePath + "//result.txt");
			Iterator<LogEntry> it = logEntries.iterator();
			while (it.hasNext()) {
				logEntry = it.next();
				// Right now we are only interested in Kills

				// If a Position is given, then..
				heatmapHM = getDataPoints(heatmapHM, logEntry);

				// Write the event to the file
				fw.write(logEntry.toString());
				fw.append(System.getProperty("line.separator")); // e.g. "\n"

			}
		} catch (IOException e) {
			System.err.println("Konnte Datei nicht erstellen");
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		// Getting a Set of Key-value pairs
		Set entrySet = heatmapHM.entrySet();

		// Obtaining an iterator for the entry set
		Iterator it = entrySet.iterator();
		JSONObject obj;
		String x, y;
		fw = null;
		try {
			fw = new FileWriter(basePath + "//heatmap_with_number.json");
			fw.write("[");

			fw.append(System.getProperty("line.separator")); // e.g.
			while (it.hasNext()) {
				obj = new JSONObject();
				Map.Entry me = (Map.Entry) it.next();
				dpo = (DataPoint) me.getValue();

				obj.put("x", dpo.getX());
				obj.put("y", dpo.getY());
				obj.put("value", dpo.getNumber());

				fw.write(obj.toJSONString() + ",");

				fw.append(System.getProperty("line.separator")); // e.g.
																	// "\n"

			}
			fw.write("]");

		} catch (IOException e) {
			System.err.println("Konnte Datei nicht erstellen");
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public ArrayList<LogEntry> getBattle() {
		return battle;
	}

	public HashMap<String, ArrayList<LogEntry>> getBattles() {
		return battles;
	}
	
	private HashMap<String, DataPoint> getDataPoints(HashMap<String, DataPoint> heatmap, LogEntry kill) {
		String heatmapCoord;
		DataPoint dpo;

		if (kill.getPlayerPosition() != null) {

			// ..determine the Coordinate where it occured
			heatmapCoord = Double.toString(kill.getPlayerPosition().getXNormalizedDatapointRounded(1)) + "/"
					+ Double.toString(kill.getPlayerPosition().getZNormalizedDatapointRounded(1));

			// Check if a kill has already occured at that coordinate...
			if (heatmap.containsKey(heatmapCoord)) {
				// ..and add this to it if so
				dpo = heatmap.get(heatmapCoord);
				dpo.setNumber(dpo.getNumber() + 1);
				heatmap.put(heatmapCoord, dpo);
			} else {
				// Otherwise create a new DataPoint for this kill
				dpo = new DataPoint();
				dpo.setX(kill.getPlayerPosition().getXNormalizedDatapointRounded(1));
				dpo.setY(kill.getPlayerPosition().getZNormalizedDatapointRounded(1));
				dpo.setNumber(1);
				heatmap.put(heatmapCoord, dpo);
			}
		}

		if (kill.getVictimPosition() != null) {

			// ..determine the Coordinate where it occured
			heatmapCoord = Double.toString(kill.getVictimPosition().getXNormalizedDatapointRounded(1)) + "/"
					+ Double.toString(kill.getVictimPosition().getZNormalizedDatapointRounded(1));

			// Check if a kill has already occured at that coordinate...
			if (heatmap.containsKey(heatmapCoord)) {
				// ..and add this to it if so
				dpo = heatmap.get(heatmapCoord);
				dpo.setNumber(dpo.getNumber() + 1);
				heatmap.put(heatmapCoord, dpo);
			} else {
				// Otherwise create a new DataPoint for this kill
				dpo = new DataPoint();
				dpo.setX(kill.getVictimPosition().getXNormalizedDatapointRounded(1));
				dpo.setY(kill.getVictimPosition().getZNormalizedDatapointRounded(1));
				dpo.setNumber(1);
				heatmap.put(heatmapCoord, dpo);
			}
		}
		return heatmap;
	}

	public ArrayList<LogEntry> getLogEntries() {
		return logEntries;
	}

	public String getMapname() {
		return mapname;
	}

	public int getMapnumber() {
		return mapnumber;
	}

	private String getPaddedRoundNumber(int number){
		
//		if (number < 10)
//			return "0" + number;
//		else 
			return Integer.toString(number);
			
	}

	public String getRoundname() {
		return roundname;
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public HashMap<String, ArrayList<LogEntry>> getRounds() {
		return rounds;
	}

	public void readBattleLogs(String basePath) {
		System.out.println(basePath);
		Path p = Paths.get(basePath);
		this.basePath = basePath;
		LogMapper mapper = new LogMapper();
		HashMap<String, String> missingKits = new HashMap<>();

		// Walk over all faLog files

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Scanner scanner;
				int dot = file.toString().lastIndexOf(".");
				LogEntry logEntry;
				ArrayList<LogEntry> round = new ArrayList<>();

				int roundnumber = 0;

				if (file.toString().substring(dot - 5, dot).equals(FragalyzerConstants.logfilename)) {
					System.out.println(file.toString());
					round = new ArrayList<>();
					try {
						scanner = new Scanner(file);
					} catch (FileNotFoundException e) {
						return null;
					}
					// now read the file line by line...

					while (scanner.hasNextLine()) {
						// Read each file from the from the log..
						String line = scanner.nextLine();
						// ..and create a proper entry from it
						logEntry = mapper.createLogEntryFromLogLine(missingKits, line);
						// If an entry was created, add it to the List
						if (logEntry != null) {
							getLogEntries().add(logEntry);

							switch (logEntry.getEventType()) {
							case INIT:
								if (!getMapname().equals(logEntry.getMapname())) {
									// Mapname has changed. Save battle bucket
									if (getBattle().size() > 0){
										System.out.println("Increasing mapnumber from " + getMapnumber());
										
										getBattles().put(getMapname(), getBattle());
										setMapnumber(getMapnumber()+1);
									}
									
									setMapname(logEntry.getMapname());
									
									//getBattles().put(mapname, getBattle());
									setBattle(new ArrayList<>());
									
									System.out.println("Mapname is " + getMapname());
								}

								setRoundname(getMapname() + "_round_" + getPaddedRoundNumber(logEntry.getRoundNumber()));
								setRoundNumber(logEntry.getRoundNumber());

								System.out.println("roundname is " + getRoundname());
								getLog().append("\n" + getMapname() + " " + getRoundname());	
								break;
							case KILL:
							case SCORE:
								//System.out.println(logEntry.toString());
								getBattle().add(logEntry);
								round.add(logEntry);
								getLog().append("\n" + logEntry.toString());
								break;
							default:

								break;
							}
						}

					}
					scanner.close();
					getRounds().put(roundname, round);
					getBattles().put(getMapname(), getBattle());
					
					//round = new ArrayList<>();
					
				}

				return FileVisitResult.CONTINUE;
			}

		};

		try {
			Files.walkFileTree(p, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Map.Entry<String, String> entry : mapper.missingKits
				.entrySet()) {
			System.out.println("Problem: " + entry.getKey() + " " + entry.getValue());
			getLog().append("\n" + ("Problem: " + entry.getKey() + " " + entry.getValue()));
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(basePath + "//log.txt"))) {
			bw.append(getLog());//Internally it does aSB.toString();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	


	public void setBattle(ArrayList<LogEntry> battle) {
		this.battle = battle;
	}

	public void setBattles(HashMap<String, ArrayList<LogEntry>> battles) {
		this.battles = battles;
	}

	public void setLogEntries(ArrayList<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}

	public void setMapnumber(int mapnumber) {
		this.mapnumber = mapnumber;
	}

	public void setRoundname(String roundname) {
		this.roundname = roundname;
	}

	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	public void setRounds(HashMap<String, ArrayList<LogEntry>> rounds) {
		this.rounds = rounds;
	}

	public StringBuffer getLog() {
		return log;
	}

	public void setLog(StringBuffer log) {
		this.log = log;
	}

}
