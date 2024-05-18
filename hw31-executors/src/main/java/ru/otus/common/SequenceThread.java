package ru.otus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.enums.NameThread;

import java.util.Queue;

public class SequenceThread extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(SequenceThread.class);
    private final NameThread nameThread;
    private final Monitor monitor;
    private final Queue<Integer> dataQueue;

    public SequenceThread(NameThread nameThread, Monitor monitor, Queue<Integer> dataQueue) {
        this.nameThread = nameThread;
        this.monitor = monitor;
        this.dataQueue = dataQueue;
    }

    @Override
    public void run(){
        while (!this.isInterrupted()) {
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
            var dataQueueElement = dataQueueElement(monitor);
            logger.atInfo().setMessage(monitor.getNameThread().getName() + " : " + dataQueueElement).log();

            if(nameThread.equals(NameThread.TWO)){
                System.out.println("\n");
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

    private Integer dataQueueElement(Monitor monitor){
        if (monitor.getNameThread().equals(NameThread.ONE)){
            return dataQueue.peek();
        }
        var element = dataQueue.poll();
        dataQueue.add(element);
        return element;
    }
}
