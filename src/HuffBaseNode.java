//Reference : OpenDSA Website

import java.util.*;

/** Huffman tree node implementation: Base class */
interface HuffBaseNode {
  boolean isLeaf(); 
  int weight();
}
