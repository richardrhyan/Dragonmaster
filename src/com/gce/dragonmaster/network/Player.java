package com.gce.dragonmaster.network;

import java.net.Socket;

import com.gce.dragonmaster.cards.Card;
import com.gce.dragonmaster.cards.Hand;
import com.rhyan.multiUserNetwork.Connection;

public class Player extends Connection {
	private static	int	count			= 0;
	private int			id;
	private	String		playerName;
	private	int 		gold			= 100000;
	private Hand		hand			= new Hand();
	private Hand		tricks			= new Hand();
	private boolean		firstTrick		= false;
	private boolean		lastTrick		= false;
	private Card		lastCardPlayed	= null;
	
	public Player(Socket socket) {
		super(socket);
		id = count++;
	}
	
	
	/* ************************************
	 * Getters and Setters
	 **************************************/
	public	void	setPlayerName(String playerName)	{ this.playerName = playerName;		}
	public	synchronized void	setGold(int gold)					{ this.gold = gold;					}	
	public	synchronized void	setFirstTrick(boolean firstTrick)	{ this.firstTrick = firstTrick;		}
	public	synchronized void	setLastTrick(boolean lastTrick)		{ this.lastTrick = lastTrick;		}
	public	synchronized void	setLastCardPlayed(Card card)		{ this.lastCardPlayed = card;		}
	
	public	String					getPlayerName()		{ return playerName;		}
	public	synchronized int		getGold()			{ return gold;				}
	public	synchronized Hand		getHand()			{ return hand;				}
	public	synchronized Hand		getTricks()			{ return tricks;			}
	public	synchronized boolean	hasFirstTrick()		{ return firstTrick;		}
	public	synchronized boolean	hasLastTrick()		{ return lastTrick;			}
	public	synchronized Card		getLastCardPlayed()	{ return lastCardPlayed;	}
	public	synchronized int		getID()				{ return id;				}
}