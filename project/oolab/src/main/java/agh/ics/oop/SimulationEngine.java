package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync(){
        for(Simulation simulation : simulations){
            System.out.println("Starting simulation");
            simulation.run();
            System.out.println("Simulation completed");
        }
    }

    public void runAsync(){
        for(Simulation simulation : simulations){
            System.out.println("Starting simulation");
            Thread thread = new Thread(simulation);
            threads.add(thread);
            thread.start();
        }
        awaitSimulationsEnd();
    }
    public void runAsyncInThreadPool(){
        for(Simulation simulation : simulations){
            threadPool.submit(simulation);
        }
        awaitSimulationsEnd();
    }
    public void awaitSimulationsEnd(){
        try{
            for(Thread thread : threads){
                thread.join();
            }
            threadPool.shutdown();
            if(!threadPool.awaitTermination(10, TimeUnit.SECONDS)){
                threadPool.shutdownNow();
            }
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

}