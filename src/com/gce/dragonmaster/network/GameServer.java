package com.gce.dragonmaster.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Vector;

import com.gce.dragonmaster.cards.Card;
import com.gce.dragonmaster.cards.Deck;
import com.gce.dragonmaster.cards.GameLevel;
import com.gce.dragonmaster.cards.Hand;
import com.gce.dragonmaster.cards.Suit;
import com.gce.dragonmaster.game.GameHand;
import com.gce.dragonmaster.network.info.HandInfo;
import com.gce.dragonmaster.network.info.ItsYourTurnInfo;
import com.gce.dragonmaster.network.requests.GameHandRequest;

public class GameServer {
	public final static int PORT 			= 52225;
	public final static String LOCALHOST 	= "127.0.0.1";

	private Vector<Player> 			players;
	private ServerSocket 			serverSocket;
	private MyConnectionListener 	connectionListener;

	private	GameLevel 				gameLevel		= null;
	private Card 					centerCard 		= null;
	private Hand 					cardsPlayed		= new Hand();
	private int 					dragonmasterID	= 0;
	private GameHand 				currentHand 	= null;
	private Map<GameHand, Boolean>	handsPlayed		= new LinkedHashMap<GameHand, Boolean>();
	private boolean 				gameOver 		= false;
	private	String					nextPlayerName	= "";
	private Suit					leadSuit		= null;
	private Player					roundLeader		= null;
	private	int						fullRoundCount	= 0;

	public GameServer(String myPlayerName, GameLevel gameLevel) throws IOException {
		players = new Vector<Player>();
		serverSocket = new ServerSocket(PORT);
		connectionListener = new MyConnectionListener(this, players);
		this.gameLevel = gameLevel;

		resetHandsPlayed();
		
		nextPlayerName = myPlayerName;
		connectionListener.start();
		new CollectPlayers().start();

		// Wait for all players to join, then start
		new Thread() {
			public void run() {
				while (players.size() < 4) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) { /* Do nothing */
					}
				}
				new PlayGame().start();
			}
		}.start();
	}

	class PlayGame extends Thread {
		public void run() {
			dealCards();
			
			roundLeader = players.get(0);
			roundLeader.write(new GameHandRequest(handsPlayed));
			roundLeader.write(new ItsYourTurnInfo(null));
			
			while (!gameOver && players.size() > 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) { /* Do nothing */
				}
			}
			close();
		}
	}
	
	class CollectPlayers extends Thread {
		Socket clientSocket;

		public void run() {
			while (!gameOver && players.size() < 4) {
				// wait for next client connection request
				try {
					clientSocket = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				Player player = new Player(clientSocket);
				players.add(player);
				player.start();
			}
		}
	}
	
	public void resetHandsPlayed() {
		for (GameHand g : GameHand.values())
			handsPlayed.put(g, false);
	}
	
	public void dealCards() {
		boolean isBasicGame = gameLevel == GameLevel.BASIC;

		Deck d = new Deck(4, !isBasicGame);
		d.shuffle();
		
		// The dragon is included in Adv and Exp games. Put one card in the center 
		// to deal all cards. The center card cannot be the dragon.
		while (!isBasicGame && centerCard == null) {
			centerCard = d.dealCard();
			if (centerCard.getSuit().equals(Suit.DRAGON)) {
				d.shuffle();
				centerCard = null;
			}
		}

		// Deal the card to each player
		do {
			for (Player p : players)
				p.getHand().addCard(d.dealCard());
			
		} while (d.cardsLeft() >= 4);
		
		// Send card information to each player
		for (Player p : players)
			p.write(new HandInfo(p.getHand()));
	}

	public void close() {
		for (Player p : players)
			p.close();
		connectionListener = null;
		serverSocket = null;
		players = null;
	}
	

	/* **********************************************
	 * Getters and Setters**********************************************
	 */
	public	Vector<Player>			getPlayers()		{ return players;			}
	public	GameLevel				getGameLevel()		{ return gameLevel;			}
	public	Card					getCenterCard()		{ return centerCard;		}
	public	Hand					getCardsPlayed()	{ return cardsPlayed;		}
	public	int						getDragonmasterID()	{ return dragonmasterID;	}
	public	GameHand				getCurrentHand()	{ return currentHand;		}
	public	Map<GameHand, Boolean>	getHandsPlayed()	{ return handsPlayed;		}
	public	boolean					isGameOver()		{ return gameOver;			}
	public	String					getNextPlayerName()	{ return nextPlayerName;	}
	public	Suit					getLeadSuit()		{ return leadSuit;			}
	public	Player					getRoundLeader()	{ return roundLeader;		}
	public	int						getFullRoundCount()	{ return fullRoundCount;	}
	
	public	void	setGameLevel(GameLevel gameLevel)			{ this.gameLevel		= gameLevel;		}
	public	void	setCurrentCard(Card centerCard)				{ this.centerCard		= centerCard;		}
	public	void	setDragonmasterID(int dragonmasterID)		{ this.dragonmasterID	= dragonmasterID;	}
	public	void	setCurrentHand(GameHand currentHand) 		{ this.currentHand		= currentHand;		}
	public	void	setGameOver(boolean gameOver)				{ this.gameOver			= gameOver;			}
	public	void	setNextPlayerName(String nextPlayerName)	{ this.nextPlayerName	= nextPlayerName;	}
	public	void	setLeadSuit(Suit suit)						{ this.leadSuit 		= suit;				}
	public	void	setRoundLeader(Player leader)				{ this.roundLeader 		= leader;			}
	
	public	void	increaseFullRountCount()					{ fullRoundCount++; 						}
	
	public Player whoHasDragon() {
		for (Player p : players)
			for (int i = 0; i < p.getHand().getSize(); i++)
				if (p.getHand().getCard(i).getSuit().equals(Suit.DRAGON))
					return p;
		return null;
	}
}
