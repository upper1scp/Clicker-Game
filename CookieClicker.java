import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GoldClicker extends JFrame {
    // non graphical variables
    private long gold = 999999999;
    private int clicker = 1;
    private int clickerPrice = 20;

    // graphical variables
    int numberOfColumns = 5;

    Container container;

    JLabel goldLabel;
    JButton increaseGoldButton;

    JLabel clickerLabel;
    JButton increaseClickerButton;

    // buildings
    Building forge;
    boolean forgeUnlocked;

    Building goldpaner;
    boolean goldpanerUnlocked;

    Building goldmine;
    boolean goldmineUnlocked;

    public GoldClicker() {
        container = getContentPane();
        container.setLayout(new GridLayout(5, 1));

        bakery = new Building("Forge", 0, 1, 20);
        forgeUnlocked = false;

        robot = new Building("Goldpaner", 0, 5, 100);
        goldpanerUnlocked = false;

        factory = new Building("Goldmine", 0, 10, 200);
        goldmineUnlocked = false;

        // produce cookies by hand
        GoldLabel = new JLabel("Gold Bar: " + Gold);
        increaseGoldButton = new JButton("Increase Gold");
        increaseGoldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gold += clicker;
            }
        });

        // improve clicking production rate
        clickerLabel = new JLabel("Clicker Level: " + clicker);
        increaseClickerButton = new JButton("Improve Clicker (Costs: " + clickerPrice + ")");
        increaseClickerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                increaseClicker();
            }

            private void increaseClicker() {
                if(gold >= clickerPrice) {
                    clicker++;
                    gold -= clickerPrice;
                    clickerPrice *= 2;
                    JOptionPane.showMessageDialog(null, "You have improved your clicker!");
                } else {
                    JOptionPane.showMessageDialog(null, "You don't have enough money!");
                }
            }
        });

        java.util.Timer actualizeProgress = new java.util.Timer();
        actualizeProgress.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                goldLabel.setText("Gold: " + gold);
                clickerLabel.setText("Clicker Level: " + clicker);
                increaseClickerButton.setText("Improve Clicker (Costs: " + clickerPrice + ")");
            }
        }, 0, 25);

        java.util.Timer getMoreBuildings = new java.util.Timer(); 
        getMoreBuildings.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (forgeUnlocked == false && clicker >= 2) {
                    forge.unlock();
                    forgeUnlocked = true;
                }
                if (goldpanerUnlocked == false && forge.getLevel() >= 2) {
                    goldpaner.unlock();
                    goldpanerUnlocked = true;
                }         
                if (goldmineUnlocked == false && goldpaner.getLevel() >= 2) {
                    goldmine.unlock();
                    goldmineUnlocked = true;
                }
            }
        }, 0, 2000);

        java.util.Timer produceWithBuildings = new java.util.Timer();
        produceWithBuildings.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                gold += bakery.getProductionRate() + goldpaner.getProductionRate() + goldmine.getProductionRate();
            }
        }, 0, 1000);

        container.add(goldLabel);
        container.add(increaseGoldButton);
        container.add(new JLabel("")); // blank label
        container.add(clickerLabel);
        container.add(increaseClickerButton);
    }

    public class Building {
        // non graphical variables
        private String name;
        private int level;
        private int productionRate;
        private int costs;

        // graphical variables
        JLabel label;
        JButton button;

        public Building(String name, int level, int productionRate, int costs) {
            // non graphical variables
            this.name = name;
            this.level = level;
            this.productionRate = productionRate;
            this.costs = costs;

            // graphical variables
            label = new JLabel();
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    improve();
                }
            });
        }

        public int getLevel() {
            return level;
        }

        public void unlock() {
            numberOfColumns += 3;
            container.setLayout(new GridLayout(numberOfColumns, 1));
            container.add(new JLabel(""));
            container.add(label);
            container.add(button);
            setSize(210, getHeight() + 120);
            actualize();
        }

        public void improve() {
            if(gold >= costs) {
                level++;
                gold -= costs;
                costs *= 2;
                JOptionPane.showMessageDialog(null, "You have improved the " + name + "!");
            } else {
                JOptionPane.showMessageDialog(null, "You don't have enough money!");
            }
            actualize();
        }

        public int getProductionRate() {
            return productionRate * level;
        }

        public void actualize() {
            label.setText(name + " Prod. Rate: " + getProductionRate());
            button.setText("Improve (costs: " + costs + ")");
        }
    }

    public static void main(String[] args) {
        GoldClicker goldClicker = new GoldClicker();
        goldClicker.setTitle("Gold Clicker");
        goldClicker.setSize(99999, 99999);
        goldClicker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        goldClicker.setVisible(true);
    }
}
