class PriorityBasedProcessManager : ProcessManager {
    override fun getProcessToKill(processes: List<Process>, newProcess: Process): Process? {
        val lowPriorityProcessors = processes
            .sortedBy { it.createdAt }
            .filter { it.priority.value < newProcess.priority.value }

        if (lowPriorityProcessors.isEmpty()) {
            return null
        }

        return lowPriorityProcessors.first()
    }
}