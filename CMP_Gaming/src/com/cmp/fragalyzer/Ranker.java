
package com.cmp.fragalyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cmp.fragalyzer.types.EventType;
import com.cmp.fragalyzer.types.KillType;
import com.cmp.fragalyzer.types.KitType;
import com.cmp.fragalyzer.types.PlayerStats;
import com.cmp.fragalyzer.types.StatScope;
import com.cmp.fragalyzer.types.VehicleType;
import com.cmp.fragalyzer.types.WeaponType;
import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.H1;
import com.hp.gagawa.java.elements.H2;
import com.hp.gagawa.java.elements.H4;
import com.hp.gagawa.java.elements.Head;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Text;
import com.hp.gagawa.java.elements.Title;
import com.hp.gagawa.java.elements.Tr;

public class Ranker {

	private String basePath;
	private HashMap<String, Integer> cpCapAassistRanking;
	private HashMap<String, Integer> cpCapRanking;
	private HashMap<String, Integer> cpDefendRanking;
	private HashMap<String, Integer> cpNeutralizeAssistRanking;
	private HashMap<String, Integer> cpNeutralizeRanking;
	private HashMap<String, Integer> deathsRanking;

	private HashMap<String, Double> kdrRanking;
	private HashMap<String, Integer> killRanking;
	private Html report;
	private StatScope scope;

	private String scopeName;

	private Table table;

	private int td_counter;

	private String team;

	private HashMap<String, Integer> tkRanking;
	private HashMap<String, Integer> tkedRanking;

	private Tr tr;
	private HashMap<String, HashMap<String, Integer>> vehicleNameRanking;
	private HashMap<VehicleType, HashMap<String, Integer>> vehicleTypeRanking;

	private HashMap<String, HashMap<KitType, Integer>> weaponRanking;
	private HashMap<String, HashMap<String, Integer>> weaponNameRanking;

	public HashMap<String, HashMap<String, Integer>> getWeaponNameRanking() {
		return weaponNameRanking;
	}

	public void setWeaponNameRanking(HashMap<String, HashMap<String, Integer>> weaponNameRanking) {
		this.weaponNameRanking = weaponNameRanking;
	}

	private HashMap<WeaponType, HashMap<String, Integer>> weaponTypeRanking;

	public Ranker(String basePath) {
		super();
		this.setBasePath(basePath);
		init();
		setReport(new Html());
		Head head = new Head();
		getReport().appendChild(head);
		setTd_counter(0);
		tr = new Tr();
		table = new Table();
		setTeam(new String(""));
	}

	private void appendPlayerStats(String player, PlayerStats stats) {

		Body body = new Body();

		Table table = new Table();
		Tr tr = new Tr();
		Td td = new Td();

		H1 h1 = new H1();
		h1.appendChild(new Text("Stats for: " + player));
		body.appendChild(h1);

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Kills"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getKills()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Deaths"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getDeaths()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Teamkills"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getTeamKills()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Flag Captures"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getFlagCaps()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Flag Capture Assists"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getFlagCapAssists()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Flag Neutralizes"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getFlagNeutralizes()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Flag Neutralize Assits"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getFlagNeutralizeAssist()));

		tr = new Tr();
		table.appendChild(tr);

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text("Flag Defends"));

		td = new Td();
		tr.appendChild(td);
		td.appendChild(new Text(stats.getFlagDefends()));

		H4 h2 = new H4();
		h2.appendChild(new Text(FragalyzerConstants.vehicletypekills));

		body.appendChild(table);
		body.appendChild(h2);
		table = new Table();

		for (Map.Entry<VehicleType, Integer> entry : MapUtil.sortByValueDesc(stats.getVehicleTypeKills()).entrySet()) {
			tr = new Tr();
			// tr = new Tr();

			td = new Td();
			td.appendChild(new Text(FragalyzerConstants.vehicleTypeNames.get(entry.getKey())));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(entry.getValue()));
			tr.appendChild(td);
			table.appendChild(tr);
		}
		body.appendChild(table);

		h2 = new H4();
		h2.appendChild(new Text(FragalyzerConstants.vehiclekill));

		body.appendChild(h2);
		table = new Table();

		for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getVehicleNameKills()).entrySet()) {
			tr = new Tr();
			// tr = new Tr();

			td = new Td();
			td.appendChild(new Text(entry.getKey()));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(entry.getValue()));
			tr.appendChild(td);
			table.appendChild(tr);
		}
		body.appendChild(table);

		h2 = new H4();
		h2.appendChild(new Text(FragalyzerConstants.weaponkill));

		body.appendChild(h2);
		table = new Table();

		for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getWeaponNameKills()).entrySet()) {
			tr = new Tr();
			// tr = new Tr();

			td = new Td();
			td.appendChild(new Text(entry.getKey()));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(entry.getValue()));
			tr.appendChild(td);
			table.appendChild(tr);
		}
		body.appendChild(table);

		h2 = new H4();
		h2.appendChild(new Text(FragalyzerConstants.enemies));

		body.appendChild(h2);
		table = new Table();

		for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getEnemies()).entrySet()) {
			tr = new Tr();
			// tr = new Tr();

			td = new Td();
			td.appendChild(new Text(entry.getKey()));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(entry.getValue()));
			tr.appendChild(td);
			table.appendChild(tr);
		}
		body.appendChild(table);

		h2 = new H4();
		h2.appendChild(new Text(FragalyzerConstants.victims));

		body.appendChild(h2);
		table = new Table();

		for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getVictims()).entrySet()) {
			tr = new Tr();
			// tr = new Tr();

			td = new Td();
			td.appendChild(new Text(entry.getKey()));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(entry.getValue()));
			tr.appendChild(td);
			table.appendChild(tr);
		}
		body.appendChild(table);

		getReport().appendChild(body);

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	private void appendToHTLMReportDouble(String map_round, String scope, HashMap<String, Double> ranks) {

		int count = 0;
		Title title = new Title();
		title.appendChild(new Text(map_round + " " + getScopeName()));
		getReport().appendChild(title);

		Body body = new Body();

		Table table = new Table();
		Tr tr = new Tr();
		Td td = new Td();

		// H1 h1 = new H1();
		// h1.appendChild(new Text(scope));
		// body.appendChild(h1);

		table = new Table();

		H2 h2 = new H2();

		for (Map.Entry<String, Double> entry : MapUtil.sortByValueDesc(ranks).entrySet()) {
			tr = new Tr();
			// tr = new Tr();
			table.appendChild(tr);
			td = new Td();
			td.appendChild(new Text(entry.getKey()));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(round(entry.getValue(), 2)));
			tr.appendChild(td);
			count = count + entry.getValue().intValue();
		}

		h2.appendChild(new Text(map_round + " (" + count + ")"));
		body.appendChild(h2);

		body.appendChild(table);
		getReport().appendChild(body);

	}

	private void appendToHTLMReport(String map_round, String scope, HashMap<String, Integer> ranks) {

		int count = 0;
		Title title = new Title();
		title.appendChild(new Text(map_round + " " + getScopeName()));
		getReport().appendChild(title);

		Body body = new Body();

		Table table = new Table();
		Tr tr = new Tr();
		Td td = new Td();

		// H1 h1 = new H1();
		// h1.appendChild(new Text(scope));
		// body.appendChild(h1);

		table = new Table();

		H2 h2 = new H2();

		for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(ranks).entrySet()) {
			tr = new Tr();
			// tr = new Tr();
			table.appendChild(tr);
			td = new Td();
			td.appendChild(new Text(entry.getKey()));
			tr.appendChild(td);
			td = new Td();
			td.appendChild(new Text(entry.getValue()));
			tr.appendChild(td);
			count = count + entry.getValue().intValue();
		}

		h2.appendChild(new Text(map_round + " (" + count + ")"));
		body.appendChild(h2);

		body.appendChild(table);
		getReport().appendChild(body);

	}

	/*
	 * Per player, count his CP actions
	 */
	private PlayerStats countCPStuff(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();

		LogEntry logEntry;
		int cpCaptures = 0;
		int cpCaptureAssists = 0;
		int cpDefends = 0;
		int cpNeutralizes = 0;
		int cpNeutralizeAssists = 0;

		while (it.hasNext()) {
			logEntry = it.next();
			if (logEntry.getEventType().equals(EventType.SCORE)) {
				switch (logEntry.getCpEvent()) {
				case cpCaptures:
					cpCaptures++;
					break;
				case cpAssists:
					cpCaptureAssists++;
					break;
				case cpDefends:
					cpDefends++;
					break;
				case cpNeutralizes:
					cpNeutralizes++;
					break;
				case cpNeutralizeAssists:
					cpNeutralizeAssists++;
					break;
				default:
					break;
				}
			}

		}

		stats.setFlagCaps(cpCaptures);
		stats.setFlagCapAssists(cpCaptureAssists);
		stats.setFlagDefends(cpDefends);
		stats.setFlagNeutralizeAssist(cpNeutralizeAssists);
		stats.setFlagNeutralizes(cpNeutralizes);
		getCpCapRanking().put(stats.getPlayerName(), new Integer(stats.getFlagCaps()));
		getCpCapAassistRanking().put(stats.getPlayerName(), new Integer(stats.getFlagCapAssists()));
		getCpDefendRanking().put(stats.getPlayerName(), new Integer(stats.getFlagDefends()));
		getCpNeutralizeRanking().put(stats.getPlayerName(), new Integer(stats.getFlagNeutralizes()));
		getCpNeutralizeAssistRanking().put(stats.getPlayerName(), new Integer(stats.getFlagNeutralizeAssist()));

		// System.out.println("Player " + cpCaptures + " " + cpCaptureAssists +
		// " " + cpNeutralizeAssists + " " +cpNeutralizes + " " + cpDefends + "
		// " + stats.getPlayerName());

		return stats;
	}

	/*
	 * Per player, count how often he was killed
	 */
	private PlayerStats countDeaths(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();

		LogEntry logEntry;
		int deaths = 0;

		while (it.hasNext()) {
			logEntry = it.next();
			if (logEntry.getEventType().equals(EventType.KILL) && !logEntry.getKillType().equals(KillType.SUICIDE)
					&& logEntry.getVictim().equals(stats.getPlayerName())) {
				deaths++;
			}

		}

		stats.setDeaths(deaths);
		getDeathsRanking().put(stats.getPlayerName(), new Integer(stats.getDeaths()));

		return stats;
	}

	/*
	 * Per player, count the kills with a special WeaponType
	 */
	private PlayerStats countEnemies(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();
		HashMap<String, Integer> opponents = new HashMap<>();

		LogEntry logEntry;

		while (it.hasNext()) {
			logEntry = it.next();
			if (logEntry.getEventType().equals(EventType.KILL) && !logEntry.getKillType().equals(KillType.SUICIDE)
					&& logEntry.getVictim().equals(stats.getPlayerName())) {
				// System.out.println(logEntry.getAttackerWeaponType() + ": " +
				// logEntry.getWeapon());
				if (opponents.containsKey(logEntry.getPlayer()))
					opponents.put(logEntry.getPlayer(),
							new Integer(opponents.get(logEntry.getPlayer()).intValue() + 1));
				else
					opponents.put(logEntry.getPlayer(), new Integer(1));
			}

		}

		stats.setEnemies(opponents);

		return stats;
	}

	/*
	 * Per player, count how many kills he had without TKs and Suicides
	 */
	private PlayerStats countKills(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();

		LogEntry logEntry;
		int kills = 0;

		while (it.hasNext()) {
			logEntry = it.next();
			if (isRealKill(logEntry) && logEntry.getPlayer().equals(stats.getPlayerName())) {
				kills++;
			}

		}

		stats.setKills(kills);
		getKillRanking().put(stats.getPlayerName(), new Integer(stats.getKills()));

		return stats;
	}

	/*
	 * Per player, count how often he teamkilled
	 */
	private PlayerStats countTeamKills(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();

		LogEntry logEntry;
		int tks = 0;

		while (it.hasNext()) {
			logEntry = it.next();
			if (!logEntry.getKillType().equals(KillType.SUICIDE) && logEntry.isTeamkill()
					&& logEntry.getPlayer().equals(stats.getPlayerName())) {
				tks++;
			}

		}

		stats.setTeamKills(tks);

		getTkRanking().put(stats.getPlayerName(), new Integer(stats.getTeamKills()));

		return stats;
	}

	/*
	 * Per player, count how often he was teamkilled
	 */
	private PlayerStats countTeamKilled(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();

		LogEntry logEntry;
		int tks = 0;

		while (it.hasNext()) {
			logEntry = it.next();
			if (!logEntry.getKillType().equals(KillType.SUICIDE) && logEntry.isTeamkill()
					&& logEntry.getVictim().equals(stats.getPlayerName())) {
				tks++;
			}

		}

		stats.setTeamKilled(tks);

		getTkedRanking().put(stats.getPlayerName(), new Integer(stats.getTeamKilled()));

		return stats;
	}

	/*
	 * Per player, count the kills with a special VehicleType
	 */
	private PlayerStats countVehicleKills(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();
		HashMap<String, Integer> vehicleKill = new HashMap<>();
		HashMap<VehicleType, Integer> vehicleTypeKill = new HashMap<>();

		HashMap<String, Integer> vehicleTypeKillRankingForVehicleType;
		HashMap<String, Integer> vehicleNameKillRanking;
		LogEntry logEntry = null;

		while (it.hasNext()) {
			logEntry = it.next();
			if (logEntry.getPlayer().equals(stats.getPlayerName()) && isRealKill(logEntry)) {
				switch (logEntry.getKillType()) {
				case VEHICLE_INF:
				case VEHICLE_VEHICLE:
					if (vehicleKill.containsKey(logEntry.getAttackerVehicleName()))
						vehicleKill.put(logEntry.getAttackerVehicleName(),
								new Integer(vehicleKill.get(logEntry.getAttackerVehicleName()).intValue() + 1));
					else
						vehicleKill.put(logEntry.getAttackerVehicleName(), new Integer(1));
					if (vehicleTypeKill.containsKey(logEntry.getAttackerVehicleType()))
						vehicleTypeKill.put(logEntry.getAttackerVehicleType(),
								new Integer(vehicleTypeKill.get(logEntry.getAttackerVehicleType()).intValue() + 1));
					else
						vehicleTypeKill.put(logEntry.getAttackerVehicleType(), new Integer(1));

					if (!getVehicleTypeRanking().containsKey((logEntry.getAttackerVehicleType())))
						vehicleTypeKillRankingForVehicleType = new HashMap<>();
					else
						vehicleTypeKillRankingForVehicleType = getVehicleTypeRanking()
								.get(logEntry.getAttackerVehicleType());

					if (!vehicleTypeKillRankingForVehicleType.containsKey(logEntry.getPlayer()))
						vehicleTypeKillRankingForVehicleType.put(logEntry.getPlayer(), new Integer(1));
					else
						vehicleTypeKillRankingForVehicleType.put(logEntry.getPlayer(), new Integer(
								vehicleTypeKillRankingForVehicleType.get(logEntry.getPlayer()).intValue() + 1));

					getVehicleTypeRanking().put(logEntry.getAttackerVehicleType(),
							vehicleTypeKillRankingForVehicleType);

					// from here
					if (!getVehicleNameRanking().containsKey((logEntry.getAttackerVehicleName())))
						vehicleNameKillRanking = new HashMap<>();
					else
						vehicleNameKillRanking = getVehicleNameRanking().get(logEntry.getAttackerVehicleName());

					if (!vehicleNameKillRanking.containsKey(logEntry.getPlayer()))
						vehicleNameKillRanking.put(logEntry.getPlayer(), new Integer(1));
					else
						vehicleNameKillRanking.put(logEntry.getPlayer(),
								new Integer(vehicleNameKillRanking.get(logEntry.getPlayer()).intValue() + 1));

					getVehicleNameRanking().put(logEntry.getAttackerVehicleName(), vehicleNameKillRanking);
					// to here
					break;
				default:
					break;
				}

			}
		}

		stats.setVehicleNameKills(vehicleKill);
		stats.setVehicleTypeKills(vehicleTypeKill);

		return stats;
	}

	private PlayerStats countVictims(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();
		HashMap<String, Integer> opponents = new HashMap<>();

		LogEntry logEntry;

		while (it.hasNext()) {
			logEntry = it.next();
			if (isRealKill(logEntry) && !logEntry.getVictim().equals(stats.getPlayerName())) {
				// System.out.println(logEntry.getAttackerWeaponType() + ": " +
				// logEntry.getWeapon());
				if (opponents.containsKey(logEntry.getVictim()))
					opponents.put(logEntry.getVictim(),
							new Integer(opponents.get(logEntry.getVictim()).intValue() + 1));
				else
					opponents.put(logEntry.getVictim(), new Integer(1));
			}

		}

		stats.setVictims(opponents);

		return stats;
	}

	/*
	 * Per player, count the kills with a special WeaponType
	 */
	private PlayerStats countWeaponKills(PlayerStats stats) {
		Iterator<LogEntry> it = stats.getEvents().iterator();
		HashMap<String, Integer> weaponKill = new HashMap<>();
		HashMap<WeaponType, Integer> weaponTypeKill = new HashMap<>();
		HashMap<String, Integer> weaponTypeKillRankingForVehicleType;
		HashMap<String, Integer> weaponNameKillRanking;

		LogEntry logEntry;

		while (it.hasNext()) {
			logEntry = it.next();
			if (logEntry.getPlayer().equals(stats.getPlayerName()) && isRealKill(logEntry)) {
				switch (logEntry.getKillType()) {
				case INF_INF:
				case INF_VEHICLE:
					// Count weapon Kills
					if (weaponKill.containsKey(logEntry.getWeaponName()))
						weaponKill.put(logEntry.getWeaponName(),
								new Integer(weaponKill.get(logEntry.getWeaponName()).intValue() + 1));
					else
						weaponKill.put(logEntry.getWeaponName(), new Integer(1));

					// Count weaponType kills
					if (weaponTypeKill.containsKey(logEntry.getAttackerWeaponType()))
						weaponTypeKill.put(logEntry.getAttackerWeaponType(),
								new Integer(weaponTypeKill.get(logEntry.getAttackerWeaponType()).intValue() + 1));
					else
						weaponTypeKill.put(logEntry.getAttackerWeaponType(), new Integer(1));

					if (!getWeaponTypeRanking().containsKey((logEntry.getAttackerWeaponType())))
						weaponTypeKillRankingForVehicleType = new HashMap<>();
					else
						weaponTypeKillRankingForVehicleType = getWeaponTypeRanking()
								.get(logEntry.getAttackerWeaponType());

					if (!weaponTypeKillRankingForVehicleType.containsKey(logEntry.getPlayer()))
						weaponTypeKillRankingForVehicleType.put(logEntry.getPlayer(), new Integer(1));
					else
						weaponTypeKillRankingForVehicleType.put(logEntry.getPlayer(), new Integer(
								weaponTypeKillRankingForVehicleType.get(logEntry.getPlayer()).intValue() + 1));

					getWeaponTypeRanking().put(logEntry.getAttackerWeaponType(), weaponTypeKillRankingForVehicleType);

					// from here
					if (!getWeaponNameRanking().containsKey((logEntry.getWeaponName())))
						weaponNameKillRanking = new HashMap<>();
					else
						weaponNameKillRanking = getWeaponNameRanking().get(logEntry.getWeaponName());

					if (!weaponNameKillRanking.containsKey(logEntry.getPlayer()))
						weaponNameKillRanking.put(logEntry.getPlayer(), new Integer(1));
					else
						weaponNameKillRanking.put(logEntry.getPlayer(),
								new Integer(weaponNameKillRanking.get(logEntry.getPlayer()).intValue() + 1));

					getWeaponNameRanking().put(logEntry.getWeaponName(), weaponNameKillRanking);
					if (logEntry.getWeaponName() == null)
						System.out.println("null" + logEntry.toString());
					// to here

					break;
				default:
					break;
				}
			}

		}

		stats.setWeaponNameKills(weaponKill);
		stats.setWeaponTypeKills(weaponTypeKill);

		return stats;
	}

	private HashMap<String, PlayerStats> createPlayerStats(HashMap<String, PlayerStats> playerEvents) {
		// Walk over each player
		HashMap<String, PlayerStats> stats = playerEvents;
		for (Map.Entry<String, PlayerStats> entry : stats.entrySet()) {
			String player = entry.getKey();
			PlayerStats playerStats = entry.getValue();

			// Count number of kill per Weapon
			playerStats = countWeaponKills(playerStats);

			// Count number of kill per Vehicle
			playerStats = countVehicleKills(playerStats);

			// Count number of victims
			playerStats = countVictims(playerStats);

			// Count number of enemies
			playerStats = countEnemies(playerStats);

			// Count number of kills
			playerStats = countKills(playerStats);

			// Count number of deaths
			playerStats = countDeaths(playerStats);

			// Count number of TKs
			playerStats = countTeamKills(playerStats);

			// Count number of being TKed
			playerStats = countTeamKilled(playerStats);

			// Count number of CP Things
			playerStats = countCPStuff(playerStats);

			if (playerStats.getDeaths() > 0)
				playerStats.setKdrRatio((double) playerStats.getKills() / (double) playerStats.getDeaths());
			getKdrRanking().put(playerStats.getPlayerName(), new Double(playerStats.getKdrRatio()));

			stats.put(player, playerStats);
		}

		return stats;
	}

	private String dumpRank(String name, HashMap<String, Integer> ranks) {
		JSONObject obj, event;
		JSONArray list;
		obj = new JSONObject();
		list = new JSONArray();

		for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(ranks).entrySet()) {
			event = new JSONObject();
			event.put(entry.getKey(), entry.getValue());

			list.add(event);
		}
		obj.put(name, list);

		appendToHTLMReport(name, getScope().name(), ranks);

		return obj.toJSONString();
	}

	private String dumpRankDouble(String name, HashMap<String, Double> ranks) {

		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.CEILING);

		JSONObject obj, event;
		JSONArray list;
		obj = new JSONObject();
		list = new JSONArray();
		for (Map.Entry<String, Double> entry : MapUtil.sortByValueDesc(ranks).entrySet()) {
			event = new JSONObject();
			event.put(entry.getKey(), String.format(df.format(entry.getValue().doubleValue())));
			list.add(event);
		}
		obj.put(name, list);

		appendToHTLMReportDouble(name, getScope().name(), ranks);

		return obj.toJSONString();
	}

	private void dumpRanks(HashMap<String, PlayerStats> playerEvents, String name) {

		// Getting a Set of Key-value pairs
		Set<Entry<String, PlayerStats>> entrySet = playerEvents.entrySet();

		String player;
		PlayerStats stats;
		JSONArray list;

		// Obtaining an iterator for the entry set
		Iterator<Map.Entry<String, PlayerStats>> it = entrySet.iterator();
		JSONObject obj, event;

		FileWriter fw = null;
		String line = new String();
		try {
			fw = new FileWriter(getBasePath() + "//" + getScope() + "_" + name + "_stats.json");
			fw.append(System.getProperty("line.separator")); // e.g.
			fw.write("[");

			fw.write(dumpRank(FragalyzerConstants.killrankings, getKillRanking()) + ",");
			fw.write(dumpRank(FragalyzerConstants.deathrankings, getDeathsRanking()) + ",");
			fw.write(dumpRank(FragalyzerConstants.tkrankings, getTkRanking()) + ",");
			fw.write(dumpRank(FragalyzerConstants.tkedrankings, getTkedRanking()) + ",");
			// fw.write(dumpRank(FragalyzerConstants.cpcapassistranking,
			// getCpCapAassistRanking()) + ",");
			// fw.write(dumpRank(FragalyzerConstants.cpcapranking,
			// getCpCapRanking()) + ",");
			// fw.write(dumpRank(FragalyzerConstants.cpdefendranking,
			// getCpDefendRanking()) + ",");
			// fw.write(dumpRank(FragalyzerConstants.cpneutralizeassistranking,
			// getCpNeutralizeAssistRanking()) + ",");
			// fw.write(dumpRank(FragalyzerConstants.cpneutralizeranking,
			// getCpNeutralizeRanking()) + ",");
			fw.write(dumpRankDouble(FragalyzerConstants.kdrranking, getKdrRanking()) + ",");
			dumpVehicleTypeRank(FragalyzerConstants.vehicletype, getVehicleTypeRanking(), getVehicleNameRanking(), fw);
			// dumpVehicleNameRank(FragalyzerConstants.vehiclename,
			// getVehicleNameRanking(), fw);
			dumpWeaponTypeRank(FragalyzerConstants.weapontype, getWeaponTypeRanking(), getWeaponNameRanking(), fw);
			// dumpWeaponNameRank(FragalyzerConstants.weaponname,
			// getWeaponNameRanking(), fw);
			while (it.hasNext()) {

				obj = new JSONObject();
				event = new JSONObject();
				list = new JSONArray();
				Map.Entry<String, PlayerStats> me = it.next();
				/*
				 * fw = new FileWriter(basePath + "//stats_" +
				 * me.getKey().replaceAll("[^A-Za-z0-9]", "") + ".json");
				 * fw.append(System.getProperty("line.separator")); // e.g.
				 * fw.write("[");
				 */
				player = me.getKey();
				stats = me.getValue();

				obj.put("player", player);
				obj.put("kills", stats.getKills());
				obj.put("deaths", stats.getDeaths());
				obj.put("teamkills", stats.getTeamKills());
				obj.put("cpcaps", stats.getFlagCaps());
				obj.put("cpcapassists", stats.getFlagCapAssists());
				obj.put("cpdefends", stats.getFlagDefends());
				obj.put("cpneutralizes", stats.getFlagNeutralizes());
				obj.put("cpneutralizeassists", stats.getFlagNeutralizeAssist());

				appendPlayerStats(player, stats);

				list = new JSONArray();
				for (Map.Entry<VehicleType, Integer> entry : MapUtil.sortByValueDesc(stats.getVehicleTypeKills())
						.entrySet()) {
					event = new JSONObject();
					event.put(FragalyzerConstants.vehicleTypeNames.get(entry.getKey()), entry.getValue());
					list.add(event);

				}
				obj.put("vehicletypekills", list);

				list = new JSONArray();
				for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getWeaponNameKills())
						.entrySet()) {
					event = new JSONObject();
					event.put(entry.getKey(), entry.getValue());
					list.add(event);
				}
				obj.put("weaponkill", list);

				list = new JSONArray();
				for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getVehicleNameKills())
						.entrySet()) {
					event = new JSONObject();
					event.put(entry.getKey(), entry.getValue());
					list.add(event);
				}
				obj.put("vehiclekill", list);

				list = new JSONArray();
				for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getVictims()).entrySet()) {
					event = new JSONObject();
					event.put(entry.getKey(), entry.getValue());
					list.add(event);
				}
				obj.put("victims", list);

				list = new JSONArray();
				for (Map.Entry<String, Integer> entry : MapUtil.sortByValueDesc(stats.getEnemies()).entrySet()) {
					event = new JSONObject();
					event.put(entry.getKey(), entry.getValue());
					list.add(event);
				}
				obj.put("enemies", list);

				line = obj.toJSONString();
				if (it.hasNext())
					line = line + ",";
				fw.write(line);

				/*
				 * fw.write(obj.toJSONString());
				 *
				 * fw.append(System.getProperty("line.separator")); // e.g.
				 * fw.write("]"); // "\n" fw.close();
				 */
			}

			fw.append(System.getProperty("line.separator")); // e.g.
			fw.write("]"); // "\n"
			fw.close();

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

		try {
			fw = new FileWriter(getBasePath() + "//" + getScope() + "_" + getScopeName() + "_stats.html");
			fw.write(getReport().write());
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	private void dumpVehicleNameRank(String name, HashMap<String, HashMap<String, Integer>> ranks, FileWriter fw) {

		for (Map.Entry<String, HashMap<String, Integer>> entry : ranks.entrySet()) {
			try {
				fw.write(dumpRank(entry.getKey(), entry.getValue()) + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void dumpVehicleTypeRank(String name, HashMap<VehicleType, HashMap<String, Integer>> ranks,
			HashMap<String, HashMap<String, Integer>> vehicleNameRanking, FileWriter fw) {
		int count = 0;

		for (Map.Entry<VehicleType, HashMap<String, Integer>> entry : ranks.entrySet()) {
			try {
				fw.write(dumpRank(FragalyzerConstants.vehicleTypeNames.get(entry.getKey()), entry.getValue()) + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Map.Entry<String, HashMap<String, Integer>> entry2 : vehicleNameRanking.entrySet()) {
				if (entry2.getKey() != null) {
					String tn = findTemplateNameFromHudName(entry2.getKey()).toLowerCase();
					if (FragalyzerConstants.vehicleTypes.containsKey(tn)) {
						if (FragalyzerConstants.vehicleTypes.get(tn.toLowerCase()).equals(entry.getKey())) {
							try {
								fw.write(dumpRank(entry2.getKey(), entry2.getValue()) + ",");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else
						System.out.println("Missing template for " + tn);
				}
			}
		}

	}

	private String findTemplateNameFromHudName(String name) {
		for (Entry<String, String> entry : FragalyzerConstants.names.entrySet()) {
			if (Objects.equals(name, entry.getValue())) {
				return entry.getKey();
			}
		}
		return "Unknown";
	}

	private String findWeaponTemplateNameFromHudName(String name) {
		for (Entry<String, String> entry : FragalyzerConstants.names.entrySet()) {
			if (Objects.equals(name, entry.getValue())) {
				return entry.getKey();
			}
		}
		return "Unknown";
	}

	private void dumpVehicleTypeRank(String name, HashMap<VehicleType, HashMap<String, Integer>> ranks, FileWriter fw) {
		int count = 0;

		for (Map.Entry<VehicleType, HashMap<String, Integer>> entry : ranks.entrySet()) {
			try {
				fw.write(dumpRank(FragalyzerConstants.vehicleTypeNames.get(entry.getKey()), entry.getValue()) + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

	private void dumpWeaponTypeRank(String name, HashMap<WeaponType, HashMap<String, Integer>> ranks,
			HashMap<String, HashMap<String, Integer>> weaponNameRanks, FileWriter fw) {

		for (Map.Entry<WeaponType, HashMap<String, Integer>> entry : ranks.entrySet()) {
			try {
				fw.write(dumpRank(FragalyzerConstants.weaponTypeNames.get(entry.getKey()), entry.getValue()) + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (Map.Entry<String, HashMap<String, Integer>> entry2 : weaponNameRanks.entrySet()) {
				if (entry2.getKey() != null) {
					String tn = findWeaponTemplateNameFromHudName(entry2.getKey()).toLowerCase();
					if (FragalyzerConstants.weaponTypes.containsKey(tn)) {
						if (FragalyzerConstants.weaponTypes.get(tn.toLowerCase()).equals(entry.getKey())) {
							try {
								fw.write(dumpRank(entry2.getKey(), entry2.getValue()) + ",");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else
						System.out.println("Missing template for " + tn);
				}
			}
		}

	}

	private void dumpWeaponNameRank(String name, HashMap<String, HashMap<String, Integer>> ranks, FileWriter fw) {

		for (Map.Entry<String, HashMap<String, Integer>> entry : ranks.entrySet()) {
			try {
				fw.write(dumpRank(entry.getKey(), entry.getValue()) + ",");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getBasePath() {
		return basePath;
	}

	public HashMap<String, Integer> getCpCapAassistRanking() {
		return cpCapAassistRanking;
	}

	public HashMap<String, Integer> getCpCapRanking() {
		return cpCapRanking;
	}

	public HashMap<String, Integer> getCpDefendRanking() {
		return cpDefendRanking;
	}

	public HashMap<String, Integer> getCpNeutralizeAssistRanking() {
		return cpNeutralizeAssistRanking;
	}

	public HashMap<String, Integer> getCpNeutralizeRanking() {
		return cpNeutralizeRanking;
	}

	public HashMap<String, Integer> getDeathsRanking() {
		return deathsRanking;
	}

	public HashMap<String, Double> getKdrRanking() {
		return kdrRanking;
	}

	public HashMap<String, Integer> getKillRanking() {
		return killRanking;
	}

	public Html getReport() {
		return report;
	}

	public StatScope getScope() {
		return scope;
	}

	public String getScopeName() {
		return scopeName;
	}

	public int getTd_counter() {
		return td_counter;
	}

	public String getTeam() {
		return team;
	}

	public HashMap<String, Integer> getTkRanking() {
		return tkRanking;
	}

	public HashMap<String, Integer> getTkedRanking() {
		return tkedRanking;
	}

	private Tr getTr() {
		int c = getTd_counter();
		c++;
		if (getTd_counter() == 8) {
			tr = new Tr();
			c = 1;

		}
		setTd_counter(c);
		return tr;
	}

	public HashMap<String, HashMap<String, Integer>> getVehicleNameRanking() {
		return vehicleNameRanking;
	}

	public HashMap<VehicleType, HashMap<String, Integer>> getVehicleTypeRanking() {
		return vehicleTypeRanking;
	}

	public HashMap<String, HashMap<KitType, Integer>> getWeaponRanking() {
		return weaponRanking;
	}

	public HashMap<WeaponType, HashMap<String, Integer>> getWeaponTypeRanking() {
		return weaponTypeRanking;
	}

	private void init() {
		setKillRanking(new HashMap<>());
		setDeathsRanking(new HashMap<>());
		setTkRanking(new HashMap<>());
		setTkedRanking(new HashMap<>());
		setCpCapRanking(new HashMap<>());
		setCpCapAassistRanking(new HashMap<>());
		setCpDefendRanking(new HashMap<>());
		setCpNeutralizeAssistRanking(new HashMap<>());
		setCpNeutralizeRanking(new HashMap<>());
		setVehicleTypeRanking(new HashMap<>());
		setVehicleNameRanking(new HashMap<>());
		setWeaponNameRanking(new HashMap<>());
		setWeaponTypeRanking(new HashMap<>());
		setWeaponRanking(new HashMap<>());
		setKdrRanking(new HashMap<>());

	}

	private boolean isRealKill(LogEntry logEntry) {
		return logEntry.getEventType().equals(EventType.KILL) && !logEntry.getKillType().equals(KillType.SUICIDE)
				&& !logEntry.isTeamkill();
	}

	public void rank(ArrayList<LogEntry> logEntries, String name, StatScope scope) {
		setScope(scope);
		setScopeName(name);
		Iterator<LogEntry> it = logEntries.iterator();
		setReport(new Html());
		tr = new Tr();
		table = new Table();
		setTd_counter(0);
		LogEntry kill;

		init();

		PlayerStats stats = new PlayerStats();
		HashMap<String, PlayerStats> playerEvents = new HashMap<>();
		LogEntry logEntry;
		while (it.hasNext()) {
			logEntry = it.next();

			if (logEntry.getEventType().equals(EventType.KILL) || logEntry.getEventType().equals(EventType.SCORE)) {

				kill = logEntry;

				// Add to player's eventlist
				if (!playerEvents.containsKey(kill.getPlayer())) {
					stats = new PlayerStats();
					stats.setPlayerName(kill.getPlayer());

				} else {
					stats = playerEvents.get(kill.getPlayer());
				}

				stats.getEvents().add(kill);
				playerEvents.put(kill.getPlayer(), stats);

				if (logEntry.getEventType().equals(EventType.KILL)) {
					if (!playerEvents.containsKey(kill.getVictim())) {
						stats = new PlayerStats();
						stats.setPlayerName(kill.getVictim());
					} else {
						stats = playerEvents.get(kill.getVictim());
					}

					stats.getEvents().add(kill);
					playerEvents.put(kill.getVictim(), stats);
				}

			}

		}

		playerEvents = createPlayerStats(playerEvents);

		dumpRanks(playerEvents, name);

	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void setCpCapAassistRanking(HashMap<String, Integer> cpCapAassistRanking) {
		this.cpCapAassistRanking = cpCapAassistRanking;
	}

	public void setCpCapRanking(HashMap<String, Integer> cpCapRanking) {
		this.cpCapRanking = cpCapRanking;
	}

	public void setCpDefendRanking(HashMap<String, Integer> cpDefendRanking) {
		this.cpDefendRanking = cpDefendRanking;
	}

	public void setCpNeutralizeAssistRanking(HashMap<String, Integer> cpNeutralizeAssistRanking) {
		this.cpNeutralizeAssistRanking = cpNeutralizeAssistRanking;
	}

	public void setCpNeutralizeRanking(HashMap<String, Integer> cpNeutralizeRanking) {
		this.cpNeutralizeRanking = cpNeutralizeRanking;
	}

	public void setDeathsRanking(HashMap<String, Integer> deathsRanking) {
		this.deathsRanking = deathsRanking;
	}

	public void setKdrRanking(HashMap<String, Double> kdrRanking) {
		this.kdrRanking = kdrRanking;
	}

	public void setKillRanking(HashMap<String, Integer> killRanking) {
		this.killRanking = killRanking;
	}

	public void setReport(Html report) {
		this.report = report;
	}

	public void setScope(StatScope scope) {
		this.scope = scope;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public void setTd_counter(int td_counter) {
		this.td_counter = td_counter;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public void setTkRanking(HashMap<String, Integer> tkRanking) {
		this.tkRanking = tkRanking;
	}

	public void setTkedRanking(HashMap<String, Integer> tkedRanking) {
		this.tkedRanking = tkedRanking;
	}

	public void setVehicleNameRanking(HashMap<String, HashMap<String, Integer>> vehicleNameRanking) {
		this.vehicleNameRanking = vehicleNameRanking;
	}

	public void setVehicleTypeRanking(HashMap<VehicleType, HashMap<String, Integer>> vehicleTypeRanking) {
		this.vehicleTypeRanking = vehicleTypeRanking;
	}

	public void setWeaponRanking(HashMap<String, HashMap<KitType, Integer>> weaponRanking) {
		this.weaponRanking = weaponRanking;
	}

	public void setWeaponTypeRanking(HashMap<WeaponType, HashMap<String, Integer>> weaponTypeRanking) {
		this.weaponTypeRanking = weaponTypeRanking;
	}

}
