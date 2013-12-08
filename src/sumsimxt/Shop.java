/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sumsimxt;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import sumsimxt.Buyable.AirBrakes;
import sumsimxt.Buyable.HealthUpgrade;
import sumsimxt.Buyable.Laser;
import sumsimxt.Buyable.ShieldUpgrade;
import sumsimxt.Buyable.SuperBomb;


public class Shop extends JFrame {

    private JPanel overPanel = new JPanel();
    private JPanel titlePanel = new JPanel();
    private JPanel superBombPanel = new JPanel();
    private JPanel laserPanel = new JPanel();
    private JPanel airBrakesPanel = new JPanel();
    private JPanel jetThrustersPanel = new JPanel();
    private JPanel healthPanel = new JPanel();
    private JPanel shieldPanel = new JPanel();
    private JLabel titleLabel = new JLabel("Sven's Space Shop");
    private JButton superBombButton = new JButton("SUPERBOMB");
    private JButton laserButton = new JButton("LASER");
    private JButton jetThrustersButton = new JButton("Jet Thrusters");
    private JButton healthButton = new JButton("Health Upgrade");
    private JButton shieldButton = new JButton("Shield Recharge");
    private JButton airBrakesButton = new JButton("Air Brakes");
    private JPanel itemsPanel = new JPanel();
    private JPanel exitPanel = new JPanel();
    private JLabel yourGoldLabel = new JLabel("  Funds:  ");
    private JLabel yourGoldIcon = new JLabel();
    private JButton hangarButton = new JButton("Back to Hangar");
    private List<JPanel> panels = new ArrayList<>();
    private List<Buyable> buyables = new ArrayList<>();
    private BuyListener buyListener = new BuyListener();
    private PlayerObject player = SumSimXT.getPlayer();
    private HashMap<String,JLabel> haveLabels = new HashMap<>();
    
    // buyables
    private SuperBomb superBomb = new SuperBomb();
    private Laser laser = new Laser();
    private HealthUpgrade healthUpgrade = new HealthUpgrade();
    private ShieldUpgrade shieldUpgrade = new ShieldUpgrade();
    private AirBrakes airBrakes = new AirBrakes();
    
    public static void main(String args[]) {
        Shop ns = new Shop();
    }
    
    public Shop() {
        super("SHOP");
        buyables.add(superBomb);
        buyables.add(laser);
        buyables.add(healthUpgrade);
        buyables.add(shieldUpgrade);
        buyables.add(airBrakes);
        panels.add(superBombPanel);
        panels.add(laserPanel);
        panels.add(healthPanel);
        panels.add(shieldPanel);
        panels.add(airBrakesPanel);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 26));
        titleLabel.setIcon(new ImageIcon(Sprite.getScaledSprite("ALIEN", 30, 30).getImage()));
        titlePanel.add(titleLabel);
        overPanel.setLayout(new BoxLayout(overPanel, BoxLayout.PAGE_AXIS));
        
        initShop();
        
        this.add(overPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void initShop() {
        itemsPanel = new JPanel();
        overPanel.add(titlePanel);
        overPanel.add(itemsPanel);
        GridLayout itemsLayout = new GridLayout(buyables.size(), 1);
        itemsLayout.setVgap(10);
        itemsPanel.setLayout(itemsLayout);
        
        Image coinIcon = Sprite.getScaledSprite("COIN", 10, 15).getImage();
        
        for (int i = 0; i < panels.size(); i++) {
            JPanel panel = panels.get(i);
            Buyable item = buyables.get(i);
            GridLayout gl = new GridLayout(1,2);
            gl.setHgap(10);
            panel.setLayout(gl);
            itemsPanel.add(panel);
            
            // put buy button
            JButton buyButton = new JButton(item.getName());
            buyButton.setIcon(new ImageIcon(item.getIcon()));
            buyButton.setVerticalTextPosition(SwingConstants.TOP);
            buyButton.addActionListener(buyListener);
            panel.add(buyButton);
            
            // put details panel
            JPanel details = new JPanel();
            details.setLayout(new BoxLayout(details, BoxLayout.PAGE_AXIS));
            JLabel costLabel = new JLabel(String.format("%d", item.getCost()));
            costLabel.setIcon(new ImageIcon(coinIcon));
            costLabel.setHorizontalTextPosition(JLabel.RIGHT);
            details.add(costLabel);
            int num = 0;
            switch (item.getName()) {
                case "SuperBomb":
                    num = player.getSuperBombs();
                    break;
                case "Laser":
                    num = player.getLasers();
                    break;
                case "Health Upgrade":
                    num = player.getTotalHP();
                    break;
                case "Air Brakes":
                    num = (player.hasAirBrakes()) ? 1 : 0;
                    break;
            }
            if (!item.getName().equals("Shield Recharge")) {
                JLabel haveLabel = new JLabel(String.format("Have: %d", num));
                if (!haveLabels.keySet().contains(item.getName())) {
                    haveLabels.put(item.getName(), haveLabel);
                }
                details.add(haveLabel);
            }
            panel.add(details);
        }
        GridLayout gl = new GridLayout(2,2);
        gl.setHgap(10);
        exitPanel.setLayout(gl);
        yourGoldLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
        yourGoldIcon.setIcon(new ImageIcon(Sprite.getScaledSprite("COIN", 30, 40).getImage()));
        yourGoldIcon.setText(String.format("%d", player.getGold()));
        yourGoldIcon.setFont(new Font("Helvetica", Font.BOLD, 18));
        exitPanel.add(yourGoldLabel);
        exitPanel.add(new JLabel());
        exitPanel.add(yourGoldIcon);
        exitPanel.add(hangarButton);
        hangarButton.addActionListener(buyListener);
        overPanel.add(exitPanel);
    }
    
    private class BuyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            int gold = player.getGold();
            String text = button.getText();
            if (text.equalsIgnoreCase("SuperBomb")) {
                if (gold > superBomb.getCost() && player.getSuperBombs() < 5) {
                    player.spendGold(superBomb.getCost());
                    player.addSuperBomb();
                }
            } else if (text.equalsIgnoreCase("Laser")) {
                if (gold > laser.getCost() && player.getLasers() < 5) {
                    player.spendGold(laser.getCost());
                    player.addLaser();
                }
            } else if (text.equalsIgnoreCase("Health Upgrade")) {
                if (gold > healthUpgrade.getCost() && player.getTotalHP() < 10) {
                    player.spendGold(healthUpgrade.getCost());
                    player.setTotalHP(player.getTotalHP() + 1);
                }
            } else if (text.equalsIgnoreCase("Shield Recharge")) {
                if (gold > shieldUpgrade.getCost()) {
                    player.spendGold(shieldUpgrade.getCost());
                    player.rechargeShields();
                }
            } else if (text.equalsIgnoreCase("Air Brakes")) {
                if (gold > airBrakes.getCost() && !player.hasAirBrakes()) {
                    player.spendGold(airBrakes.getCost());
                    player.addAirBrakes();
                }
            } else if (text.equalsIgnoreCase("Back to Hangar")) {
                Shop.this.dispose();
            }
            if (!text.equalsIgnoreCase("Back to Hangar")) {
                Shop shop = new Shop();
            }
            Shop.this.dispose();
//            overPanel.remove(itemsPanel);
//            initShop();
//            for (String key : haveLabels.keySet()) {
//                JLabel haveLabel = haveLabels.get(key);
//                int num = 0;
//                switch (key) {
//                    case "SuperBomb":
//                        num = player.getSuperBombs();
//                        break;
//                    case "Laser":
//                        num = player.getLasers();
//                        break;
//                    case "Health Upgrade":
//                        num = player.getTotalHP();
//                        break;
//                    case "Air Brakes":
//                        num = (player.hasAirBrakes()) ? 1 : 0;
//                        break;
//                }
//                if (!key.equals("Shield Recharge")) {
//                    haveLabel.setText(String.format("Have: %d", num));
//                }
//            }
//            Shop.this.repaint();
        }
    }
}
