class FIFOProcessManager : ProcessManager {
    override fun getProcessToKill(processes: List<Process>, newProcess: Process): Process? {
        if (processes.isEmpty()) {
            return null
        }

        return processes.minByOrNull { it.createdAt }
    }
}
