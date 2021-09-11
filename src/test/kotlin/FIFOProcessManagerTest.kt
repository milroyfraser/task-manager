import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FIFOProcessManagerTest {
    private val processManager: FIFOProcessManager = FIFOProcessManager()

    @Test
    fun `it returns first process in the list`() {
        val firstProcess = Process(Priority.MEDIUM)
        val secondProcess = Process(Priority.LOW)
        val processors = listOf<Process>(firstProcess, secondProcess)

        assertEquals(firstProcess, processManager.getProcessToKill(processors, Process(Priority.LOW)))
    }

    @Test
    fun `it returns null when the processors List is empty`() {
        val processors = listOf<Process>()

        assertNull(processManager.getProcessToKill(processors, Process(Priority.LOW)))
    }
}