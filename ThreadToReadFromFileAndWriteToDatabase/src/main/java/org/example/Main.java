package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledListener = Executors.newSingleThreadScheduledExecutor();

        scheduledListener.scheduleAtFixedRate(new Listen("E:\\testingTheThreads"), 0, 5, TimeUnit.SECONDS);
    }
}


class Listen implements Runnable {

    private final ThreadPoolCont threadPoolCont = new ThreadPoolCont();
    private final String pathName;

    Listen(String path) {
        this.pathName = path;
    }

    @Override
    public void run() {
        File rootPath = new File(pathName);
        try {
            System.out.println("insideListener");
            for (File file : Objects.requireNonNull(rootPath.listFiles())) {

                if (file.getName().endsWith(".txt")) {
                    threadPoolCont.getReaderThreadPool().submit(new Reader(file));
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
}


    class ThreadPoolCont {
    private final ExecutorService readerThreadPool;

    public ThreadPoolCont() {
        readerThreadPool = Executors.newFixedThreadPool(5);
    }


    public ExecutorService getReaderThreadPool() {
        return readerThreadPool;
    }
}

    class Reader implements Runnable {
    private final File myFile;
    private static int counter = 0;


    public Reader(File myFile) {
        this.myFile = myFile;
    }

    @Override
    public void run() {
        try {
            String fileName = myFile.getName();

            String renamedFileName = fileName + "-Ismail";
// myFile.getPath()= D:/hamada/buffer.txt-Ismail
            Path filePath = Files.move(Paths.get(myFile.getPath()), Paths.get(myFile.getParent() + "/" + renamedFileName), StandardCopyOption.REPLACE_EXISTING);

            File renamedFile = new File(filePath.toString());


            try (BufferedReader reader = new BufferedReader(new FileReader(renamedFile.getAbsolutePath()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    counter++;
                    System.out.println("Line number=  " + counter + "  " + Thread.currentThread().getName() + " Read: " + line);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}