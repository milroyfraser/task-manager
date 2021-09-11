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

    fun list(): List<Process> {
        return processes
    }

    private fun canAcceptNewProcess(): Boolean {
        return processes.count() < maxProcessorsLimit
    }
}