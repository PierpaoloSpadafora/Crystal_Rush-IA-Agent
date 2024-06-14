package it.unical.utility;

import it.unical.model.World;

public class Timer extends Thread{

    private World world;

    public Timer(){
        this.world = World.getInstance();
    }

    @Override
    public void run(){
        while(!world.isFinished()){
            try {
                this.world.setTime(System.currentTimeMillis());
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
