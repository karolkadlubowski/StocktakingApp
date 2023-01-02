package pl.polsl.stocktakingApp.base


import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest<T : Any> {
    abstract val mocks: Array<Any>

    @JvmField
    @RegisterExtension
    val coroutinesTestRule = CoroutinesTestRule()

    @BeforeEach
    open fun beforeEach() {
        clearAllMocks()
    }

    @AfterEach
    open fun afterEach() {
        confirmVerified(*mocks)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun runTestIn(testBody: suspend TestScope.() -> Unit) = runTest(
        coroutinesTestRule.testDispatcher,
        testBody = testBody
    )

    protected lateinit var service: T

    protected fun <T, B> MockKStubScope<T, B>.coAnswersWithDelay(
        delay: Long,
        answer: MockKAnswerScope<T, B>.(Call) -> T
    ) = coAnswers {
        delay(delay)
        answer(it)
    }
}