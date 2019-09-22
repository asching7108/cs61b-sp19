/** Performs some basic linked list tests. */
public class ArrayDequeTest {
	
	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");

		ArrayDeque<Integer> ad1 = new ArrayDeque<>();

		boolean passed = checkEmpty(true, ad1.isEmpty());

		ad1.addFirst(1);
		for (int i = 2; i <= 100; i++) {
			ad1.addLast(i);
		}

		ad1.removeLast();
		for (int i = 1; i < 80; i++) {
			ad1.removeFirst();
		}
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(20, ad1.size()) && passed;

		System.out.println("Printing out deque: ");
		ad1.printDeque();

		printTestStatus(passed);

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, ad1.isEmpty());

		ad1.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, ad1.isEmpty()) && passed;

		ad1.removeFirst();
		// should be empty 
		passed = checkEmpty(true, ad1.isEmpty()) && passed;

		printTestStatus(passed);

	}

	/** Create a deque, then get item with iteration and recursion. */
	public static void getTest() {

		System.out.println("Running get test.");

		ArrayDeque<Integer> ad1 = new ArrayDeque<>();

		ad1.addLast(2);
		ad1.addLast(4);
		ad1.addLast(8);
		ad1.removeFirst();
		boolean passed = true;
		if (ad1.get(1) != 8) {
			passed = false;
		}

		printTestStatus(passed);

	}

	/** Create a deque, then use the copy constructor to copy it. */
	public static void copyTest() {

		System.out.println("Running copy test.");

		ArrayDeque<Integer> ad1 = new ArrayDeque<>();
		ad1.addLast(2);
		ad1.addLast(4);
		ad1.addLast(8);

		ArrayDeque<Integer> ad2 = new ArrayDeque<>(ad1);
		boolean passed = true;
		for (int i = 0; i < ad1.size(); i++) {
			if (ad1.get(i) != ad2.get(i)) {
				passed = false;
			}
		}

		printTestStatus(passed);

	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
		getTest();
		copyTest();
	}
} 