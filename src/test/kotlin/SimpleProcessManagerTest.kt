import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class SimpleProcessManagerTest {
    private val processManager: SimpleProcessManager = SimpleProcessManager()

    @Test
    fun `it returns null`() {
        val processors = listOf<Process>(Process(Priority.MEDIUM))

        assertNull(processManager.getProcessToKill(processors, Process(Priority.LOW)))
    }
}