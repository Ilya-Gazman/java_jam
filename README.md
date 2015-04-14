Project Structure
-----------------

input/ the input files, those will be renamed to java main class file
output/source.zip your source code excluding other main files
output/output.txt the solution
settings/ directory created by the library
src/ Your core files
src/com.gazman/ is where the library lives

Usage
-----
 
 1. Drop an input file, name does not matter, to input folder
 2. Create new main file(with the method main()) in the default package, use Processor template for that.
   
```Java
   #parse("File Header.java")
   import com.gazman.processor.Parser;
   import com.gazman.processor.Processor;
   
   /**
    * Created by ilya gazman on 3/14/15.
    */
   public class ${NAME} extends Processor{
   
       public static void main(String arg[]){
           Parser parser = new Parser();
           parser.load(new ${NAME}());
       }
       
       @Override
       public void onProcess(int currentCase) {
           
       }
   }
```   