import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
public class DiskSchedulingSimulator extends JPanel {
private final List<Integer> requests;
private final int headStart;
public DiskSchedulingSimulator(List<Integer> requests, int headStart) {
this.requests = requests;
this.headStart = headStart;
}
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
Graphics2D g2 = (Graphics2D) g;
g2.setStroke(new BasicStroke(2));
g2.setColor(Color.BLACK);
int startX = 50, startY = 50, spacing = 50;
int previousX = startX + headStart * 5;
int previousY = startY + 50;
// Draw baseline with spaced intervals
for (int i = 0; i <= 200; i += 20) {
int x = startX + i * 5;
g2.drawLine(x, startY, x, startY + 10);
g2.drawString(String.valueOf(i), x - 5, startY - 15);
}
g2.drawLine(startX, startY, startX + 1000, startY);
// Draw the initial head position
g2.setColor(Color.RED);
g2.fillOval(previousX, previousY, 10, 10);
g2.drawString("Start", previousX - 10, previousY - 10);
// Draw path for each request
g2.setColor(Color.GREEN);
for (int i = 0; i < requests.size(); i++) {
int x = startX + requests.get(i) * 5;
int y = startY + (i + 1) * 30;
g2.fillOval(x, y, 10, 10);
g2.drawString(String.valueOf(requests.get(i)), x - 5, startY - 10);
g2.drawLine(previousX + 5, previousY + 5, x + 5, y + 5);
previousX = x;
previousY = y;
}
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
// Generate random disk requests and head position
System.out.print("Enter number of disk requests: ");
int n = sc.nextInt();
List<Integer> requests = new ArrayList<>();
Random rand = new Random();
for (int i = 0; i < n; i++) {
requests.add(rand.nextInt(200)); // Random disk requests between 0 and 199
}
System.out.println("Enter initial Head Position");
int head = sc.nextInt();
System.out.println("Initial head position: " + head);
System.out.println("Disk requests: " + requests);
System.out.println("Choose Disk Scheduling Algorithm:");
System.out.println("1. FCFS (First Come First Serve)");
System.out.println("2. SSTF (Shortest Seek Time First)");
System.out.println("3. SCAN (Elevator Algorithm)");
int choice = sc.nextInt();
int totalMoves = 0;
if (choice == 2) {
requests = sstf(requests, head);
} else if (choice == 3) {
int diskSize = 200;
System.out.print("For SCAN algorithm, enter direction (0 for left first, 1 for
right first): ");
int direction = sc.nextInt();
requests = scan(requests, head, diskSize, direction);
}
// Output disk head moves to console
System.out.println("Disk head movements:");
int prevHead = head;
for (int req : requests) {
int move = Math.abs(req - prevHead);
System.out.println("Moved from " + prevHead + " to " + req + " (" + move +
" cylinders)");
totalMoves += move;
prevHead = req;
}
System.out.println("Total cylinder movements: " + totalMoves);
sc.close();
// Create the Swing frame to visualize the movements
JFrame frame = new JFrame("Disk Scheduling Simulator");
frame.add(new DiskSchedulingSimulator(requests, head));
frame.setSize(800, 400);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setVisible(true);
}
private static List<Integer> sstf(List<Integer> requests, int head) {
List<Integer> order = new ArrayList<>();
boolean[] visited = new boolean[requests.size()];
for (int i = 0; i < requests.size(); i++) {
int minDist = Integer.MAX_VALUE, index = -1;
for (int j = 0; j < requests.size(); j++) {
if (!visited[j] && Math.abs(requests.get(j) - head) < minDist) {
minDist = Math.abs(requests.get(j) - head);
index = j;
}
}
visited[index] = true;
order.add(requests.get(index));
head = requests.get(index);
}
return order;
}
private static List<Integer> scan(List<Integer> requests, int head, int diskSize, int
direction) {
// Create a new list including the head and one of the disk endpoints based on
direction
List<Integer> order = new ArrayList<>(requests);
order.add(head);
order.add(direction == 1 ? diskSize - 1 : 0);
Collections.sort(order);
// followed by the ones less than head in reverse order.
if (direction == 1) {
List<Integer> right = new ArrayList<>();
List<Integer> left = new ArrayList<>();
for (int value : order) {
if (value >= head)
right.add(value);
else
left.add(value);
}
Collections.reverse(left);
right.addAll(left);
return right;
} else { // if left first, then reverse: left part then right part
List<Integer> left = new ArrayList<>();
List<Integer> right = new ArrayList<>();
for (int value : order) {
if (value <= head)
left.add(value);
else
right.add(value);
}
Collections.reverse(left);
left.addAll(right);
return left;
}
}
}
