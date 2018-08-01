import java.util.*;

/** A Huffman coding tree */
class HuffTree implements Comparable {
	
	private HuffBaseNode root;  

	/** Constructors */
	public HuffTree(char el, int wt) {
		root = new HuffLeafNode(el, wt);
	}
	
	public HuffTree(HuffBaseNode l, HuffBaseNode r, int wt) {
		root = new HuffInternalNode(l, r, wt);
	}

	public HuffBaseNode root() {
		return root;
	}
	
	// Weight of tree is weight of root
	public int weight() {
		return root.weight();
	}
	
	public int compareTo(Object t) {
		HuffTree that = (HuffTree)t;
		if (weight() < that.weight()) return -1;
		else if (weight() == that.weight()) return 0;
		else return 1;
	}
}