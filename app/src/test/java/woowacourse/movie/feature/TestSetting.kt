package woowacourse.movie.feature

import io.mockk.checkUnnecessaryStub
import io.mockk.junit5.MockKExtension
import org.junit.After
import org.junit.Before

@MockKExtension.CheckUnnecessaryStub
abstract class TestSetting {

    @Before
    abstract fun setUp()

    @After
    fun check() {
        checkUnnecessaryStub()
    }
}
