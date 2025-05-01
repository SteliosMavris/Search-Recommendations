
public class Trie {

    TrieNode root;

    static int ALPHABET_SIZE = 26;

    class TrieNode {

        int wordLength;
        TrieNode[] subTrieNodes;

        public TrieNode() {
            this.wordLength = 0;
            this.subTrieNodes = new TrieNode[ALPHABET_SIZE];
        }

    }

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode temp = this.root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a'; // Convert character to index
            if (temp.subTrieNodes[index] == null)
                temp.subTrieNodes[index] = new TrieNode(); // Create new Trie Node
            temp = temp.subTrieNodes[index]; // Move downwards
        }
        temp.wordLength = word.length();
    }

    public boolean search(String word) {
        TrieNode temp = this.root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a'; // Convert character to index
            if (temp.subTrieNodes[index] == null) { // If Trie Node is null, word doesn't exist
                return false;
            }
            temp = temp.subTrieNodes[index]; // Move to next Trie Node
        }
        return temp.wordLength != 0;
    }
}