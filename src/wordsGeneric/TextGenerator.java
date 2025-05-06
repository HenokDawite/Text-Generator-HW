/**
 * Class to artificially generate sentences
 * 
 * 
 * @author Henok Dawite
 **/
package wordsGeneric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TextGenerator {
    protected HashMap<StringPair, FreqList> letPairList;
    protected Random random;
    
    
    /**
     * creates a new text generator with empty frequency tables
     */
    public TextGenerator() {
        letPairList = new HashMap<>();
        random = new Random();
    }
    
    /** 
     * Add a reference to <first,second>->third to our letPairList
     * @param first string in triad
     * @param second string in triad
     */
    public void enter(String first, String second, String third) {
        StringPair pair = new StringPair(first, second);
        FreqList freqList = letPairList.getOrDefault(pair, new FreqList());
        freqList.add(third);
        letPairList.put(pair, freqList);
    }

    /**
     * Use the <first,second> FreqList to choose a word to follow them
     * @param first String in triad
     * @param second String in triad
     * @return likely third String to follow the first two
	 *
	 * Note: it would also be very good to do something graceful
	 *       if nothing has followed the <first,second> pair.
     */
    public String getNextWord(String first, String second) {
        StringPair pair = new StringPair(first, second);
        FreqList freqList = letPairList.get(pair);
        if (freqList == null) {
            return ""; // Returns empty string if no words follow this pair
        }
        return freqList.get(random.nextDouble());
    }

    public static void main(String args[]) {

        WordStream ws = new WordStream();
        JFileChooser dialog = new JFileChooser(".");

        // Display the dialog box and make sure the user did not cancel.
        if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            // Find out which file the user selected.
            File file = dialog.getSelectedFile();
            try {
                // Open the file.
                BufferedReader input = new BufferedReader(new FileReader(file));

                // Fill up the editing area with the contents of the file being
                // read.
                String line = input.readLine();
                while (line != null) {
                    ws.addLexItems(line.toLowerCase());
                    System.out.println(line);
                    line = input.readLine();
                }
                System.out.println("Finished reading data");
                // Close the file
                input.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Can't load file "
                        + e.getMessage());
            }

            // Create extGenerator, populate it from the WordStream
            TextGenerator textGenerator = new TextGenerator();
            
            // Reset WordStream to start from beginning
            ws = new WordStream();
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                String line = input.readLine();
                while (line != null) {
                    ws.addLexItems(line.toLowerCase());
                    line = input.readLine();
                }
                input.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
                return;
            }
            
            // Get first two words for initial pair
            String first = ws.nextToken();
            String second = ws.nextToken();
            
            // Build frequency table
            while (ws.hasMoreTokens()) {
                String third = ws.nextToken();
                textGenerator.enter(first, second, third);
                first = second;
                second = third;
            }

            // Print resulting <StringPair,FreqList> map
            System.out.println("\nFrequency Map:");
            for (Map.Entry<StringPair, FreqList> entry : textGenerator.letPairList.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }

            // Test getNextWord with pairs from MLK's speech
            System.out.println("\nTesting getNextWord:");
            System.out.println("Next word after 'i have': " + textGenerator.getNextWord("i", "have"));
            System.out.println("Next word after 'let freedom': " + textGenerator.getNextWord("let", "freedom"));

            // Reset WordStream again for text generation
            ws = new WordStream();
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                String line = input.readLine();
                while (line != null) {
                    ws.addLexItems(line.toLowerCase());
                    line = input.readLine();
                }
                input.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
                return;
            }
            
            // Pick two starting words
            first = ws.nextToken();
            second = ws.nextToken();
            
            // Generate 400 words of text, choosing likely random words to follow each preceding pair
            System.out.println("\nGenerated text:");
            System.out.println("----------------------------------------");
            StringBuilder generatedText = new StringBuilder();
            int wordCount = 0;
            
            while (wordCount < 400) {
                String nextWord = textGenerator.getNextWord(first, second);
                if (nextWord.isEmpty()) {
                    // If no word follows, start over with new random words
                    if (ws.hasMoreTokens()) {
                        first = ws.nextToken();
                        second = ws.nextToken();
                    } else {
                        // If we've reached the end of the input, start over from beginning
                        ws = new WordStream();
                        try {
                            BufferedReader input = new BufferedReader(new FileReader(file));
                            String line = input.readLine();
                            while (line != null) {
                                ws.addLexItems(line.toLowerCase());
                                line = input.readLine();
                            }
                            input.close();
                        } catch (IOException e) {
                            System.err.println("Error reading file: " + e.getMessage());
                            return;
                        }
                        first = ws.nextToken();
                        second = ws.nextToken();
                    }
                    continue;
                }
                
                generatedText.append(nextWord);
                wordCount++;
                
                // Add space between words (except for the last word)
                if (wordCount < 400) {
                    generatedText.append(" ");
                }
                
                // Add newline after every 20 words
                if (wordCount % 20 == 0) {
                    generatedText.append("\n");
                }
                
                first = second;
                second = nextWord;
            }
            System.out.println(generatedText.toString());
            System.out.println("----------------------------------------");
            System.out.println("\nTotal words generated: " + wordCount);
        } else {
            System.out.println("User cancelled file chooser");
        }
    }
}
