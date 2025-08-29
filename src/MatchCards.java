import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
   
    class Card{
        String cardName;
        ImageIcon cardImageICon;

        Card(String cardName, ImageIcon cardImageIcon){
            this.cardName = cardName;
            this.cardImageICon = cardImageIcon;
        }

        public String toString(){
            return cardName;
        }

    }

    String[] cardList = {
        "ArmoredTitan",
        "AttackTitan",
        "BeastTitan",
        "CartTitan",
        "ColossalTitan",
        "FemaleTitan",
        "FoundingTitam",
        "JawTitan",
        "Mikasa",
        "WarHammerTitan"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; // 5*128 = 640px
    int boardHeight = rows * cardHeight; //  4*90 = 360px
 
    int errorCount = 0; 
    ArrayList<JButton> board;
    Timer hideCardtimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;


    JFrame frame = new JFrame("Attack on Titan Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restarButton = new JButton();


    MatchCards(){
        setupCard();
        shuffleCards();

        //frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);

        frame.add(textPanel, BorderLayout.NORTH);

        //game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for(int i = 0; i < cardSet.size(); i++){
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageICon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if(!gameReady){
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if(tile.getIcon() == cardBackImageIcon){
                        if(card1Selected == null){
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageICon);
                        }
                        else if(card2Selected == null){
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageICon);

                            if(card1Selected.getIcon() != card2Selected.getIcon()){
                                errorCount += 1;
                                textLabel.setText("Erros: " + Integer.toString(errorCount));
                                hideCardtimer.start();
                            }
                            else{
                                card1Selected = null;
                                card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        
        frame.add(boardPanel);

        //resart button
        restarButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restarButton.setText("Restart Game");
        restarButton.setPreferredSize(new Dimension(boardWidth, 30));
        restarButton.setFocusable(false);
        restarButton.setEnabled(false);
        restarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(!gameReady){
                    return;
                }
                gameReady = false;
                restarButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                shuffleCards();

                // re assign buttons with new cards
                for(int i = 0; i < board.size(); i++){
                    board.get(i).setIcon(cardSet.get(i).cardImageICon);
                    errorCount = 0;
                    textLabel.setText("Erros: " + Integer.toString(errorCount));
                    hideCardtimer.start();
                }
            }
        });
        restartGamePanel.add(restarButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);


        frame.pack();
        frame.setVisible(true);

        //start game
        hideCardtimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hideCards();
            }
        });
        hideCardtimer.setRepeats(false);
        hideCardtimer.start();


    }

    void setupCard(){
        cardSet = new ArrayList<Card>();
        for(String cardName : cardList){
            //load each card image
            Image cardImg = new ImageIcon(getClass().getResource("/img/"+ cardName + ".jpeg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }

        cardSet.addAll(cardSet);

        //load back of the card
        Image cardBackImg = new ImageIcon(getClass().getResource("/img/back.jpeg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards(){
        System.out.println(cardSet);

        //shuffle
        for(int i = 0; i < cardSet.size(); i++){
            int j = (int) (Math.random() * cardSet.size());

            //swap
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
        System.out.println(cardSet);
    }

    void hideCards(){
        if(gameReady && card1Selected != null && card2Selected != null){ // only flip 2 cards
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        }
        else{ // flip all cards face down
            for(int i = 0; i < board.size(); i++){
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restarButton.setEnabled(true);
        }
        
    }
}
