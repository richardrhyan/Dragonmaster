package com.gce.dragonmaster.gui.table;

import java.text.DecimalFormat;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.gce.dragonmaster.network.info.PlayerPanelUpdate;

@SuppressWarnings("serial")
public class PlayAreaModel extends AbstractTableModel {
	private	String		headings[] 	= new String[]{ "DM", "Name", "Card Played", "Tricks", "1st", "Last", "Gold" };
	private	Object[][]	data		= new Object[][] {
			{ "", "Waiting...", "", 0, Boolean.FALSE, Boolean.FALSE, 0 },
			{ "", "Waiting...", "", 0, Boolean.FALSE, Boolean.FALSE, 0 },
			{ "", "Waiting...", "", 0, Boolean.FALSE, Boolean.FALSE, 0 },
			{ "", "Waiting...", "", 0, Boolean.FALSE, Boolean.FALSE, 0 }};
	
	
	@Override	public	int		getRowCount()					{ return data.length;					}
	@Override	public	int		getColumnCount() 				{ return data[0].length;				}
	@Override	public	Object	getValueAt(int row, int column)	{ return data[row][column];				}
	@Override	public	String	getColumnName(int column)		{ return headings[column];				}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override	public	Class	getColumnClass(int column)		{ return data[0][column].getClass();	}	
	
	public void update(final PlayerPanelUpdate players) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				DecimalFormat myFormatter = new DecimalFormat("###,###");
				
				for (int i = 0; i < players.size(); i++) {
					data[i][0] = (i==players.getCurrentDragonmaster()) ? "*" : "";
					data[i][1] = players.getName(i);
					try { data[i][2] =  players.getCard(i).toString(); } catch (NullPointerException e) { data[i][2] = ""; }
					data[i][3] = players.getTricks(i);
					data[i][4] = (players.getFirst(i)) ? "*" : "";
					data[i][5] = (players.getLast(i))  ? "*" : "";
					data[i][6] = myFormatter.format(players.getGold(i));
				}
				fireTableDataChanged();
			}
		});
	}
}
