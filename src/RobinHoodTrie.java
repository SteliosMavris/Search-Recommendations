
/**
 * Represents a Robin Hood Trie data structure
 */
public class RobinHoodTrie {

	/**
	 * Root node of the Trie
	 */
	RobinHoodTrieNode root;

	/**
	 * Default constructor for the Trie Initialises the root
	 */
	RobinHoodTrie() {
		this.root = new RobinHoodTrieNode();
	}

	/**
	 * Inserts a word into the trie
	 * 
	 * @param word the word to be inserted
	 */
	public void insertWord(String word) {
		RobinHoodTrieNode currentNode = this.root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);

			if (currentNode.hashTable == null) {
				currentNode.hashTable = new RobinHoodTrieNode.RobinHoodHashing();
			}

			if (!currentNode.hashTable.search(c)) {
				currentNode.hashTable.insert(c);
			}

			for (RobinHoodTrieNode.RobinHoodHashing.Element element : currentNode.hashTable.table) {
				if (element != null && element.key == c) {
					if (element.robinHoodTrieNode == null) {
						element.robinHoodTrieNode = new RobinHoodTrieNode();
					}
					currentNode = element.robinHoodTrieNode;
					break;
				}
			}
		}

		currentNode.wordLength = word.length();
	}

	/**
	 * Represents a node in the trie
	 */
	public static class RobinHoodTrieNode {
		int importance;
		int wordLength;
		RobinHoodHashing hashTable;

		/**
		 * Default constructor for a trie node
		 */
		public RobinHoodTrieNode() {
			this.wordLength = 0;
			this.importance = 0;
			this.hashTable = null;
		}

		/**
		 * Represents a Robin Hood Hashing structure
		 */
		public static class RobinHoodHashing {
			Element[] table;
			int capacity;
			int size;
			int maxProbeLength;

			/**
			 * Represents an element stored in the hash table
			 */
			public static class Element {
				char key;
				int probeLength;
				RobinHoodTrieNode robinHoodTrieNode;

				/**
				 * Constructor for an element with a given key
				 * 
				 * @param c the character key to store
				 */
				public Element(char c) {
					this.key = c;
					this.probeLength = 0;
					this.robinHoodTrieNode = null;
				}
			}

			/**
			 * Default constructor for a Robin Hood Hashing table with default capacity
			 */
			public RobinHoodHashing() {
				this.capacity = 5;
				this.size = 0;
				this.maxProbeLength = 0;
				this.table = new Element[capacity];
			}

			/**
			 * Constructor for a Robin Hood Hashing table with a specified capacity
			 * 
			 * @param newCapacity the initial capacity of the hash table
			 */
			public RobinHoodHashing(int newCapacity) {
				this.capacity = newCapacity;
				this.size = 0;
				this.maxProbeLength = 0;
				this.table = new Element[capacity];
			}

			/**
			 * Inserts a character into the hash table
			 * 
			 * @param key the character to insert
			 */
			public void insert(char key) {
				Element newElement = new Element(key);
				Element replacement;
				int value = key;
				int hashValue = value % this.capacity;

				boolean finishedRobinhoodHashing = false;

				while (!finishedRobinhoodHashing) {
					if (this.table[hashValue] == null) {
						this.table[hashValue] = newElement;
						this.size++;
						finishedRobinhoodHashing = true;
					} else {
						if (newElement.probeLength > this.table[hashValue].probeLength) {
							replacement = table[hashValue];
							this.table[hashValue] = newElement;
							newElement = replacement;
							newElement.probeLength++;
							hashValue++;
						} else {
							newElement.probeLength++;
							hashValue++;
						}
					}

					if (hashValue == capacity) {
						hashValue = 0;
					}

					if (newElement.probeLength > this.maxProbeLength) {
						this.maxProbeLength = newElement.probeLength;
					}

					if (this.size > (this.capacity * 0.9)) {
						this.rehash();
					}
				}
			}

			/**
			 * Searches for a character in the hash table
			 * 
			 * @param key the character to search for
			 * @return true if the character exists in the table, false otherwise
			 */
			public boolean search(char key) {
				int value = key;
				int hashValue = value % this.capacity;

				for (int i = 0; i <= this.maxProbeLength; i++) {
					if (this.table[hashValue] == null) {
						continue;
					}
					if (this.table[hashValue].key == key) {
						return true;
					} else {
						hashValue++;
						if (hashValue == capacity) {
							hashValue = 0;
						}
					}
				}
				return false;
			}

			/**
			 * Rehashes the hash table to a larger capacity when needed.
			 */
			public void rehash() {

				int newCapacity = this.capacity;
				switch (this.capacity) {
				case 5:
					newCapacity = 11;
					break;
				case 11:
					newCapacity = 19;
					break;
				case 19:
					newCapacity = 29;
					break;
				}

				Element[] tmp = new Element[this.capacity];

				for (int i = 0; i < this.capacity; i++) {
					if (this.table[i] != null) {
						tmp[i] = new Element(this.table[i].key);
						tmp[i].robinHoodTrieNode = this.table[i].robinHoodTrieNode;
					}
				}

				this.table = new Element[newCapacity];
				this.capacity = newCapacity;
				this.size = 0;
				this.maxProbeLength = 0;

				for (int i = 0; i < tmp.length; i++) {
					if (tmp[i] != null) {
						insert(tmp[i].key);
						for (RobinHoodTrieNode.RobinHoodHashing.Element element : this.table) {
							if (element != null && element.key == tmp[i].key) {
								element.robinHoodTrieNode = tmp[i].robinHoodTrieNode;
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Searches for a given word in the trie and if its found, increase its
	 * importance
	 * 
	 * @param word
	 * @return
	 */
	public boolean searchWord(String word) {
		RobinHoodTrieNode currentNode = this.root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			/*
			 * If the word's characters lead to an uninitialised hash table then the word
			 * doesn't exist
			 */
			try {
				if (currentNode.hashTable == null) {
					throw new NullPointerException("HashTable is null");
				}
			} catch (NullPointerException e) {
				return false;
			}

			/*
			 * If even a single character of the word doesn't exist in the Trie then the
			 * word being searched doesn't exist
			 */
			try {
				if (!currentNode.hashTable.search(c)) {
					throw new NullPointerException("HashTable is null");
				}
			} catch (NullPointerException e) {
				return false;
			}

			/*
			 * Iterate through the hash table until the element containing the character of
			 * the word is found and then set the current node to the node that that element
			 * points to
			 */
			for (RobinHoodTrieNode.RobinHoodHashing.Element element : currentNode.hashTable.table) {
				if (element != null && element.key == c) {
					currentNode = element.robinHoodTrieNode;
					break;
				}
			}
		}

		// If ended on a node with 0 length then the word doesn't exist
		if (currentNode.wordLength == 0)
			return false;
		// Increase word's importance
		currentNode.importance++;
		System.out.println(word + " Importance: " + currentNode.importance + "\n" + word + " Word Length: "
				+ currentNode.wordLength);

		// Word exists if it passed all the checks above for every character of the word
		return true;
	}

	/**
	 * Uses the criteria functions to generate word suggestions
	 * 
	 * @param prefix
	 * @return
	 */
	public void autocomplete(String stringGiven, MinHeap minHeap) {
		// Start from the root of the Trie
		RobinHoodTrieNode currentNode = this.root;
		// Create MinHeap to store top k words with most importance

		// Call criteria methods to collect all valid recommendations
		prefixCriteria(minHeap, currentNode, stringGiven, true);


		sameLengthCriteria(minHeap, currentNode, stringGiven, "", 0);



		differentLengthCriteria(minHeap, currentNode, stringGiven, "");
		for (MinHeap.HeapElement element : minHeap.heapContents) {
			if (element != null)
				System.out.println(element.word + " " + element.importance);
		}

	}

	/**
	 * (temporary usage until minHeap is implemented) Recursively runs the trie
	 * starting from the given node and prints all valid words following the prefix
	 * criteria.
	 *
	 * @param node
	 * @param subString
	 * @param currentlyOnPrefix boolean to track if node is the end of the prefix to
	 *                          avoid printing it
	 */

	public void prefixCriteria(MinHeap minHeap, RobinHoodTrieNode node, String subString, boolean currentlyOnPrefix) {

		// Descend Trie only if we're currently still on the prefix within the Trie
		if (currentlyOnPrefix) {
			// Traverse the trie until the end of the prefix
			for (int i = 0; i < subString.length(); i++) {
				char c = subString.charAt(i);

				// Check if the current node contains the current letter of the prefix
				if (node.hashTable != null && node.hashTable.search(c)) {
					/*
					 * Search for element in the hash table holding the current character of the
					 * prefix
					 */
					for (RobinHoodTrieNode.RobinHoodHashing.Element element : node.hashTable.table) {
						if (element != null && element.key == c) {
							node = element.robinHoodTrieNode;
							break;
						}
					}

				} // If the prefix doesn't exist in the Trie, no recommendations are available
				else {
					System.out.println("No words found starting with " + subString);
					return;
				}
			}
		}

		if (node.wordLength > 0 && !currentlyOnPrefix) {
			if (minHeap.size < minHeap.maxSize - 1)
				minHeap.insertMin(subString, node.importance);
			else if (minHeap.getTop().importance < node.importance) {
				minHeap.deleteMin();
				minHeap.insertMin(subString, node.importance);
			}
		}
		if (node.hashTable != null) {

			for (RobinHoodTrieNode.RobinHoodHashing.Element element : node.hashTable.table) {
				if (element != null && element.robinHoodTrieNode != null) {
					prefixCriteria(minHeap, element.robinHoodTrieNode, subString + element.key, false);
				}
			}
		}
	}

	/**
	 * @param minHeap a heap used to store words with the most importance
	 * 
	 */
	public void sameLengthCriteria(MinHeap minHeap, RobinHoodTrieNode node, String wordGiven, String currentWord,
			int differences) {

		if (node.hashTable != null && currentWord.length() < wordGiven.length() && !(differences > 2)) {
			for (RobinHoodTrieNode.RobinHoodHashing.Element element : node.hashTable.table) {

				if (element != null && element.robinHoodTrieNode != null) {
					if (wordGiven.charAt(currentWord.length()) == element.key) {
						sameLengthCriteria(minHeap, element.robinHoodTrieNode, wordGiven, currentWord + element.key,
								differences);
					} else {
						sameLengthCriteria(minHeap, element.robinHoodTrieNode, wordGiven, currentWord + element.key,
								differences + 1);
					}
				}
			}
		}
		if (node.wordLength == wordGiven.length() && differences > 0 && differences <= 2) {
			if (minHeap.size < minHeap.maxSize - 1)
				minHeap.insertMin(currentWord, node.importance);
			else if (minHeap.getTop().importance < node.importance) {
				minHeap.deleteMin();
				minHeap.insertMin(currentWord, node.importance);
			}
		}
	}

	public void differentLengthCriteria(MinHeap minHeap, RobinHoodTrieNode node, String wordGiven, String currentWord) {

		// If currently on valid word and difference in length of two words isn't
		// greater than 3
		if (node.hashTable != null) {
			// Track which letter of the word given and the current word is examined
			int longerWordIndex = 0, shorterWordIndex = 0;
			// Track current differences to exit as soon as two strikes are found
			int strikes = 0;
			// Sets the upper limit of different characters allowed
			int maxStrikes = 0;
			// Holds amount of common characters
			int commons = 0;
			// Holds the longer word out of word given and current word
			String longerWord = null;
			// Holds the shorter word out of word given and current word
			String shorterWord = null;
			// Holds complete version of current word
			String completeCurrentWord = null;
			for (RobinHoodTrieNode.RobinHoodHashing.Element element : node.hashTable.table) {
				if (element != null && element.robinHoodTrieNode != null) {
					completeCurrentWord = currentWord + element.key;
					if (completeCurrentWord.equals(wordGiven))
						return;
					if (element.robinHoodTrieNode.wordLength > 0
							&& (wordGiven.length() - completeCurrentWord.length() == 1
									|| (completeCurrentWord.length() - wordGiven.length() > 0)
											&& (completeCurrentWord.length() - wordGiven.length() <= 2))) {

						// If currently on a word that is one character shorter
						if (wordGiven.length() - completeCurrentWord.length() == 1) {

							longerWord = wordGiven;
							shorterWord = completeCurrentWord;
							maxStrikes = 1;
						} // If currently on a word with maximum 2 characters longer
						else if ((completeCurrentWord.length() - wordGiven.length() > 0)
								&& (completeCurrentWord.length() - wordGiven.length() <= 2)) {

							longerWord = completeCurrentWord;
							shorterWord = wordGiven;
							maxStrikes = 2;
						}
						if (longerWord != null) {
							// Check if current word is "hidden" inside word given
							while (strikes <= maxStrikes && longerWordIndex < longerWord.length()
									&& shorterWordIndex < shorterWord.length()) {
								if (longerWord.charAt(longerWordIndex) == shorterWord.charAt(shorterWordIndex)) {
									shorterWordIndex++;
									commons++;
								} else
									strikes++;

								longerWordIndex++;
							}
						}
						// If after comparing the two words, maximum strikes were not surpassed, insert
						// current word in the heap if its importance is higher than that of the top
						// element's
						if (completeCurrentWord.length() < wordGiven.length() && strikes <= maxStrikes
								&& completeCurrentWord.length() != wordGiven.length()) {
							if (minHeap.size < minHeap.maxSize - 1)
								minHeap.insertMin(completeCurrentWord, element.robinHoodTrieNode.importance);
							else if (minHeap.getTop().importance < element.robinHoodTrieNode.importance) {
								minHeap.deleteMin();
								minHeap.insertMin(completeCurrentWord, element.robinHoodTrieNode.importance);
							}
						} else if (commons == shorterWord.length()
								&& completeCurrentWord.length() != wordGiven.length()) {
							if (minHeap.size < minHeap.maxSize - 1)
								minHeap.insertMin(completeCurrentWord, element.robinHoodTrieNode.importance);
							else if (minHeap.getTop().importance < element.robinHoodTrieNode.importance) {
								minHeap.deleteMin();
								minHeap.insertMin(completeCurrentWord, element.robinHoodTrieNode.importance);
							}
						}
					}
					differentLengthCriteria(minHeap, element.robinHoodTrieNode, wordGiven, currentWord + element.key);

				}
			}
		}

	}

}