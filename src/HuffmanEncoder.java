//Reference : OpenDSA Website

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class HuffmanEncoder implements HuffmanCoding {
	
	// This method is used to read file and save each character from the file to an array list.
	public char[] readFile(File fileToRead) {
		ArrayList<Character> charArray = new ArrayList<Character>();

		// Read file and save each character to the array list.
		try {
			FileInputStream charToRead = new FileInputStream(fileToRead);
			char currentChar;
			while (charToRead.available() > 0) {
				currentChar = (char) charToRead.read();
				charArray.add(currentChar);
			}

		charToRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		char[] characters = new char[charArray.size()];
		for (int i = 0; i < charArray.size(); i++) {
			characters[i] = charArray.get(i);
		}

		return characters;
	}

	// This method takes a file as input and create a table with the characters and
	// their frequencies
	// and print the characters and their frequencies
	@Override
	public String getFrequencies(File inputFile) throws FileNotFoundException {

		String charFreqTable = new String();

		char[] characters = readFile(inputFile);

		Arrays.sort(characters);

		// Calculate the frequencies of characters.
		ArrayList<HuffLeafNode> leafNodes = new ArrayList<HuffLeafNode>();

		for (int i = 0; i < characters.length; i++) {
			int counter = 0;
			for (int j = 0; j < characters.length; j++) {
				if (j < i && characters[i] == characters[j]) {
					break;
				}
				if (characters[j] == characters[i]) {
					counter++;
				}
				if (j == characters.length - 1) {
					HuffLeafNode leaf = new HuffLeafNode(characters[i], counter);
					leafNodes.add(leaf);
					charFreqTable = charFreqTable.concat(characters[i] + " " + counter + "\n");
				}
			}
		}

		return charFreqTable;
	}

	// This method takes a file as input and create a Huffman Tree of the
	// characters.
	@Override
	public HuffTree buildTree(File inputFile) throws FileNotFoundException, Exception {

//				getFrequencies(inputFile);
//		
//				// ArrayList<HuffLeafNode> tree = new ArrayList<HuffLeafNode>();
//				PriorityQueue<HuffTree> Hheap = new PriorityQueue<HuffTree>();
//		
//				int y = 0;
//				while (y < intArray.size()) {
//					HuffTree x = new HuffTree(charArray.get(y), intArray.get(y));
//					Hheap.add(x);
//					y++;
//				}

		char[] characters = readFile(inputFile);

		ArrayList<HuffLeafNode> tree = new ArrayList<HuffLeafNode>();

		Arrays.sort(characters);

		for (int i = 0; i < characters.length; i++) {
			int counter = 0;
			for (int j = 0; j < characters.length; j++) {
				if (j < i && characters[i] == characters[j]) {
					break;
				}
				if (characters[j] == characters[i]){
					counter++;
				}
				if (j == characters.length - 1) {
					HuffLeafNode leaf = new HuffLeafNode(characters[i], counter);
					tree.add(leaf);
				}
			}

		}
		
		// Used to build the tree
		PriorityQueue<HuffTree> Hheap = new PriorityQueue<HuffTree>();

		for(int i = 0; i < tree.size(); i++) {
			HuffTree x = new HuffTree(tree.get(i).value() , tree.get(i).weight());
			Hheap.add(x);
		}

		HuffTree tmp1, tmp2, tmp3 = null;

		while (Hheap.size() > 1) { // While two items left

			if (Hheap.size() == 1) {
				return Hheap.poll();
			}

			tmp1 = Hheap.poll();
			tmp2 = Hheap.poll();
			tmp3 = new HuffTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
			
			Hheap.add(tmp3);
		}
		return tmp3;
	}

	// THis method takes a file and a HuffTree and encode the file.
	// and output a string of 1's and 0's representing the file.
	@Override
	public String encodeFile(File inputFile, HuffTree huffTree) throws FileNotFoundException {
		
		//Ref: Caleb

		String[] readLine = null;
		char charToRead;
		String code = "";
		TreeMap<Character, String> encodeMap = new TreeMap<Character, String>();
		try {
			readLine = traverseHuffmanTree(huffTree).split("\\r?\\n");

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int x = 1;
		while (x < readLine.length) {

			String length = readLine[x];

			charToRead = readLine[x].charAt(0);

			for (int y = 2; y < length.length(); ++y) {
				code += readLine[x].charAt(y);
			}

			encodeMap.put(charToRead, code);
			code = "";
			++x;
		}

		String encodedString = "";
		Scanner scanner = null;
		char[] chars = null;
		try {
			scanner = new Scanner(inputFile, "utf-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (scanner.hasNext()) {
			chars = scanner.nextLine().toCharArray();

		}
		// chars is now a character array of the input file and all you have to
		// do is match the leaf to the code and

		for (int z = 0; z < chars.length; ++z) {
			encodedString += encodeMap.get(chars[z]);

		}

		return encodedString.replaceAll("\\s","");

	}

	// This method takes a code (String) and search the HuffTree to find the word
	// associated with the code,
	// and output the decoded words
	
	@Override
	public String decodeFile(String code, HuffTree huffTree) throws Exception {
		
		//Ref: Caleb
		
		String decodedString = "";
		StringBuilder tempString = new StringBuilder();
		char p;
		HuffBaseNode rootNode = (HuffBaseNode) huffTree.root();
		HuffBaseNode currentNode = (HuffBaseNode) rootNode;

		for (int i = 0; i < code.length(); i++) {

			if (currentNode.isLeaf()) {
				HuffBaseNode node = (HuffBaseNode) currentNode;
				HuffLeafNode nodeY = (HuffLeafNode) node;
				p = nodeY.value();
				currentNode = (HuffInternalNode) rootNode;
				tempString.append(p);
			}

			if (code.charAt(i) == '1') {
				HuffInternalNode nodeZ = (HuffInternalNode) currentNode;
				currentNode = nodeZ.right();
			}

			if (code.charAt(i) == '0') {
				HuffInternalNode nodeX = (HuffInternalNode) currentNode;

				currentNode = nodeX.left();
			}

		}

		HuffBaseNode curNode = (HuffBaseNode) currentNode;
		HuffLeafNode leafNode = (HuffLeafNode) curNode;
		p = leafNode.value();
		currentNode = (HuffInternalNode) rootNode;
		tempString.append(p);

		decodedString = tempString.toString();

		return decodedString;

	}

	// This method traverses the HuffmanTree and print all the characters in the
	// tree and their codes
	@Override
	public String traverseHuffmanTree(HuffTree huffTree) throws Exception {

		String nodePath = "";

		HuffBaseNode treeRoot = (HuffBaseNode)huffTree.root();

		if (treeRoot.isLeaf()) {
			HuffLeafNode newLeafNode = (HuffLeafNode) treeRoot;
			nodePath = "" + newLeafNode.value();
		}

		else {
			HuffInternalNode newInternalNode = (HuffInternalNode) treeRoot;
			nodePath = treeTraverse(newInternalNode, nodePath);
		}

		String[] current = nodePath.split("\\r?\\n");

		List<String> charString = Arrays.asList(current);
		Collections.sort(charString);

		int i = 0;
		nodePath = "";
		while (i < charString.size()) {
			nodePath += charString.get(i) + "\n";

			i++;
		}

		return nodePath;
	}

	public static String treeTraverse(HuffBaseNode heapNode, String charCode) {

		String nodePath = "";
		nodePath += "\n";

		if (heapNode.isLeaf()) {
			HuffLeafNode nodex = (HuffLeafNode) heapNode;
			nodePath = nodePath.concat(nodex.value() + " " + charCode);


		}

		else {
			HuffInternalNode nodey = (HuffInternalNode) heapNode;

			String leftNodeChild;
			String rightNodeChild;

			leftNodeChild = treeTraverse(nodey.left(), charCode + "0");
			rightNodeChild = treeTraverse(nodey.right(), charCode + "1");

			nodePath = leftNodeChild + "" + rightNodeChild;

		}
		return nodePath;
	}

}
