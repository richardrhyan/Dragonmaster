package com.gce.dragonmaster.network.requests;

import com.rhyan.requests.Request;
import com.gce.dragonmaster.gui.frames.GameFrame;

@SuppressWarnings("serial")
public abstract class ServerRequest extends Request {
	public abstract Object execute(GameFrame gameFrame);
}
