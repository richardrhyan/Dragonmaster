package com.gce.dragonmaster.network.info;

import com.rhyan.requests.Request;
import com.gce.dragonmaster.network.Player;
import com.gce.dragonmaster.network.GameServer;

@SuppressWarnings("serial")
public abstract class ClientInfo extends Request {
	public abstract void execute(GameServer gameServer, Player player);
}
