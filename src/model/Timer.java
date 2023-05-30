package model;

import controller.GameController;

import javax.swing.*;

import static model.PlayerColor.BLUE;
import static model.PlayerColor.RED;

public class Timer extends Thread {
    public static int time = 45;
    public GameController gameController;

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                PlayerColor player = gameController.getCurrentPlayer();
                boolean b = true;
                while (time > 0) {
                    time--;
                    try {
                        if (gameController.isPlayBack) {
                            Thread.sleep(gameController.model.steps.size() * 500L);
                        }
                        Thread.sleep(1000);
                        gameController.view.timeLabel.setText("Time: " + time);
                        if (gameController.getCurrentPlayer() != player) {
                            //gameController.swapColor();
                            b = false;
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time = 60;


                if (b) {
                    ChessboardPoint[] points = gameController.eastAIGetPoint();
                    ChessboardPoint src = points[0];
                    ChessboardPoint dest = points[1];

                    if (gameController.model.getChessPieceAt(dest) == null) {
                        gameController.model.moveChessPiece(src, dest);
                        gameController.view.setChessComponentAtGrid(dest, gameController.view.removeChessComponentAtGrid(src));
                    } else {
                        gameController.model.captureChessPiece(src, dest);
                        gameController.view.removeChessComponentAtGrid(dest);
                        gameController.view.setChessComponentAtGrid(dest, gameController.view.removeChessComponentAtGrid(src));
                    }
                    //gameController.getSelectedPoint() = null;
                    gameController.setCanStepFalse();
                    gameController.swapColor();
                    gameController.view.repaint();
                    gameController.view.gridComponents[dest.getRow()][dest.getCol()].revalidate();

                    if (gameController.ifEnd()) {
                        int reddead = gameController.model.getRedDeadSize();
                        int bluedead = gameController.model.getBlueDeadSize();
                        if (gameController.getCurrentPlayer() == RED && gameController.beginFrame.selectedUser != null) {
                            gameController.beginFrame.selectedUser.score += 10;
                        }
                        JOptionPane.showMessageDialog(null, gameController.getCurrentPlayer() == BLUE ? RED : BLUE + " Win.\n" + "Red dead: " + reddead + "\n" + "Blue dead: " + bluedead);
                        gameController.reset();
                    }
                } 
            }
        }

    }

    public Timer(GameController gameController) {
        this.gameController = gameController;
    }
}
