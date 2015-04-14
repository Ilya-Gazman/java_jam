package com.gazman.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ilyagazman on 3/11/15.
 */
public class Processor {

    public int numberOfTestCasesToGenerate = 100;
    private int nextTask = 1;
    private ArrayList<Task> tasks = new ArrayList<>();
    protected int numberOfCases;
    protected final Parser parser = new Parser();
    private FileManager fileManager;

    private static final ExecutorService executorService;
    private static final ExecutorService outputService = Executors.newFixedThreadPool(1);
    static {
        int cores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(cores);
    }

    private Class<? extends ProcessTask> task;
    private long startingTime = System.currentTimeMillis();
    private boolean testMode;
    private boolean disposed;
    private boolean genertateMode;
    private int generatedCaseNumber;

    public void init(Class<? extends ProcessTask> task) {
        this.task = task;
        fileManager = new FileManager();
        fileManager.setTestMode(testMode);
        fileManager.setGenerateMode(genertateMode && generatedCaseNumber == 0);
        fileManager.load(task.getSimpleName());
        parser.init(fileManager);
    }

    public void process() {
        if(genertateMode){
            generate();
        }
        numberOfCases = parser.readInt() + 1;
        for (int i = 1; i < numberOfCases && !disposed; i++) {
            final int caseNumber = i;
            ProcessTask processTask = createTask();
            processTask.onParse(parser);
            final ProcessTask finalProcessTask = processTask;
            if(!genertateMode || generatedCaseNumber == 0 || generatedCaseNumber == caseNumber){
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        execute(caseNumber, finalProcessTask);
                    }
                });
                if(genertateMode && caseNumber != 0 && generatedCaseNumber == caseNumber){
                    break;
                }
            }
        }
    }

    private void generate() {
        if(generatedCaseNumber == 0){
            ProcessTask processTask = createTask();
            fileManager.generateOutput(numberOfTestCasesToGenerate + "\n");
            for (int i = 0; i < numberOfTestCasesToGenerate; i++) {
                String generate = processTask.generate();
                if(generate == null){
                    throw new Error("Nothing is been generated");
                }
                fileManager.generateOutput(generate);
                fileManager.generateOutput("\n");
            }
        }
        fileManager.switchToGenerateRoot();
    }

    private ProcessTask createTask() {
        ProcessTask processTask;
        try {
            processTask = task.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Something went wrong");
        }
        return processTask;
    }


    private void execute(int caseNumber, ProcessTask processTask) {
        final StringBuilder stringBuilder = new StringBuilder();
        OutputManager outputManager = new OutputManager(new OutputCallback() {
            @Override
            public void onOutput(String message) {
                stringBuilder.append(message);
            }
        });
        outputManager.print(getPrefix(caseNumber));
        Assert anAssert = new Assert();
        anAssert.currentCase = caseNumber;
        processTask.onProcess(caseNumber, outputManager, anAssert);
        outputManager.print("\n");
        final Task task = new Task();
        task.caseNumber = caseNumber;
        task.solution = stringBuilder.toString();
        if(testMode){
            stringBuilder.setLength(0);
            outputManager.print(getPrefix(caseNumber));
            processTask.onTestProcess(caseNumber, outputManager, anAssert);
            outputManager.print("\n");
            if(stringBuilder.length() > 0 && !task.solution.equals(stringBuilder.toString())){
                System.err.println("*******************************");
                System.err.println("*******************************");
                System.err.println("**************** Failed on case " + caseNumber);
                System.err.println("*******************************");
                System.err.println("*******************************");
                System.err.println();
            }
        }
        outputService.execute(new Runnable() {
            @Override
            public void run() {
                printTask(task);
            }
        });
    }

    protected String getPrefix(int currentCase) {
        return "Case #" + currentCase + ": ";
    }

    private void printTask(Task newTask) {
        if(genertateMode && generatedCaseNumber != 0){
            fileManager.onOutput(newTask.solution);
            System.out.print(newTask.solution);
            dispose();
            return;
        }
        tasks.add(newTask);
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.caseNumber, o2.caseNumber);
            }
        });
        if(newTask.caseNumber == nextTask){
            for (Task task : tasks) {
                if(task.caseNumber == nextTask){
                    nextTask++;
                    fileManager.onOutput(task.solution);
                    System.out.print(task.solution);
                }
            }
        }
        if(nextTask == numberOfCases){
            dispose();
        }
    }

    private void dispose() {
        fileManager.dispose();
        executorService.shutdown();
        outputService.shutdown();
        disposed= true;
        long timePath = System.currentTimeMillis() - startingTime;
        System.out.println("Completed in " + timePath);
    }

    public void setTestMode() {
        testMode = true;
    }

    public void setGenerateMode(int caseNumber) {
        this.generatedCaseNumber = caseNumber;
        genertateMode = true;
    }

    private class Task{
        int caseNumber;
        String solution;
    }
}
