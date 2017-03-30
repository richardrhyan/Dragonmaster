package com.gce.dragonmaster.network.info;

import com.gce.dragonmaster.cards.Hand;
import com.gce.dragonmaster.gui.frames.GameFrame;
import com.gce.dragonmaster.gui.panels.CardsPanel;

public class HandInfo extends ServerInfo {
	private static final long serialVersionUID = 1L;
	private Hand hand = new Hand();
	
	public HandInfo(Hand hand) {
		for (int i = 0; i < hand.getSize(); i++)
			this.hand.addCard(hand.getCard(i));
	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		CardsPanel panel = gameFrame.getCardsPanel();
		panel.updateHand(this.hand);
	}

}
