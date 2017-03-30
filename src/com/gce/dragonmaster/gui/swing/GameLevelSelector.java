package com.gce.dragonmaster.gui.swing;

import java.awt.Dimension;

import javax.swing.JComboBox;

import com.gce.dragonmaster.cards.GameLevel;

@SuppressWarnings("serial")
public class GameLevelSelector extends JComboBox<GameLevel>{

	public GameLevelSelector() {
//		setEditable(false);
		for (GameLevel l : GameLevel.values())
			addItem(l);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(150, 25);
	}
	
//	@Override
//	public Dimension getMinimumSize() {
//		return getPreferredSize();
//	}
//	
//	@Override
//	public Dimension getMaximumSize() {
//		return getPreferredSize();
//	}
}
