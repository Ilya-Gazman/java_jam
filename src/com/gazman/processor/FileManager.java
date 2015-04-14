package com.gazman.processor;

import com.google.gson.Gson;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ilyagazman on 3/10/15.
 */
public class FileManager implements OutputCallback {

    private File projectRoot;
    private File inputRoot;
    private File settingsRoot;
    private File outputRoot;
    private String tag;
    private FileWriter fileWriter;
    private FileReader fileReader;
    private boolean testMode;
    private File generateRoot;
    private FileWriter generateWriter;
    private boolean generateMode;

    public void load(String tag) {
        this.tag = tag;
        initRoots();
        if(generateMode){
            initGeneratedRoot();
        }
        else{
            initInput();
        }
        initOutput();
        buildSource();
    }

    private void initInput() {
        File file = new File(inputRoot, this.tag + ".txt");
        ArrayList<String> settings = loadSettings();
        if(!file.exists()){
            File[] files = inputRoot.listFiles();
            File newFile = null;
            for (File inputFile : files) {
                if(!settings.contains(inputFile)){
                    newFile = inputFile;
                    break;
                }
            }
            if(newFile != null){
                newFile.renameTo(file);
                settings.add(file.getName());
                saveSettings(settings);
            }
        }
        else if(!settings.contains(file.getName())){
            settings.add(file.getName());
            saveSettings(settings);
        }

        if(!file.exists()){
            if(testMode){
                System.err.println("Test input not found at /test_input");
                return;
            }
            throw new RuntimeException("Input not found");
        }

        try {
            if(fileReader != null){
                fileReader.close();
            }
            fileReader = new FileReader(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileReader getReader() {
        return fileReader;
    }

    public void dispose()  {
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildSource() {
        File file = new File(outputRoot, "tmp");
        file.mkdir();
        File sourceFolder = new File(projectRoot, "src");
        File[] files = sourceFolder.listFiles();
        for (File sourceFile : files) {
            if(sourceFile.isDirectory() || sourceFile.getName().equals(tag + ".java")){
                try {
                    String destination = sourceFile.getAbsolutePath().replace(sourceFolder.getAbsolutePath(), "");
                    File output = new File(file, destination);

                    FileUtils.copy(sourceFile, output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ZipUtil.pack(file, new File(outputRoot, "source.zip"));
        FileUtils.deleteDirectory(file);
    }

    private void initOutput() {
        FileUtils.deleteDirectory(outputRoot);
        outputRoot.mkdir();
        File file = new File(outputRoot, "output.txt");
        try {
            fileWriter = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGeneratedRoot() {
        FileUtils.deleteDirectory(generateRoot);
        generateRoot.mkdir();
        File file = new File(generateRoot, this.tag + ".txt");
        try {
            generateWriter = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRoots() {
        projectRoot = new File(System.getProperty("user.dir"));
        inputRoot = new File(projectRoot , testMode ? "test_input" : "input");
        settingsRoot = new File(projectRoot , "settings");
        outputRoot = new File(projectRoot , "output");
        generateRoot = new File(projectRoot , "test_output");

        inputRoot.mkdir();
        settingsRoot.mkdir();
        outputRoot.mkdir();
        generateRoot.mkdir();
    }

    private ArrayList<String> loadSettings() {
        File file = new File(settingsRoot, "files.json");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);

            String[] strings = new Gson().fromJson(fileReader, String[].class);
            ArrayList<String> list = new ArrayList<>();
            Collections.addAll(list, strings);
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void saveSettings(ArrayList<String> files){
        String json = new Gson().toJson(files);
        File file = new File(settingsRoot, "files.json");
        try {
            Files.write(file.toPath(), json.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOutput(String message) {
        try {
            fileWriter.append(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void generateOutput(String message) {
        try {
            generateWriter.append(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGenerateMode(boolean generateMode) {
        this.generateMode = generateMode;
    }

    public void switchToGenerateRoot() {
        try {
            if(generateWriter != null){
                generateWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputRoot = generateRoot;
        initInput();
    }

    public int read() {
        try {
            return fileReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new Error("Fail reading");
    }
}
