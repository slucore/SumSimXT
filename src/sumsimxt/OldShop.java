/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import sumsimxt.Buyable.AirBrakes;
import sumsimxt.Buyable.JetThrusters;
import sumsimxt.Buyable.Laser;
import sumsimxt.Buyable.ShieldUpgrade;
import sumsimxt.Buyable.SuperBomb;


public class OldShop extends JFrame {    

  //  private List<Buyable> allBuyables = new ArrayList<>();
    private List<Buyable> boughtItems = new ArrayList<>();
    private int gold = 200; // grab the score here
    private int numBombs = 0; // grab the bombs here
    private boolean jetBool; // if you want to make these carry over from previous levels set them to true. 
    private boolean shieldBool;
    private boolean brakeBool; 
    private boolean flameBool;
    private boolean laserBool;
    
    private JButton shieldButton;
    private JButton superBombButton;
    private JButton speedBoostButton;
    private JButton brakesButton;
    private JButton flamesButton;
    private JButton laserButton;
    private JLabel goldLabel;
    
    /*
     * Make an object of each to grab price and icon
     */
    SuperBomb superbomb = new SuperBomb();
    JetThrusters speedboost = new JetThrusters();
    ShieldUpgrade shield = new ShieldUpgrade();
    AirBrakes brakes = new AirBrakes();
    Laser laser = new Laser();

    public static void main(String args[]) {
        OldShop shop = new OldShop();
        shop.setVisible(true);
        shop.pack();
    }
    
    public OldShop() {
        super("L00t Shop");        
        setLayout(new BorderLayout()); //overall JFrame layout
        
        /*
         * Upper Panel
         */
        JPanel goldPanel = new JPanel();
        goldPanel.setLayout(new FlowLayout());        
        goldLabel = new JLabel("YOUR GOLD: " + Integer.toString(gold));
        goldPanel.add(goldLabel);        
        add(goldPanel, BorderLayout.NORTH);
        
        /*
         * Lower Panel
         */
        JPanel shopPanel = new JPanel();        
        shopPanel.setLayout(new GridLayout(0, 2, 50, 0));  
        
      
        /*
         * Super Bomb Panel
         */
        JLabel superBombLabel = new JLabel("Super Bomb");
        //superBombLabel.setIcon(new ImageIcon(superbomb.getSprite().getImage()));
        superBombButton = new JButton(Integer.toString(superbomb.getCost()) + " Gold " + "(You have " + numBombs + " bombs)" );
        superBombButton.addActionListener(new BuyBomb());
        
        JPanel superPanel = new JPanel();
        superPanel.setLayout(new BorderLayout());
        superPanel.add(superBombLabel, BorderLayout.CENTER);
        superPanel.add(superBombButton, BorderLayout.SOUTH); 
        if(numBombs >= 3){
        	
        	superBombButton.setEnabled(false);
        }
        shopPanel.add(superPanel);
        
        /*
         * Shield Panel
         */
        JLabel shieldLabel = new JLabel("Shield");
       // speedBoostLabel.setIcon(new ImageIcon(speedboost.getSprite().getImage()));
        shieldButton = new JButton(Integer.toString(shield.getCost()) + " Gold");
        shieldButton.addActionListener(new BuyShield());
        
        JPanel shieldPanel = new JPanel();
        shieldPanel.setLayout(new BorderLayout());
        shieldPanel.add(shieldLabel, BorderLayout.CENTER);
        shieldPanel.add(shieldButton, BorderLayout.SOUTH);   
        shopPanel.add(shieldPanel);        
         
        
        /*
         * Jet Thrusters Panel
         */
        JLabel speedBoostLabel = new JLabel("Jet Thrusters");
       // speedBoostLabel.setIcon(new ImageIcon(shield.getSprite().getImage()));
        speedBoostButton = new JButton(Integer.toString(speedboost.getCost()) + " Gold");
        speedBoostButton.addActionListener(new BuySpeed());
        
        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new BorderLayout());
        speedPanel.add(speedBoostLabel, BorderLayout.CENTER);
        speedPanel.add(speedBoostButton, BorderLayout.SOUTH);   
        shopPanel.add(speedPanel);
        
        /*
         * Air Brakes Panel
         */
        JLabel brakesLabel = new JLabel("Air Brakes");
       // speedBoostLabel.setIcon(new ImageIcon(shield.getSprite().getImage()));
        brakesButton = new JButton(Integer.toString(brakes.getCost()) + " Gold");
        brakesButton.addActionListener(new BuyBrakes());
        
        JPanel brakesPanel = new JPanel();
        brakesPanel.setLayout(new BorderLayout());
        brakesPanel.add(brakesLabel, BorderLayout.CENTER);
        brakesPanel.add(brakesButton, BorderLayout.SOUTH);   
        shopPanel.add(brakesPanel);
               
        /*
         * Laser Panel
         */
        JLabel laserLabel = new JLabel("Laser");
       // speedBoostLabel.setIcon(new ImageIcon(shield.getSprite().getImage()));
        laserButton = new JButton(Integer.toString(laser.getCost()) + " Gold");
        laserButton.addActionListener(new BuyLaser());
        
        JPanel laserPanel = new JPanel();
        laserPanel.setLayout(new BorderLayout());
        laserPanel.add(laserLabel, BorderLayout.CENTER);
        laserPanel.add(laserButton, BorderLayout.SOUTH);   
        shopPanel.add(laserPanel);
        
        disableButtons();
        add(shopPanel, BorderLayout.CENTER);     
        
    }

    public List<Buyable> getBoughtItems() {
        return boughtItems;
    }
    
    public int getGold() {    	
    	return gold; 
    }
    
    public void disableButtons(){
    	
    	if(jetBool){
    		speedBoostButton.setEnabled(false);
    	}
    	
    	if(brakeBool){
    		brakesButton.setEnabled(false);
    	}
    	
    	if(flameBool){
    		flamesButton.setEnabled(false);
    	}
    	
    	if(laserBool){
    		laserButton.setEnabled(false);
    	}
    	if(shieldBool){
    		shieldButton.setEnabled(false);
    	}
    	
    	if(gold < laser.getCost()){
    		laserButton.setEnabled(false);
    	}
    	
    	if(gold < speedboost.getCost()){
    		speedBoostButton.setEnabled(false);
    	}
    	if(gold < brakes.getCost()){
    		brakesButton.setEnabled(false);
    	}
    	if(gold < shield.getCost()){
    		shieldButton.setEnabled(false);
    	}
    	if(gold < superbomb.getCost()){
    		superBombButton.setEnabled(false);
    	}
    }
    
    private class BuyBomb implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
            
        	numBombs++;
        	superBombButton.setText(Integer.toString(superbomb.getCost()) + " Gold " + "(You have " + numBombs + " bombs)");
        	boughtItems.add(superbomb);
        	gold -= superbomb.getCost(); 
        	goldLabel.setText("YOUR GOLD: " + Integer.toString(gold));
        	
        	if(numBombs == 3){
        		
        		superBombButton.setText("MAXED OUT");
        		superBombButton.setEnabled(false);        		
        	}    
        	
        	disableButtons();        	
        }
    }
    
    private class BuyShield implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
        	
        	boughtItems.add(shield);
        	shieldButton.setEnabled(false);
        	shieldButton.setText("SOLD");
        	gold -= shield.getCost();
        	goldLabel.setText("YOUR GOLD: " + Integer.toString(gold)); 
        	disableButtons();
           
        }
    }
    
    private class BuySpeed implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
            
        	boughtItems.add(speedboost);
        	speedBoostButton.setEnabled(false);
        	speedBoostButton.setText("SOLD");
        	gold -= speedboost.getCost();
        	goldLabel.setText("YOUR GOLD: " + Integer.toString(gold));
            disableButtons();
        }
    }
    
    private class BuyBrakes implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
        	
        	boughtItems.add(brakes);
        	brakesButton.setEnabled(false);
        	brakesButton.setText("SOLD");
        	gold -= brakes.getCost();
        	goldLabel.setText("YOUR GOLD: " + Integer.toString(gold));
        	disableButtons();
        }
    }
    
    private class BuyLaser implements ActionListener{
        
        public void actionPerformed(ActionEvent e){
           
        	boughtItems.add(laser);
        	laserButton.setEnabled(false);
        	laserButton.setText("SOLD");
        	gold -= laser.getCost();    
        	goldLabel.setText("YOUR GOLD: " + Integer.toString(gold));
        	disableButtons();
        }
    }
    
}


