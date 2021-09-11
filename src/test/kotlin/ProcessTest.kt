import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ProcessTest {
    private val process: Process = Process(Priority.LOW)

    @Test
    fun getPriority() {
        assertEquals(Priority.LOW, process.priority)
    }
}