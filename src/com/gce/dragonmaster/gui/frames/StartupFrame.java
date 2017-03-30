package com.gce.dragonmaster.gui.frames;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.gce.dragonmaster.cards.GameLevel;
import com.gce.dragonmaster.gui.swing.GameLevelSelector;
import com.gce.dragonmaster.gui.swing.MyFonts;
import com.gce.dragonmaster.network.GameServer;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StartupFrame extends JFrame implements DocumentListener {
	private final String TITLE = "Dragonmaster";
	
	private JLabel headerLabel;
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton joinButton;
	private JButton hostButton;
	private JLabel ipAddressLabel;
	private JTextField ipAddressField;
	private GameLevelSelector gameLevelSelection;
	
	private StartupFrame me = this;
	private boolean hosting;
	
	
	public StartupFrame() {
		setTitle(TITLE);
		setLayout(new MigLayout());

		/* Header Label */
		JPanel r1 = new JPanel();
		r1.setLayout(new FlowLayout(FlowLayout.CENTER));
		headerLabel = new JLabel("DRAGONMASTER");
		headerLabel.setFont(new Font(MyFonts.DRAGONMASTER, Font.PLAIN, 28));
		r1.add(headerLabel);
		add(r1, "cell 0 1, align center, gap bottom 15");
		
		/* Enter Player Name */
		JPanel r2 = new JPanel();
		r2.setLayout(new BoxLayout(r2, BoxLayout.Y_AXIS));
		nameLabel = new JLabel("Enter your name");
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		r2.add(nameLabel);
		nameField = new JTextField(15);
		nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		r2.add(nameField);
		add(r2, "cell 0 2, align center, gap bottom 15");
		
		/* Enter IP Address */
		JPanel r4 = new JPanel();
		r4.setLayout(new BoxLayout(r4, BoxLayout.Y_AXIS));
		ipAddressLabel = new JLabel("Host address if joining");
		ipAddressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		r4.add(ipAddressLabel);
		ipAddressField = new JTextField(15);
		ipAddressField.setAlignmentX(Component.CENTER_ALIGNMENT);
		r4.add(ipAddressField);
		add(r4, "cell 0 3, align center, gap bottom 15");
		
		
		/* Select the game level */
		JPanel r5 = new JPanel();
		r5.setLayout(new FlowLayout(FlowLayout.CENTER));
		gameLevelSelection = new GameLevelSelector();
		r5.add(gameLevelSelection);
		add(r5, "cell 0 4, align center, gap bottom 15");
		
		/* Buttons  */
		JPanel r3 = new JPanel();
		r3.setLayout(new FlowLayout(FlowLayout.CENTER));
		hostButton = new JButton("Host Game");
		hostButton.setEnabled(false);
		r3.add(hostButton, "align center");
		joinButton = new JButton("Join Game");
		joinButton.setEnabled(false);
		joinButton.setPreferredSize(hostButton.getPreferredSize());
		r3.add(joinButton, "align center");
		add(r3, "cell 0 5, align center");
		
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

		nameField.getDocument().addDocumentListener(this);
		ipAddressField.getDocument().addDocumentListener(this);
		
		hostButton.addActionListener(new MyActionListener(true));
		joinButton.addActionListener(new MyActionListener(false));
	}
	
	@Override public void changedUpdate(DocumentEvent e) 	{ setButtonEnable();	}
	@Override public void insertUpdate(DocumentEvent e)		{ setButtonEnable();	}
	@Override public void removeUpdate(DocumentEvent e)		{ setButtonEnable();	}
	
	private void setButtonEnable() {
		hostButton.setEnabled(!nameField.getText().equals(""));
		joinButton.setEnabled(!nameField.getText().equals("") && !ipAddressField.getText().equals(""));
	}

	
	public	String		getPlayerName()		{ return nameField.getText();	}
	public	boolean		isHosting() 		{ return hosting;				}
	public	String		getHostAddress()	{ return (hosting) ? GameServer.LOCALHOST : ipAddressField.getText(); }
	public	GameLevel	getGameLevel()		{ return (GameLevel) gameLevelSelection.getSelectedItem(); }

	
	private class MyActionListener implements ActionListener {
		private boolean host;

		private MyActionListener(boolean host) {
			this.host = host;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			hosting = host;
			new GameFrame(me);
			hosting = false;
			try { Thread.sleep(2000); } catch (InterruptedException e1) { e1.printStackTrace(); }
			nameField.setText("Joe");
			new GameFrame(me);
			try { Thread.sleep(1000); } catch (InterruptedException e1) { e1.printStackTrace(); }
			nameField.setText("Rob");
			new GameFrame(me);
			try { Thread.sleep(1000); } catch (InterruptedException e1) { e1.printStackTrace(); }
			nameField.setText("Chuck");
			new GameFrame(me);
		}
	}
	
	public static void main(String[] args) {
		new MyFonts();
		new StartupFrame();
	}
}

