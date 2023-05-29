//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package view;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

import model.PlayerColor;

public class DenChessComponent extends JComponent {
    private PlayerColor owner;
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D)g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setColor(this.owner.getColor());
//    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon pic = new ImageIcon("src/resources/rd.png");
        if (owner == PlayerColor.BLUE){
            pic = new ImageIcon("src/resources/bd.png");
        }
        Image image = pic.getImage();
        pic = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getWidth(),Image.SCALE_SMOOTH));
        JLabel label = new JLabel(pic);
        label.setSize(this.getWidth(), this.getWidth());
        //bgLabel.setLocation(0, 0);
        add(label);
        }
    }

