package com.gce.dragonmaster.gui.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.gce.dragonmaster.game.GameHand;

@SuppressWarnings("serial")
public class GamesPanel extends JPanel {
	private final Color AVAILABLE_COLOR = Color.BLACK;
	private final Color PLAYED_COLOR = Color.LIGHT_GRAY;
	private final Color CURRENT_GAME_COLOR = Color.red;
	
	private MyButton[] gameButton = new MyButton[GameHand.size()];
	private MyButton buttonPressed;
	
	public GamesPanel() {
		setLayout(new GridLayout(5, 1));
		for (int i = 0; i < GameHand.size(); i++) {
			GameHand g = GameHand.values()[i];
			gameButton[i] = new MyButton(g);
			gameButton[i].setToolTipText(g.getObjective());
			gameButton[i].setForeground(AVAILABLE_COLOR);
			add(gameButton[i]);
		}
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
	}
	
	public void setGamePlayed( GameHand currentHand) {
		for (MyButton b : gameButton)
			b.setGamePlayed(currentHand);
	}
	
	public void clear() {
		for (MyButton b : gameButton) { 
			b.setForeground(AVAILABLE_COLOR);
			b.setEnabled(true);
			b.played = false;
		}
	}
	
	public GameHand gameSelection() {
		buttonPressed = null;
		while (true) {
			if (buttonPressed != null)
				return buttonPressed.gameHand;
			try { Thread.sleep(250); } catch (InterruptedException e) {	e.printStackTrace(); }
		}
	}
	
	
	class MyButton extends JButton {
		GameHand gameHand;
		boolean played;
		
		public MyButton(GameHand hand) {
			super(hand.toString());
			this.gameHand = hand;
			setHorizontalAlignment(CENTER);
			setForeground(AVAILABLE_COLOR);
			
			final MyButton me = this;
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					buttonPressed = me; 
				}
			});
		}
		
		public void setGamePlayed(GameHand currentHand) {
			if (gameHand.equals(currentHand)) {
				setForeground(CURRENT_GAME_COLOR);
				played = true;
			}
			else if (played) {
				setForeground(PLAYED_COLOR);
				setEnabled(false);
			}
		}
		
		
		
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		JFrame f = new JFrame();
		GamesPanel g = new GamesPanel();
		f.add(g);
		
		LinkedHashMap<GameHand, Boolean> handsPlayed = new LinkedHashMap<GameHand, Boolean>();
		handsPlayed.put(GameHand.RUNESWORD, false);
		handsPlayed.put(GameHand.WIZARDS, false);
		handsPlayed.put(GameHand.FIRST_OR_LAST, false);
		handsPlayed.put(GameHand.DRAGONLORDS, false);
		handsPlayed.put(GameHand.STAFF_OF_POWER, false);
		
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		Thread.sleep(1000);
		g.setGamePlayed(GameHand.RUNESWORD);
		Thread.sleep(1000);
		g.setGamePlayed(GameHand.WIZARDS);
		Thread.sleep(1000);
		g.setGamePlayed(GameHand.FIRST_OR_LAST);
		Thread.sleep(1000);
		g.setGamePlayed(GameHand.DRAGONLORDS);
		Thread.sleep(1000);
		g.setGamePlayed(GameHand.STAFF_OF_POWER);
		Thread.sleep(1000);
		g.clear();
	}
	

}
