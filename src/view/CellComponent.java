//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package view;

import model.ChessboardPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class CellComponent extends JPanel {
    private Color background;
    public boolean canmove;
    int size;
    public CellComponent(Color background, Point location, int size) {
        this.setLayout(new GridLayout(1, 1));
        this.setLocation(location);
        this.setSize(size, size);
        this.background = background;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(this.background);
        g.fillRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        if (canmove) { // Highlights the model if selected.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 253, 87, 150));
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1,
                    this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
            Rectangle2D rectangle2D = new Rectangle2D.Double(1,1,this.getWidth(),this.getHeight());
            g2d.fill(rectangle2D);
        }
    }
}
