Futures
-------
 - Generate source code and output file.
 - providing parsing library
 - Allows you to compare brut with your actual algorithm
 - Allows you to generate input
 - Working in multithread


Project Structure
-----------------

 - **input/** The input files, those will be renamed to java main class file
 - **output/source.zip** Your source code excluding other main files
 - **output/output.txt** The solution
 - **settings/** Directory created by the library
 - **src/** Your core files
 - **src/com.gazman/** Is where the library lives

Usage
-----
 
 1. Drop an input file, name does not matter, to input folder
 2. Create new main file(with the method main()) in the **default package**, use Processor template for that.
   
```Java
   import com.gazman.processor.*;

/**
 * Created by ${USER} on ${DATE}.
 * Dependencies
 * File processing
 * - com.google.code.gson:gson:1.2.2
 * - org.zeroturnaround:zt-zip:1.8
 * Java version 1.7.0_60
 */
public class ${NAME} extends ProcessTask{

    public static void main(String arg[]) {
        Processor processor = new Processor();
//        processor.setTestMode();
//        processor.setGenerateMode(0);
        processor.init(${NAME}.class);
        processor.process();
    }

    @Override
    protected void onParse(Parser parser) {
        
    }

    @Override
    protected void onProcess(int caseNumber, OutputManager out, Assert tester) {

    }
}
```   

Testing
-------
 - **setTestMode()** when uncommenting this line, the input will be taken from testInput/ drirectory
 - Overriding the method **onTestProcess()** - It has the same signature as onProcess, you should use it to implement brute solution for small input, when you run, the result from onProcess will be compared to onTestProcess, and if those do not much you will be notified.
 - **setGenerateMode()** - It allows you to generate the input by yourself and test with your brute via onTestProcess. To use it you need to do three things:
   - uncomment setGenerateMode(0)
   - override the methode generate(), this is where you actually generating the input, it will be called 100 times, you only need to generate 1 test case and use some random.
   - implemt onTestProcess to compare it with your brute
 Once you find an error, it will tell you the case number. Pass this number to setGenerateMode, and it will use your last generated input with that test case.


