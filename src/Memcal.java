import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;

public class Memcal {
    public static long fillTrieRh(String filename) {

        RobinHoodTrie trie = new RobinHoodTrie();

        try (Scanner dictionary = new Scanner(new File(filename))) {
            while (dictionary.hasNextLine()) {
                String wordInput = dictionary.nextLine().toLowerCase().trim();
                if (!wordInput.isEmpty() && wordInput.matches("^[a-z]+$")) {
                    trie.insertWord(wordInput);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: the file given as dictionary doesn't exist!");
            System.exit(0);
        }
        return MemoryCalculator.calcRobinHoodMemory(trie);
    }

    public static long fillTrie(String filename) {

        Trie trie = new Trie();

        try (Scanner dictionary = new Scanner(new File(filename))) {
            while (dictionary.hasNextLine()) {
                String wordInput = dictionary.nextLine().toLowerCase().trim();
                if (!wordInput.isEmpty() && wordInput.matches("^[a-z]+$")) {
                    trie.insert(wordInput);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: the file given as dictionary doesn't exist!");
            System.exit(0);
        }
        return MemoryCalculator.calcStaticMemory(trie);
    }

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        int counter = 1;
        System.out.println("Enter num of words: \n");
        int words = Integer.valueOf(myObj.nextLine());
        System.out.println("Enter increment size: \n");
        int step = Integer.valueOf(myObj.nextLine());
        System.out.println("Enter word limit: \n");
        int until = Integer.valueOf(myObj.nextLine());
        System.out.println("Lognormal dist? (y/n): \n");
        char yn = myObj.nextLine().charAt(0);
        boolean log = true;
        int staticWordLength = 10;
        if (yn == 'y') {
            log = true;
        } else if (yn == 'n') {
            log = false;
            System.out.println("Enter static word length: \n");
            staticWordLength = Integer.valueOf(myObj.nextLine());
        } else {
            System.out.println("Invalid will continue with log\n");
        }
        System.out.println("RH trie? (y/n): \n");
        yn = myObj.nextLine().charAt(0);
        boolean rh = true;
        if (yn == 'y') {
            rh = true;
        } else if (yn == 'n') {
            rh = false;
        } else {
            System.out.println("Invalid will continue with RH\n");
        }
        String dic = "words";
        myObj.close();
        String out = "";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("y.txt"))) {
            try (BufferedWriter sriter = new BufferedWriter(new FileWriter("x.txt"))) {
                while (words <= until) {
                    if (log) {
                        RandomWordGenerator.generateWordsToFile(words, dic);
                    } else {
                        RandomWordGenerator.generateFixedLengthWordsToFile(words, staticWordLength, dic);
                    }
                    if (rh) {
                        out = "" + fillTrieRh(dic);
                    } else {
                        out = "" + fillTrie(dic);
                    }
                    writer.write(out);
                    writer.newLine();
                    sriter.write(Integer.toString(words));
                    sriter.newLine();
                    System.out.println(counter);
                    counter++;
                    words += step;
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to: " + e.getMessage());
        }
    }
}