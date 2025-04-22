import java.util.*;
class Block {
int totalSize; // Total size of the memory block
int freeSpace; // Remaining free space in the block
List<Integer> pIds; // List of process IDs allocated to this block
boolean isFree; // Availability status of the block (True if available)
Block(int size) {
this.totalSize = size;
this.freeSpace = size;
this.pIds = new ArrayList<>();
this.isFree = new Random().nextInt(3) != 0; // 1 in 3 chance for block to be unavailable
}
void allocate(int pId, int size) {
pIds.add(pId);
freeSpace -= size; // Decrease free space after allocation
}
boolean canFit(int size) {
return freeSpace >= size && isFree; // Check if the block can fit the process

}
boolean isEmpty() {
return pIds.isEmpty(); // Check if the block is empty
}
}
public class MemoryManagment {
private static List<Block> blocks = new ArrayList<>();
private static int pIdCounter = 1; // Process ID counter
private static Random rand = new Random();
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
// Step 1: Get the number of memory blocks
System.out.print("Enter number of blocks: ");
int numBlocks = sc.nextInt();
// Step 2: Create blocks with random sizes
for (int i = 0; i < numBlocks; i++) {
int size = rand.nextInt(150) + 50; // Random block size between 300 and 500
blocks.add(new Block(size));
}
System.out.println("\nInitial Memory Blocks:");
displayBlocks(); // Display memory blocks
while (true) {
System.out.println("\nMemory Allocation Strategies:");
System.out.println("1. First Fit");
System.out.println("2. Best Fit");
System.out.println("3. Worst Fit");
System.out.println("4. Display Blocks");
System.out.println("5. Exit");
System.out.print("Enter your choice: ");
int choice = sc.nextInt();
switch (choice) {
case 1:
allocateFirstFit(sc);
break;
case 2:
allocateBestFit(sc);
break;
case 3:
allocateWorstFit(sc);
break;
case 4:
displayBlocks();
break;
case 5:
System.out.println("Exiting...");
sc.close();
return;
default:
System.out.println("Invalid choice. Try again.");
}
}
}
// First Fit Allocation
private static void allocateFirstFit(Scanner sc) {
System.out.print("Enter number of processes: ");
int numProcesses = sc.nextInt();
for (int i = 0; i < numProcesses; i++) {
int pSize = rand.nextInt(101) + 50; // Random process size between 50 and 150
System.out.println("Allocating Process " + pIdCounter + " of size " + pSize);
boolean allocated = false;
for (Block b : blocks) {
if (b.canFit(pSize)) {
b.allocate(pIdCounter++, pSize);
allocated = true;
System.out.println("Process " + (pIdCounter - 1) + " allocated.");
break;
}
}
if (!allocated) {
System.out.println("No suitable block for Process " + (pIdCounter - 1));
}
}
displayBlocks(); // Show updated blocks
}
// Best Fit Allocation
private static void allocateBestFit(Scanner sc) {
System.out.print("Enter number of processes: ");
int numProcesses = sc.nextInt();
for (int i = 0; i < numProcesses; i++) {
int pSize = rand.nextInt(101) + 50; // Random process size between 50 and 150
System.out.println("Allocating Process " + pIdCounter + " of size " + pSize);
Block bestBlock = null;
for (Block b : blocks) {
if (b.canFit(pSize)) {
if (bestBlock == null || b.freeSpace < bestBlock.freeSpace) {

bestBlock = b;
}
}
}
if (bestBlock != null) {
bestBlock.allocate(pIdCounter++, pSize);
System.out.println("Process " + (pIdCounter - 1) + " allocated.");
} else {
System.out.println("No suitable block for Process " + (pIdCounter - 1));
}
}
displayBlocks(); // Show updated blocks
}
// Worst Fit Allocation
private static void allocateWorstFit(Scanner sc) {
System.out.print("Enter number of processes: ");
int numProcesses = sc.nextInt();
for (int i = 0; i < numProcesses; i++) {
int pSize = rand.nextInt(101) + 50; // Random process size between 50 and 150
System.out.println("Allocating Process " + pIdCounter + " of size " + pSize);
Block worstBlock = null;
for (Block b : blocks) {
if (b.canFit(pSize)) {
if (worstBlock == null || b.freeSpace > worstBlock.freeSpace) {
worstBlock = b;
}
}
}
if (worstBlock != null) {
worstBlock.allocate(pIdCounter++, pSize);
System.out.println("Process " + (pIdCounter - 1) + " allocated.");
} else {
System.out.println("No suitable block for Process " + (pIdCounter));
}
}
displayBlocks(); // Show updated blocks
}
// Display current memory blocks
private static void displayBlocks() {
System.out.println("\nMemory Blocks (With Status):");
for (Block b : blocks) {
System.out.print("| ");
if (b.isEmpty()) {
System.out.print("Free ");
} else {
for (int pId : b.pIds) {
System.out.print("P" + pId + " ");
}
}
System.out.print("(Total: " + b.totalSize + ", Free: " + b.freeSpace + ", Available: " + b.isFree + ") | ");
}
System.out.println();
// Calculate total free space
int totalFree = blocks.stream().mapToInt(block -> block.freeSpace).sum();
System.out.println("Total Free Space: " + totalFree);
}
}