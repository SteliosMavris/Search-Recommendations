import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomWordGenerator {

    // Distribution of word lengths (observed frequencies in percentages)
    private static final double[] wordLengthDistribution = {
        3.160, 16.975, 21.192, 15.678, 10.852, 8.524, 7.724, 5.623, 4.032, 
        2.766, 1.582, 0.917, 0.483, 0.262, 0.099, 0.050, 0.027, 0.022, 
        0.011, 0.006, 0.005, 0.002, 0.001, 0.001, 0.001, 0.001, 0.001
    };

    private static int pickWordLength() {
        // Normalize the distribution to cumulative probabilities
        double[] cumulativeDistribution = new double[wordLengthDistribution.length];
        cumulativeDistribution[0] = wordLengthDistribution[0];
        for (int i = 1; i < wordLengthDistribution.length; i++) {
            cumulativeDistribution[i] = cumulativeDistribution[i - 1] + wordLengthDistribution[i];
        }

        // Generate a random number and pick the corresponding length
        Random random = new Random();
        double rand = random.nextDouble() * cumulativeDistribution[cumulativeDistribution.length - 1];
        for (int i = 0; i < cumulativeDistribution.length; i++) {
            if (rand <= cumulativeDistribution[i]) {
                return i + 1; // Length is index + 1
            }
        }

        return 1; // Fallback, should not happen
    }

    private static String generateWord(int length) {
        Random random = new Random();
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('a' + random.nextInt(26)); // Random lowercase letter
            word.append(randomChar);
        }
        return word.toString();
    }
    
 // New method to generate words of a specific length
    public static void generateFixedLengthWordsToFile(int m, int length, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < m; i++) {
                String word = generateWord(length);
                writer.write(word);
                writer.newLine();
            }
            System.out.println("Fixed-length words written to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void generateWordsToFile(int m, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < m; i++) {
                int wordLength = pickWordLength();
                String word = generateWord(wordLength);
                writer.write(word);
                writer.newLine();
            }
            System.out.println("Words written to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int m = 700000; // Number of words to generate
        //String filePath = "main-distributed-dictionary.txt";

        // Generate words and write to file
        //generateWordsToFile(m, filePath);
        
        // Generate words with fixed length and write to file
        int fixedLength = 20; // Specify desired word length
        String fixedFilePath = "fixed-length-20-dictionary.txt";
        generateFixedLengthWordsToFile(m, fixedLength, fixedFilePath);
    }
}
