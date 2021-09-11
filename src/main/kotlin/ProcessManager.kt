interface ProcessManager {
    fun getProcessToKill(processes: List<Process>, newProcess: Process): Process?
}