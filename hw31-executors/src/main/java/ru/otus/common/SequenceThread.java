package ru.otus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.enums.NameThread;

public class SequenceThread extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(SequenceThread.class);
    private static final Integer MAX_SEQUENCE_VALUE = 20;
    private final NameThread nameThread;
    private final Monitor monitor;

    public SequenceThread(NameThread nameThread, Monitor monitor) {
        this.nameThread = nameThread;
        this.monitor = monitor;
    }

    @Override
    public void run(){
        while (!this.isInterrupted() && canNextRun()) {
            try{
                synchronizedRun(monitor);
            } catch (Exception e) {
                logger.error(e.getMessage());
                this.interrupt();
            }
        }
    }

    private void synchronizedRun(Monitor monitor) throws InterruptedException {
        synchronized (monitor) {
            while (!nameThread.equals(monitor.getNameThread())) {
                monitor.wait();
            }


            logger.atInfo().setMessage(monitor.getNameThread().getName() + " : " + sequence()).log();

            if(nameThread.equals(NameThread.TWO)){
                System.out.println("\n");
                monitor.increaseCounter();
                sleep(300);
            }
            var switchedNameThread = switchNameThread(nameThread);
            monitor.setNameThread(switchedNameThread);
            monitor.notify();
        }
    }

    private NameThread switchNameThread(NameThread nameThread){
        if(nameThread.equals(NameThread.ONE)){
            return NameThread.TWO;
        }
        return NameThread.ONE;
    }

    private boolean canNextRun(){
        if (nameThread.equals(NameThread.ONE)){
            return monitor.getCounter() + 1 < MAX_SEQUENCE_VALUE;
        }
        return (monitor.getCounter() ) < MAX_SEQUENCE_VALUE;
    }

    private int sequence(){
        if (monitor.getCounter() > 10){
            return MAX_SEQUENCE_VALUE - monitor.getCounter();
        }
        return monitor.getCounter();
    }
}
