import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    // Method to initialize the deck with all card types
    private void initializeDeck() {
        // Add number cards and special cards to deck card(String color, String type, int value)
        for (String color : new String[] { "Red", "Green", "Blue", "Yellow" }) {
            cards.add(new Card(color, "Number", 0)); // one 0 card for each color

            // Adding two of each number card (1 - 9)
            for (int i = 1; i <= 9; i++) {
                cards.add(new Card(color, "Number", i)); // 1st instance
                cards.add(new Card(color, "Number", i)); // 2nd instance
            }

            // Adding two special action cards
            cards.add(new DrawTwo(color)); // two Draw Two cards
            cards.add(new DrawTwo(color)); // second Draw Two card
            cards.add(new Skip(color)); // two Skip cards
            cards.add(new Skip(color)); // second Skip card
            cards.add(new Reverse(color)); // two Reverse cards
            cards.add(new Reverse(color)); // second Reverse card
        }

        // Adding Wild and Wild DrawFour cards
        for (int i = 0; i < 4; i++) {
            cards.add(new WildCard()); // four Wild cards
            cards.add(new WildDrawFour()); // four Wild Draw Four cards
        }
    }

    // Method to shuffle the deck
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Method to draw a card from the deck
    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("The deck is empty!");
            return null;
        }
        return cards.remove(cards.size() - 1); // Draws the top card from the deck
    }

    // Method to reset the deck
    public void resetDeck() {
        cards.clear();
        initializeDeck();
        shuffle();
    }
    public void addCard(Card card) {
        // Check if the card is not null before adding
        if (card != null) {
            // Add the card to the deck
            cards.add(card);
        }
    }

    // Method to get the number of remaining cards in the deck
    public int getRemainingCards() {
        return cards.size();
    }}