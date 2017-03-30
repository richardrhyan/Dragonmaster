package com.gce.dragonmaster.gui.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.gce.dragonmaster.cards.Card;
import com.gce.dragonmaster.cards.Hand;
import com.gce.dragonmaster.cards.Suit;
import com.gce.dragonmaster.gui.frames.GameFrame;
import com.gce.dragonmaster.network.info.CardPlayedInfo;

@SuppressWarnings("serial")
public class CardsPanel extends JPanel{
	private GameFrame		gameFrame;
	private Hand			hand;
	private CardButton[] 	cardButton = new CardButton[8];
	
	
	public CardsPanel(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		setLayout(new GridLayout(2, 4));
		
		for (int i = 0; i < cardButton.length; i++) {
			cardButton[i] = new CardButton();
			cardButton[i].setPreferredSize(new Dimension(100, 75));
			cardButton[i].setEnabled(false);
			add(cardButton[i]);
		}
		
	}
	
	public void updateHand(Hand hand) {
		this.hand = hand;
		this.hand.sortBySuit();
		
		clearCards();
		
		for (int i = 0; i < this.hand.getSize(); i++)
			cardButton[i].setCard(this.hand.getCard(i));
	}
	
	public void activate(Suit suitLead) {
		boolean holdsSuitLead = hand.contains(suitLead);
		
		clearCards();
		for (int i = 0; i < this.hand.getSize(); i++) {
			Card c = hand.getCard(i);
			cardButton[i].setCard(c);
			cardButton[i].setEnabled(c.getSuit().equals(suitLead) || suitLead == null || !holdsSuitLead);
		}
	}
	
	public void setEnableCards(boolean enable) {
		for (int i = 0 ; i < cardButton.length; i++) {
			if (!cardButton[i].getText().equals(""))
				cardButton[i].setEnabled(enable);
			else
				cardButton[i].setEnabled(false);
		}
	}
	
	private void clearCards() {
		for (int i = 0; i < cardButton.length; i++) {
			cardButton[i].setCard(null);
			cardButton[i].setEnabled(false);
		}
	}
	
	
	class CardButton extends JButton {
		private Card card;
		
		
		public void setCard(Card card) {
			if (card==null) {
				setText("");
				this.card = null;
				return;
			}
				
			setText("<html>" + card.toString());
			this.card = card;
			
			addActionListener(new MyActionListener());
		}
		
		public Card getCard() {
			return card;
		}
		
		class MyActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e){
				if (card == null)
					return;
				
				Card temp = card;
				card = null;
				hand.removeCard(temp);
				setText("");
				setEnableCards(false);
				gameFrame.getOutput().write(new CardPlayedInfo(temp));
			}
		}
		
		
	}
}
