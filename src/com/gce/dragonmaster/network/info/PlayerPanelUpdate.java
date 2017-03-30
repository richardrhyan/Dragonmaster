package com.gce.dragonmaster.network.info;

import java.util.Vector;

import com.gce.dragonmaster.cards.Card;
import com.gce.dragonmaster.cards.Hand;
import com.gce.dragonmaster.gui.frames.GameFrame;
import com.gce.dragonmaster.gui.panels.PlayerPanel;
import com.gce.dragonmaster.network.Player;

public class PlayerPanelUpdate extends ServerInfo {
	private static final long serialVersionUID = 1L;
	private String[]	name;
	private Card[]		card; // Last Card Played
	private	Hand[]		hand;
	private int[]		tricksTaken;
	private boolean[]	firstTrick;
	private boolean[]	lastTrick;
	private int[]		gold;
	private int			currentDragonmaster;
	private boolean		newDM = false;
	
	public PlayerPanelUpdate(Vector<Player> players, int currentDragonmaster, String activePlayer) {
		int length 	= players.size();
		name		= new String[length];
		card		= new Card[length];
		hand		= new Hand[length];
		tricksTaken	= new int[length];
		firstTrick	= new boolean[length];
		lastTrick	= new boolean[length];
		gold		= new int[length];
		for (int i = 0; i < length; i++) {
			Player p = players.get(i);
			name[i] = p.getPlayerName();
			if (name[i].equals(activePlayer))
				name[i] = "<html><b>" + name[i] + "</b><html>";
			card[i] = p.getLastCardPlayed();
			hand[i] = p.getHand();
			tricksTaken[i] = p.getTricks().getSize() / 4;
			firstTrick[i] = p.hasFirstTrick();
			lastTrick[i] = p.hasLastTrick();
			gold[i] = p.getGold();
		}
		this.currentDragonmaster= currentDragonmaster; 
	}
	
	public PlayerPanelUpdate(Vector<Player> players, int currentDragonmaster, String activePlayer, boolean newDM) {
		this(players, currentDragonmaster, activePlayer);
		this.newDM = newDM;
	}
	
	public	String	getName(int index)			{ return name		[index];	}
	public	Card	getCard(int index)			{ return card		[index];	}
	public	int		getTricks(int index)		{ return tricksTaken[index];	}
	public	boolean	getFirst(int index)			{ return firstTrick	[index];	}
	public	boolean	getLast(int index)			{ return lastTrick	[index];	}
	public	int		getGold(int index)			{ return gold		[index];	}
	public	int		size()						{ return name.length;			}
//	public	String	getNextPlayerName()			{ return nextPlayerName;		}
	public	int		getCurrentDragonmaster()	{ return currentDragonmaster;	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		gameFrame.getPlayAreaTable().getModel().update(this);
		if (newDM)
			gameFrame.getGamesPanel().clear();
		
		for (int i = 0; i < name.length; i++) {
			if (name[i].equals(gameFrame.getPlayerName()) || name[i].equals("<html><b>" + gameFrame.getPlayerName() + "</b><html>")) {
				PlayerPanel p = gameFrame.getPlayerPanel();
				p.setPlayertName(name[i]);
				p.setGold(gold[i]);
				return;
			}
		}
	}
	
}
