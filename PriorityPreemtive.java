  int pid;
    int at;
    int bt;
    int remainingTime;
    int priority;
    int completionTime;
    int waitingTime;
    int turnAroundTime;

    public Process2(int id, int at,int bt,int p) {
        this.processID = id;
        this.arrivalTime = at;
        this.burstTime = bt;
        this.remainingTime = bt;
        this.priority = priority;
    }
}

public class PriorityPreemtive {
    public static int getRandom() {
        return (int) Math.floor(Math.random() 25);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int n = sc.nextInt();

        Process2[] processes = new Process2[n];

        for (int i = 0; i < n; i++) {
            int at= getRandom();
            int bt= getRandom();
            int priority = getRandom(); // Higher number means lower priority
            processes[i] = new Process2(i + 1, at,bt, priority);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int completed = 0;
        int tWT = 0, tTAT = 0;
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

                if (prevProcess != currentProcess.pid) {
                    ganttProcess.add(currentProcess.pid);
                    ganttTimes.add(currentTime);
                    prevProcess = currentProcess.pid;
                }

                currentProcess.remainingTime--;
                currentTime++;

                if (currentProcess.remainingTime == 0) {
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.at;
                    currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.bt;

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

       
        System.out.println("|\tP\t|\tBT\t|\tAT\t|\tPr\t|\tCT\t|\tWT\t|\tTAT\t|");
        for (Process2 process : processes) {
            System.out.println("|\tP" + process.pid + "\t|\t" + process.bt + "\t|\t" +
                    process.at + "\t|\t" + process.priority + "\t|\t" +
                    process.completionTime + "\t|\t" + process.waitingTime + "\t|\t" +
                    process.turnAroundTime + "\t|");
        }



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