package week2;
import java.util.*;
class Process1 {
    int processID;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnAroundTime;

    public Process1(int id, int arrivalTime, int burstTime) {
        this.processID = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class SRTF {
    public static int getRandom() {
        return (int) Math.floor(Math.random() * 24) + 1; // Ensures burstTime is never 0
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int n = scanner.nextInt();

        Process1[] processes = new Process1[n];

        for (int i = 0; i < n; i++) {
            int arrivalTime = getRandom();
            int burstTime = getRandom();
            processes[i] = new Process1(i + 1, arrivalTime, burstTime);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int completed = 0;
        int totalWaitingTime = 0, totalTurnAroundTime = 0;
        int prevProcess = -1;

        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttTimes = new ArrayList<>();

        while (completed < n) {
            int idx = -1;
            int minBurstTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && processes[i].remainingTime > 0 &&
                        processes[i].remainingTime < minBurstTime) {
                    minBurstTime = processes[i].remainingTime;
                    idx = i;
                }
            }

            if (idx != -1) {
                Process1 currentProcess = processes[idx];

                if (prevProcess != currentProcess.processID) {
                    ganttProcess.add(currentProcess.processID);
                    ganttTimes.add(currentTime);
                    prevProcess = currentProcess.processID;
                }

                currentProcess.remainingTime--;
                currentTime++;

                if (currentProcess.remainingTime == 0) {
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;

                    totalWaitingTime += currentProcess.waitingTime;
                    totalTurnAroundTime += currentProcess.turnAroundTime;

                    completed++; // FIXED: Increment completed processes count
                }
            } else {
                if (ganttProcess.isEmpty() || ganttProcess.get(ganttProcess.size() - 1) != 0) {
                    ganttProcess.add(0); // Add idle time only if not already recorded
                    ganttTimes.add(currentTime);
                }
                currentTime++;
            }
        }

        ganttTimes.add(currentTime);

        System.out.println("+-----------------------------------------------+");
        System.out.println("|\tP\t|\tBT\t|\tAT\t|\tCT\t|\tWT\t|\tTAT\t|");
        System.out.println("+-----------------------------------------------+");
        for (Process1 process : processes) {
            System.out.println("|\tP" + process.processID + "\t|\t" + process.burstTime + "\t|\t" +
                    process.arrivalTime + "\t|\t" + process.completionTime + "\t|\t" +
                    process.waitingTime + "\t|\t" + process.turnAroundTime + "\t|");
        }
        System.out.println("+-----------------------------------------------+");

        System.out.println("\nAverage Waiting Time: " + (float) totalWaitingTime / n);
        System.out.println("Average Turnaround Time: " + (float) totalTurnAroundTime / n);

        System.out.println("\nGantt Chart:");
        System.out.print("+");
        for(int i=0; i<ganttProcess.size(); i++){
            System.out.print("--------");
        }
        System.out.print("+");
        System.out.print("\n|");
        for (int i = 0; i < ganttProcess.size(); i++) {
            if (ganttProcess.get(i) == 0) {
                System.out.print(" IDLE |");
            } else {
                System.out.print("\tP" + ganttProcess.get(i) + "\t|");
            }
        }
        System.out.println();
        System.out.print("+");
        for(int i=0; i<ganttProcess.size(); i++){
            System.out.print("--------");
        }
        System.out.print("+");

        System.out.print("\n" + ganttTimes.get(0));
        for (int i = 1; i < ganttTimes.size(); i++) {
            System.out.print("\t\t" + ganttTimes.get(i));
        }
        System.out.println();

        scanner.close(); // Close scanner to prevent resource leaks
    }
}
