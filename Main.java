
//public class Main {

//}
import java.util.Arrays;
//public static void main2(String[] args) {



public class Main {
	
	private int [] board;
	public static Board b2, bp2;
	// public static Tree boardTree = new Tree();
	private static int [] Hamming = new int[4]; // Hamming distance of the neighbors
	private static int [][] nbr  = new int [4][9]; // Define up to 4 neighbors
	private static int nonbr; // number of neighbors: should be 2, 3, or 4.tic
	public static int gidx; // goal idx
	private static int[] start1 = {3, 1, 2, 7, 0, 4, 5, 8, 6};
	public static int [] start2 = {0, 3, 1, 2, 7, 4, 5, 8, 6};
	public static int [] start3 = {1, 2, 3, 4, 5, 6, 7, 8, 0};  // Using goal as start
	public static int [] start4 = {0, 1, 3, 4, 2, 5, 7, 8, 6};  // Stanford COS 226 example
	// Should arrive goal in 4 steps. if not, should not be more than 100 steps
	// public static int [] start  = {1, 2, 3, 4, 5, 6, 7, 0, 8}; // this state is only one step from the goal
	public static int [] start = {1, 2, 3, 4, 5, 6, 7, 8, 0};  
	//public static int [] goal = {2, 8, 3, 1, 6, 0, 7, 5, 4}; 
	public static int [] goal = {1, 4, 8, 2, 5, 7, 3, 6, 0}; 
	private static int SIZE = 1000; // define up to 1000 boards, number changeable later
	private static int bidx; // the number for blank
	private static boolean done = false;
	private static int SLIMIT = 200; // SLIMIT <= SIZE. The program will display when the number of steps 
	                          // reaches SLIMIT and then stop.
	private int ELIMIT = 500; // SLIMIT <= ELIMIT <= SIZE. The program will display when the number
	                          // of states expanded reaches ELIMIT and then stop
	// Basically when the program reaches SLIMIT or ELIMIT and it does NOT reach goal, then
	// probably the algorithm is not efficient. These limits will change when we change to 
	// 15 puzzle, 24 puzzle etc.	
	
	// 1. Design Issues: Initial design issues: how to represent the 3x3 board (8 puzzle) programmatically 
	// Every tile 1, 2, , through 8 can be represented either as a one character string 
	// like "1" or a character '1'; or simply a number like 1.
	
	// Next, 3x3 board can be a two dimensional array of size 3x3 or a one dimensional way
	// of size 9. 
	
	// 2. Print: Whether this is represented as string, character, or integer; and two dimensional 3x3
	// or 1 dimension of size 9; we always need to have the ability to print that in the 3x3 
	// format
	
	//   a  b  c
	//   d  e  f
	//   g  h  i
	
	//  3. Compare: Also, we need to be able to compare two boards (say an intermediate node board and the goal
	//  to see if we have done.
	
	// 4. Calculate the heuristic functions h (n) based on the board. The functions can be
	// 4.1 Hamming: the number of tiles different from the goal state
	// 4.2 Manhattan: the number of moves needed to move tiles from this state to reach
	//     goal state
	// 4.3 Sum of permutation inversions 
	// 5. Expand at a node:
	
	// A node has 2 to 4 neighbors depending on how the blank (hole) tile moves.
	// We can calculate heuristic function value (based on which heuristic function is picked).
	// One of these 2 to 4 neighbors had been "visited" before unless we are at the initial state
	
	// We need to mark that nodes are "visited" so as NOT to go in circles.
	
	// When expanding at a node, we pick a node with the least h(n) value. It is possible that 
	// there may be 2 or 3 nodes with minimum h(n) value and we have to pick one of them
	// (which may be the right or not choice). It is helpful to display all the neighboring nodes 
	// with the h function values (except the parent node).
	
	// See charts 9, 10, and 11 of D-heuristic 
	// parent node leading to this node) with the understanding 

	public static void main (String[] args) {
		// TODO Auto-generated method stub

	  Board root = new Board (start);
	  System.out.println("Starting at board: ");
	
				  
    Board br;
	 Tree boardTree = new Tree ();
	  root.setidx (0);  // set root idx
	  root.setExpandable (true);
	  root.print();
	  boardTree.setBTA (root, 0);
	  boardTree.setTrLength(1);
	  
	  System.out.println();
	  
	  for (int i = 0; !done; i++)
	  {
		  System.out.println();
		  System.out.println("Step " + i);
		  Board [] offspring;
		  int offcount = 0;
	//	  System.out.println ("\n\nIndex " + i + " of BoardTree");
		  br = boardTree.getBTA()[i];	
		  br.setidx(i);
		  //br.print();
		  System.out.println();
		  
	//	  System.out.println("\nFind this board's parent");
		  Board pr; 
		
		  pr = br.getParent();
		 // if (pr!= null)
		 // { System.out.print("Parent:\n"); pr.print(); System.out.println();}
		  
		  if (br == null)
		  {
			  System.out.println("Null pointer for board, error!");
			  System.exit(1);
		  }
		
		  if (br.getExpandable()) {  // Expand expandable node
			  offspring = br.findNbr();
			  if(br.getGoal())  // If any of the child is goal, then set found goal true and done.			  
				  done = true;
			  // Add offspring to boardTree's BTA
			  // Print what is in offspring
	//		  System.out.println("size of offspring, up to: " + offspring.length);
			  for (int j = 0 ; j < offspring.length; j ++)
				  if (offspring[j] != null)
				  {
					 
					  // Save this node in the tree ,whether to be expanded or NOT.
					  int index = boardTree.getTrlength()+j;
					  boardTree.setBTA(offspring[j],index);  // This line must be debugged
					  
					  offspring[j].setidx(index);
			//		  System.out.println("Printing offspring with index: " + j);
					  offspring[j].print();
	
					 // idx of offspring[j] is boardTree.getTrlength()+j
					 offcount ++;
					 
					 if (done) // find the idx of the goal which is one of offspring[j]
					 {
						 if (Arrays.equals(offspring[j].getboard(),goal)) // 
						 {
							 gidx = offspring[j].getidx();
				//			 System.out.println("Index of goal in BTA is " + gidx);
						 }					
								 
					 }
				  }
			  // end of for loop
			  boardTree.setTrLength(boardTree.getTrlength() + offcount);
		  }  // end of if br.expandable 
		  else {
	//		  System.out.println("This board not expandable, it is not expanded");
		//	  br.print();
		  }  // 
	
		  Board bt; // board from the tree
		  
		  // Print board tree for debugging
		  int count = 0;
	//	  System.out.println("\nPrint BTA if board is expandable");
		  if (br.getExpandable()) {
			  for (int k = 0; k < 1000 ; k ++)
			  {
				  bt = boardTree.getBTA()[k];
				  if (bt!=null)
				  {
		//			  System.out.println("Next board at index: " + k);
			//		  bt.print();
					  count++;
				  }
				  else
					  break;
			  }
	//		  System.out.println ("Tree nodes count: " + count);
		  }
	  }  // end of for loop		
	  
	  // After for loop, boardTree with the array is built. We can print the path from start to goal
	  // or we can print the whole tree from start to goal including those nodes not expanded.
	  int trlength = boardTree.getTrlength();
	//  System.out.println("Total number of nodes in the tree is " + trlength);
	  
	  int [] pathidx = new int[100]; // Create the path from start to goal
	  Board b; // Board from the tree
	  
	  // Print from goal backward to start
//	  System.out.println("Get the nodes backward");
	  for (int i = 0; i < trlength; i ++)		
	  {
		  b= boardTree.getBTA()[i];
	//	  b.print();
	  }
	  
	  // Generate the array from goal back to start (because not every tree node will lead to the goal)
	  int [] pathrev = new int[100];
	  int acount = 0;
	  int aidx; // array entry index
	  
	  pathrev[0] = gidx;
	//  System.out.println("\n\nStart from the goal back");
	  b2 = boardTree.getBTA()[gidx]; // Get the board for goal
	//  b2.print();
	  aidx = gidx;
	  acount ++;
	
	  Board b3;
	  while (aidx != 0) { // Not reaching start node yet {
		  b2 = b2.getParent(); // get the parent of b2, call that b2 also
	//	  b2.print();
		  pathrev[acount]= b2.getidx();
		  aidx = pathrev[acount];
		  acount++;
	
    }		
	 //  System.out.println("Trace backward from goal to start");
	  int [] path = new int [acount];
	  for (int k = 0; k < acount ; k++)
		  path [k] = pathrev[acount - 1 - k];
	  
	  System.out.println("\n\nPrint from start to goal");
	  for (int k = 0; k < acount; k ++) {
		  b2 = boardTree.getBTA()[path[k]];
		  System.out.println();
		  System.out.println("Step " + (k));
		  b2.print();
	  }
	  
	  int y = 7; // A place to stop
  } // end of main		

} // end of class Solve8Puzzle
