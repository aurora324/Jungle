//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import model.PlayerColor;

public class ElephantChessComponent extends ChessComponent {
    private PlayerColor owner;
    private boolean selected;

    public ElephantChessComponent(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;
        this.setSize(size / 2, size / 2);
        this.setLocation(0, 0);
        this.setVisible(true);
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon pic = new ImageIcon("src/resources/elephant-head.png");
        if (owner == PlayerColor.BLUE){
            pic = new ImageIcon("src/resources/be.png");
        }
        Image image = pic.getImage();
        pic = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getWidth(),Image.SCALE_SMOOTH));
        JLabel label = new JLabel(pic);
        label.setSize(this.getWidth(), this.getWidth());
        //bgLabel.setLocation(0, 0);
        add(label);
    }
}
