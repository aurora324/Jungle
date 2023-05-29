package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ChessComponent extends JComponent{
    private PlayerColor owner;
    private boolean selected;

    public ChessComponent() {
    }

    public ChessComponent(PlayerColor owner, boolean selected) {
        this.owner = owner;
        this.selected = selected;
    }
    public PlayerColor getOwner() {
        return owner;
    }


    public void setOwner(PlayerColor owner) {
        this.owner = owner;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isSelected()) {
            if (isSelected()) { // Highlights the model if selected.
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(210, 188, 20));
                Rectangle2D Rectangle = new Rectangle2D.Double(
                        0, 0, this.getWidth() , this.getHeight());
                g2d.fill(Rectangle);
            }
        }
    }
}
