import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

internal class TaskManagerTest {
    @Test
    fun `can add a process when limit is not exceeded`() {
        val processors = listOf<Process>()
        val newProcess = Process(Priority.LOW)
        val processManager = makeProcessManager(processors, newProcess, null)
        val taskManager: TaskManager = TaskManager(processManager, 2)

        assertDoesNotThrow() {
            taskManager.add(newProcess)
        }
    }

    @Test
    fun `can not add a process when limit is exceeded`() {
        val firstProcess = Process(Priority.LOW)
        val processors = listOf<Process>(firstProcess)
        val newProcess = Process(Priority.LOW)
        val processManager = makeProcessManager(processors, newProcess, null)
        val taskManager: TaskManager = TaskManager(processManager, 1)
        taskManager.add(firstProcess)

        assertThrows<MaxProcessorsLimitExceeded> {
            taskManager.add(newProcess)
        }
    }

    @Test
    fun `can add a process when an existing process is killed`() {
        val firstProcess = Process(Priority.LOW)
        val processors = listOf<Process>(firstProcess)
        val newProcess = Process(Priority.LOW)
        val processManager = makeProcessManager(processors, newProcess, firstProcess)
        val taskManager: TaskManager = TaskManager(processManager, 1)
        taskManager.add(firstProcess)

        assertEquals(1, taskManager.list().count())
        assertDoesNotThrow() {
            taskManager.add(newProcess)
        }
        assertEquals(1, taskManager.list().count())
    }

    @Test
    fun `can kill a process`() {
        val newProcess = spy(Process(Priority.LOW))
        val processManager = mock<ProcessManager>()
        val taskManager: TaskManager = TaskManager(processManager, 1)
        taskManager.add(newProcess)

        assertEquals(1, taskManager.list().count())
        taskManager.kill(newProcess)
        verify(newProcess).kill()
        assertEquals(0, taskManager.list().count())
    }

    @Test
    fun `can kill a group of processors`() {
        val firstProcess = spy(Process(Priority.LOW))
        val secondProcess = spy(Process(Priority.LOW))
        val thirdProcess = spy(Process(Priority.LOW))
        val processManager = mock<ProcessManager>()
        val taskManager: TaskManager = TaskManager(processManager, 5)
        taskManager.add(firstProcess)
        taskManager.add(secondProcess)
        taskManager.add(thirdProcess)

        assertEquals(3, taskManager.list().count())
        taskManager.killGroup(listOf(firstProcess, secondProcess))
        verify(firstProcess).kill()
        verify(secondProcess).kill()
        verify(thirdProcess, never()).kill()
        assertEquals(1, taskManager.list().count())
    }

    @Test
    fun `can kill all processors`() {
        val firstProcess = spy(Process(Priority.LOW))
        val secondProcess = spy(Process(Priority.LOW))
        val thirdProcess = spy(Process(Priority.LOW))
        val processManager = mock<ProcessManager>()
        val taskManager: TaskManager = TaskManager(processManager, 5)
        taskManager.add(firstProcess)
        taskManager.add(secondProcess)
        taskManager.add(thirdProcess)

        assertEquals(3, taskManager.list().count())
        taskManager.killAll()
        verify(firstProcess).kill()
        verify(secondProcess).kill()
        verify(thirdProcess).kill()
        assertEquals(0, taskManager.list().count())
    }

    private fun makeProcessManager(processors: List<Process>, newProcess: Process, returnValue: Process?): ProcessManager {
        return mock<ProcessManager> {
            on { getProcessToKill(processors, newProcess) } doReturn returnValue
        }
    }

    @Test
    fun `can list processors by default ordered by time of creation`() {
        val firstProcess = Process(Priority.LOW)
        val secondProcess = Process(Priority.LOW)
        val thirdProcess = Process(Priority.LOW)
        val processManager = mock<ProcessManager>()
        val taskManager: TaskManager = TaskManager(processManager, 5)
        taskManager.add(firstProcess)
        taskManager.add(secondProcess)
        taskManager.add(thirdProcess)

        val listOfProcessors = taskManager.list()

        assertEquals(firstProcess, listOfProcessors.first())
        assertEquals(thirdProcess, listOfProcessors.last())
        assertEquals(3, listOfProcessors.count())
    }

    @Test
    fun `can list processors sorted by priority`() {
        val firstProcess = Process(Priority.MEDIUM)
        val secondProcess = Process(Priority.LOW)
        val thirdProcess = Process(Priority.HIGH)
        val processManager = mock<ProcessManager>()
        val taskManager: TaskManager = TaskManager(processManager, 5)
        taskManager.add(firstProcess)
        taskManager.add(secondProcess)
        taskManager.add(thirdProcess)

        val listOfProcessors = taskManager.list(SortField.PRIORITY)

        assertEquals(secondProcess, listOfProcessors.first())
        assertEquals(thirdProcess, listOfProcessors.last())
        assertEquals(3, listOfProcessors.count())
    }
}