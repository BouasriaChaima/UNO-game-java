import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> playerHand;

    // Constructor
    public Player(String name) {
        this.name = name;
        this.playerHand = new ArrayList<>();
    }

    // Set player's name
    public void setPlayerName(String name) {
        this.name = name;
    }

    // Get player's name
    public String getName() {
        return name;
    }

    // Get player's hand
    public ArrayList<Card> getplayerHand() {
        return new ArrayList<>(playerHand);
    }

    // Set player's hand
    public void setPlayerHand() {
        this.playerHand = new ArrayList<>();
    }

    // Get number of cards in player's hand
    int playerHandSize() {
        return playerHand.size();
    }

    // A method to check if a specific card can be played
    public boolean canPlayCard(Card card, Card topCard) {
        // Wild cards are always playable
        if (card instanceof WildCard || card instanceof WildDrawFour) {
            return true;
        }
        
        // Check color match
        if (card.getColor().equals(topCard.getColor())) {
            return true;
        }
        
        // Special cards handling - match by actual type, not just the string
        if ((card instanceof DrawTwo && topCard instanceof DrawTwo) ||
            (card instanceof Reverse && topCard instanceof Reverse) ||
            (card instanceof Skip && topCard instanceof Skip)) {
            return true;
        }
        
        // Check number cards by type and value
        if (card.getType().equals("Number") && topCard.getType().equals("Number") &&
            card.getValue() == topCard.getValue()) {
            return true;
        }
        
        // If none of the above conditions are met
        return false;
    }

    // A method to remove a card from the player's hand
    public Card playCard(int cardIndex) {
        if (cardIndex >= 0 && cardIndex < playerHand.size()) {
            return playerHand.remove(cardIndex);
        }
        return null;
    }

    // A method to draw a card from the deck
    public void drawCard(Card card) {
        if (card != null) {
            playerHand.add(card);
        }
    }
}
