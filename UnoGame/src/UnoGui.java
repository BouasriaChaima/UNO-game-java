import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;// for the borders
import javax.swing.SwingUtilities;//to be thread safe
import javax.swing.table.DefaultTableModel;// for the table model ( cant customize it)

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
	 // logo image
	 GameImageIcon image = new GameImageIcon("uno1.jpg");
	 GameLabel toplabel = new GameLabel();
	 GamePanel toppanel = new GamePanel();
	 toppanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     GameLabel label = new GameLabel();
     GamePanel panel = new GamePanel();
     GamePanel logopanel = new GamePanel();
     panel.setLayout(new BorderLayout());
     logopanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     // top panel of the dialog has the welcoming label
     toplabel.setText("WELCOME TO UNO GAME:");
     toppanel.add(toplabel);
     
     label.setIcon(image);
     label.setHorizontalTextPosition(GameLabel.CENTER);
     label.setVerticalTextPosition(GameLabel.BOTTOM);
     label.setIconTextGap(10);
     label.setLayout(new FlowLayout());
     label.setText("Please choose the number of players");
     // the logo panel has the logo icon with a label under it
     logopanel.add(label);
     // buttons to set the number of players
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
             // dispose of the dialog
             dialog.dispose();
             // the frame pops up
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
        
         
	    // Initialize the played cards table as empty
	    setupplayerstable();
	    
	    // setting the current player's label
	    currentplayerlabel = new GameLabel();
	    currentplayerlabel.setText("Current Player: " + game.getPlayers().get(currentplayerindex).getName());
	    currentplayerlabel.setHorizontalAlignment(GameLabel.CENTER);
	    
	    // Top card panel
	    topcardpanel = new GamePanel();
	    topcardpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    topcardlabel = new GameLabel();
	    updatetopcard();
	    topcardpanel.add(topcardlabel);// adding the label containing the name and icon of the top card
	    GamePanel centerpanel = new GamePanel(new BorderLayout());
	    //the center panel has the current player's label in the south , the top card in the center and the direction in the north
	    centerpanel.add(currentplayerlabel, BorderLayout.SOUTH);
	    centerpanel.add(topcardpanel, BorderLayout.CENTER);
	    centerpanel.add(directionLabel, BorderLayout.NORTH);
	    
	    // south: Player's hand and buttons
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
	    setupotherplayers();// calls to set the other panels
	}
	// method to initialize opponents panels
	private void setupotherplayers() {
		// creating an array of panels = number of opponents
		opponentPanels = new GamePanel[nump - 1];
		
		if (nump == 2) {
			// if the number of players is 2
			// we calculate the index of the next player
			int nextplayer;
			 if (game.getDirection().equals("right")) {
		            nextplayer = (currentplayerindex + 1) % nump; // Move to the next player
		        } else {
		        	 nextplayer= (currentplayerindex - 1 + nump) % nump; // Move to the previous player
		        }
            // For 2 players: opponent in EAST
            GamePanel eastOpponent = new GamePanel();
            eastOpponent.setLayout(new FlowLayout());
            eastOpponent.setPreferredSize(new Dimension(150, 400));// dimension for vertical display
            // creating a border with the title opponent
            eastOpponent.setBorder(BorderFactory.createTitledBorder("Opponent"));
            updateOpponentDisplay(eastOpponent, nextplayer ); // Player 1 is opponent, with its index
            mainpanel.add(eastOpponent, BorderLayout.EAST);// the first opponent is in the east of the main panel
            opponentPanels[0] = eastOpponent;
        } 
		
		 else if (nump == 3) {
			 // 3 players we calculate the indexes of the other 2 players
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
	            // For 3 players oopponent 1 is in the east and opponent 2 is in the  west of the main panel
	            GamePanel eastOpponent = new GamePanel();
	            eastOpponent.setLayout(new FlowLayout());
	            eastOpponent.setPreferredSize(new Dimension(150, 400));
	            // creating a border with the title opponent
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
	            // For 4 players 1 opponent is in the east , 2 nd is in the west , 3rd is in the north of the main panel
	            GamePanel eastOpponent = new GamePanel();
	            eastOpponent.setLayout(new FlowLayout());
	            eastOpponent.setPreferredSize(new Dimension(150, 400));
	            // creating a border with the title opponent
	            eastOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 1"));
	            updateOpponentDisplay(eastOpponent, nextplayer);
	            mainpanel.add(eastOpponent, BorderLayout.EAST);
	            opponentPanels[0] = eastOpponent;
	            
	            GamePanel westOpponent = new GamePanel();
	            westOpponent.setLayout(new FlowLayout());
	            westOpponent.setPreferredSize(new Dimension(150, 400));// vertical
	            westOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 2"));
	            updateOpponentDisplay(westOpponent, nextplayer2);
	            mainpanel.add(westOpponent, BorderLayout.WEST);
	            opponentPanels[1] = westOpponent;
	            
	            GamePanel northOpponent = new GamePanel();
	            northOpponent.setLayout(new FlowLayout());
	            northOpponent.setPreferredSize(new Dimension(800, 150));// horizontal
	            northOpponent.setBorder(BorderFactory.createTitledBorder("Opponent 3"));
	            updateOpponentDisplay(northOpponent, nextplayer3);
	            mainpanel.add(northOpponent, BorderLayout.NORTH);
	            opponentPanels[2] = northOpponent;
	        }
	}
	// method to update each opponent's panel
	private void updateOpponentDisplay(GamePanel panel, int playerIndex) {
	    panel.removeAll();
	    // clears the opponents hand
	    Player opponent = game.getPlayers().get(playerIndex);
	    int numCards = opponent.playerHandSize();
	    // checks if we have a vertical or horizontal display by the width we set for each panel( vertical for east and west) 
	    boolean isVertical = panel.getPreferredSize().width <= 150;
	    
	    // Load card back images horizontal or vertical
	    GameImageIcon verticalbackIcon = new GameImageIcon("images/backvertical.png");
	    GameImageIcon horizontalbackIcon = new GameImageIcon("images/backhorizontal.png");
	    
	    // setting the dimensions for the cards
	    int verticalcardWidth = 80;  
	    int verticalcardHeight = 70; 
	    int horizontalcardWidth = 70;
	    int horizontalcardHeight = 80; 
	    
	    // scaling images
	    Image verticalbackImg = verticalbackIcon.getImage().getScaledInstance(
	        verticalcardWidth, verticalcardHeight, Image.SCALE_SMOOTH);
	    verticalbackIcon = new GameImageIcon(verticalbackImg);
	    
	    Image horizontalbackImg = horizontalbackIcon.getImage().getScaledInstance(
	        horizontalcardWidth, horizontalcardHeight, Image.SCALE_SMOOTH);
	    horizontalbackIcon = new GameImageIcon(horizontalbackImg);
	    
	    // player info label
	    GameLabel infoLabel = new GameLabel("P"+ opponent.getName() + ": " + numCards + " cards");
	    panel.add(infoLabel);
	    
	    // cards panel w
	    GamePanel cardsPanel = new GamePanel();
	    if (isVertical) {
	        // Vertical layout 
	        cardsPanel.setLayout(new GridLayout(numCards, 1, 0, -55)); // -55 is for vertical overlapping
	    } else {
	        // Horizontal layout 
	        cardsPanel.setLayout(new GridLayout(1, numCards, -45, 0)); // -45 is for horizontal overlapping
	    }
	    
	    // adding the back image for the number of cards each opponent has
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
	//initializing the played cards table
	 private void setupplayerstable() {
		    String[] columns = {"Player:", "Card"};// column names
		    tablemodel = new DefaultTableModel(columns, 0) {
		        @Override
		        public boolean isCellEditable(int row, int column) {
		            return false; // Make table non-editable
		        }
		    };
		    // initialize a table with the model
		    playerstable = new GameTable(tablemodel);
		    GameScrollPane tableScrollPane = new GameScrollPane(playerstable);// adding it in a scrollpane
		    tableScrollPane.setVerticalScrollBarPolicy(GameScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		    tableScrollPane.setPreferredSize(new Dimension(200, 200));   // dimension
	  tablepanel = new GamePanel(new BorderLayout());
		 GameLabel tablelabel = new GameLabel();
    tablelabel.setText("game History:");
    // adding the table with its label to the table panel
	    tablepanel.add(tablelabel, BorderLayout.NORTH);
	    tablepanel.add(tableScrollPane, BorderLayout.CENTER);
		    tablepanel.setPreferredSize(new Dimension(200, 150));
		}
// method to update the top card
private void updatetopcard() {
	// gets the top card of the game with its image path
	Card topcard = game.getTopCard();
	String topcardpath = getcardpath(topcard);
	// sizing and scaling the icon
	 GameImageIcon topcardimage = new GameImageIcon(topcardpath);
	 Image topcardimg = topcardimage.getImage().getScaledInstance(75,110, Image.SCALE_SMOOTH);
	 topcardimage = new GameImageIcon(topcardimg);
     // setting the label and its icon + gapping between them
	 topcardlabel.setText("Top Card:"+ topcard.toString() );
	 topcardlabel.setIcon(topcardimage);
	 topcardlabel.setHorizontalTextPosition(GameLabel.CENTER);
     topcardlabel.setVerticalTextPosition(GameLabel.TOP);
     topcardlabel.setIconTextGap(10); 
	
}

// method to get the path to the card's photo from the folder images
private String getcardpath(Card card) {
	// getting the type and color ( making them lower case)
	String color = card.getColor().toLowerCase();
	String type = card.getType().toLowerCase();
	// gets the name based on the cards type
	// as the "number" cards where named (color_value.png)
	// the special cards where named ( color_type.png
	  if (type.equals("number")) {
          return "images/" + color + "_" + card.getValue() + ".png";
          } else {
              return "images/" + color + "_" + type.replace(" ", "_") + ".png";
          }
}
// method to update the current players hand
private void updatehanddisplay() {
	playerhand.removeAll();// clears the previous hand 
     cardbuttons.clear();// also the array that holds the cards
     Player currentplayer = game.getPlayers().get(currentplayerindex);
     ArrayList<Card> hand = currentplayer.getplayerHand();// getting the current players cards
     // a loop that gets the cards and its icon 
     for(int i=0;i<hand.size();i++) {
    	  Card card = hand.get(i);
          String imagePath = getcardpath(card);
          GameImageIcon cardIcon = new GameImageIcon(imagePath);
          Image cardimg = cardIcon.getImage().getScaledInstance(75,110, Image.SCALE_SMOOTH);
          cardIcon = new GameImageIcon(cardimg);
          // after the scaling each card is put as an icon of a button
          GameButton cardbutton = new GameButton();
          cardbutton.setIcon(cardIcon);
          cardbutton.setPreferredSize(new Dimension(75,110));
          // adding an action listener to everu card (button)
          int cardindex=i;
          cardbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				playcard(cardindex);// if clicked the card is played
			}
        	  
          });
          
          cardbuttons.add(cardbutton);
          playerhand.add(cardbutton);
     }
     playerhand.revalidate();
     playerhand.repaint();
}
//handeling the played card(game logique)
private void playcard(int cardindex) {// called by the index of the clicked card
	Player currentp = game.getPlayers().get(currentplayerindex);
	Card playercard = currentp.getplayerHand().get(cardindex);
    Card topcard = game.getTopCard();
    // checking if the card can be played 
    if(!currentp.canPlayCard(playercard, topcard)) {
    	// if not a GameOptionPane pops out telling the player he cant play the card
    	  GameOptionPane.showMessageDialog(frame, 
                  "You can't play that card! It must match either:\n" +
                  "- The color (" + topcard.getColor() + ")\n" +
                  "- The card type\n" +
                  "- Or the value (if number card)");
              return;
    }
    // else the top card is set to the played card
    Card playedCard = currentp.playCard(cardindex);
    game.setTopCard(playedCard);
    
    // Add the played card to the table
    //handle special cards effecta
    handlespecialcards(playedCard);
    addcardtotable(playedCard);
    if (currentp.playerHandSize() == 0) {
        game.checkWinner();// checking if its a winner 
        GameOptionPane.showMessageDialog(frame, currentp.getName() + " wins the game!");
        System.exit(0);
    }
    
    if (currentp.playerHandSize() == 1) {// checkig if the player has an uno
        GameOptionPane.showMessageDialog(frame, currentp.getName() + " has UNO!");
    }
    
    updategamedisplay();// updating the display each time a card is played
	
}
// method to add the played card to the table
private void addcardtotable(Card card) {
    Player currentp = game.getPlayers().get(currentplayerindex);
    String name = currentp.getName();
    String tablecard = card.toString();
    
    // add new row to the table with the player and the card he played
    tablemodel.addRow(new Object[]{name, tablecard});

    // scroll to the bottom of the table
    int lastRow = tablemodel.getRowCount() - 1;
    playerstable.scrollRectToVisible(playerstable.getCellRect(lastRow, 0, true));
}

// method to handle the special cards 
private void handlespecialcards(Card card) {
    int numplayers = game.getPlayers().size();
    int nextplayerindex;
    
    if (game.getDirection().equals("right")) {
    	nextplayerindex = (currentplayerindex + 1) % numplayers;
    } else {
    	nextplayerindex = (currentplayerindex - 1 + numplayers) % numplayers;
    }
    
    Player nextplayer = game.getPlayers().get(nextplayerindex);
	
    if(card instanceof DrawTwo) {//the next player draws 2 cards and his turn is skipped
            for (int i = 0; i < 2; i++) {
                Card drawnCard = game.getDeck().drawCard();
                if (drawnCard != null) {
                    nextplayer.drawCard(drawnCard);
                }
            }
       
            game.advanceToNextPlayer(); 
    }
    else if (card instanceof WildDrawFour) {
    	// a GameOptionPane pops out telling the player to set the color 
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
                  nextplayer.drawCard(drawnCard);// the next player draws 4 cards and his turn is skipped
              }
          }
          game.advanceToNextPlayer(); 
    	  
    }   else if (card instanceof Reverse) {
        game.ReverseDirection();// reversing the direction
        updateDirectionIndicator();// updating the direction label
        GameOptionPane.showMessageDialog(frame, "Direction reversed!");
        // In two-player game, reverse acts like a Skip
        if (game.getPlayers().size() == 2) {
            game.advanceToNextPlayer();
        }
    }  else if (card instanceof Skip) {
        game.advanceToNextPlayer();
        game.advanceToNextPlayer();// skipping the next player
        return; 
    } 
    else if (card instanceof WildCard) {// a GameOptionPane pops out telling the player to set the color 
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
    game.advanceToNextPlayer();// advancing
	
}
// method to update the direction label
private void updateDirectionIndicator() {
    if (game.getDirection().equals("right")) {
        directionLabel.setText("Direction : Right →");
    } else {
        directionLabel.setText("Direction : Left ←");
    }
}

//method to update the game display
private void updategamedisplay() {
    currentplayerindex = game.getCurrentPlayer();// gets the current player and sets its label
    currentplayerlabel.setText("Current Player: " + game.getPlayers().get(currentplayerindex).getName() );
    updatetopcard();// updating the top card
    updatehanddisplay();// the current player hand
    updateAllOpponents();// and the other opponents
}
// method to update the other opponents panels
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
        opponentIndices.add(nextIndex);// adding each next players indices to the array
    }
    
    // update each opponent panel with the correct player
    for (int i = 0; i < opponentIndices.size() && i < opponentPanels.length; i++) {
        GamePanel panel = opponentPanels[i];// getting the first panel( 1st opponent)
        int playerindex = opponentIndices.get(i);//( getting the index of the next player)
       // creating the title indicating the next players turn
        String turnPosition ;
        if (i==0) {
        	turnPosition ="Next" ;// if we are in the first opponent he is next
        }
        	else{
        		 turnPosition ="+" + (i + 1);// how many players before him
        	}
        
        String title = "Opponent " + (i + 1) + " (" + turnPosition + ")";
        // puttinh in it as a border 
        panel.setBorder(BorderFactory.createTitledBorder(title));
        // updatin each panel
        updateOpponentDisplay(panel, playerindex);
    }
}
// method to draw buttons
public void drawbuttons() {
   buttonpanel = new GamePanel();
    buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    // Draw button
     GameButton draw = new GameButton("Draw");// the draw button
     draw.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player currentplayer = game.getPlayers().get(currentplayerindex);
            String name = currentplayer.getName() ;
            Card drawnCard = game.getDeck().drawCard();
            if (drawnCard != null) {
            	
                currentplayer.drawCard(drawnCard);
                tablemodel.addRow(new Object[] {name, "drew a card"});// adding it to the history table

                int lastRow = tablemodel.getRowCount() - 1;
                playerstable.scrollRectToVisible(playerstable.getCellRect(lastRow, 0, true));
                // advancing to the next player
                game.advanceToNextPlayer();
                updategamedisplay();
            } else {
                GameOptionPane.showMessageDialog(frame, "No more cards in the deck!");
            }
        }
    });
    buttonpanel.add(draw);
    
    // game history button
    historyButton = new GameButton("Game History");
    historyButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showHistoryDialog();
        }
    });
    buttonpanel.add(historyButton);
    
}
// method for the action listener of the game history button
private void showHistoryDialog() {
	// creating a new dialog
    GameDialog historyDialog = new GameDialog(frame, "Game History", false);
    historyDialog.setLayout(new BorderLayout());
    // adding the table panel to it
    GameScrollPane tableScrollPane = new GameScrollPane(playerstable);
    historyDialog.add(tableScrollPane, BorderLayout.CENTER);
    // addint a button to exit
    GameButton closeButton = new GameButton("Close");
    closeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            historyDialog.dispose();// when the button is clicked the dialog is disposed of
        }
    });
    GamePanel buttonPanel = new GamePanel();
    buttonPanel.add(closeButton);
    historyDialog.add(buttonPanel, BorderLayout.SOUTH);
    
    historyDialog.setSize(400, 300);
    historyDialog.setLocationRelativeTo(frame);
    historyDialog.setVisible(true);
}


// invoking the gui 
public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new UnoGui();
        }
    });
}
}

