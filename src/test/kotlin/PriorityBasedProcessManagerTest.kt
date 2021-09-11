import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PriorityBasedProcessManagerTest {
    private val processManager: PriorityBasedProcessManager = PriorityBasedProcessManager()

    @Test
    fun `it returns lowest priority and oldest process`() {
        val firstProcess = Process(Priority.MEDIUM)
        val secondProcess = Process(Priority.LOW)
        val thirdProcess = Process(Priority.HIGH)
        val forthProcess = Process(Priority.LOW)
        val processors = listOf<Process>(firstProcess, secondProcess, thirdProcess, forthProcess)

        assertEquals(secondProcess, processManager.getProcessToKill(processors, Process(Priority.MEDIUM)))
    }

    @Test
    fun `it returns null whe there are no low priority processes` () {
        val firstProcess = Process(Priority.MEDIUM)
        val thirdProcess = Process(Priority.HIGH)
        val processors = listOf<Process>(firstProcess, thirdProcess)

        assertNull(processManager.getProcessToKill(processors, Process(Priority.MEDIUM)))
    }

    @Test
    fun `it returns null when the processors list is empty`() {
        val processors = listOf<Process>()

        assertNull(processManager.getProcessToKill(processors, Process(Priority.LOW)))
    }
}