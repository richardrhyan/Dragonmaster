package com.gce.dragonmaster.network.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.gce.dragonmaster.gui.frames.GameFrame;
import com.gce.dragonmaster.network.Player;

public class GameOverInfo extends ServerInfo {
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> scores = new HashMap<String, Integer>();
	
	public GameOverInfo(Vector<Player> players) {
		for (Player p : players)
			scores.put(p.getPlayerName(), p.getGold());
	}
	
	public GameOverInfo(Map<String, Integer> scores) {
		this.scores = scores;
	}
	
	static class MapUtil {
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
    
		List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		String message = "";
		String title = "";
		
		scores = MapUtil.sortByValue(scores);
		List<String> keys = new ArrayList<String>(scores.keySet());
		for (String key : keys)
			message += String.format("%s has %d\n", key, scores.get(key));
		
		title = String.format("%s WINS!", keys.get(0));
		
		JOptionPane.showMessageDialog(
				gameFrame,
				message,
				title,
				JOptionPane.INFORMATION_MESSAGE);

		gameFrame.close();
	}
}
