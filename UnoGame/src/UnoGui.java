import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class UnoGui {
private GameFrame frame ;
private GamePanel mainpanel;
private GamePanel tablepanel;
private GamePanel topcardpanel;
private GamePanel buttonpanel;
private GameTable playerstable;
private DefaultTableModel tablemodel;
private Game game;
private GameLabel topcardlabel;
private GameLabel currentplayerlabel;
private GamePanel playerhand;
private GameButton  historyButton;
private int currentplayerindex;
private GameScrollPane handscroll;
private int nump;
private GameImageIcon image;
private ArrayList<GameButton> cardbuttons;
private GameImageIcon backImage;
private GamePanel[] opponentPanels;
private GameLabel directionLabel;

 public UnoGui() {
  frame = new GameFrame("Uno Game");
  image = new GameImageIcon("UNOLOGO.jpg");
  frame.setIconImage(image.getImage());
  //to get the number of players
  setupdialog();
  //start the game
  game = new Game(nump, 4);
  currentplayerindex = game.getCurrentPlayer();
  cardbuttons = new ArrayList<>();
 
  setupmainpanel();
  updategamedisplay();
  
  
  frame.add(mainpanel);
  
  
  
 }
 // setting up the dialog
 private void setupdialog() {
	 GameDialog dialog = new GameDialog(frame, "WELCOME TO UNO", true );
	 GameImageIcon image = new GameImageIcon("uno1.jpg");
	 GameLabel toplabel = new GameLabel();
	 GamePanel toppanel = new GamePanel();
	 toppanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     GameLabel label = new GameLabel();
     GamePanel panel = new GamePanel();
     GamePanel logopanel = new GamePanel();
     panel.setLayout(new BorderLayout());
     logopanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     
     toplabel.setText("WELCOME TO UNO GAME:");
     toppanel.add(toplabel);
     
     label.setIcon(image);
     label.setHorizontalTextPosition(GameLabel.CENTER);
     label.setVerticalTextPosition(GameLabel.BOTTOM);
     label.setIconTextGap(10);
     label.setLayout(new FlowLayout());
     label.setText("Please choose the number of players");
     logopanel.add(label);
     
     GameButton twop = new GameButton("Two");
     GameButton threep = new GameButton("Three");
     GameButton fourp = new GameButton("Four");
     GamePanel buttonpanel = new GamePanel();
     buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     // adding actionlisteners to each button
     twop.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             nump = 2;
             dialog.dispose();
             frame.setVisible(true);
         }
     });
     
     threep.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             nump = 3;
             dialog.dispose();
             frame.setVisible(true);
         }
     });
     
     fourp.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             nump = 4;
             dialog.dispose();
             frame.setVisible(true);
         }
     });
     buttonpanel.add(twop);
     buttonpanel.add(threep);
     buttonpanel.add(fourp);
     panel.add(buttonpanel, BorderLayout.SOUTH);
     panel.add(toppanel, BorderLayout.NORTH);
     panel.add(logopanel, BorderLayout.CENTER);
     dialog.add(panel);
     dialog.setBounds(300, 300, 800, 650);
     dialog.setLocationRelativeTo(null);
     dialog.setResizable(true);
     dialog.setVisible(true);
     
     
 }

	private void setupmainpanel() {
	    mainpanel = new GamePanel();
	    mainpanel.setLayout(new BorderLayout());
	    directionLabel = new GameLabel("Direction : Right →");
        directionLabel.setHorizontalAlignment(GameLabel.CENTER);
        
         
	    // Initialize the played cards table
	    setupplayerstable();
	    
	    // NORTH: displaying the current player
	    currentplayerlabel = new GameLabel();
	    currentplayerlabel.setText("Current Player: " + game.getPlayers().get(currentplayerindex).getName());
	    currentplayerlabel.setHorizontalAlignment(GameLabel.CENTER);
	    
	    // Top card panel
	    topcardpanel = new GamePanel();
	    topcardpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    topcardlabel = new GameLabel();
	    updatetopcard();
	    topcardpanel.add(topcardlabel);
	    GamePanel centerpanel = new GamePanel(new BorderLayout());
	    centerpanel.add(currentplayerlabel, BorderLayout.SOUTH);
	    centerpanel.add(topcardpanel, BorderLayout.CENTER);
	    centerpanel.add(directionLabel, BorderLayout.NORTH);
	    
	    // SOUTH: Player's hand and buttons
	    GamePanel southpanel = new GamePanel(new BorderLayout());
	    
	    // Player hand panel
	    playerhand = new GamePanel();
	    playerhand.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    updatehanddisplay();
	    
	    // Scroll pane for player hand
	    handscroll = new GameScrollPane(playerhand);
	    handscroll.setPreferredSize(new Dimension(800, 150));
	    southpanel.add(handscroll, BorderLayout.CENTER);
	   
	    // Buttons panel
	    drawbuttons();
	    southpanel.add(buttonpanel, BorderLayout.SOUTH);
	    mainpanel.add(centerpanel, BorderLayout.CENTER);
	    mainpanel.add(southpanel, BorderLayout.SOUTH);
	    setupotherplayers();
	}
	
	private void setupotherplayers() {
		opponentPanels = new GamePanel[nump - 1];
		
		if (nump == 2) {
			int nextplayer;
			 if (game.getDirection().equals("right")) {
		            nextplayer = (currentplayerindex + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer= (currentplayerindex - 1 + nump) % nump; // Move to the previous player
		        }
            // For 2 players: opponent in EAST
            GamePanel eastOpponent = new GamePanel();
            eastOpponent.setLayout(new FlowLayout());
            eastOpponent.setPreferredSize(new Dimension(150, 400));
            eastOpponent.setBorder(BorderFactory.createTitledBorder("Opponent"));
            updateOpponentDisplay(eastOpponent, nextplayer ); // Player 1 is opponent
            mainpanel.add(eastOpponent, BorderLayout.EAST);
            opponentPanels[0] = eastOpponent;
        } 
		
		 else if (nump == 3) {
			 int nextplayer;
			 if (game.getDirection().equals("right")) {
		            nextplayer = (currentplayerindex + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer= (currentplayerindex - 1 + nump) % nump; // Move to the previous player
		        }
			 int nextplayer2;
			 if (game.getDirection().equals("right")) {
		            nextplayer2 = (nextplayer + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer2= (nextplayer - 1 + nump) % nump; // Move to the previous player
		        }
	            // For 3 players: opponents in EAST and WEST
	            GamePanel eastOpponent = new GamePanel();
	            eastOpponent.setLayout(new FlowLayout());
	            eastOpponent.setPreferredSize(new Dimension(150, 400));
	            eastOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 1"));
	            updateOpponentDisplay(eastOpponent, nextplayer); // Player 1
	            mainpanel.add(eastOpponent, BorderLayout.EAST);
	            opponentPanels[0] = eastOpponent;
	            GamePanel westOpponent = new GamePanel();
	            westOpponent.setLayout(new FlowLayout());
	            westOpponent.setPreferredSize(new Dimension(150, 400));
	            westOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 2"));
	            updateOpponentDisplay(westOpponent,nextplayer2 ); // Player 2
	            mainpanel.add(westOpponent, BorderLayout.WEST);
	            opponentPanels[1] = westOpponent;
	        } 
		
		 else if (nump == 4) {
			 int nextplayer;
			 if (game.getDirection().equals("right")) {
		            nextplayer = (currentplayerindex + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer= (currentplayerindex - 1 + nump) % nump; // Move to the previous player
		        }
			 int nextplayer2;
			 if (game.getDirection().equals("right")) {
		            nextplayer2 = (nextplayer + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer2= (nextplayer - 1 + nump) % nump; // Move to the previous player
		        }
			 int nextplayer3;
			 if (game.getDirection().equals("right")) {
		            nextplayer3 = (nextplayer2 + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer3= (nextplayer2  - 1 + nump) % nump; // Move to the previous player
		        }
	            // For 4 players: opponents in EAST, WEST, and NORTH
	            GamePanel eastOpponent = new GamePanel();
	            eastOpponent.setLayout(new FlowLayout());
	            eastOpponent.setPreferredSize(new Dimension(150, 400));
	            eastOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 1"));
	            updateOpponentDisplay(eastOpponent, nextplayer);
	            mainpanel.add(eastOpponent, BorderLayout.EAST);
	            opponentPanels[0] = eastOpponent;
	            
	            GamePanel westOpponent = new GamePanel();
	            westOpponent.setLayout(new FlowLayout());
	            westOpponent.setPreferredSize(new Dimension(150, 400));
	            westOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 2"));
	            updateOpponentDisplay(westOpponent, nextplayer2);
	            mainpanel.add(westOpponent, BorderLayout.WEST);
	            opponentPanels[1] = westOpponent;
	            
	            GamePanel northOpponent = new GamePanel();
	            northOpponent.setLayout(new FlowLayout());
	            northOpponent.setPreferredSize(new Dimension(800, 150));
	            northOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 3"));
	            updateOpponentDisplay(northOpponent, nextplayer3);
	            mainpanel.add(northOpponent, BorderLayout.NORTH);
	            opponentPanels[2] = northOpponent;
	        }
	}
	private void updateOpponentDisplay(GamePanel panel, int playerIndex) {
	    panel.removeAll();
	    Player opponent = game.getPlayers().get(playerIndex);
	    int numCards = opponent.playerHandSize();
	    
	    boolean isVertical = panel.getPreferredSize().width <= 150;
	    
	    // Load card back images
	    GameImageIcon verticalbackIcon = new GameImageIcon("images/backvertical.png");
	    GameImageIcon horizontalbackIcon = new GameImageIcon("images/backhorizontal.png");
	    
	    // Slightly larger card dimensions to show more of each card
	    int verticalcardWidth = 80;  // Increased from 75
	    int verticalcardHeight = 70; // Increased from 65
	    int horizontalcardWidth = 70; // Increased from 65
	    int horizontalcardHeight = 80; // Increased from 75
	    
	    // Scale images
	    Image verticalbackImg = verticalbackIcon.getImage().getScaledInstance(
	        verticalcardWidth, verticalcardHeight, Image.SCALE_SMOOTH);
	    verticalbackIcon = new GameImageIcon(verticalbackImg);
	    
	    Image horizontalbackImg = horizontalbackIcon.getImage().getScaledInstance(
	        horizontalcardWidth, horizontalcardHeight, Image.SCALE_SMOOTH);
	    horizontalbackIcon = new GameImageIcon(horizontalbackImg);
	    
	    // Player info label
	    GameLabel infoLabel = new GameLabel(opponent.getName() + ": " + numCards + " cards");
	    panel.add(infoLabel);
	    
	    // Cards panel with adjusted overlap
	    GamePanel cardsPanel = new GamePanel();
	    if (isVertical) {
	        // Vertical layout (EAST/WEST panels)
	        cardsPanel.setLayout(new GridLayout(numCards, 1, 0, -55)); 
	    } else {
	        // Horizontal layout (NORTH panel)
	        cardsPanel.setLayout(new GridLayout(1, numCards, -45, 0)); 
	    }
	    
	    // Add cards
	    for (int i = 0; i < numCards; i++) {
	    	if(isVertical) {
	    		 GameLabel cardLabel = new GameLabel();
	 	        cardLabel.setIcon(verticalbackIcon);
	 	        cardsPanel.add(cardLabel);
	    	}
	    	else{	       
	    		GameLabel cardLabel = new GameLabel();
	        cardLabel.setIcon(horizontalbackIcon);
	        cardsPanel.add(cardLabel);
	    }
	    }
	    
	 

	    panel.add(cardsPanel);
	    panel.revalidate();
	    panel.repaint();
	}
	//setting up the mainpanel of the frame(display)
	 private void setupplayerstable() {
		    String[] columns = {"Player:", "Card"};
		    tablemodel = new DefaultTableModel(columns, 0) {
		        @Override
		        public boolean isCellEditable(int row, int column) {
		            return false; // Make table non-editable
		        }
		    };
		    
		    playerstable = new GameTable(tablemodel);
		    GameScrollPane tableScrollPane = new GameScrollPane(playerstable);
		    tableScrollPane.setVerticalScrollBarPolicy(GameScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		    tableScrollPane.setPreferredSize(new Dimension(200, 200));   
	  tablepanel = new GamePanel(new BorderLayout());
		 GameLabel tablelabel = new GameLabel();
    tablelabel.setText("game History:");
	    tablepanel.add(tablelabel, BorderLayout.NORTH);
	    tablepanel.add(tableScrollPane, BorderLayout.CENTER);
		    tablepanel.setPreferredSize(new Dimension(200, 150));
		}

private void updatetopcard() {
	Card topcard = game.getTopCard();
	String topcardpath = getcardpath(topcard);
	
	 GameImageIcon topcardimage = new GameImageIcon(topcardpath);
	 Image topcardimg = topcardimage.getImage().getScaledInstance(75,110, Image.SCALE_SMOOTH);
	 topcardimage = new GameImageIcon(topcardimg);
     
	 topcardlabel.setText("Top Card:"+ topcard.toString() );
	 topcardlabel.setIcon(topcardimage);
	 topcardlabel.setHorizontalTextPosition(GameLabel.CENTER);
     topcardlabel.setVerticalTextPosition(GameLabel.TOP);
     topcardlabel.setIconTextGap(10); 
	
}

// method to get the path to the card's photo from the folder
private String getcardpath(Card card) {
	String color = card.getColor().toLowerCase();
	String type = card.getType().toLowerCase();
	
	  if (type.equals("number")) {
          return "images/" + color + "_" + card.getValue() + ".png";
          } else {
              return "images/" + color + "_" + type.replace(" ", "_") + ".png";
          }
}
private void updatehanddisplay() {
	playerhand.removeAll();
     cardbuttons.clear();
     Player currentplayer = game.getPlayers().get(currentplayerindex);
     ArrayList<Card> hand = currentplayer.getplayerHand();
     
     for(int i=0;i<hand.size();i++) {
    	  Card card = hand.get(i);
          String imagePath = getcardpath(card);
          GameImageIcon cardIcon = new GameImageIcon(imagePath);
          Image cardimg = cardIcon.getImage().getScaledInstance(75,110, Image.SCALE_SMOOTH);
          cardIcon = new GameImageIcon(cardimg);
          
          GameButton cardbutton = new GameButton();
          cardbutton.setIcon(cardIcon);
          cardbutton.setPreferredSize(new Dimension(75,110));
          
          int cardindex=i;
          cardbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				playcard(cardindex);
			}
        	  
          });
          
          cardbuttons.add(cardbutton);
          playerhand.add(cardbutton);
     }
     playerhand.revalidate();
     playerhand.repaint();
}
//handeling the played card(game logique)
private void playcard(int cardindex) {
	Player currentp = game.getPlayers().get(currentplayerindex);
	Card playercard = currentp.getplayerHand().get(cardindex);
    Card topcard = game.getTopCard();
    
    if(!currentp.canPlayCard(playercard, topcard)) {
    	  GameOptionPane.showMessageDialog(frame, 
                  "You can't play that card! It must match either:\n" +
                  "- The color (" + topcard.getColor() + ")\n" +
                  "- The card type\n" +
                  "- Or the value (if number card)");
              return;
    }
    Card playedCard = currentp.playCard(cardindex);
    game.setTopCard(playedCard);
    
    // Add the played card to the table
    //handle special cards effecta
    handlespecialcards(playedCard);
    addcardtotable(playedCard);
    if (currentp.playerHandSize() == 0) {
        game.checkWinner();
        GameOptionPane.showMessageDialog(frame, currentp.getName() + " wins the game!");
        System.exit(0);
    }
    
    if (currentp.playerHandSize() == 1) {
        GameOptionPane.showMessageDialog(frame, currentp.getName() + " has UNO!");
    }
    
    updategamedisplay();
	
}

private void addcardtotable(Card card) {
    Player currentp = game.getPlayers().get(currentplayerindex);
    String name = currentp.getName();
    String tablecard = card.toString();
    
    // Add new row to the table
    tablemodel.addRow(new Object[]{name, tablecard});

    // Scroll to the bottom of the table
    int lastRow = tablemodel.getRowCount() - 1;
    playerstable.scrollRectToVisible(playerstable.getCellRect(lastRow, 0, true));
}


private void handlespecialcards(Card card) {
    int numplayers = game.getPlayers().size();
    int nextplayerindex;
    
    if (game.getDirection().equals("right")) {
    	nextplayerindex = (currentplayerindex + 1) % numplayers;
    } else {
    	nextplayerindex = (currentplayerindex - 1 + numplayers) % numplayers;
    }
    
    Player nextplayer = game.getPlayers().get(nextplayerindex);
	
    if(card instanceof DrawTwo) {
            for (int i = 0; i < 2; i++) {
                Card drawnCard = game.getDeck().drawCard();
                if (drawnCard != null) {
                    nextplayer.drawCard(drawnCard);
                }
            }
       
            game.advanceToNextPlayer(); 
    }
    else if (card instanceof WildDrawFour) {
    	   String[] colors = {"Red", "Green", "Blue", "Yellow"};
           String chosencolor = (String) GameOptionPane.showInputDialog(
               frame, "Choose a color:", "Wild Card Color Selection",GameOptionPane.PLAIN_MESSAGE,null,
               colors,
               colors[0]);
           
           if (chosencolor != null) {
               card.setColor(chosencolor);
               game.setTopCard(card);
           }
    	  for (int i = 0; i < 4; i++) {
              Card drawnCard = game.getDeck().drawCard();
              if (drawnCard != null) {
                  nextplayer.drawCard(drawnCard);
              }
          }
          game.advanceToNextPlayer(); 
    	  
    }   else if (card instanceof Reverse) {
        game.ReverseDirection();
        updateDirectionIndicator();
        GameOptionPane.showMessageDialog(frame, "Direction reversed!");
        // In two-player game, Reverse acts like a Skip
        if (game.getPlayers().size() == 2) {
            game.advanceToNextPlayer();
        }
    }  else if (card instanceof Skip) {
        game.advanceToNextPlayer();
        game.advanceToNextPlayer();
        return; 
    } 
    else if (card instanceof WildCard) {
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        String chosenColor = (String) GameOptionPane.showInputDialog(
            frame,
            "Choose a color:","Wild Card Color Selection",GameOptionPane.PLAIN_MESSAGE,null,
            colors,
            colors[0]);
        
        if (chosenColor != null) {
            card.setColor(chosenColor);
 
        }
    }
    game.advanceToNextPlayer();
	
}
private void updateDirectionIndicator() {
    if (game.getDirection().equals("right")) {
        directionLabel.setText("Direction : Right →");
    } else {
        directionLabel.setText("Direction : Left ←");
    }
}
private void updategamedisplay() {
    currentplayerindex = game.getCurrentPlayer();
    currentplayerlabel.setText("Current Player: " + game.getPlayers().get(currentplayerindex).getName() );
    updatetopcard();
    updatehanddisplay();
    updateAllOpponents();
}

private void updateAllOpponents() {
    int numPlayers = game.getPlayers().size();
    
    // Calculate next player indices based on current direction
    ArrayList<Integer> opponentIndices = new ArrayList<>();
    int nextIndex = currentplayerindex;
    
    for (int i = 0; i < numPlayers - 1; i++) {
        if (game.getDirection().equals("right")) {
            nextIndex = (nextIndex + 1) % numPlayers;
        } else {
            nextIndex = (nextIndex - 1 + numPlayers) % numPlayers;
        }
        opponentIndices.add(nextIndex);
    }
    
    // Update each opponent panel with the correct player
    for (int i = 0; i < opponentIndices.size() && i < opponentPanels.length; i++) {
        GamePanel panel = opponentPanels[i];
        int playerIndex = opponentIndices.get(i);
       // creating the title indicating the next players turn
        String turnPosition = (i == 0) ? "Next" : "+" + (i + 1);
        String title = "Opponent " + (i + 1) + " (" + turnPosition + ")";
        // puttinh in it as a border 
        panel.setBorder(BorderFactory.createTitledBorder(title));
        // updatin each panel
        updateOpponentDisplay(panel, playerIndex);
    }
}
public void drawbuttons() {
   buttonpanel = new GamePanel();
    buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    // Draw button
     GameButton draw = new GameButton("Draw");
     draw.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player currentplayer = game.getPlayers().get(currentplayerindex);
            String name = currentplayer.getName() ;
            Card drawnCard = game.getDeck().drawCard();
            if (drawnCard != null) {
                currentplayer.drawCard(drawnCard);
                GameOptionPane.showMessageDialog(frame, "You drew a card!");
                tablemodel.addRow(new Object[] {name, "drew a card"});

                int lastRow = tablemodel.getRowCount() - 1;
                playerstable.scrollRectToVisible(playerstable.getCellRect(lastRow, 0, true));

                
                game.advanceToNextPlayer();
                updategamedisplay();
            } else {
                GameOptionPane.showMessageDialog(frame, "No more cards in the deck!");
            }
        }
    });
    buttonpanel.add(draw);
    
    
    // History button
    historyButton = new GameButton("Game History");
    historyButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showHistoryDialog();
        }
    });
    buttonpanel.add(historyButton);
    
}

private void showHistoryDialog() {
    GameDialog historyDialog = new GameDialog(frame, "Game History", false);
    historyDialog.setLayout(new BorderLayout());
    
    GameScrollPane tableScrollPane = new GameScrollPane(playerstable);
    historyDialog.add(tableScrollPane, BorderLayout.CENTER);
    
    GameButton closeButton = new GameButton("Close");
    closeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            historyDialog.dispose();
        }
    });
    GamePanel buttonPanel = new GamePanel();
    buttonPanel.add(closeButton);
    historyDialog.add(buttonPanel, BorderLayout.SOUTH);
    
    historyDialog.setSize(400, 300);
    historyDialog.setLocationRelativeTo(frame);
    historyDialog.setVisible(true);
}



public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new UnoGui();
        }
    });
}
}

