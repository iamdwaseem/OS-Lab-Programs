class Process3 {
    int processID;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnAroundTime;

    public Process3(int id, int arrivalTime, int burstTime) {
        this.processID = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class SJF {
    public static int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int n = scanner.nextInt();

        Process3[] processes = new Process3[n];
        Set<Integer> usedArrivalTimes = new HashSet<>();

        System.out.println("Generated arrival and burst times for each process:");
        for (int i = 0; i < n; i++) {
            int arrivalTime;
            do {
                arrivalTime = getRandom(0, 10);
            } while (usedArrivalTimes.contains(arrivalTime));
            usedArrivalTimes.add(arrivalTime);

            int burstTime = getRandom(1, 10);
            processes[i] = new Process3(i + 1, arrivalTime, burstTime);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int completed = 0;
        int totalWaitingTime = 0, totalTurnAroundTime = 0;

        List<Integer> ganttProcess = new ArrayList<>();
        List<Integer> ganttStartTimes = new ArrayList<>();
        List<Integer> ganttEndTimes = new ArrayList<>();
        int idleStart = -1;

        while (completed < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && processes[i].remainingTime > 0 && processes[i].remainingTime < minBurst) {
                    minBurst = processes[i].remainingTime;
                    idx = i;
                }
            }

            if (idx != -1) {
                if (idleStart != -1) {
                    ganttProcess.add(-1);
                    ganttStartTimes.add(idleStart);
                    ganttEndTimes.add(currentTime);
                    idleStart = -1;
                }

                Process3 currentProcess = processes[idx];
                int start = currentTime;
                int end = currentTime + currentProcess.burstTime;

                ganttProcess.add(currentProcess.processID);
                ganttStartTimes.add(start);
                ganttEndTimes.add(end);

                currentTime = end;
                currentProcess.remainingTime = 0;
                currentProcess.completionTime = end;
                currentProcess.turnAroundTime = end - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;

                totalWaitingTime += currentProcess.waitingTime;
                totalTurnAroundTime += currentProcess.turnAroundTime;
                completed++;
            } else {
                if (idleStart == -1) {
                    idleStart = currentTime;
                }
                currentTime++;
            }
        }

        if (idleStart != -1) {
            ganttProcess.add(-1);
            ganttStartTimes.add(idleStart);
            ganttEndTimes.add(currentTime);
        }

        List<Integer> ganttTimes = new ArrayList<>();
        if (!ganttStartTimes.isEmpty()) {
            for (int start : ganttStartTimes) ganttTimes.add(start);
            ganttTimes.add(ganttEndTimes.get(ganttEndTimes.size() - 1));
        }

        System.out.println("+-----------------------------------------------+");
        System.out.println("|\tP\t|\tBT\t|\tAT\t|\tCT\t|\tWT\t|\tTAT\t|");
        System.out.println("+-----------------------------------------------+");
        for (Process3 p : processes) {
            System.out.printf("|\tP%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\n",
                    p.processID, p.burstTime, p.arrivalTime, p.completionTime, p.waitingTime, p.turnAroundTime);
        }
        System.out.println("+-----------------------------------------------+\n");

        System.out.printf("Average Waiting Time: %.2f\n", (float) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f\n\n", (float) totalTurnAroundTime / n);

        System.out.println("Gantt Chart:");
        if (ganttProcess.isEmpty()) {
            System.out.println("No processes scheduled.");
            return;
        }

        for (int i = 0; i < ganttProcess.size(); i++) {
            System.out.print("+-------");
        }
        System.out.println("+");

        System.out.print("|");
        for (int pid : ganttProcess) {
            if (pid == -1) System.out.print(" Idle  |");
            else System.out.printf("  P%-2d  |", pid);
        }
        System.out.println();

        for (int i = 0; i < ganttProcess.size(); i++) {
            System.out.print("+-------");
        }
        System.out.println("+");

        System.out.print(ganttTimes.get(0));
        for (int i = 1; i < ganttTimes.size(); i++) {
            System.out.printf("%7d", ganttTimes.get(i));
        }
        System.out.println();
    }
}
