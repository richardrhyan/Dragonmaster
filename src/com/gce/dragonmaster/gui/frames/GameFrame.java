package com.gce.dragonmaster.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gce.dragonmaster.cards.GameLevel;
import com.gce.dragonmaster.gui.panels.*;
import com.gce.dragonmaster.gui.table.PlayAreaTable;
import com.gce.dragonmaster.network.GameServer;
import com.gce.dragonmaster.network.info.ServerInfo;
import com.gce.dragonmaster.network.requests.PlayerNameRequest;
import com.gce.dragonmaster.network.requests.ServerRequest;
import com.rhyan.multiUserNetwork.In;
import com.rhyan.multiUserNetwork.Out;
import com.rhyan.requests.Request;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements ActionListener {
	private final static String EXIT_MENU = "Exit";
	private final static String CARD_RANK_MENU = "Card Rank Window";
	
	private StartupFrame	startupFrame;
	private boolean			hosting;
	private String			hostAddress;
	private String			playerName;
	
	private	Socket		socket;
	private	Out			out;
	private	In			in;
	private	GameServer	gameServer;
	private GameLevel	gameLevel;
	
	private	PlayerPanel			playerPanel;
	private GamesPanel			gamesPanel;
	private PlayAreaTable		playAreaTable;
	private CardsPanel			cardsPanel;
	private InformationPanel	informationPanel;
	private JMenuBar			menuBar;
	private JMenu				fileMenu;
	private JMenu				windowsMenu;
	private JMenuItem			exitMenuItem;
	private JMenuItem			rankMenuItem;
	
	
	public GameFrame(final StartupFrame startupFrame) {
		this.hosting		= startupFrame.isHosting();
		this.hostAddress	= startupFrame.getHostAddress();
		this.playerName		= startupFrame.getPlayerName();


		/* 
		 * Hide the startup frame, but prepare to show it again if this frame closes
		 */
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		startupFrame.setVisible(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				startupFrame.setVisible(true);
			}
		});
		
		
		// Start Game Server
		if (hosting) {
			this.gameLevel = startupFrame.getGameLevel();
			startGameServer();
			if (gameServer == null) {
				close();
				return;
			}
		}
		
		
		// Connect to server
		connectToServer();
        if (socket == null) {
        	close();
        	return;
        }
        listen();
        
        // Menus
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitMenuItem = new JMenuItem(EXIT_MENU);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(this);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        windowsMenu = new JMenu("Windows");
        fileMenu.setMnemonic(KeyEvent.VK_W);
        rankMenuItem = new JMenuItem(CARD_RANK_MENU);
        rankMenuItem.setMnemonic(KeyEvent.VK_R);
        rankMenuItem.addActionListener(this);
        windowsMenu.add(rankMenuItem);
        menuBar.add(windowsMenu);
        
        this.setJMenuBar(menuBar);
        
        
        // Build window components
        playerPanel = new PlayerPanel(hostAddress);
        playerPanel.setPlayertName(playerName);
        add(playerPanel, BorderLayout.NORTH);
        
        gamesPanel = new GamesPanel();
        add(gamesPanel, BorderLayout.WEST);
        
        playAreaTable = new PlayAreaTable();
        add(new JScrollPane(playAreaTable), BorderLayout.CENTER);
        

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        
        cardsPanel = new CardsPanel(this);
        south.add(cardsPanel, BorderLayout.NORTH);
        
        informationPanel = new InformationPanel();
        south.add(informationPanel, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
        
        setSize(750,350);
        setResizable(false);
        setVisible(true);
        
        // Send Name to Server
        out.write(new PlayerNameRequest(playerName));
	}

	public PlayerPanel		getPlayerPanel()		{ return playerPanel;		}
	public PlayAreaTable	getPlayAreaTable()		{ return playAreaTable;		}
	public CardsPanel		getCardsPanel()			{ return cardsPanel;		}
	public GamesPanel		getGamesPanel()			{ return gamesPanel;		}
	public String			getPlayerName()			{ return playerName;		}
	public InformationPanel	getInformationPanel()	{ return informationPanel;	}
	
    // listen to socket and print everything that server broadcasts
    public void listen() {
        new Thread() {
        	public void run() {
        		Request s;

        		while ((s = (Request)in.readRequest()) != null) {
        			if (s instanceof ServerRequest)
        				out.write(processRequest(s));
        			else if (s instanceof ServerInfo)
        				processRequest(s);
        		}
        		out.close();
                in.close();
                try                 { socket.close();      }
                catch (Exception e) { e.printStackTrace(); }
                System.err.println("Closed client socket");
        	}
        }.start();
    }
    
    public Object processRequest(Request request) {
    	if (request instanceof ServerRequest)
    		return ((ServerRequest)request).execute(this);
    	if (request instanceof ServerInfo) { 
    		((ServerInfo) request).execute(this);}
   		
   		return null;
    }
	
	
	
	private void connectToServer() {
		try {
        	socket = new Socket(this.hostAddress, GameServer.PORT);
            out    = new Out(socket);
            in     = new In(socket);
        }
        catch (IOException e) {
        	e.printStackTrace();
        	JOptionPane.showMessageDialog(
        			startupFrame,
        			"Unable to connect to host.",
        			"GameFrame: IOException",
        			JOptionPane.WARNING_MESSAGE);
        	close();
        }
	}
	
	
	private void startGameServer() {
		try {
			gameServer = new GameServer(playerName, gameLevel);
			
		} catch (IOException e) {
			gameServer = null;
			JOptionPane.showMessageDialog(
					null,
					"Unable to start GameServer.",
					"IO Exception",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	
	public void close() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try { gameServer.close(); } catch (NullPointerException e) {}
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public Out getOutput() {
		return out;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case EXIT_MENU:
			System.exit(0);
		case CARD_RANK_MENU:
			new RankFrame(this);
		}
		
	}
}
