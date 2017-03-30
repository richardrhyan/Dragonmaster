package com.gce.dragonmaster.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gce.dragonmaster.network.GameServer;
import com.rhyan.network.HTMLUtilities;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {
	private final String GOLD = "Gold: ";
	private Font font = new Font(Font.SERIF, Font.PLAIN, 24);
	
	private	JLabel	nameLabel		= new JLabel();
	private	JLabel	goldLabel 		= new JLabel();
	private JLabel	ipAddressLabel	= new JLabel();
	
	
	public PlayerPanel(String ipAddress) {
		setLayout(new BorderLayout());
		
		// Set initial value of labels
		nameLabel.setText("Unknown");
		nameLabel.setFont(font);
		
		goldLabel.setText(GOLD + "unknown");
		goldLabel.setFont(font);
		
		if (ipAddress.equals(GameServer.LOCALHOST)) {
			String ip = "Undetermined";
			try {
				ip = HTMLUtilities.getMyInternetIPAddress();
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	ipAddressLabel.setText("Hosting at " + ip);
		} else {
			ipAddressLabel.setText("Host Address: " + ipAddress);
		}
		ipAddressLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
		ipAddressLabel.setHorizontalAlignment(JLabel.CENTER);
		ipAddressLabel.setVerticalAlignment(JLabel.BOTTOM);
		
		
		// Add labels to panel
		add(nameLabel, BorderLayout.WEST);
		add(goldLabel, BorderLayout.EAST);
		add(ipAddressLabel, BorderLayout.CENTER);
		// Add border to panel
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	}
	
	public void setPlayertName(String name) {
		nameLabel.setText(name);
	}
	
	public void setGold(int gold) {
		DecimalFormat myFormatter = new DecimalFormat("###,###");
		goldLabel.setText(GOLD + myFormatter.format(gold));
	}
}
