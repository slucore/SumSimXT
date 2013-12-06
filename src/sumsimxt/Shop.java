/**
 * Stephen LuCore and Chad Stapes
 * Intro to Software Design: Group Project
 * 12-2-13
 */
package sumsimxt;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Shop extends JFrame {

    private List<Buyable> allBuyables = new ArrayList<>();
    private List<Buyable> boughtItems = new ArrayList<>();
    
    public Shop() {
        allBuyables.add(new SuperBomb());
        // allBuyables.add(new whatever());
        // allBuyables.add(new thirdThing());
        
        // construct and show GUI
        
        // for example:

        // generic implementation
/*      for (Buyable buyable : allBuyables) {
            JLabel label = new JLabel(buyable.getName());
            label.setIcon(new ImageIcon(buyable.getSprite().getImage()));
            somePanel.add(label);
            somePanel.add(new JLabel(String.format("%d", buyable.getCost())));
        }
*/

        // hardcoded implementation
/*      JLabel superbombLabel = new JLabel("SuperBomb");
        superbombLabel.setIcon(new ImageIcon(superBomb.getSprite().getImage()));
        panel.add(superbombLabel);
        panel.add(new JLabel(String.format("%d"), superBomb.getCost()));
*/
        
        // either way is fine; do whatever you're comfortable with
        
        // when user confirms purchases, add them to boughtItems list
    }

    public List<Buyable> getBoughtItems() {
        return boughtItems;
    }
    
}
