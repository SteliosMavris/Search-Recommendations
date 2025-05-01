
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RUN {
	public static void main(String[] args) {
		// Trie statTrie = new Trie(); //initialises normal trie for measurements
		RobinHoodTrie trie = new RobinHoodTrie();// initialises a rh trie
		MinHeap minHeap = null;// initialises a minheap
		Scanner dictionary = null;// scanner for the dictionary
		Scanner importance = null;// scanner for the text
		Scanner scan = new Scanner(System.in);// initiate scanner
		String wordInput;// the word being read
		String searchWord;// the word being searched for

		try {// try to read the dictionary file
			dictionary = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			System.out.println("Error: the file given as dictionary doesn't exist!");
			System.exit(0);
		}

		try {// try to read the text file
			importance = new Scanner(new File(args[1]));
		} catch (FileNotFoundException e) {
			System.out.println("Error: the file given as sample text doesn't exist!");
			System.exit(0);
		}

		while (dictionary.hasNextLine()) {// while the dictionary has more words
			wordInput = dictionary.nextLine().toLowerCase().trim();// read next line, make it lowercase, remove
																	// whitespaces
			if (wordInput.isEmpty() || !wordInput.matches("^[a-z]+$")) {
				continue;// if the word read is not null or does not contain special characters
			}
			System.out.println("Inserting: " + wordInput);
			trie.insertWord(wordInput);// insert the word in the trie
		}

		while (importance.hasNext()) {// while the text has more words
			wordInput = importance.next().toLowerCase().trim();// read next word, make it lowercase, remove whitespaces
			wordInput = wordInput.replaceAll("^[^a-z]+|[^a-z]+$", "");
			if (wordInput.isEmpty() || !wordInput.matches("^[a-z]+$")) {
				continue;// if the word read is not null or does not contain special characters
			}
			System.out.println("Searching for: " + wordInput);
			if (trie.searchWord(wordInput)) {// search the trie for the word
				System.out.println("Successfully detected " + wordInput);
			} else {
				System.out.println("Not found: " + wordInput);
			}
		}

		System.out.print("Search with (one) word: ");
		searchWord = scan.nextLine();// read input from user
		searchWord = searchWord.toLowerCase();// make it lowercase

		System.out.print("Choose (>0) amount of suggested words for " + searchWord + ": ");
		int k = scan.nextInt();// get amount of words to suggest from the user
		while (k <= 0) {
			System.out.println("Invalid amount given!\n Try again: ");
			k = scan.nextInt();// if the number is less than or 0
		}
		minHeap = new MinHeap(k);// initialise a minheap with the size given
		System.out.println("Checking for top " + k + " reccomended words for " + searchWord + ":");
		trie.autocomplete(searchWord, minHeap);// use the autocomplete function to search for similar words

		scan.close();
	}
}