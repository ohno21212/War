import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WarInterface extends JFrame{
        private Game game;
        private JLabel j1;
        private JLabel j2;
        private JLabel j3;
        private JLabel j4;
        private JButton j5;
        private JLabel j6;
        private JLabel j7;
        private JLabel j8;
        private JLabel j9;
        private Card playerCard;
        private Card computerCard;
        public WarInterface(){ 
        
           setLayout(new GridLayout(0,3));
           game= new Game();
           
           //get text
           //add player card image
           j1= new JLabel((""),SwingConstants.CENTER);//+game.getPlayer().getDeckSize()
           //j1.setIcon(new ImageIcon("pics/2h.jpg"));
           this.getContentPane().add(j1);
           
           //War thing
           j2 = new JLabel("WAR",SwingConstants.CENTER);
           this.getContentPane().add(j2);
           //add compute card
           j3=new JLabel((""),SwingConstants.CENTER);//+game.getComputer().getDeckSize()
           //j3.setIcon(new ImageIcon("pics/5h.jpg"));
           this.getContentPane().add(j3);
           //add cad back
           j4= new JLabel("" ,SwingConstants.CENTER);
           j4.setIcon(new ImageIcon("pics/back.jpg"));
           this.getContentPane().add(j4);
           j5= new JButton("Click to action");
           //make button
           j5.addActionListener(new ButtonListener()); 
           this.getContentPane().add(j5);
           

           j6= new JLabel("",SwingConstants.CENTER);
           j6.setIcon(new ImageIcon("pics/back.jpg"));
           this.getContentPane().add(j6);
           
           j7= new JLabel(("Player "+ game.getPlayerDiscard().getDeckSize()),SwingConstants.CENTER);
           this.getContentPane().add(j7);
           
           j8= new JLabel("",SwingConstants.CENTER);
           this.getContentPane().add(j8);
           
           j9= new JLabel(("Computer "+ game.getComputerDiscard().getDeckSize()),SwingConstants.CENTER);
           this.getContentPane().add(j9);   
   
 }  
 //pressing button activates another round, you need to put something in that will display who won, and then can loop back to allow the game to be replayed
   private class ButtonListener implements ActionListener{
   public void actionPerformed(ActionEvent e){
      if(((game.getPlayer().getDeckSize())+game.getPlayerDiscard().getDeckSize())>0&&((game.getComputer().getDeckSize())+(game.getComputerDiscard().getDeckSize())>0)){
          try{
            playerCard=(game.getPlayer()).takeNextCard();
          }catch(ArrayIndexOutOfBoundsException f){
            (game.getPlayer()).cloneCardPile((game.getPlayerDiscard()).getCardPile());
            (game.getPlayer()).shuffleDeck();
            game.setPlayerDiscard(new CardPile());
            playerCard=(game.getPlayer()).takeNextCard();
          } 
          j1.setIcon(new ImageIcon(playerCard.getImage()));//playerCard.getImage())

          try{
            computerCard=(game.getComputer()).takeNextCard();
          }catch(ArrayIndexOutOfBoundsException f){
            (game.getComputer()).cloneCardPile((game.getComputerDiscard()).getCardPile());
            (game.getComputer()).shuffleDeck();
            game.setComputerDiscard(new CardPile());
            computerCard=(game.getComputer()).takeNextCard();
          } 
          j3.setIcon(new ImageIcon(computerCard.getImage()));
          if(playerCard.getRank()<computerCard.getRank()){
            game.getComputerDiscard().addCard(playerCard);
            game.getComputerDiscard().addCard(computerCard);
            //change this to output in the jbutton
            j2.setText("Computer card "+computerCard.toString()+" is larger than Player Card"+playerCard.toString());

          }else if(computerCard.getRank()<playerCard.getRank()){
             game.getPlayerDiscard().addCard(playerCard);
             game.getPlayerDiscard().addCard(computerCard);
             j2.setText("Player card "+playerCard.toString()+" is larger than Computer Card"+computerCard.toString());

          }else if(computerCard.getRank()==playerCard.getRank()){
            CardPile playerWar= new CardPile();
            CardPile computerWar = new CardPile();
            int warCount=1;
            playerWar.addCard(playerCard);
            computerWar.addCard(computerCard);
            war(playerWar,computerWar,warCount);

          }
        }
         //top player card goes to j1 top compute card goes to j3
         // 
    }
 }
   public void war(CardPile p, CardPile c,int w){
   //collect all cards in war pile
      int warCount=w;
      CardPile playerWar=p;
      CardPile computerWar=c;
      int exceptionCount;
      j2.setText("WAR!");
      if((game.getPlayer().getDeckSize()+game.getPlayerDiscard().getDeckSize())<2){
         j2.setText("Player is out of cards and cannot continue the war!");
      }else if((game.getComputer().getDeckSize()+game.getComputerDiscard().getDeckSize())<2){
         j2.setText("Computer is out of cards and cannot continue the war!");
         //if not add cards from either the main deck or the discard
      }else{
         for(int i=0;i<2;i++){
            try{
               playerWar.addCard(game.getPlayer().takeNextCard());
               
            }catch(IndexOutOfBoundsException e){
               game.getPlayer().cloneCardPile(game.getPlayerDiscard().getCardPile());
               game.getPlayer().shuffleDeck();
               game.getPlayerDiscard().dumpDeck();
               playerWar.addCard(game.getPlayer().takeNextCard());           
            }
         }
         for(int i=0;i<2;i++){
            try{
               computerWar.addCard(game.getComputer().takeNextCard());
               
            }catch(IndexOutOfBoundsException e){
               game.getComputer().cloneCardPile(game.getComputerDiscard().getCardPile());
               game.getComputer().shuffleDeck();
               game.getComputerDiscard().dumpDeck(); //POSSIBLE PROBLEMS
               computerWar.addCard(game.getComputer().takeNextCard());           
            }
         }
         
         int warMult=((warCount*2)+1);
         if(((computerWar.getNextCard()).getRank())>((playerWar.getNextCard()).getRank())){
            System.out.println("Computer card "+(computerWar.getNextCard().toString())+" is bigger than "+ playerWar.getNextCard().toString());
            for(int i=0;i<warMult;i++){
               game.getComputerDiscard().addCard(playerWar.takeNextCard());
               game.getComputerDiscard().addCard(computerWar.takeNextCard());
            }
         }else if(((playerWar.getNextCard()).getRank())>((computerWar.getNextCard()).getRank())){
            System.out.println("Player card "+(playerWar.getNextCard().toString())+" is bigger than "+ computerWar.getNextCard().toString());
            for(int i=0;i<warMult;i++){
               game.getPlayerDiscard().addCard(playerWar.takeNextCard());
               game.getPlayerDiscard().addCard(computerWar.takeNextCard());
            }
         }else if(((playerWar.getNextCard()).getRank())==((playerWar.getNextCard()).getRank())){
            warCount++;
            System.out.println("Second war declared!");
            war(playerWar,computerWar,warCount);
            
         }
       }           
   }
}

/*
public void playGame(){
                           System.out.println("P: "+player.getDeckSize()+" C: "+ computer.getDeckSize());  
         System.out.println("PD: "+playerDiscard.getDeckSize()+" C: "+computerDiscard.getDeckSize());

           }
      
   }
   public void war(CardPile p, CardPile c,int w){
     
                  //now compare the latest war card;
         int warMult=((warCount*2)+1);
         if(((computerWar.getNextCard()).getRank())>((playerWar.getNextCard()).getRank())){
            System.out.println("Computer card "+(computerWar.getNextCard().toString())+" is bigger than "+ playerWar.getNextCard().toString());
            for(int i=0;i<warMult;i++){
               computerDiscard.addCard(playerWar.takeNextCard());
               computerDiscard.addCard(computerWar.takeNextCard());
            }
         }else if(((playerWar.getNextCard()).getRank())>((computerWar.getNextCard()).getRank())){
            System.out.println("Player card "+(playerWar.getNextCard().toString())+" is bigger than "+ computerWar.getNextCard().toString());
            for(int i=0;i<warMult;i++){
               playerDiscard.addCard(playerWar.takeNextCard());
               playerDiscard.addCard(computerWar.takeNextCard());
            }
         }else if(((playerWar.getNextCard()).getRank())==((playerWar.getNextCard()).getRank())){
            warCount++;
            war(playerWar,computerWar,warCount);
            System.out.println("Second war declared! press enter to continue");
            keyboard.nextLine();
         }
      }
   }
   public CardPile getPlayer(){return player;}
   public CardPile getComputer(){return computer;}  
}
*/