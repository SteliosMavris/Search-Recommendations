
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MemoryCalculator {

	private static long calcStaticTrie(Trie.TrieNode node) {
		// Null node consumes no memory
		if (node == null)
			return 0;
		// Add memory for the current node
		long sum = 216;
		for (int i = 0; i < Trie.ALPHABET_SIZE; i++)
			if (node.subTrieNodes[i] != null)
				// Add memory for each child recursively
				sum += (10 + calcStaticTrie(node.subTrieNodes[i]));
		return sum;
	}

	private static long calcRobinHoodTrie(RobinHoodTrie.RobinHoodTrieNode node) {
		// Null node consumes no memory
		if (node == null)
			return 0;
		// Null table consumes no memory
		if(node.hashTable == null)
			return 0;
		// Add memory for the current node
		long sum = 16 + calcRobinHoodTable(node.hashTable);
		for (int i = 0; i < node.hashTable.capacity; i++)
			if (node.hashTable.table[i] != null)
				// Add memory for the hash table and each child recursively
				sum += (14 + calcRobinHoodTrie(node.hashTable.table[i].robinHoodTrieNode));
		return sum;
	}

	private static long calcRobinHoodTable(RobinHoodTrie.RobinHoodTrieNode.RobinHoodHashing table) {
		
		// Memory for the hash table elements
		return (long) table.capacity * 8 + 12;
	}

	public static long calcStaticMemory(Trie trie) {
		// Null trie consumes no memory
		if (trie == null)
			return 8;
		return 8 + calcStaticTrie(trie.root);
	}

	public static long calcRobinHoodMemory(RobinHoodTrie trie) {
		// Null trie consumes no memory
		if (trie == null)
			return 8;
		return 8 + calcRobinHoodTrie(trie.root);
	}

	public static void main(String args[]) {

		Trie staticTrie = new Trie();
		RobinHoodTrie robinHoodTrie = new RobinHoodTrie();
		Scanner dictionary = null; // scanner for the dictionary
		String wordInput;

		try {// try to read the dictionary file
			dictionary = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			System.out.println("Error: the file given as dictionary doesn't exist!");
			System.exit(0);
		}

		while (dictionary.hasNextLine()) {// while the dictionary has more words
			wordInput = dictionary.nextLine().toLowerCase().trim();// read next line, make it lowercase, remove
																	// whitespaces
			if (wordInput.isEmpty() || !wordInput.matches("^[a-z]+$")) {
				continue;// if the word read is not null or does not contain special characters
			}
			robinHoodTrie.insertWord(wordInput);// insert the word in the trie
			staticTrie.insert(wordInput);
		}

		System.out.println(calcStaticMemory(staticTrie) + " " + calcRobinHoodMemory(robinHoodTrie));

	}
}