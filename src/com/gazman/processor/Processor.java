package com.gazman.processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ilya Gazman on 3/11/15.
 */
@SuppressWarnings("unused")
public class Processor {

    public int numberOfTestCasesToGenerate = 100;
    private int nextTask = 1;
    private ArrayList<Task> tasks = new ArrayList<>();
    protected int numberOfCases;
    protected final Parser parser = new Parser();
    private FileManager fileManager;

    private ExecutorService executorService;
    private ExecutorService outputService = Executors.newFixedThreadPool(1);

    private Class<? extends ProcessTask> task;
    private long startingTime = System.currentTimeMillis();
    private boolean testMode;
    private boolean disposed;
    private boolean generateMode;
    private int generatedCaseNumber;
    private int executesCount = Runtime.getRuntime().availableProcessors() * 2;

    public Processor(Class<? extends  ProcessTask> task){
        this.task = task;
        init();
    }

    public void setExecutesCount(int executesCount) {
        this.executesCount = executesCount;
    }

    private void init() {
        executorService = Executors.newFixedThreadPool(executesCount);
        fileManager = new FileManager();
        fileManager.setTestMode(testMode);
        fileManager.setGenerateMode(generateMode && generatedCaseNumber == 0);
        fileManager.load(task.getSimpleName());
        parser.init(fileManager);

        onInit();
    }

    protected void onInit() {

    }

    public void process() {
        if (generateMode) {
            generate();
        }
        readNumberOfCases();
        for (int i = 1; i < numberOfCases && !disposed; i++) {
            final int caseNumber = i;
            ProcessTask processTask = createTask();
            processTask.onCaseParse(parser);
            final ProcessTask finalProcessTask = processTask;
            if (!generateMode || generatedCaseNumber == 0 || generatedCaseNumber == caseNumber) {
                executorService.execute(() -> execute(caseNumber, finalProcessTask));
                if (generateMode && caseNumber != 0 && generatedCaseNumber == caseNumber) {
                    break;
                }
            }
        }
    }

    protected void readNumberOfCases() {
        if(numberOfCases > 0){
            return;
        }
        numberOfCases = parser.readInt() + 1;
    }

    private void generate() {
        if (generatedCaseNumber == 0) {
            ProcessTask processTask = createTask();
            fileManager.generateOutput(numberOfTestCasesToGenerate + "\n");
            for (int i = 0; i < numberOfTestCasesToGenerate; i++) {
                String generate = processTask.generate();
                if (generate == null) {
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
        OutputManager outputManager = new OutputManager(stringBuilder::append);
        outputManager.print(getPrefix(caseNumber));
        Assert anAssert = new Assert();
        anAssert.currentCase = caseNumber;
        processTask.onCaseProcess(caseNumber, outputManager, anAssert);
        outputManager.print("\n");
        final Task task = new Task();
        task.caseNumber = caseNumber;
        task.solution = stringBuilder.toString();
        if (testMode) {
            stringBuilder.setLength(0);
            outputManager.print(getPrefix(caseNumber));
            processTask.onTestProcess(caseNumber, outputManager, anAssert);
            outputManager.print("\n");
            if (stringBuilder.length() > 0 && !task.solution.equals(stringBuilder.toString())) {
                System.err.println("*******************************");
                System.err.println("*******************************");
                System.err.println("**************** Failed on case " + caseNumber);
                System.err.println("*******************************");
                System.err.println("*******************************");
                System.err.println();
            }
        }
        outputService.execute(() -> printTask(task));
    }

    protected String getPrefix(int currentCase) {
        return "Case #" + currentCase + ": ";
    }

    private void printTask(Task newTask) {
        if (generateMode && generatedCaseNumber != 0) {
            fileManager.onOutput(newTask.solution);
            System.out.print(newTask.solution);
            dispose();
            return;
        }
        tasks.add(newTask);
        tasks.sort(Comparator.comparingInt(o -> o.caseNumber));
        if (newTask.caseNumber == nextTask) {
            for (Task task : tasks) {
                if (task.caseNumber == nextTask) {
                    nextTask++;
                    fileManager.onOutput(task.solution);
                    System.out.print(task.solution);
                }
            }
        }
        if (nextTask == numberOfCases) {
            dispose();
        }
    }

    private void dispose() {
        fileManager.dispose();
        executorService.shutdown();
        outputService.shutdown();
        disposed = true;
        long timePath = System.currentTimeMillis() - startingTime;
        System.out.println("Completed in " + timePath);
    }

    public void setTestMode() {
        testMode = true;
    }

    public void setGenerateMode(int caseNumber) {
        this.generatedCaseNumber = caseNumber;
        generateMode = true;
    }

    private class Task {
        int caseNumber;
        String solution;
    }
}
