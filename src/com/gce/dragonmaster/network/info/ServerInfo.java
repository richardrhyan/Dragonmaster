package com.gce.dragonmaster.network.info;

import com.rhyan.requests.Request;
import com.gce.dragonmaster.gui.frames.GameFrame;

@SuppressWarnings("serial")
public abstract class ServerInfo extends Request {
	public abstract void execute(GameFrame gameFrame);
}
