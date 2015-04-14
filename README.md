Project Structure
-----------------

 - *input/* the input files, those will be renamed to java main class file
 - *output/source.zip* your source code excluding other main files
 - *output/output.txt* the solution
 - *settings/* directory created by the library
 - *src/* Your core files
 - *src/com.gazman/* is where the library lives

Usage
-----
 
 1. Drop an input file, name does not matter, to input folder
 2. Create new main file(with the method main()) in the default package, use Processor template for that.
   
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
