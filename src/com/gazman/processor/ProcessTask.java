package com.gazman.processor;

/**
 * Created by ilyagazman on 4/12/15.
 */
public abstract class ProcessTask {

    protected abstract void onParse(Parser parser);

    protected abstract void onProcess(int caseNumber, OutputManager out, Assert tester);


    protected void onTestProcess(int caseNumber, OutputManager out, Assert tester) {}

    public String generate() {
        return null;
    }
}
