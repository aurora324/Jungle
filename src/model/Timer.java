package model;

import controller.GameController;

public class Timer extends Thread {
    public static int time = 45;
    public GameController gameController;

    @Override
    public void run(){
        synchronized (this){
            while (true){
                PlayerColor player = gameController.getCurrentPlayer();
                boolean b = true;
                while(time > 0) {
                    time--;
                    try {
                        if(gameController.isPlayBack){
                            Thread.sleep(gameController.model.steps.size()* 500L);
                        }
                        Thread.sleep(1000);
                        gameController.view.timeLabel.setText("Time: " + time);
//                        if (gameController.getCurrentPlayer() != player){
//                            gameController.swapColor();
//                            b = false;
//                            break;
//                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time = 60;
            }
        }
    }

    public Timer(GameController gameController){
        this.gameController = gameController;
    }

}
