package view;

import controller.GameController;
import model.Difficulty;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class AIFrame extends JFrame {
    public BeginFrame beginFrame;
    public boolean isLogin;
    public User user;
    public ArrayList<User> users;

    private final int WIDTH;
    private final int HEIGHT;

    public AIFrame() {
        setTitle("Jungle");
        this.WIDTH = 400;
        this.HEIGHT = 500;
        users = new ArrayList<>();

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addBackButton();
        addEasyButton();
        addNormalButton();
//        addUserButton();
//        addRankButton();

        Image image = new ImageIcon("resource/background/bg.png").getImage();
        image = image.getScaledInstance(400, 500, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel bg = new JLabel(icon);
        bg.setSize(400, 500);
        bg.setLocation(0, 0);
        add(bg);

        try {
            File file = new File("user/users");

            String temp;
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
            ArrayList<String> readList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(read);

            while ((temp = reader.readLine()) != null && !"".equals(temp)) {
                readList.add(temp);
                System.out.println(temp);
            }

            for (String s : readList) {
                String[] strArr = s.split(" ");
                users.add(new User(strArr[0], strArr[1], Integer.parseInt(strArr[2])));
            }

        } catch (Exception ignored) {
        }
    }

    private void addBackButton() {
        JButton button = new JButton("Back");
        button.addActionListener((e) -> {
            isLogin = false;
            user = null;
            this.setVisible(false);
            beginFrame.setVisible(true);
        });
        button.setLocation(20, 20);
        button.setSize(60, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(button);
    }

    private void addEasyButton() {
        JButton button = new JButton("Easy");
        button.addActionListener((e) -> {
            if (GameController.timer != null) {
                GameController.timer.stop();
                GameController.timer = null;
            }
            beginFrame.chessGameFrame.getChessboardComponent().gameController.reset();
            this.setVisible(false);
            beginFrame.chessGameFrame.timeLabel.setVisible(false);
            beginFrame.chessGameFrame.statusLabel.setLocation(810, 81);
            beginFrame.chessGameFrame.repaint();
            beginFrame.chessGameFrame.getChessboardComponent().gameController.AIDiff = Difficulty.EASY;
            beginFrame.chessGameFrame.setVisible(true);
        });
        button.setLocation(100, 150);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addNormalButton() {
        JButton button = new JButton("Normal");
        button.addActionListener((e) -> {
            if (GameController.timer != null) {
                GameController.timer.stop();
                GameController.timer = null;
            }

            beginFrame.chessGameFrame.getChessboardComponent().gameController.reset();
            this.setVisible(false);
            beginFrame.chessGameFrame.timeLabel.setVisible(false);
            beginFrame.chessGameFrame.statusLabel.setLocation(810, 81);
            beginFrame.chessGameFrame.repaint();
            beginFrame.chessGameFrame.getChessboardComponent().gameController.AIDiff = Difficulty.NORMAL;
            beginFrame.chessGameFrame.setVisible(true);

        });
        button.setLocation(100, 300);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }


}
