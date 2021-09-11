fun main() {
    val processorsLimit = 2
    val processManager = PriorityBasedProcessManager()
    val taskManager = TaskManager(processManager, processorsLimit)

    dispatch(taskManager,Process(Priority.MEDIUM))
    dispatch(taskManager,Process(Priority.LOW))
    dispatch(taskManager,Process(Priority.LOW))
    dispatch(taskManager,Process(Priority.MEDIUM))
    dispatch(taskManager,Process(Priority.HIGH))

    println("----List----")
    taskManager.list().forEach {
        println("${it.pid} ${it.priority} ${it.createdAt}")
    }
}

private fun dispatch(taskManger: TaskManager, process: Process) {
    try {
        println("DISPATCHED: ${process.pid} ${process.priority} ${process.createdAt}")
        Thread.sleep(1000)
        taskManger.add(process)
    } catch (exception: MaxProcessorsLimitExceeded) {
        println("REJECTED  : ${exception.process.pid} ${exception.process.priority} ${exception.process.createdAt}")
    }
}