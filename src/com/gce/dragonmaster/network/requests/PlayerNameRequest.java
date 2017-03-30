package com.gce.dragonmaster.network.requests;

import com.gce.dragonmaster.network.GameServer;
import com.gce.dragonmaster.network.Player;
import com.gce.dragonmaster.network.info.PlayerPanelUpdate;

public class PlayerNameRequest extends ClientRequest {
	private static final long serialVersionUID = 1L;
	private String playerName;
	
	public PlayerNameRequest(String playerName) {
		this.playerName = playerName;
	}
	
	@Override
	public Object execute(GameServer gameServer, Player player) {
		player.setPlayerName(playerName);
		return new PlayerPanelUpdate(
				gameServer.getPlayers(),
				gameServer.getDragonmasterID(),
				gameServer.getNextPlayerName());
	}

}
