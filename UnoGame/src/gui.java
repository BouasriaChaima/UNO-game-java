import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class gui {
	private JTable playedCardsTable;
	private DefaultTableModel tableModel;
	private GamePanel tablePanel;
    private Game game;
    private GameFrame frame;
    private GameButton draw;
    private GameButton sayUnoButton;
    private GamePanel mainpanel;
    private GamePanel topcardpanel;
    private GameLabel topcardlabel;
    private GameLabel currentplayerlabel;
    private GamePanel playerhand;
    private GameScrollPane scrollpane;
    private int currentplayerindex;
    private int nump;
    private GameImageIcon image = new GameImageIcon("UNOLOGO.jpg");
    private ArrayList<GameButton> cardButtons;
    private GameImageIcon backImage;

    public gui() {
        frame = new GameFrame("UNO");
       
        frame.setIconImage(image.getImage());
        
        backImage = new GameImageIcon("images/back.png");
        
        numplayers();
        
        game = new Game(nump, 4);
        currentplayerindex = game.getCurrentPlayer();
        cardButtons = new ArrayList<>();
        
        setupMainPanel();
        updateGameDisplay();
        
        frame.add(mainpanel);
        
    }
    
    private void setupMainPanel() {
        mainpanel = new GamePanel();
        mainpanel.setLayout(new BorderLayout());
        
        // Initialize the played cards table
        initializePlayedCardsTable();
        
        // Create a panel for the top section (current player and top card)
        GamePanel topSectionPanel = new GamePanel();
        topSectionPanel.setLayout(new BorderLayout());
        
        currentplayerlabel = new GameLabel();
        currentplayerlabel.setText("Current Player: " + game.getPlayers().get(currentplayerindex).getName());
        currentplayerlabel.setHorizontalAlignment(GameLabel.CENTER);
        topSectionPanel.add(currentplayerlabel, BorderLayout.NORTH);
        
        topcardpanel = new GamePanel();
        topcardpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topcardlabel = new GameLabel();
        updateTopCardDisplay();
        topcardpanel.add(topcardlabel);
        topSectionPanel.add(topcardpanel, BorderLayout.CENTER);
        
        // Create a center panel that contains both player hand and table
        GamePanel centerPanel = new GamePanel();
        centerPanel.setLayout(new BorderLayout());
        
        // Player hand panel
        playerhand = new GamePanel();
        playerhand.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        updatePlayerHand();
        
        scrollpane = new GameScrollPane(playerhand);
        scrollpane.setPreferredSize(new Dimension(500, 300));
        centerPanel.add(scrollpane, BorderLayout.CENTER);
        
        // Add table to the east of center panel
        
        mainpanel.add(tablePanel, BorderLayout.WEST);
        mainpanel.add(topSectionPanel, BorderLayout.NORTH);
        mainpanel.add(centerPanel, BorderLayout.CENTER);
        
        // Buttons panel at the bottom
        drawbuttons();
    }
    private void addPlayedCardToTable(Card card) {
        Player currentPlayer = game.getPlayers().get(currentplayerindex);
        String playerName = currentPlayer.getName();
        String cardInfo = card.toString(); 
        // Add new row to the table
        tableModel.addRow(new Object[]{playerName, cardInfo});
        playedCardsTable.scrollRectToVisible(playedCardsTable.getCellRect(
            tableModel.getRowCount()-1, 0, true));
    }
    
    private void initializePlayedCardsTable() {
        // Column names
        String[] columnNames = {"Player", "Card Played"};
        
        // Create table model with columns
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        playedCardsTable = new GameTable(tableModel);
      
        
        // Create scroll pane for table
        GameScrollPane tableScrollPane = new GameScrollPane(playedCardsTable);
        
        // Create panel for the table
        tablePanel = new GamePanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(350, 300));
        tablePanel.add(new GameLabel("Played Cards History"), BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
    }

    private void updateTopCardDisplay() {
        Card topCard = game.getTopCard();
        String imagePath = getCardImagePath(topCard);
        

   	 GameImageIcon topcardimage = new GameImageIcon(imagePath);
   	 Image topcardimg = topcardimage.getImage().getScaledInstance(150,200, Image.SCALE_SMOOTH);
   	 topcardimage = new GameImageIcon(topcardimg);
        
        topcardlabel.setIcon(topcardimage);
        topcardlabel.setText("Top Card:" + topCard.toString() );
        topcardlabel.setHorizontalTextPosition(GameLabel.CENTER);
        topcardlabel.setVerticalTextPosition(GameLabel.BOTTOM);
        topcardlabel.setIconTextGap(10);
    }
    
    private String getCardImagePath(Card card) {
        String color = card.getColor().toLowerCase();
        String type = card.getType().toLowerCase();
        
        if (type.equals("number")) {
            return "images/" + color + "_" + card.getValue() + ".png";
        } else {
            return "images/" + color + "_" + type.replace(" ", "_") + ".png";
        }
    }
    
    private void updatePlayerHand() {
        playerhand.removeAll();
        cardButtons.clear();
        
        Player currentPlayer = game.getPlayers().get(currentplayerindex);
        ArrayList<Card> hand = currentPlayer.getplayerHand();
        
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            String imagePath = getCardImagePath(card);
            GameImageIcon cardIcon = new GameImageIcon(imagePath);
            
            Image img = cardIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            cardIcon = new GameImageIcon(img);
            
            GameButton cardButton = new GameButton();
            cardButton.setIcon(cardIcon);
            cardButton.setPreferredSize(new Dimension(150,200));
            
            final int cardIndex = i;
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playCard(cardIndex);
                }
            });
            
            cardButtons.add(cardButton);
            playerhand.add(cardButton);
        }
        
        playerhand.revalidate();
        playerhand.repaint();
    }
    
    private void playCard(int cardIndex) {
        Player currentPlayer = game.getPlayers().get(currentplayerindex);
        Card cardToPlay = currentPlayer.getplayerHand().get(cardIndex);
        Card topCard = game.getTopCard();
        
        if (!currentPlayer.canPlayCard(cardToPlay, topCard)) {
            GameOptionPane.showMessageDialog(frame, 
                "You can't play that card! It must match either:\n" +
                "- The color (" + topCard.getColor() + ")\n" +
                "- The card type\n" +
                "- Or the value (if number card)");
            return;
        }
        
        Card playedCard = currentPlayer.playCard(cardIndex);
        game.setTopCard(playedCard);
        
        // Add the played card to the table
        addPlayedCardToTable(playedCard);
        
        handleSpecialCardEffects(playedCard);
        
        if (currentPlayer.playerHandSize() == 0) {
            game.checkWinner();
            GameOptionPane.showMessageDialog(frame, currentPlayer.getName() + " wins the game!");
            System.exit(0);
        }
        
        if (currentPlayer.playerHandSize() == 1) {
            GameOptionPane.showMessageDialog(frame, currentPlayer.getName() + " has UNO!");
        }
        
        updateGameDisplay();
    }

    private void handleSpecialCardEffects(Card playedCard) {
        int numplayers = game.getPlayers().size();
        int nextplayerindex;
        
        if (game.getDirection().equals("right")) {
        	nextplayerindex = (currentplayerindex + 1) % numplayers;
        } else {
        	nextplayerindex = (currentplayerindex - 1 + numplayers) % numplayers;
        }
        
        Player nextPlayer = game.getPlayers().get(nextplayerindex);
        
        if (playedCard instanceof DrawTwo) {
            for (int i = 0; i < 2; i++) {
                Card drawnCard = game.getDeck().drawCard();
                if (drawnCard != null) {
                    nextPlayer.drawCard(drawnCard);
                }
            }
            GameOptionPane.showMessageDialog(frame, nextPlayer.getName() + " draws 2 cards!");
            game.advanceToNextPlayer(); // Skip the next player's turn
        } 
        else if (playedCard instanceof WildDrawFour) {
            String[] colors = {"Red", "Green", "Blue", "Yellow"};
            String chosenColor = (String) GameOptionPane.showInputDialog(
                frame,
                "Choose a color:",
                "Wild Card Color Selection",
                GameOptionPane.PLAIN_MESSAGE,
                null,
                colors,
                colors[0]);
            
            if (chosenColor != null) {
                playedCard.setColor(chosenColor);
                game.setTopCard(playedCard);
            }
            
            for (int i = 0; i < 4; i++) {
                Card drawnCard = game.getDeck().drawCard();
                if (drawnCard != null) {
                    nextPlayer.drawCard(drawnCard);
                }
            }
            GameOptionPane.showMessageDialog(frame, nextPlayer.getName() + " draws 4 cards!");
            game.advanceToNextPlayer(); // Skip the next player's turn
        } 
        else if (playedCard instanceof Reverse) {
            game.ReverseDirection();
            GameOptionPane.showMessageDialog(frame, "Direction reversed!");
            // In two-player game, Reverse acts like a Skip
            if (game.getPlayers().size() == 2) {
                game.advanceToNextPlayer();
            }
        } 
        else if (playedCard instanceof Skip) {
            GameOptionPane.showMessageDialog(frame, nextPlayer.getName() + " was skipped!");
            game.advanceToNextPlayer();
            game.advanceToNextPlayer();// Skip the next player
            return; // Add this return statement to prevent double advancement
        } 
        else if (playedCard instanceof WildCard) {
            String[] colors = {"Red", "Green", "Blue", "Yellow"};
            String chosenColor = (String) GameOptionPane.showInputDialog(
                frame,
                "Choose a color:",
                "Wild Card Color Selection",
                GameOptionPane.PLAIN_MESSAGE,
                null,
                colors,
                colors[0]);
            
            if (chosenColor != null) {
                playedCard.setColor(chosenColor);
                game.setTopCard(playedCard);
            }
        }
        
        // For normal cards, advance to next player
        game.advanceToNextPlayer();
    }
    
    private void updateGameDisplay() {
        currentplayerindex = game.getCurrentPlayer();
        currentplayerlabel.setText("Current Player: " + game.getPlayers().get(currentplayerindex).getName() );
        updateTopCardDisplay();
        updatePlayerHand();
    }
    
    public void numplayers() {
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
                dialog.setVisible(false);
                frame.setVisible(true);
            }
        });
        
        threep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nump = 3;
                dialog.setVisible(false);
                frame.setVisible(true);
            }
        });
        
        fourp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nump = 4;
                dialog.setVisible(false);
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

    public void drawbuttons() {
        GamePanel buttonpanel = new GamePanel();
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Draw button
        draw = new GameButton("Draw");
        draw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player currentPlayer = game.getPlayers().get(currentplayerindex);
                Card drawnCard = game.getDeck().drawCard();
                if (drawnCard != null) {
                    currentPlayer.drawCard(drawnCard);
                    GameOptionPane.showMessageDialog(frame, "You drew a card!");
                    game.advanceToNextPlayer();
                    updateGameDisplay();
                } else {
                    GameOptionPane.showMessageDialog(frame, "No more cards in the deck!");
                }
            }
        });
        buttonpanel.add(draw);
        
        // Say UNO button
        sayUnoButton = new GameButton("Say UNO");
        sayUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player currentPlayer = game.getPlayers().get(currentplayerindex);
                if (currentPlayer.playerHandSize() == 1) {
                    GameOptionPane.showMessageDialog(frame, currentPlayer.getName() + " says UNO!");
                } else {
                    GameOptionPane.showMessageDialog(frame, "You can only say UNO when you have 1 card left!");
                }
            }
        });
        buttonpanel.add(sayUnoButton);
        
        mainpanel.add(buttonpanel, BorderLayout.SOUTH);
    }
  
}