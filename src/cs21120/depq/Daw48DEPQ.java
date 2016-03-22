package cs21120.depq;

/**
 * This implementation of a Double-ended Priority Queue uses an interval heap to
 * store and retrieve elements. Elements can be added using the add(Comparable)
 * method and can be inspected as well as inspected and removed. Further detail
 * for these methods can be found in the JavaDoc comments (as well as code
 * comments) below. The interval heap is similar to a min-max heap but
 * combines both of them into one. This means that each node in the heap contains
 * two elements and is also a binary tree. These two elements in the node are
 * referred to as a closed interval. The left element must be smaller than the
 * right element (these elements are also known as min and max) but if there is
 * only one element in the interval then there are no boundary constraints. Each
 * child interval must be within the bounds of its parent interval. The root
 * interval will also contain the min and max elements due to how the heap is
 * structured. All of the elements are stored in nodes (each node holds two) and
 * all nodes are stored in an array (resized when needed). The finer technical
 * details are within the JavaDoc comments for each method. This implementation
 * was chosen due to it's fast nature of adding, inspecting and removing elements.
 * <p>
 * The time complexity for adding a new element is O(log n) because the greatest
 * factor in the algorithm (bubbleMaxUp or bubbleMinUp) only use a single while
 * loop to traverse the tree from the last leaf to the root and the height at
 * which this leaf node is at is log n. Note that n is the number of elements
 * and not the number of nodes.
 * <p>
 * The time complexity for getting the 'least' or 'most' element is O(log n)
 * because again the greatest time factor here is the while loop which iterates
 * from the root to the bottom of the tree.
 * <p>
 * Inspecting the 'least' or 'most' element is O(1) because the simply accesses
 * the array of nodes (which is constant) and return the correct element.
 * <p>
 * isEmpty and size also have a time complexity of O(1) because they are simple
 * constant expressions which only access the size variable to determine the
 * size of the array (and compare it to 0 for isEmpty).
 * <p>
 * Correct functioning of each method (25%): Each of the methods implemented
 * work as intended each time (tested with JUnit tests) and the queue is able
 * to hold at least 1000 items so I would award my self 25 marks for this
 * section).
 * <p>
 * Overall efficiency (25%): The solution used has O(log n) complexity for
 * the add, getMost and getLeast methods, and the other methods have O(1). I
 * would award myself 25 marks for this section as I the methods implemented
 * have time complexities of that required.
 * <p>
 * Documentation based report (25%): I have properly documented each method
 * using JavaDoc and have supplied necessary comments to the code. I have used
 * references but not cited them as I used them for a general overview of how
 * interval heaps work. I would award myself 20 marks for this due to not
 * citing any references (only listing them instead).
 * <p>
 * Complexity Analysis (20%): I have included an analysis of my implementation
 * for each method (in the DEPQ interface). I was not fully confident of how
 * to do this so I don't think I did very well here so I would award myself 10.
 * <p>
 * Overall I would award myself 85 marks.
 * <p>
 * References:
 * <p>
 * http://www.mhhe.com/engcs/compsci/sahni/enrich/c9/interval.pdf
 * http://www.cise.ufl.edu/~sahni/dsaaj/enrich/c13/double.htm
 * https://en.wikipedia.org/wiki/Double-ended_priority_queue
 *
 * @author Darren White
 */
public class Daw48DEPQ<E extends Comparable<E>> implements DEPQ<E> {

	/**
	 * Used to store all of the nodes in the queue
	 * start with a minimum size of 10
	 */
	@SuppressWarnings("unchecked")
	private Node<E>[] nodes = (Node<E>[]) new Node[10];

	/**
	 * Number of elements in the queue,
	 * this is not the number of nodes
	 */
	private int numElements = 0;

	/**
	 * The number of nodes in the array, this will also
	 * act as a pointer to the last node in the array
	 */
	private int numNodes = 0;

	/**
	 * Adds a new Comparable element into the DEPQ in the correct position.
	 * If the last node only has one element in it then the new element is
	 * inserted correctly into that node otherwise a new node is created
	 * with the new element and the new node is inserted into the end of the
	 * array, expanding the size of the array when needed. If there is 2 or less
	 * elements then we do not need to resort the heap otherwise the newly
	 * inserted element needs to be moved to its correct position in the heap.
	 * we do this by bubbling up the element using bubbleMinUp() or
	 * bubbleMaxUp() depending which element it was inserted as (left = min,
	 * right = max). No null elements can be added and will throw a
	 * NullPointerException.
	 *
	 * @param c the element to insert into the DEPQ
	 */
	@Override
	public void add(E c) {
		// Don't add a null value
		// throw an npe instead
		if (c == null) {
			throw new NullPointerException("Null values not allowed in DEPQ");
		}

		// The node to add to the array
		Node<E> n;
		// Two different add cases for odd and even number
		// of elements (not nodes!)
		// Increment numElements to keep track of number of elements
		if (numElements % 2 == 0) {
			// Number of elements is even, so only have a left element
			n = new Node<>(c, null);

			// If the array is full we need to make it larger
			if (nodes.length == numNodes) {
				// Create a new array
				// The minimum length is 10
				// Otherwise it will expand by 1.5
				@SuppressWarnings("unchecked")
				Node<E>[] newNodes = (Node<E>[]) new Node[numNodes + (numNodes >> 1)];
				// Copy the existing array into the new array
				System.arraycopy(nodes, 0, newNodes, 0, numNodes);
				// Set the full array as the larger array
				nodes = newNodes;
			}

			// Insert node at the end of the array
			// We added another node so increase number of nodes
			nodes[numNodes++] = n;
		} else {
			// Number of elements is odd
			// Get the last element in the array
			// and put the element in left/right
			n = nodes[numNodes - 1];

			// Note here: n.right == null
			// The left element is larger than c
			if (n.left.compareTo(c) > 0) {
				// Move the left element to the right
				// and put c as the left element
				n.right = n.left;
				n.left = c;
			} else {
				// The left element is smaller than c
				n.right = c;
			}
		}

		// We added a new element
		numElements++;

		// The heap is fine if there are only two elements
		// Otherwise we need to move the element to its correct position
		if (numElements <= 2) {
			return;
		}

		// Get the parent of the node
		// Note: last element is at numNodes - 1
		// so its parent is at (numNodes - 1 - 1) / 2
		Node<E> parent = nodes[(numNodes - 2) / 2];
		if (parent.left.compareTo(c) > 0) {
			// The parent left element is larger than c
			// so we have to move c up to the correct position
			bubbleMinUp();
		} else if (parent.right.compareTo(c) < 0) {
			// The parent right element is smaller than c
			// so we have to move c up to the correct position
			bubbleMaxUp();
		}

		// The element should stay here otherwise
	}

	/**
	 * Bubbles the max element up the heap until it is in the correct
	 * position. This will compare the last nodes max (right) element with
	 * its parent max (right) element and will swap them if the parent node
	 * max element is smaller than the last node max element. This is then
	 * repeated on the next parent node up the heap until the node is in the
	 * correct position where its parent max element is larger than its max
	 * element and its children's max elements are smaller than it.
	 */
	private void bubbleMaxUp() {
		// The current index of node to shift up, start with the last element
		int index = numNodes - 1;
		// Its parent index
		int parentIndex;
		// The node at the index and its parent
		Node<E> n, parent;
		// Used for the swap (the right element maybe null in odd numElements cases)
		E c;

		// Keep looping while we have a valid parent
		// when we reach the root we should stop
		while ((parentIndex = (index - 1) / 2) >= 0) {
			// Retrieve the node and its parent
			n = nodes[index];
			parent = nodes[parentIndex];

			// Get the right element, but if its null get the left
			if (n.right == null) {
				c = n.left;
			} else {
				c = n.right;
			}

			// If the parent right element is smaller than the node right element
			// then they need to be swapped
			if (parent.right.compareTo(c) < 0) {
				// Swap the parent and node right elements (or left if right was null)
				E tmp = parent.right;

				parent.right = c;

				if (n.right == null) {
					n.left = tmp;
				} else {
					n.right = tmp;
				}

				// Set the node index as its parent
				// so we shift up the next level
				index = parentIndex;
			} else {
				// Otherwise the nodes are in correct positions, so we stop
				break;
			}
		}
	}

	/**
	 * Bubbles the min element up the heap until it is in the correct
	 * position. This will compare the last nodes min (left) element with
	 * its parent min (left) element and will swap them if the parent node
	 * min element is larger than the last node min element. This is then
	 * repeated on the next parent node up the heap until the node is in the
	 * correct position where its parent min element is smaller than its min
	 * element and its children's min elements are larger than it.
	 */
	private void bubbleMinUp() {
		// The current index of node to shift up, start with the last element
		int index = numNodes - 1;
		// Its parent index
		int parentIndex;
		// The node at the index and its parent
		Node<E> n, parent;

		// Keep looping while we have a valid parent
		// when we reach the root we should stop
		while ((parentIndex = (index - 1) / 2) >= 0) {
			// Retrieve the node and its parent
			n = nodes[index];
			parent = nodes[parentIndex];

			// If the parent left element is larger than the node left element
			// then they need to be swapped
			if (parent.left.compareTo(n.left) > 0) {
				// Swap the parent and node left elements
				E tmp = parent.left;

				parent.left = n.left;
				n.left = tmp;

				// Set the node index as its parent
				// so we shift up the next level
				index = parentIndex;
			} else {
				// Otherwise the nodes are in correct positions, so we stop
				break;
			}
		}
	}

	/**
	 * Gets the smallest element in the DEPQ and removes it from the DEPQ. The
	 * smallest element is retrieved using inspectLeast(); this is the element
	 * which will be returned after removing and ensuring that the heap is
	 * correct and if not swapping elements where needed. If there is only
	 * one element in the queue then it is removed (and its node) and returned.
	 * Otherwise, the last nodes min (left) element is moved into the root node
	 * and the last node left element is removed and replaced with the max (right)
	 * element if there is one. Now, starting at the root node, we compare both
	 * children to get the child with the smallest min (left) element. Next we
	 * compare this child's left element with the nodes left element and if
	 * the child left element is smaller then we swap them otherwise the element
	 * is in the correct position and we can stop. We continue this while the
	 * current node has a child or until the node is in the correct position,
	 * where the child node min (left) element is larger than the nodes min
	 * (left) element.
	 *
	 * @return returns the smallest element in the DEPQ
	 */
	@Override
	public E getLeast() {
		// Get the least element - may be null if queue is empty
		E min = inspectLeast();

		// If it is the only element, remove the node and return it
		// Remember to decrease number of elements as well
		if (numElements == 1) {
			// Decrease how many nodes are in the array
			nodes[numNodes--] = null;
			numElements--;

			// Return the only element in the array
			return min;
		}

		// Get the last node
		Node<E> last = nodes[numNodes - 1];

		// Put the last nodes left element in the root left element
		nodes[0].left = last.left;

		// If the last node has no right element, remove it
		// as we just removed the left element so it's an empty interval
		if (last.right == null) {
			// Remove last node, it is empty
			nodes[numNodes - 1] = null;
			// Decrease the number of nodes as we just remove a node
			numNodes--;
		} else {
			// Move the right element to the left
			last.left = last.right;
			last.right = null;
		}

		// Decrease number of elements as we just removed one
		numElements--;

		// The node index, start at the root
		int index = 0;
		// To get node children use
		// (i * 2) + 1 (for left or + 2 for right)
		int childIndex;
		// The current node and it's child
		Node<E> n, child;

		// Keep looping while we have a child, moving the left element down
		// the heap until it's in the correct position
		// Note: we are storing the childIndex as the left child (as it's + 1)
		while ((childIndex = index * 2 + 1) < numNodes) {
			// Get the node and its child
			n = nodes[index];
			child = nodes[childIndex];

			// If we have a right child - the childIndex is the left child
			// so adding + 1 will get the right child index
			if (childIndex + 1 < numNodes) {
				// Compare the children to find the smallest left element
				if (child.left.compareTo(nodes[childIndex + 1].left) > 0) {
					// The right child has a smaller left element so we
					// use the right child
					// Remember to increase the childIndex as we are using the
					// right child
					child = nodes[++childIndex];
				}
			}

			// The node left element is larger than the child left element
			// so they need to be swapped
			if (n.left.compareTo(child.left) > 0) {

				// Otherwise swap the node and child left elements
				E tmp = child.left;
				child.left = n.left;
				n.left = tmp;

				// Ensure that the left element is the smallest
				// otherwise swap left & right
				if (child.right != null && child.left.compareTo(child.right) > 0) {
					tmp = child.left;
					child.left = child.right;
					child.right = tmp;
				}

				// Continue down the heap
				index = childIndex;
			} else {
				// Otherwise the nodes are in correct positions, so we stop
				break;
			}
		}

		// Return the smallest element removed from the root node
		return min;
	}

	/**
	 * Gets the largest element in the DEPQ and removes it from the DEPQ. The
	 * largest element is retrieved using inspectMost(); this is the element
	 * which will be returned after removing and ensuring that the heap is
	 * correct and if not swapping elements where needed. If there is only
	 * one element in the queue then it is removed (and its node) and returned.
	 * Otherwise, the last nodes min (left) element is moved into the root node
	 * and the last node left element is removed and replaced with the max (right)
	 * element if there is one. Now, starting at the root node, we compare both
	 * children to get the child with the smallest min (left) element. Next we
	 * compare this child's left element with the nodes left element and if
	 * the child left element is smaller then we swap them otherwise the element
	 * is in the correct position and we can stop. We continue this while the
	 * current node has a child or until the node is in the correct position,
	 * where the child node min (left) element is larger than the nodes min
	 * (left) element.
	 *
	 * @return returns the largest element in the DEPQ
	 */
	@Override
	public E getMost() {
		// Get the most element - may be null if queue is empty
		E max = inspectMost();

		if (max == null) {
			return null;
		}

		// If it is the only element, remove the node and return it
		// Remember to decrease number of elements as well
		if (numElements == 1) {
			// Set the node as null and decrease the number of nodes
			nodes[numNodes--] = null;
			numElements--;

			return max;
		}

		// Get the last node
		Node<E> last = nodes[numNodes - 1];
		// If the last node has a right element that put that
		// in the root node as the right element otherwise
		// the last node only has one element which is the left
		// so move that into the root node instead
		nodes[0].right = last.right != null ? last.right : last.left;

		// If the last node has no right element, remove it
		// as we just removed the left element so it's an empty interval
		if (last.right == null) {
			// Remove last node, it is empty
			nodes[numNodes - 1] = null;
			// and decrease the number of nodes
			numNodes--;
		} else {
			// We just moved the right element to the root
			// so remove it from the last node
			last.right = null;
		}

		// Decrease number of elements as we just removed one
		numElements--;

		// The node index, start at the root
		int index = 0;
		// To get node children use
		// (i * 2) + 1 (for left or + 2 for right)
		int childIndex;
		// The current node and it's child
		Node<E> n, child;

		// Keep looping while we have a child, moving the left element down
		// the heap until it's in the correct position
		// Note: we are storing the childIndex as the left child (as it's + 1)
		while ((childIndex = index * 2 + 1) < numNodes) {
			// Get the node and its child
			n = nodes[index];
			child = nodes[childIndex];

			// If we have a right child - the childIndex is the left child
			// so adding + 1 will get the right child index
			if (childIndex + 1 < numNodes && nodes[childIndex + 1].right != null) {
				// Compare the children to find the largest right element
				if (child.right.compareTo(nodes[childIndex + 1].right) < 0) {
					// The right child has a larger right element so we
					// use the right child
					// Remember to increase the childIndex as we are using the
					// right child
					child = nodes[++childIndex];
				}
			}

			// If the child has a right element and the node right element is
			// smaller than the child right element then they need to be swapped
			if (child.right != null && n.right.compareTo(child.right) < 0) {
				// Otherwise swap the node and child right elements
				E tmp = child.right;
				child.right = n.right;
				n.right = tmp;

				// Ensure that the right element is the largest
				// otherwise swap left & right
				if (child.right != null && child.left.compareTo(child.right) > 0) {
					tmp = child.right;
					child.right = child.left;
					child.left = tmp;
				}

				// Continue down the heap
				index = childIndex;
			} else {
				// Otherwise the nodes are in correct positions, so we stop
				break;
			}
		}

		// Return the largest element removed from the root node
		return max;
	}

	/**
	 * Gets the smallest element in the DEPQ but does not remove it from
	 * the DEPQ. This will simply access the nodes array and retrieve the min
	 * element from the root node. Null may be returned if the queue is empty.
	 *
	 * @return returns the smallest element in the DEPQ
	 */
	@Override
	public E inspectLeast() {
		// If there are elements in the queue
		// then return the least value is the
		// left element of the root node otherwise
		// return null
		return numElements > 0 ? nodes[0].left : null;
	}

	/**
	 * Gets the largest element in the DEPQ but does not remove it from
	 * the DEPQ. This will simply access the nodes array and retrieve the max
	 * element from the root node. Null may be returned if the queue is empty.
	 *
	 * @return returns the largest element in the DEPQ
	 */
	@Override
	public E inspectMost() {
		// No elements in the queue so there
		// is no most value
		if (numElements == 0) {
			return null;
		}

		// The most value is the right element of the root node
		// but the right element is null if only one element
		// is in the queue as we occupy the left element first
		// then swap if needed
		Node<E> root = nodes[0];
		return numElements == 1 ? root.left : root.right;
	}

	/**
	 * Checks if the DEPQ is empty which is only true when the number
	 * of elements is 0 (also the number of nodes will be 0)
	 *
	 * @return returns true if the queue is empty
	 */
	@Override
	public boolean isEmpty() {
		// The queue is empty if it contains 0 elements
		// We could also check the number of nodes but this is redundant
		return numElements == 0;
	}

	/**
	 * Gets the number of elements in the DEPQ. This is not the number
	 * of nodes. This simply increments when adding and decrements when
	 * removing elements.
	 *
	 * @return returns the number of elements currently in the DEPQ
	 */
	@Override
	public int size() {
		// This is the number of elements not number of nodes
		return numElements;
	}

	/**
	 * This class represents a closed interval which we will use
	 * as the basic of the interval heap. The root node will have
	 * the smallest element in the left element and the largest
	 * element in the right element
	 */
	public static class Node<E> {

		/**
		 * Left element is the smaller in the interval
		 * Right element is the larger in the interval
		 */
		public E left, right;

		/**
		 * Create a new node with a left and right element
		 *
		 * @param left  The left element - minimum
		 * @param right The right element - maximum
		 */
		public Node(E left, E right) {
			this.left = left;
			this.right = right;
		}
	}
}