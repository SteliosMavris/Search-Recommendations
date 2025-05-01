

public class MinHeap {

	HeapElement heapContents[];
	int maxSize;
	int size;

	// Initialises a heap with a maximum of k slots
	public MinHeap(int k) {
		/*
		 * Initialise a heap of maxSize k (for storing the k most important words) + 1
		 * for the starting position of the array that is not used
		 */
		this.maxSize = k + 1;
		this.size = 0;
		this.heapContents = new HeapElement[this.maxSize];

	}

	// fucntion representing an element in the heap
	public class HeapElement {
		String word;
		int importance;

		public HeapElement(String wordValue, int importanceValue) {

			this.word = wordValue;
			this.importance = importanceValue;
		}

	}

	// returns the top element in the heap
	public HeapElement getTop() {
		return this.heapContents[1];
	}

	// inserts an element into the heap
	public void insertMin(String wordValue, int importanceValue) {
		if (this.size >= this.maxSize - 1) {
			throw new IllegalStateException("Heap is full");
		}

		HeapElement newElement = new HeapElement(wordValue, importanceValue);
		this.size++; // Increment size
		percolateUp(newElement); // Re-heapify
	}

	// deletes element from the heap
	public HeapElement deleteMin() {
		if (size == 0) {
			throw new IllegalStateException("Heap is empty");
		}

		HeapElement minimumElement = this.heapContents[1];
		this.heapContents[1] = this.heapContents[size];
		this.heapContents[size] = null;
		this.size--;
		percolateDown(this.heapContents[1]);

		return minimumElement;
	}

	public void percolateUp(HeapElement newElement) {

		HeapElement replacement = null;
		// Variable to track position being examined in the heap
		int pos = this.size;

		// If this is the first element to be placed in the heap, place at 1st spot
		if (pos == 1) {
			this.heapContents[1] = newElement;
		}

		// Repeat percolate up until spot was found to place the new element
		while (true) {
			// If the first spot was reached, no possible comparisons left, end percolate up
			if (pos == 1) {
				break;
			}
			if (this.heapContents[pos / 2].importance > newElement.importance) {// If parent has larger importance than
																				// its child swap them
				replacement = this.heapContents[pos / 2];// Swap swap the parent with the new lement
				this.heapContents[pos / 2] = newElement;
				this.heapContents[pos] = replacement;
				pos /= 2;// Update the position
			} else {
				this.heapContents[pos] = newElement;
				break;// Last position found, end
			}

		}

	}

	public void percolateDown(HeapElement savedElement) {
		int pos = 1; // Start at the root

		while (2 * pos <= this.size) {
			int child = 2 * pos; // Left child index

			// Find the smaller child
			if (child + 1 <= this.size
					&& this.heapContents[child + 1].importance < this.heapContents[child].importance) {
				child++; // Right child is smaller
			}
			if (savedElement.importance <= this.heapContents[child].importance) {// If saved element is smaller than the
																					// smaller child, stop
				break;
			}
			this.heapContents[pos] = this.heapContents[child];// Move the smaller child up
			pos = child; // Move down a position
		}

		this.heapContents[pos] = savedElement; // Place saved element in its final position
	}

	// debug and test function
	public static void main(String args[]) {

		MinHeap m = new MinHeap(6);
		m.insertMin("pineapple", 3);
		m.insertMin("apple", 22);
		m.insertMin("banana", 27);
		m.insertMin("orange", 44);
		m.insertMin("mango", 26);
		m.insertMin("grapes", 28);

		System.out.println("Contents of heap: ");
		for (HeapElement h : m.heapContents) {
			if (h == null)
				continue;
			System.out.println(h.word + " " + h.importance);
		}

		m.deleteMin();
		System.out.println("Contents of heap after deleteMin: ");
		for (HeapElement h : m.heapContents) {
			if (h == null)
				continue;
			System.out.println(h.word + " " + h.importance);
		}
	}

}