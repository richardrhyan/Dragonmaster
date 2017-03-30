package com.gce.dragonmaster.gui.swing;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class MyFonts {
	public final static String DRAGONMASTER = "stonecross";
	
	public MyFonts() {
		InputStream is = this.getClass().getResourceAsStream("/stonecross.ttf");

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

}
