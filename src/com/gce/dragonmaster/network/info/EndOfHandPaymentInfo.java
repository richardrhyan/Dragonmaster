package com.gce.dragonmaster.network.info;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;

import com.gce.dragonmaster.game.GameHand;
import com.gce.dragonmaster.gui.frames.GameFrame;

public class EndOfHandPaymentInfo extends ServerInfo {
	private static final long serialVersionUID = 1L;
	Map<String, Integer>	payments	= new LinkedHashMap<String, Integer>();
	GameHand 				currentHand;
	
	public EndOfHandPaymentInfo(Map<String, Integer> payments, GameHand currentHand) {
		this.payments = payments;
		this.currentHand = currentHand;
	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		String s = "";
		List<String> keys = new ArrayList<String>(payments.keySet());
		for (String key : keys)
			s += String.format("%s pays out %.2d\n", key, payments.get(key)); 
		
		JOptionPane.showMessageDialog(gameFrame,
				s,
				"End of hand: " + currentHand.toString(),
				JOptionPane.INFORMATION_MESSAGE);
	}
}
