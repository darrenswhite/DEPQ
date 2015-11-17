package cs21120.depq;

import java.util.NoSuchElementException;

/**
 * TODO
 * <p>
 * References:
 * <p>
 * http://www.mhhe.com/engcs/compsci/sahni/enrich/c9/interval.pdf
 * http://www.cise.ufl.edu/~sahni/dsaaj/enrich/c13/double.htm
 * https://en.wikipedia.org/wiki/Double-ended_priority_queue
 *
 * @author Darren White
 */
@SuppressWarnings("unchecked")
public class Daw48DEPQ implements DEPQ {

	/**
	 * Used to store all of the nodes in the queue
	 * start with a minimum size of 10
	 */
	private Node[] nodes = new Node[10];

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
	 * Creates a new DEPQ using my interval heap implementation
	 */
	public Daw48DEPQ() {
	}

	/**
	 * TODO
	 *
	 * @param c the element to insert into the DEPQ
	 */
	@Override
	public void add(Comparable c) {
		// Don't add a null value
		// throw an npe instead
		if (c == null) {
			throw new NullPointerException("Null values not allowed in DEPQ");
		}

		// The node to add to the array
		Node n;
		// Two different add cases for odd and even number
		// of elements (not nodes!)
		// Increment numElements to keep track of number of elements
		if (numElements % 2 == 0) {
			// Number of elements is even, so only have a left element
			n = new Node(c, null);

			// If the array is full we need to make it larger
			if (nodes.length == numNodes) {
				// Create a new array
				// The minimum length is 10
				// Otherwise it will expand by 1.5
				Node[] newNodes = new Node[numNodes + (numNodes >> 1)];
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
		Node parent = nodes[(numNodes - 2) / 2];
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
	 * TODO
	 */
	private void bubbleMaxUp() {
		// The current index of node to shift up, start with the last element
		int index = numNodes - 1;
		// Its parent index
		int parentIndex;
		// The node at the index and its parent
		Node n, parent;
		// Used for the swap (the right element maybe null in odd numElements cases)
		Comparable c;

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
				Comparable tmp = parent.right;

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
	 * TODO
	 */
	private void bubbleMinUp() {
		// The current index of node to shift up, start with the last element
		int index = numNodes - 1;
		// Its parent index
		int parentIndex;
		// The node at the index and its parent
		Node n, parent;

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
				Comparable tmp = parent.left;

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
	 * TODO
	 *
	 * @return returns the smallest element in the DEPQ
	 */
	@Override
	public Comparable getLeast() {
		// Get the least element - may throw NoSuchElementException if empty
		Comparable min = inspectLeast();

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
		Node last = nodes[numNodes - 1];

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
		Node n, child;

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
				Comparable tmp = child.left;
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
	 * TODO
	 *
	 * @return returns the largest element in the DEPQ
	 */
	@Override
	public Comparable getMost() {
		// Get the most element - may throw NoSuchElementException if empty
		Comparable max = inspectMost();

		// If it is the only element, remove the node and return it
		// Remember to decrease number of elements as well
		if (numElements == 1) {
			// Set the node as null and decrease the number of nodes
			nodes[numNodes--] = null;
			numElements--;

			return max;
		}

		// Get the last node
		Node last = nodes[numNodes - 1];
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
		Node n, child;

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
				Comparable tmp = child.right;
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
	 * TODO
	 *
	 * @return returns the smallest element in the DEPQ
	 */
	@Override
	public Comparable inspectLeast() {
		// No elements in the queue so there
		// is no least value
		if (numElements == 0) {
			throw new NoSuchElementException("DEPQ is empty");
		}

		// The least value is the left element of the root node
		return nodes[0].left;
	}

	/**
	 * TODO
	 *
	 * @return returns the largest element in the DEPQ
	 */
	@Override
	public Comparable inspectMost() {
		// No elements in the queue so there
		// is no most value
		if (numElements == 0) {
			throw new NoSuchElementException("DEPQ is empty");
		}

		// The most value is the right element of the root node
		// but the right element is null if only one element
		// is in the queue as we occupy the left element first
		// then swap if needed
		Node root = nodes[0];
		return numElements == 1 ? root.left : root.right;
	}

	/**
	 * TODO
	 *
	 * @return returns true if the queue is empty
	 */
	@Override
	public boolean isEmpty() {
		// The queue is empty if it contains 0 elements
		return numElements == 0;
	}

	/**
	 * TODO
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
	public static class Node {

		/**
		 * Left element is the smaller in the interval
		 * Right element is the larger in the interval
		 */
		public Comparable left, right;

		/**
		 * Create a new node with a left and right element
		 *
		 * @param left  The left element - minimum
		 * @param right The right element - maximum
		 */
		public Node(Comparable left, Comparable right) {
			this.left = left;
			this.right = right;
		}
	}
}