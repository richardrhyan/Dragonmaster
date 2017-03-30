package com.gce.dragonmaster.gui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PlayAreaTable extends JTable {
	TableModel model = new PlayAreaModel();
	public PlayAreaTable() {
		setModel(model);
		
		getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		
		DefaultTableCellRenderer dtcrCenter = new DefaultTableCellRenderer();
		DefaultTableCellRenderer dtcrRight = new DefaultTableCellRenderer();		
        dtcrCenter.setHorizontalAlignment(SwingConstants.CENTER);
        dtcrRight.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // DM
        getColumnModel().getColumn(0).setPreferredWidth(5);
        getColumnModel().getColumn(0).setCellRenderer(dtcrCenter);
        
        // Name
        getColumnModel().getColumn(1).setPreferredWidth(75);
        
        // Card Played
        getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Tricks Taken
        getColumnModel().getColumn(3).setPreferredWidth(10);
        getColumnModel().getColumn(3).setCellRenderer(dtcrCenter);
        
        // first card played
        getColumnModel().getColumn(4).setCellRenderer(dtcrCenter);
        getColumnModel().getColumn(4).setPreferredWidth(5);
        
        // Last Card Played
        getColumnModel().getColumn(5).setCellRenderer(dtcrCenter);
        getColumnModel().getColumn(5).setPreferredWidth(5);
        
        // Gold
        getColumnModel().getColumn(6).setCellRenderer(dtcrRight);
        getColumnModel().getColumn(6).setPreferredWidth(30);
        
	}
	
	public PlayAreaModel getModel() {
		return (PlayAreaModel)model;
	}
	
	class CheckBoxrenderer extends JCheckBox implements TableCellRenderer {
		public CheckBoxrenderer() {
			setHorizontalAlignment(CENTER);
			setBackground(Color.WHITE);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			
			setSelected(value != null && ((Boolean) value).booleanValue());
			return this;
		}
	}
}
