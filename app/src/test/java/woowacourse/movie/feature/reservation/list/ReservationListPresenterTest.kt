package woowacourse.movie.feature.reservation.list

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test
import woowacourse.movie.data.TicketsRepositoryImpl
import woowacourse.movie.feature.TestSetting
import woowacourse.movie.model.MoneyState
import woowacourse.movie.model.MovieState
import woowacourse.movie.model.TheaterState
import woowacourse.movie.model.TicketsState
import woowacourse.movie.model.mapper.asPresentation
import java.time.LocalDate
import java.time.LocalDateTime

internal class ReservationListPresenterTest : TestSetting() {
    private val view: ReservationListContract.View = mockk()
    private lateinit var movieReservations: List<TicketsState>
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

    // sut
    private lateinit var sut: ReservationListContract.Presenter

    override fun setUp() {
        movieReservations = TicketsRepositoryImpl().getAllTickets().map { it.asPresentation() }
        sut = ReservationListPresenter(view, TicketsRepositoryImpl())
    }

    @Test
    fun `영화 예매 목록을 불러오고 화면에 보여준다`() {
        // given
        every { view.setTicketList(any()) } just runs

        // when
        sut.loadListItems()

        // then
        verify { view.setTicketList(movieReservations) }
    }

    @Test
    fun `영화 상세 화면을 띄워준다`() {
        // given
        every { view.showTicketsConfirm(ticketsState) } just runs

        // when
        sut.showTicketsConfirm(ticketsState)

        // then
        verify { view.showTicketsConfirm(ticketsState) }
    }
}
