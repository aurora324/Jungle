//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package view;

import controller.GameController;
import model.Chessboard;
import model.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.channels.Pipe;
//import java.awt.image.BufferedImage;
import javax.swing.*;
//import utils.ImageUtils;

public class ChessGameFrame extends JFrame {
    public ChessboardComponent view;
    public BeginFrame beginFrame;
    private int WIDTH = 0;
    private int HEIGHT = 0;
    private int ONE_CHESS_SIZE;
    private ChessboardComponent chessboardComponent;
    public GameController gameController;
    JLabel statusLabel = new JLabel("Turn 1: BLUE");
    JLabel timeLabel = new JLabel("Time: 60");
    public Chessboard chessboard;

    public boolean isSpring;
    JLabel background;
    public JLabel springBG;
    public JLabel autumnBG;

    public ChessGameFrame() {
    }

    public ChessGameFrame(int width, int height) {
        this.setTitle("Jungle");
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ONE_CHESS_SIZE = this.HEIGHT * 4 / 5 / 9;
        this.setSize(this.WIDTH, this.HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.addChessboard();
        this.addStatusLabel();
        this.addTimeLabel();
        this.addResetButton();
        this.addBackButton();
        this.addSaveButton();
        this.addLoadButton();
        this.addRegretButton();
        this.addPlayBackButton();
        this.addChangeThemeButton();
        this.addRepaintButton();


        Image image = new ImageIcon("src/spring.png").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        springBG = new JLabel(icon);
        springBG.setSize(1100, 810);
        springBG.setLocation(0, 0);

        image = new ImageIcon("src/autumn.png").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        icon = new ImageIcon(image);
        autumnBG = new JLabel(icon);
        autumnBG.setSize(1100, 810);
        autumnBG.setLocation(0, 0);
        background = springBG;
        add(background);
    }

    public ChessboardComponent getChessboardComponent() {
        return this.chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    private void addChessboard() {
        this.chessboardComponent = new ChessboardComponent(this.statusLabel, this.timeLabel, this.chessboard, this.ONE_CHESS_SIZE);

        this.chessboardComponent.setLocation(this.HEIGHT / 5, this.HEIGHT / 10);
        this.add(this.chessboardComponent);
    }

    private void addStatusLabel() {
        statusLabel.setLocation(HEIGHT - 40, HEIGHT / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    private void addTimeLabel() {
        timeLabel.setLocation(HEIGHT + 140, HEIGHT / 10);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(timeLabel);
    }

    private void addResetButton() {
        JButton button = new JButton("Reset");
        button.addActionListener((e) -> {
            chessboardComponent.gameController.reset();
        });
        button.setLocation(HEIGHT, HEIGHT / 10 + 74);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setToolTipText("重新开始游戏");
        //button.setBorder(BorderFactory.createBevelBorder(1,new Color(1,1,1),new Color(11,11,11),new Color(111,111,1111),new Color(11111,111111,111111)));
        add(button);
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGHT, HEIGHT / 10 + 148);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setToolTipText("保存游戏");
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click Save");
            String path = JOptionPane.showInputDialog(null, "Input your File Name", "File Name:", JOptionPane.INFORMATION_MESSAGE);
            if (path == null) {
                JOptionPane.showMessageDialog(null,"File Name can NOT be NULL!");
                path = JOptionPane.showInputDialog(null, "Input your File Name", "File Name:", JOptionPane.INFORMATION_MESSAGE);
            }

            try {
                this.gameController.saveGame(path);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGHT, HEIGHT / 10 + 222);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setToolTipText("加载游戏");
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            gameController.loadGame();
        });
    }

    private void addRegretButton() {
        JButton button = new JButton("Regret");
        button.setLocation(HEIGHT, HEIGHT / 10 + 296);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setToolTipText("悔棋");
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click regret");
            gameController.regretOneStep();
            if (view.gameController.AIPlaying){
                view.gameController.regretOneStep();
            }
        });
    }

    private void addPlayBackButton() {
        JButton button = new JButton("Play Back");
        button.setLocation(HEIGHT, HEIGHT / 10 + 370);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setToolTipText("双方各悔一次棋");
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click playback");
           beginFrame.gameController.playback();
            Timer.time = 20;
        });
    }

    private void addRepaintButton() {
        JButton button = new JButton("Repaint");
        button.setLocation(HEIGHT, HEIGHT / 10 + 444);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 14));
        add(button);
        button.setToolTipText("重新加载游戏");

        button.addActionListener(e -> {
            gameController.newStep();
        });
    }
//
    private void addChangeThemeButton() {
        JButton button = new JButton("Change Theme");
        button.setLocation(HEIGHT, HEIGHT / 10 + 518);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
        button.setToolTipText("改变背景图片");

        button.addActionListener(e -> {
            System.out.println("Click change theme");
            gameController.view.changeTheme(isSpring);
            if (isSpring){
                remove(background);
                isSpring = false;
                background = autumnBG;
                add(background);
            } else {
                remove(background);
                isSpring = true;
                background = springBG;
                add(background);
            }
            repaint();
            revalidate();
        });
    }

    private void addBackButton() {
        JButton button = new JButton("Back");
        button.setLocation(HEIGHT, HEIGHT / 10 + 592);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setToolTipText("主菜单");

        button.addActionListener(e -> {
            this.getChessboardComponent().gameController.reset();
            System.out.println("Click back");
            this.setVisible(false);
            beginFrame.setVisible(true);
        });
    }

    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }
}
