import com.gazman.processor.*;

/**
 * Created by Ilya Gazman on 3/7/2017.
 * Example from: https://code.google.com/codejam/contest/90101/dashboard#s=p0
 */
public class AlienLanguageExample extends ProcessTask {

    private static String[] words;
    private String pattern;

    public static void main(String[] args){
        Processor processor = new Processor(AlienLanguageExample.class){
            @Override
            public void onInit() {
                parser.readInt();
                int numberOfWords = parser.readInt();
                readNumberOfCases();
                words = parser.readStingArray(numberOfWords);
            }
        };

        processor.process();
    }

    @Override
    protected void onCaseParse(Parser parser) {
        String s = parser.readString();
        pattern = s.replaceAll("\\(", "[").replaceAll("\\)", "]");
    }

    @Override
    protected void onCaseProcess(int caseNumber, OutputManager out, Assert tester) {
        int count = 0;
        for (String word : words) {
            if(word.matches(pattern)){
                count++;
            }
        }
        out.print(count);
    }
}
