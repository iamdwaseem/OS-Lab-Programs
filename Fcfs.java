import java.util.*;

class Process {
    int pid;            // Process ID
    int arrivalTime;    // Arrival Time
    int burstTime;      // Burst Time

    // Constructor to initialize process details
    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
}

public class Fcfs {

    public static void calculateFCFS(List<Process> processes) {
        // Sort processes by arrival time, and by PID for tie-breaking
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                .thenComparingInt(p -> p.pid));  // Tie-break by PID

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        // Print the header for the output table
        System.out.println("+-------+---------------+-----------+----------------+--------------+----------------+");
        System.out.println("|  PID  | Arrival Time  | Burst Time| Completion Time| Waiting Time | Turnaround Time|");
        System.out.println("+-------+---------------+-----------+----------------+--------------+----------------+");

        // Prepare to display the Gantt chart
        StringBuilder ganttChart = new StringBuilder();
        StringBuilder timeline = new StringBuilder();
        ganttChart.append("|");
        timeline.append("0");

        // Loop through the sorted list of processes
        for (Process p : processes) {
            // If the current time is less than the arrival time, update it to the arrival time
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }

            // Calculate completion time, waiting time, and turnaround time
            int completionTime = currentTime + p.burstTime;
            int waitingTime = currentTime - p.arrivalTime;
            int turnAroundTime = waitingTime + p.burstTime;

            // Accumulate total waiting and turnaround times
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnAroundTime;

            // Output the results for each process in a formatted way
            System.out.printf("| %-5d | %-13d | %-9d | %-14d | %-12d | %-14d |\n",
                    p.pid, p.arrivalTime, p.burstTime, completionTime, waitingTime, turnAroundTime);

            // Build the Gantt chart part for the current process
            ganttChart.append(" P" + p.pid + " |");
            timeline.append("-----" + completionTime);

            // Update current time after the process completes
            currentTime = completionTime;
        }

        // Print the footer for the output table
        System.out.println("+-------+---------------+-----------+----------------+--------------+----------------+");

        // Calculate averages
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        // Print average waiting time and turnaround time
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);

        // Print the Gantt chart
        System.out.println("\nGantt Chart:");
        System.out.println(ganttChart);
        System.out.println(timeline);
    }

    public static void main(String[] args) {
        // Example processes: some with same arrival time
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 5, 3));  // PID=1, Arrival Time=5, Burst Time=3
        processes.add(new Process(2, 2, 6));  // PID=2, Arrival Time=2, Burst Time=6
        processes.add(new Process(3, 3, 9));  // PID=3, Arrival Time=1, Burst Time=4
        processes.add(new Process(4, 1, 7));  // PID=4, Arrival Time=2, Burst Time=5

        // Call FCFS to calculate and display results
        calculateFCFS(processes);
    }
}
