package com.gce.dragonmaster.network.info;

import com.gce.dragonmaster.gui.frames.GameFrame;

public class InformationInfo extends ServerInfo {
	private static final long serialVersionUID = 1L;
	private String	message;
	
	public InformationInfo(String message) {
		this.message = message;
	}
	
	@Override
	public void execute(GameFrame gameFrame) {
		gameFrame.getInformationPanel().addMessage(message);
	}

}
