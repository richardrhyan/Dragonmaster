package com.gce.dragonmaster.network.requests;

import com.rhyan.requests.Request;
import com.gce.dragonmaster.network.Player;
import com.gce.dragonmaster.network.GameServer;

@SuppressWarnings("serial")
public abstract class ClientRequest extends Request {
		public abstract Object execute(GameServer gameServer, Player player);
}
