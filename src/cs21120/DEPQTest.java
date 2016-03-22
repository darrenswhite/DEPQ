package cs21120.depq;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Some basics tests of a Double Ended Priority Queue interface DEPQ
 * You will need to change the setUp method to create an instance of your class that implements the interface.
 * Feel free to add more tests to ensure your solution is working as you expect.
 * <p>
 * ArrayList is only used here for testing, do not use it, or any classes from java.util, in your implementation
 *
 * @author bpt
 */
@SuppressWarnings("SuspiciousMethodCalls")
public class DEPQTest {

	private DEPQ depq;

	@Before
	public void setUp() {
		depq = new Daw48DEPQ();
	}

	/**
	 * Test of add method, of class DEPQ.
	 */
	@Test
	public void testAdd() {
		System.out.println("add");

		for (int i = 0; i < 1000; i++) {
			depq.add((int) (Math.random() * 100));

			assertEquals(i + 1, depq.size());
		}
	}

	/**
	 * Test of getLeast method, of class DEPQ.
	 */
	@Test
	public void testGetLeast() {
		System.out.println("getLeast");

		for (int i = 0; i < 1000; i++) {
			depq.add((int) (Math.random() * 100));
		}

		for (int i = 0; i < 1000; i++) {
			assertEquals(depq.inspectLeast(), depq.getLeast());
		}
	}

	/**
	 * Test of getMost method, of class DEPQ.
	 */
	@Test
	public void testGetMost() {
		System.out.println("getMost");

		for (int i = 0; i < 1000; i++) {
			depq.add((int) (Math.random() * 100));
		}

		for (int i = 0; i < 1000; i++) {
			assertEquals(depq.inspectMost(), depq.getMost());
		}
	}

	/**
	 * Test of inspectLeast method, of class DEPQ.
	 */
	@Test
	public void testInspectLeast() {
		System.out.println("inspectLeast");

		List<Integer> array = new ArrayList<>();
		int smallest = 10000;

		// First check adding random number gives correct smallest
		for (int i = 0; i < 100; i++) {
			int k = (int) (Math.random() * 100);

			array.add(k);
			depq.add(k);

			if (k < smallest) {
				smallest = k;
			}

			assertEquals(smallest, depq.inspectLeast());
		}

		// Next randomly add or remove and check inspect least
		for (int i = 0; i < 99; i++) {
			boolean add = Math.random() > 0.5;

			if (add) {
				int k = (int) (Math.random() * 100);

				array.add(k);
				depq.add(k);

				if (k < smallest) {
					smallest = k;
				}
			} else {
				array.remove(depq.getLeast());
				smallest = array.get(0);

				for (int j = 1; j < array.size(); j++) {
					if (array.get(j) < smallest) {
						smallest = array.get(j);
					}
				}
			}

			assertEquals(smallest, depq.inspectLeast());
		}
	}

	/**
	 * Test of inspectMost method, of class DEPQ.
	 */
	@Test
	public void testInspectMost() {
		System.out.println("inspectMost");

		List<Integer> array = new ArrayList<>();
		int largest = -10000;

		// First check adding random number gives correct largest
		for (int i = 0; i < 100; i++) {
			int k = (int) (Math.random() * 100);

			array.add(k);
			depq.add(k);

			if (k > largest) {
				largest = k;
			}

			assertEquals(largest, depq.inspectMost());
		}

		// Next randomly add or remove and check inspect most
		for (int i = 0; i < 99; i++) {
			boolean add = Math.random() > 0.5;

			if (add) {
				int k = (int) (Math.random() * 100);

				array.add(k);
				depq.add(k);

				if (k > largest) {
					largest = k;
				}
			} else {
				array.remove(depq.getMost());
				largest = array.get(0);

				for (int j = 1; j < array.size(); j++) {
					if (array.get(j) > largest) {
						largest = array.get(j);
					}
				}
			}

			assertEquals(largest, depq.inspectMost());
		}
	}

	/**
	 * Test of isEmpty method, of class DEPQ.
	 */
	@Test
	public void testIsEmpty() {
		System.out.println("isEmpty");

		assertEquals(true, depq.isEmpty());

		for (int i = 0; i < 10; i++) {
			int count = (int) (Math.random() * 1000);

			for (int j = 0; j < count; j++) {
				depq.add(j);

				assertEquals(false, depq.isEmpty());
			}

			for (int j = 0; j < count; j++) {
				assertEquals(false, depq.isEmpty());

				depq.getLeast();
			}

			assertEquals(true, depq.isEmpty());
		}
	}

	/**
	 * Test of size method, of class DEPQ.
	 */
	@Test
	public void testSize() {
		System.out.println("size");

		for (int i = 0; i < 1000; i++) {
			depq.add((int) (Math.random() * 100));

			assertEquals(i + 1, depq.size());
		}

		for (int i = 0; i < 1000; i++) {
			boolean bigEnd = Math.random() > 0.5;

			if (bigEnd) {
				depq.getMost();
			} else {
				depq.getLeast();
			}

			assertEquals(1000 - i - 1, depq.size());
		}
	}
}