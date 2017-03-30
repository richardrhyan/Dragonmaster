package com.gce.dragonmaster.network.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.gce.dragonmaster.cards.Card;
import com.gce.dragonmaster.cards.Hand;
import com.gce.dragonmaster.cards.Suit;
import com.gce.dragonmaster.game.GameHand;
import com.gce.dragonmaster.network.GameServer;
import com.gce.dragonmaster.network.Player;
import com.gce.dragonmaster.network.requests.GameHandRequest;

public class CardPlayedInfo extends ClientInfo {
	private static final long serialVersionUID = 1L;
	private Card card;
	
	public CardPlayedInfo(Card card) {
		this.card = card;
	}
	
	@Override
	public void execute(GameServer gameServer, Player player) {
		Hand cardsPlayed = gameServer.getCardsPlayed();
		Vector<Player> players = gameServer.getPlayers();
		
		// Add card to tableau, and to players last card played
		cardsPlayed.addCard(card);
		player.setLastCardPlayed(card);
		player.getHand().removeCard(card);
		
		// Set suit lead if not already set
		if (gameServer.getLeadSuit() == null)
			gameServer.setLeadSuit(card.getSuit());
		
		// If not the last player in the round,
		// Let next player know it's their turn.
		if (cardsPlayed.getSize() < players.size()) { 
			
			int next = (player.getID() + 1 >= players.size()) ? 0 : player.getID() + 1;
			
			updatePlayers(gameServer, players.get(next).getPlayerName(), false);
			players.get(next).write(new ItsYourTurnInfo(gameServer.getLeadSuit()));
			
			return;
		}

		// Do this if this is the last player of a round
		int roundLeaderID = takeTrick(gameServer);
		Player roundLeader = players.get(roundLeaderID);
		
		// Set first and last trick taken for player
		if (roundLeader.getHand().getSize() == 7)
			roundLeader.setFirstTrick(true);
		if (roundLeader.getHand().getSize() == 0)
			roundLeader.setLastTrick(true);

		// Wait so players can see the last card played
		updatePlayers(gameServer, "", false);
		try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		// Remove the last card played for each player
		for (Player p : players)
			p.setLastCardPlayed(null);
		
		// Do more rounds to be played?
		if (players.get(0).getHand().getSize() > 0) { 
			updatePlayers(gameServer, players.get(roundLeaderID).getPlayerName(), false);
			players.get(roundLeaderID).write(new ItsYourTurnInfo(null));
			return;
		}
			
		// Do this if it's the last round of the hand
		makePayments(gameServer);
		for (Player p : players) {
			p.setFirstTrick(false);
			p.setLastTrick(false);
		}
		
		// Were all hands played by this dragonmaster?
		Map<GameHand, Boolean> handsPlayed = gameServer.getHandsPlayed();
		List<GameHand> keys = new ArrayList<GameHand>(handsPlayed.keySet());
		boolean allHandsPlayed = true;
		for (GameHand key : keys) 
			allHandsPlayed &= handsPlayed.get(key);

		// Yes, rotate first player... clear gameHands
		if (allHandsPlayed) {
			gameServer.setDragonmasterID(gameServer.getDragonmasterID() + 1);
			if (gameServer.getDragonmasterID() >= players.size()) {
				for (Player p : players)
					p.write(new GameOverInfo(players));
				return;
			}
			for (GameHand key : keys)
				gameServer.getHandsPlayed().put(key, false);
		}
		
		gameServer.dealCards();
		
		roundLeader = players.get(gameServer.getDragonmasterID());
		updatePlayers(gameServer, roundLeader.getPlayerName(), allHandsPlayed);
		roundLeader.write(new GameHandRequest(handsPlayed));
		roundLeader.write(new ItsYourTurnInfo(null));
	}
	
	private int takeTrick(GameServer gameServer) {
		Suit 				suit 		= gameServer.getLeadSuit();
		Vector<Player> 		players 	= gameServer.getPlayers();
		Card				highCard	= null;
		Player				leader		= null;
		Map<String, Card>	trick		= new HashMap<String, Card>();
		
		for (Player p : players) {
			Card lastCard = p.getLastCardPlayed();
			if (lastCard.getSuit() == suit) {
				if (highCard == null || lastCard.getValue().compareTo(highCard.getValue()) > 0) {
					highCard = lastCard;
					leader = p;
					trick.put(p.getPlayerName(), lastCard);
				}
			}
		}
		
		String message = String.format("%s took the trick with %s", leader.getPlayerName(), highCard);
		for (Player p : players) 
			p.write(new InformationInfo(message));
		
		leader.getTricks().addCards(gameServer.getCardsPlayed());
		gameServer.getCardsPlayed().clear();
		gameServer.setLeadSuit(null);
		gameServer.setRoundLeader(leader);
		return leader.getID();
	}
	
	private void makePayments(GameServer gameServer) {
		Vector<Player> players = gameServer.getPlayers();
		GameHand game = gameServer.getCurrentHand();
		String message = ""; 
		int currentDM = gameServer.getDragonmasterID();
		
		for (Player p : players) {
			if (message != "") 
				message += ", ";
			
			int payment = game.getCostOfHand(p);

			p.setGold(p.getGold() - payment);
			players.get(currentDM).setGold(players.get(currentDM).getGold() + payment);
			message += String.format("%s pays %d", p.getPlayerName(), payment);
			p.getTricks().clear();
		}
		
		for (Player p : players)
			p.write(new InformationInfo(message));
	}
	

	
	public void updatePlayers(GameServer gameServer, String nextPlayer, boolean newDM) {
		Vector<Player> players = gameServer.getPlayers();
		int currentDragonmaster = gameServer.getDragonmasterID();
		
		for (Player p : players)
			p.write(new PlayerPanelUpdate(players, currentDragonmaster, nextPlayer, newDM));
	}

}
