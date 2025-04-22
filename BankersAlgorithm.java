import java.util.*;
public class BankersAlgorithm {
public static void main(String[] args) { 
Scanner sc = new Scanner(System.in);
// Input number of processes and resources 
System.out.print("Enter number of processes: "); 
int n = sc.nextInt();
System.out.print("Enter number of resources: ");
 int m = sc.nextInt();
int[][] allocation = new int[n][m];
 int[][] max = new int[n][m];
int[][] need = new int[n][m]; 
int[] totalInstances = new int[m];
 int[] available = new int[m];
// Input total instances of each resource
 System.out.println("\nEnter total instances of each resource:"); 
for (int j = 0; j < m; j++) {
System.out.print("Resource " + j + ": ");
 totalInstances[j] = sc.nextInt();
}
// Input Allocation Matrix

System.out.println("\nEnter Allocation Matrix (process-wise):");
 for (int i = 0; i < n; i++) {
System.out.print("Process " + i + ": "); 
for (int j = 0; j < m; j++) {
allocation[i][j] = sc.nextInt();
}
}
// Input Max Matrix and compute Need Matrix
System.out.println("\nEnter Max Need Matrix (process-wise):"); 
for (int i = 0; i < n; i++) {
System.out.print("Process " + i + ": "); 
for (int j = 0; j < m; j++) {
max[i][j] = sc.nextInt();
need[i][j] = max[i][j] - allocation[i][j];
}
}
// Calculate Available = Total - Sum of Allocated 
for (int j = 0; j < m; j++) {
int allocatedSum = 0; 
for (int i = 0; i < n; i++) {
allocatedSum += allocation[i][j];
}
available[j] = totalInstances[j] - allocatedSum;
}
// Display Need Matrix 
System.out.println("\nNeed Matrix:");
 for (int i = 0; i < n; i++) {
System.out.print("Process " + i + ": ");
 for (int j = 0; j < m; j++) {
System.out.print(need[i][j] + " ");
}
System.out.println();
}
// Safety Algorithm
boolean[] finished = new boolean[n]; 
int[] work = Arrays.copyOf(available, m);
List<Integer> safeSequence = new ArrayList<>();
boolean found;
System.out.println("\nAvailable Resources at start: " + Arrays.toString(available));
while (safeSequence.size() < n) { found = false;
for (int i = 0; i < n; i++) {
 if (!finished[i]) {
int j;
for (j = 0; j < m; j++) {
if (need[i][j] > work[j]) break;
}
if (j == m) {
// Process can be safely executed System.out.print("Process P" + i + " has finished. "); System.out.print("Available after releasing: [");
for (int k = 0; k < m; k++) {
 work[k] += allocation[i][k]; 
System.out.print(work[k]);
if (k != m - 1) System.out.print(", ");
}
System.out.println("]");
 safeSequence.add(i); 
finished[i] = true;
 found = true;
}
}
}
if (!found) break;
}
// Final Safe Sequence Output
if (safeSequence.size() == n) {
System.out.print("\nSystem is in SAFE state.\nSafe Sequence: "); 
for (int i = 0; i < n; i++) {
System.out.print("P" + safeSequence.get(i));
 if (i != n - 1) System.out.print(" -> ");
}
System.out.println();
} else {
System.out.println("\nSystem is NOT in a safe state.");
}
sc.close();
}
}
