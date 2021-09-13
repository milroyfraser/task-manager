class TaskManager(
    private val processManager: ProcessManager,
    private val maxProcessorsLimit: Int
) {
    var processes: MutableList<Process> = mutableListOf()

    fun add(process: Process) {
        if ( ! canAcceptNewProcess()) {
            val processToKill = processManager.getProcessToKill(processes, process)

            if (processToKill != null) {
                kill(processToKill)
            }
        }

        if ( ! canAcceptNewProcess()) {
            throw MaxProcessorsLimitExceeded(process)
        }

        processes.add(process)
    }

    fun kill(process: Process) {
        process.kill()
        processes.remove(process)
    }

    fun killGroup(processes: List<Process>) {
        for (i in processes.indices.reversed()) {
            kill(processes[i])
        }
    }

    fun killAll() {
        killGroup(processes)
    }

    fun list(field: SortField = SortField.CREATED_AT): List<Process> {
        if (field == SortField.CREATED_AT) {
            return processes.sortedBy { it.createdAt }
        } else if (field == SortField.PRIORITY) {
            return processes.sortedBy { it.priority.value }
        }

        return processes.sortedBy { it.pid }
    }

    private fun canAcceptNewProcess(): Boolean {
        return processes.count() < maxProcessorsLimit
    }
}