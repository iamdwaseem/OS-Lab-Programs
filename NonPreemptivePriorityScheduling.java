import java.util.*;

class ProcessNP {
    int processID;
    int arrivalTime;
    int burstTime;
    int priority;
    int completionTime;
    int waitingTime;
    int turnAroundTime;

    public ProcessNP(int id, int at,int bt,int p) {
        this.processID = id;
        this.arrivalTime = at;
        this.burstTime = bt;
        this.priority = p;
    }
}

public class NonPreemptivePriorityScheduling {
    public static int getRandom() {
        return (int) Math.floor(Math.random() * 25);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int n = scanner.nextInt();

        ProcessNP[] processes = new ProcessNP[n];

        for (int i = 0; i < n; i++) {
            int arrivalTime = getRandom();
            int burstTime = getRandom();
            int priority = getRandom();
            processes[i] = new ProcessNP(i + 1, arrivalTime, burstTime, priority);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0, totalTurnAroundTime = 0;

        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttTimes = new ArrayList<>();

        boolean[] completed = new boolean[n];
        int completedCount = 0;

        while (completedCount < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!completed[i] && processes[i].arrivalTime <= currentTime && processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    idx = i;
                }
            }

            if (idx != -1) {
                ProcessNP currentProcess = processes[idx];
                ganttProcess.add(currentProcess.processID);
                ganttTimes.add(currentTime);

                currentTime += currentProcess.burstTime;
                currentProcess.completionTime = currentTime;
                currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;

                totalWaitingTime += currentProcess.waitingTime;
                totalTurnAroundTime += currentProcess.turnAroundTime;

                completed[idx] = true;
                completedCount++;
            } else {
                currentTime++;
            }
        }

        ganttTimes.add(currentTime);

        System.out.println("+-------------------------------------------------------+");
        System.out.println("|\tP\t|\tBT\t|\tAT\t|\tPr\t|\tCT\t|\tWT\t|\tTAT\t|");
        System.out.println("+-------------------------------------------------------+");
        for (ProcessNP process : processes) {
            System.out.println("|\tP" + process.processID + "\t|\t" + process.burstTime + "\t|\t" +
                    process.arrivalTime + "\t|\t" + process.priority + "\t|\t" +
                    process.completionTime + "\t|\t" + process.waitingTime + "\t|\t" +
                    process.turnAroundTime + "\t|");
        }
        System.out.println("+-------------------------------------------------------+");

        System.out.println("\nAverage Waiting Time: " + (float) totalWaitingTime / n);
        System.out.println("Average Turnaround Time: " + (float) totalTurnAroundTime / n);

        System.out.println("\nGantt Chart:");
        System.out.print("+");
        for(int i = 0; i < ganttProcess.size(); i++){
            System.out.print("--------");
        }
        System.out.print("+");
        System.out.print("\n|");
        for (int i = 0; i < ganttProcess.size(); i++) {
            System.out.print("\tP" + ganttProcess.get(i) + "\t|");
        }
        System.out.println();
        System.out.print("+");
        for(int i = 0; i < ganttProcess.size(); i++){
            System.out.print("--------");
        }
        System.out.print("+");

        System.out.print("\n" + ganttTimes.get(0));
        for (int i = 1; i < ganttTimes.size(); i++) {
            System.out.print("\t\t" + ganttTimes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}

import java.util.*;

class Process2 {
    int processID;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int priority;
    int completionTime;
    int waitingTime;
    int turnAroundTime;

    public Process2(int id, int arrivalTime, int burstTime, int priority) {
        this.processID = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
    }
}

public class PriorityPreemtive {
    public static int getRandom() {
        return (int) Math.floor(Math.random() * 24) + 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int n = scanner.nextInt();

        Process2[] processes = new Process2[n];

        for (int i = 0; i < n; i++) {
            int arrivalTime = getRandom();
            int burstTime = getRandom();
            int priority = getRandom(); // Higher number means lower priority
            processes[i] = new Process2(i + 1, arrivalTime, burstTime, priority);
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
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && processes[i].remainingTime > 0 &&
                        processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    idx = i;
                }
            }

            if (idx != -1) {
                Process2 currentProcess = processes[idx];

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

                    completed++;
                }
            } else {
                if (ganttProcess.isEmpty() || ganttProcess.get(ganttProcess.size() - 1) != 0) {
                    ganttProcess.add(0);
                    ganttTimes.add(currentTime);
                }
                currentTime++;
            }
        }

        ganttTimes.add(currentTime);

        System.out.println("+-------------------------------------------------------+");
        System.out.println("|\tP\t|\tBT\t|\tAT\t|\tPr\t|\tCT\t|\tWT\t|\tTAT\t|");
        System.out.println("+-------------------------------------------------------+");
        for (Process2 process : processes) {
            System.out.println("|\tP" + process.processID + "\t|\t" + process.burstTime + "\t|\t" +
                    process.arrivalTime + "\t|\t" + process.priority + "\t|\t" +
                    process.completionTime + "\t|\t" + process.waitingTime + "\t|\t" +
                    process.turnAroundTime + "\t|");
        }
        System.out.println("+-------------------------------------------------------+");

        System.out.println("\nAverage Waiting Time: " + (float) totalWaitingTime / n);
        System.out.println("Average Turnaround Time: " + (float) totalTurnAroundTime / n);

        System.out.println("\nGantt Chart:");
        System.out.print("+");
        for(int i = 0; i < ganttProcess.size(); i++){
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
        for(int i = 0; i < ganttProcess.size(); i++){
            System.out.print("--------");
        }
        System.out.print("+");

        System.out.print("\n" + ganttTimes.get(0));
        for (int i = 1; i < ganttTimes.size(); i++) {
            System.out.print("\t\t" + ganttTimes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}