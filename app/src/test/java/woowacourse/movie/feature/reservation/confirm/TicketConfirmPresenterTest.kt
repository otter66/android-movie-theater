package woowacourse.movie.feature.reservation.confirm

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test
import woowacourse.movie.feature.TestSetting
import woowacourse.movie.model.MoneyState
import woowacourse.movie.model.MovieState
import woowacourse.movie.model.TheaterState
import woowacourse.movie.model.TicketsState
import java.time.LocalDate
import java.time.LocalDateTime

internal class TicketConfirmPresenterTest : TestSetting() {

    private val view: TicketsConfirmContract.View = mockk()
    private val ticketsState: TicketsState = TicketsState(
        theater = TheaterState(theaterId = 0, theaterName = "선릉 극장"),
        movie = MovieState(
            id = 0,
            imgId = 0,
            title = "테스트 영화",
            startDate = LocalDate.of(2023, 4, 1),
            endDate = LocalDate.of(2023, 5, 31),
            runningTime = 120,
            description = ""
        ),
        dateTime = LocalDateTime.of(2023, 5, 28, 3, 32),
        totalDiscountedMoneyState = MoneyState(price = 5_000_000), tickets = listOf()
    )

    // System Under Test
    private lateinit var sut: TicketsConfirmContract.Presenter

    override fun setUp() {
        sut = TicketConfirmPresenter(view = view, tickets = ticketsState)
    }

    @Test
    fun `영화의 정보를 화면에 나타낸다`() {
        // given
        every { view.setViewContents(any()) } just runs

        // when
        sut.loadContents()

        // then
        verify { view.setViewContents(ticketsState) }
    }

    @Test
    fun `영화의 정보가 없다면 오류를 나타내고, 화면을 종료한다`() {
        // given
        sut = TicketConfirmPresenter(view = view, null)
        every { view.showContentError() } just runs
        every { view.finishActivity() } just runs

        // when
        sut.loadContents()

        // then
        verify { view.showContentError() }
        verify { view.finishActivity() }
    }
}
