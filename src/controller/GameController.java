//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
//IOException 有异常就会停止程序
//Exception 会继续程序
package controller;

import listener.GameListener;
import model.*;
import model.Timer;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static model.PlayerColor.BLUE;
import static model.PlayerColor.RED;

public class GameController implements GameListener {
    public Chessboard model;
    public ChessboardComponent view;
    public BeginFrame beginFrame;
    private PlayerColor currentPlayer;
    private ChessboardPoint selectedPoint;
    public static int turn = 2;
    public boolean isPlayBack;
    public static Timer timer;
    public boolean AIPlaying;
    public Difficulty AIDiff;
    public Thread thread;
    public ArrayList<ChessboardPoint> possibleMove = new ArrayList<>();

    public Chessboard getModel() {
        return model;
    }

    public ChessboardComponent getView() {
        return view;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessboardPoint getSelectedPoint() {
        return selectedPoint;
    }

    public static int getTurn() {
        return turn;
    }

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = BLUE;
        view.registerController(this);
        this.initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); ++i) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); ++j) {

            }
        }
    }

    public void swapColor() {
        currentPlayer = currentPlayer == BLUE ? PlayerColor.RED : BLUE;
        turn++;
        if (currentPlayer == BLUE) {
            view.statusLabel.setText("Turn " + turn / 2 + ": BLUE");
        } else {
            view.statusLabel.setText("Turn " + turn / 2 + ": RED");
        }
        Timer.time = 60;

    }

    public ArrayList<ChessboardPoint> getpossibleMove(ChessboardPoint src){
        ArrayList<ChessboardPoint> possibleMove = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint dest = new ChessboardPoint(i,j);
                if(this.model.isValidMove(src,dest)||this.model.isValidCapture(src,dest)){
                    view.getGridComponents(dest).canmove=true;
                    possibleMove.add(dest);
                }
            }
        }
        return possibleMove;
    }
    public void setCanStepFalse() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint a = new ChessboardPoint(i,j);
                view.getGridComponents(a).canmove = false;
            }
        }
    }

    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (this.selectedPoint != null && this.model.isValidMove(this.selectedPoint, point)) {
             possibleMove =null;
            this.setCanStepFalse();
            this.model.moveChessPiece(this.selectedPoint, point);
            this.view.setChessComponentAtGrid(point, this.view.removeChessComponentAtGrid(this.selectedPoint));
            this.selectedPoint = null;
            this.swapColor();
             this.view.repaint();
            this.view.revalidate();
            if (ifEnd()) {
                int reddead = model.getRedDeadSize();
                int bluedead = model.getBlueDeadSize();
                if(currentPlayer==RED&&beginFrame.selectedUser!=null){
                    beginFrame.selectedUser.score+=10;
                }
                JOptionPane.showMessageDialog(null, currentPlayer == BLUE ? RED : BLUE + " Win.\n" + "Red dead: " + reddead + "\n" + "Blue dead: " + bluedead);
                reset();
            }
            if (AIPlaying&&currentPlayer==RED) {
                if (AIDiff == Difficulty.EASY) {
                    easyAI();
                } else if (AIDiff == Difficulty.NORMAL) {
                    normalAI();
                }
            }
        }
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, ChessComponent component) {
        if (this.selectedPoint == null) {
            if (this.model.getChessPieceOwner(point).equals(this.currentPlayer)) {
                 possibleMove=this.getpossibleMove(point);
                selectedPoint = point;
                this.selectedPoint = point;
                component.setSelected(true);
                component.repaint();
                 this.view.repaint();
                this.view.revalidate();
            }
        } else if (this.selectedPoint.equals(point)) {
              possibleMove=null;
            setCanStepFalse();
            this.selectedPoint = null;
            component.setSelected(false);
            component.repaint();
             this.view.repaint();
                this.view.revalidate();
        } else if (this.model.isValidCapture(this.selectedPoint, point)) {        //this.selectedPoint != null&&
            //&& this.model.isValidMove(this.selectedPoint, point)
            if (this.model.getChessPieceAt(point).getOwner() == BLUE) {
                this.model.blueDead.add(this.model.getChessPieceAt(point));
            }
            if (this.model.getChessPieceAt(point).getOwner() == RED) {
                this.model.redDead.add(this.model.getChessPieceAt(point));
            }
            this.setCanStepFalse();
            this.view.removeChessComponentAtGrid(point);
            this.model.removeChess(point);
            this.model.moveChessPiece(this.selectedPoint, point);
            this.view.setChessComponentAtGrid(point, this.view.removeChessComponentAtGrid(this.selectedPoint));
            this.selectedPoint = null;
            this.swapColor();
            this.view.repaint();
             this.view.revalidate();
        }
        if (ifEnd()) {
            int reddead = model.getRedDeadSize();
            int bluedead = model.getBlueDeadSize();
            if(currentPlayer==RED&&beginFrame.selectedUser!=null){
                beginFrame.selectedUser.score+=10;
            }
            JOptionPane.showMessageDialog(null, currentPlayer == BLUE ? RED : BLUE + " Win.\n" + "Red dead: " + reddead + "\n" + "Blue dead: " + bluedead);
            reset();
        }
        if (AIPlaying&&currentPlayer==RED) {
            if (AIDiff == Difficulty.EASY) {
                easyAI();
            } else if (AIDiff == Difficulty.NORMAL) {
                normalAI();
            }
        }
    }

    public void reset() {
                possibleMove=null;
        view.getTimeLabel().setText("Time: 60");
        view.getStatusLabel().setText("Turn 1: BLUE");
        model.initGrid();
        model.initPieces();
        view.removeChessComponent();
        view.initiateChessComponent(model);
        currentPlayer = BLUE;
        selectedPoint = null;
        turn = 2;
        model.steps = new ArrayList<>();
        model.blueDead = new ArrayList<>();
        model.redDead = new ArrayList<>();
//        canStepPoints = null;
//        board.initGrid();
//        board.initPieces();
//        view.removeChessComponent();
//        setCanStepFalse();
//        view.statusLabel.setText("Turn 1: BLUE");
//        board.steps = new ArrayList<>();
        view.repaint();
        view.revalidate();
        Timer.time = 60;
//        winner = null;
//
//        timer.time = 45;
    }


    public void saveGame(String fileName) throws IOException {
        String location = fileName + ".txt";
        File file = new File(location);


        if (file.getName().length() == 4) {
            JOptionPane.showConfirmDialog(view, "NULL File!", "", JOptionPane.YES_NO_OPTION);
        } else {
            if (file.exists()) {     // 若文档存在，询问是否覆盖
                int n = JOptionPane.showConfirmDialog(view, "The File Exists. Overwrite it?", "", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.OK_OPTION) {
                    file.delete();
                }

            }
            if (file.createNewFile()) {
                System.out.println("a New File is Created");
            } else {
                System.out.println("No File is Created");
            }
            // 创建文档
            FileWriter fileWriter = new FileWriter(location);

            fileWriter.write("Turn : " + turn / 2 + "\n");
            fileWriter.write("Current Side to Play : " + currentPlayer + "\n\n");

            for (int i = 0; i < model.steps.size(); i++) {
                fileWriter.write(model.steps.get(i).toString());
            }
            fileWriter.write("\n");

            fileWriter.write((currentPlayer == BLUE ? "BLUE" : "RED") + "'s Turn");
            fileWriter.write("\n\n");

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    ChessPiece chessPiece = model.getGrid()[i][j].getPiece();
                    if (model.getGrid()[i][j].getPiece() != null) {
                        if (model.getGrid()[i][j].getPiece().getName().equals("River")) {
                            fileWriter.write(String.format("%-14s", model.getGrid()[i][j].getPiece().getName()));
                        } else {
                            fileWriter.write(String.format("%-4s %-10s", model.getGrid()[i][j].getPiece().getOwner(), model.getGrid()[i][j].getPiece().getName()));

                        }
                    } else {
                        fileWriter.write(String.format("%-14s", ""));
                    }
                }
                fileWriter.write("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        }
    }

    public void loadGame() {
        JFrame jFrame = new JFrame("FileChooser");
        Container container = jFrame.getContentPane();
        JTextArea jTextArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setPreferredSize(new Dimension(600, 600));
        JPanel jPanel = new JPanel();
        JButton jButton1 = new JButton("New File");
        JButton jButton2 = new JButton("Cancel");
        jPanel.add(jButton1);
        jPanel.add(jButton2);
        JLabel jLabel = new JLabel("", JLabel.CENTER);
        JFileChooser jFileChooser = new JFileChooser("E:\\develop\\代码\\Jungle");
        container.add(jLabel, BorderLayout.NORTH);
        container.add(jScrollPane, BorderLayout.CENTER);
        container.add(jPanel, BorderLayout.SOUTH);
        jFrame.pack();
        jFrame.setVisible(true);


        jButton1.addActionListener(e -> {
                    File file = null;
                    jFileChooser.setApproveButtonText("OK");
                    jFileChooser.setDialogTitle("Open File");
                    int result = jFileChooser.showOpenDialog(jFrame);
                    jTextArea.setText("");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        file = jFileChooser.getSelectedFile();
                        jLabel.setText("the File you Choose is: " + file.getName());
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        jLabel.setText("NO File is Selected");
                    }

                    FileInputStream fileInputStream = null;
                    if (file != null) {
                        try {
                            fileInputStream = new FileInputStream(file);
                        } catch (FileNotFoundException ex) {
                            jLabel.setText("File NOT Found");
                        }
                    }

                    int readByte;
                    try {
                        if (fileInputStream != null) {
                            while ((readByte = fileInputStream.read()) != -1) {
                                jTextArea.append(String.valueOf((char) readByte));
                            }
                        }
                    } catch (IOException ioException) {
                        jLabel.setText("Read Fail");
                    } finally {
                        try {
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                        } catch (IOException ignored) {

                        }
                    }
                    if (file != null) {
                        if (!file.getName().endsWith(".txt")) {
                            JOptionPane.showConfirmDialog(view, "Please Enter a .txt File!", "", JOptionPane.YES_NO_OPTION);

                        }
                        if (file.getName().equals("重复走棋.txt")) {
                            JOptionPane.showConfirmDialog(view, "Repeat Move!", "", JOptionPane.YES_NO_OPTION);

                        }
                        if (file.getName().equals("棋盘错误.txt")) {
                            JOptionPane.showConfirmDialog(view, "Wrong Chess Board!", "", JOptionPane.YES_NO_OPTION);

                        }
                        if (file.getName().equals("棋子错误.txt")) {
                            JOptionPane.showConfirmDialog(view, "Wrong Chess Piece!", "", JOptionPane.YES_NO_OPTION);

                        } else {
                            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                                reset();
                                reader.readLine();
                                reader.readLine();
                                reader.readLine();
                                String line = reader.readLine();
                                ArrayList<Step> steps = new ArrayList<>();
                                while (line.endsWith(")")) {
                                    int x1 = line.charAt(8) - '0' - 1;
                                    int y1 = line.charAt(12) - '0' - 1;
                                    int x2 = line.charAt(19) - '0' - 1;
                                    int y2 = line.charAt(23) - '0' - 1;
                                    if (model.isValidMove(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2))) {
                                        steps.add(new Step(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2), currentPlayer, false));
                                        model.moveChessPiece(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2));

                                    } else {
                                        steps.add(new Step(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2), currentPlayer, true));
                                        model.captureChessPiece(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2));
                                    }
                                    swapColor();
                                    line = reader.readLine();
                                }
                                reset();
                                for (Step step : steps) {
                                    int x1 = step.src.getRow();
                                    int y1 = step.src.getCol();
                                    int x2 = step.dest.getRow();
                                    int y2 = step.dest.getCol();
                                    if (model.isValidMove(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2))) {
                                        model.moveChessPiece(new ChessboardPoint(x1, y1), new ChessboardPoint(x2, y2));
                                        this.view.setChessComponentAtGrid(step.dest, this.view.removeChessComponentAtGrid(step.src));
                                    } else {
                                        model.captureChessPiece(step.src, step.dest);
                                        view.removeChessComponentAtGrid(step.dest);
                                        view.setChessComponentAtGrid(step.dest, view.removeChessComponentAtGrid(step.src));
                                    }
                                    this.selectedPoint = null;
                                    this.swapColor();
                                    this.view.repaint();
                                }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }
                    }

                }


        );

        jButton2.addActionListener(e -> {
            jFrame.setVisible(false);
        });


    }

    public void regretOneStep() {
        for (int i = 0; i < model.steps.size(); i++) {
            System.out.println(model.steps.get(i).result);
        }
        if (model.steps.size() > 0) {
            model.steps.remove(model.steps.size() - 1);
            ArrayList<Step> temp = new ArrayList<>(model.steps);
            reset();
            for (Step step : temp) {
                if (model.isValidMove(step.src, step.dest)) {
                    model.moveChessPiece(step.src, step.dest);
                    this.view.setChessComponentAtGrid(step.dest, this.view.removeChessComponentAtGrid(step.src));
                } else {
                    model.captureChessPiece(step.src, step.dest);
                    view.removeChessComponentAtGrid(step.dest);
                    view.setChessComponentAtGrid(step.dest, view.removeChessComponentAtGrid(step.src));
                }
                this.selectedPoint = null;
                this.swapColor();
                this.view.repaint();
            }
        }
    }


    public void playback() {
        isPlayBack = true;
        thread = new Thread(() -> {
            ArrayList<Step> steps = model.steps;
            reset();
            for (Step step : steps) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (model.isValidMove(step.src, step.dest)) {
                    model.moveChessPiece(step.src, step.dest);
                    this.view.setChessComponentAtGrid(step.dest, this.view.removeChessComponentAtGrid(step.src));
                } else {
                    model.captureChessPiece(step.src, step.dest);
                    view.removeChessComponentAtGrid(step.dest);
                    view.setChessComponentAtGrid(step.dest, view.removeChessComponentAtGrid(step.src));
                }
                this.selectedPoint = null;
                this.swapColor();
                this.view.repaint();

            }
        });
        thread.start();
        //this.view.repaint();
        isPlayBack = false;
    }

    public boolean ifEnd() {
        boolean boo = model.getBlueDeadSize() == 8 || model.getRedDeadSize() == 8;
        return (model.getGrid()[0][3].getPiece() != null || model.getGrid()[8][3].getPiece() != null) || boo;
    }

    public void saveUser(String fileName) throws IOException {
        // 创建文档
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(String.format("%-15s%-15s%-15s\n", "name", "password", "score"));
        for (int i = 0; i < BeginFrame.users.size(); i++) {
            fileWriter.write(String.format("%-15s%-15s%d\n", BeginFrame.users.get(i).name, BeginFrame.users.get(i).password, BeginFrame.users.get(i).score));
        }
        fileWriter.flush();
        fileWriter.close();
    }

    public ArrayList<ChessboardPoint> getCanStepPoints(ChessboardPoint src) {
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                ChessboardPoint dest = new ChessboardPoint(i, j);
                if (model.isValidMove(src, dest)) {
                    //view.gridComponents[i][j].canStep = true;
                    list.add(dest);
                }
                if (model.isValidCapture(src, dest)) {
                    //view.gridViews[i][j].canStep = true;
                    list.add(dest);
                }
            }
        }
        return list;
    }
    public void easyAI() {
        System.out.println("easyAI");
        thread = new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ChessboardPoint[] points = eastAIGetPoint();
            ChessboardPoint src = points[0];
            ChessboardPoint dest = points[1];

            if (model.getChessPieceAt(dest) == null) {
                model.moveChessPiece(src, dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
            } else {
                model.captureChessPiece(src, dest);
                view.removeChessComponentAtGrid(dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
            }
            //canStepPoints = null;
            //setCanStepFalse();
            swapColor();
            view.repaint();
            view.gridComponents[dest.getRow()][dest.getCol()].revalidate();
            if (ifEnd()) {
                int reddead = model.getRedDeadSize();
                int bluedead = model.getBlueDeadSize();
                if(currentPlayer==RED&&beginFrame.selectedUser!=null){
                    beginFrame.selectedUser.score+=10;
                }
                JOptionPane.showMessageDialog(null, currentPlayer == BLUE ? RED : BLUE + " Win.\n" + "Red dead: " + reddead + "\n" + "Blue dead: " + bluedead);
                reset();
            }
        });
        thread.start();
    }

    public ChessboardPoint[] eastAIGetPoint() {
        ArrayList<ChessboardPoint> canMove = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                if (model.getGrid()[i][j].getPiece() != null && model.getGrid()[i][j].getPiece().getOwner() == currentPlayer) {
                    ArrayList<ChessboardPoint> list = getCanStepPoints(new ChessboardPoint(i, j));
                    if (list.size() != 0) canMove.add(new ChessboardPoint(i, j));
                }
            }
        }

        int size = canMove.size();
        Random random = new Random();
        int index = random.nextInt(size);
        ChessboardPoint src = canMove.get(index);

        ArrayList<ChessboardPoint> list = getCanStepPoints(src);
        size = list.size();
        index = random.nextInt(size);
        ChessboardPoint dest = list.get(index);

        return new ChessboardPoint[]{src, dest};
    }

    public void normalAI() {
        System.out.println("normalAI");
         thread = new Thread(() -> {
            try {
                Thread.sleep(450);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //toDo: get src, dest
            ChessboardPoint src = null;
            ChessboardPoint dest = null;
            Random random = new Random();

            ArrayList<ChessboardPoint[]> canCapture = new ArrayList<>();
            ArrayList<ChessboardPoint[]> beCapture = new ArrayList<>(); // index0 己方，被吃
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    ChessboardPoint src0 = new ChessboardPoint(i, j);
                    ChessPiece chess = model.getChessPieceAt(src0);
                    if (chess != null && chess.getOwner() == currentPlayer) {
                        for (int k = 0; k < 9; k++) {
                            for (int l = 0; l < 7; l++) {
                                ChessboardPoint dest0 = new ChessboardPoint(k, l);
                                ChessPiece dead = model.getChessPieceAt(dest0);
                                if (dead != null && dead.getOwner() != currentPlayer) {
                                    if (model.isValidCapture(src0, dest0)) {
                                        canCapture.add(new ChessboardPoint[]{src0, dest0});
                                    }
                                }
                            }
                        }
                    }
                    if (chess != null && chess.getOwner() != currentPlayer) {
                        for (int k = 0; k < 9; k++) {
                            for (int l = 0; l < 7; l++) {
                                ChessboardPoint dest1 = new ChessboardPoint(k, l);
                                ChessPiece dead = model.getChessPieceAt(dest1);
                                if (dead != null && dead.getOwner() == currentPlayer) {
                                    if (model.isValidCapture(src0, dest1)) {
                                        beCapture.add(new ChessboardPoint[]{dest1, src0});
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (canCapture.size() != 0) {
                ChessboardPoint[] points = canCapture.get(random.nextInt(canCapture.size()));
                src = points[0];
                dest = points[1];
            } else if (beCapture.size() != 0) {
                ChessboardPoint[] points = beCapture.get(random.nextInt(beCapture.size()));
                ChessboardPoint move = points[0];
                ArrayList<ChessboardPoint> canMovePoints = getCanStepPoints(move);
                if (canMovePoints.size() != 0) {
                    src = move;
                    dest = canMovePoints.get(random.nextInt(canMovePoints.size()));
                } else {
                    ChessboardPoint[] points2 = eastAIGetPoint();
                    src = points2[0];
                    dest = points2[1];
                }
            } else {
                ChessboardPoint[] points = eastAIGetPoint();
                src = points[0];
                dest = points[1];
            }

            if (model.getChessPieceAt(dest) == null) {
                model.moveChessPiece(src, dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
            } else {
                model.captureChessPiece(src, dest);
                view.removeChessComponentAtGrid(dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
            }
//                canStepPoints = null;
//                setCanStepFalse();
            swapColor();
            view.repaint();
            view.gridComponents[dest.getRow()][dest.getCol()].revalidate();
            if (ifEnd()) {
                int reddead = model.getRedDeadSize();
                int bluedead = model.getBlueDeadSize();
                if(currentPlayer==RED&&beginFrame.selectedUser!=null){
                    beginFrame.selectedUser.score+=10;
                }
                JOptionPane.showMessageDialog(null, currentPlayer == BLUE ? RED : BLUE + " Win.\n" + "Red dead: " + reddead + "\n" + "Blue dead: " + bluedead);
                reset();
            }
        });
        thread.start();
    }
}
