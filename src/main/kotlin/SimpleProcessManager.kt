class SimpleProcessManager : ProcessManager {
    override fun getProcessToKill(processes: List<Process>, newProcess: Process): Process? {
        return null
    }
}