package com.gce.dragonmaster.gui.frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.gce.dragonmaster.cards.Value;

@SuppressWarnings("serial")
public class RankFrame extends JFrame {

	public RankFrame(JFrame parent) {
		setTitle("Rank of Card Values");
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(Value.values().length + 1, 0, 0, 3));
		
		JLabel x = new JLabel("Rank of Card Values");
		x.setHorizontalAlignment(JLabel.CENTER);
		pane.add(x);
		
		for (int i = Value.values().length - 1; i >= 0; i--) {
			JLabel l = new JLabel(Value.values()[i].toString());
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			pane.add(l);
		}
		
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if (parent != null)
        	setLocation(parent.getLocation().x + parent.getWidth()+ 25, parent.getLocation().y);
        setVisible(true);
	}
	
	public static void main(String[] args) {
		new RankFrame(null);
	}
}
