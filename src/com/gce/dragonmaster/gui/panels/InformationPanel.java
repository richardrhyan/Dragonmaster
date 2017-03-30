package com.gce.dragonmaster.gui.panels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class InformationPanel extends JLabel {
	List<String>	messages = new ArrayList<String>();
	List<Color>		textColor = new ArrayList<Color>();
	MyTimer				timer;
	
	public InformationPanel() {
		setText("Messages display here...");
	}
	
	public void addMessage(String message, Color textColor) {
		this.messages.add(message);
		this.textColor.add(textColor);
		
		if (timer == null || !timer.isAlive()) {
			timer = new MyTimer();
			timer.start();
		}
	}
	
	public void addMessage(String message) {
		addMessage(message, Color.BLACK);
	}
	
	
	class MyTimer extends Thread {

		@Override
		public void run() {
			while (messages.size() > 0) {
				setText(messages.remove(0));
				setForeground(textColor.remove(0));

				try { Thread.sleep(7500); }
				catch (InterruptedException e) { e.printStackTrace(); }
			}
			setText(" ");
		}
	}
}
