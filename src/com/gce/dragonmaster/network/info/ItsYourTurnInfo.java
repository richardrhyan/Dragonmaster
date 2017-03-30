package com.gce.dragonmaster.network.info;

import com.gce.dragonmaster.cards.Suit;
import com.gce.dragonmaster.gui.frames.GameFrame;
import com.gce.dragonmaster.gui.panels.CardsPanel;

public class ItsYourTurnInfo extends ServerInfo {
	private static final long serialVersionUID = 1L;

	private	Suit	suitLead;
	
	public ItsYourTurnInfo(Suit suitLead) {
		this.suitLead = suitLead;
	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		CardsPanel panel = gameFrame.getCardsPanel();
		panel.activate(suitLead);
	}
}
