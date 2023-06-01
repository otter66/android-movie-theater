package woowacourse.movie.feature.seat

import com.example.domain.model.TicketOffice
import com.example.domain.repository.TicketsRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test
import woowacourse.movie.feature.TestSetting
import woowacourse.movie.model.MoneyState
import woowacourse.movie.model.MovieState
import woowacourse.movie.model.SeatPositionState
import woowacourse.movie.model.SelectReservationState
import woowacourse.movie.model.TheaterState
import woowacourse.movie.model.TicketState
import woowacourse.movie.model.TicketsState
import java.time.LocalDate
import java.time.LocalDateTime

internal class SeatSelectPresenterTest : TestSetting() {

    private val view: SeatSelectContract.View = mockk()
    private val ticketsRepository: TicketsRepository = mockk()

    private val reserveDateTime: LocalDateTime = LocalDateTime.of(2023, 5, 28, 15, 32)

    // system under test
    private lateinit var sut: SeatSelectContract.Presenter

    override fun setUp() {
        every { view.setViewContents(any()) } just runs
        every { view.changePredictMoney(any()) } just runs // saved data를 불러올 때 (init) 호출
        every { view.setConfirmClickable(any()) } just runs // saved data를 불러올 때 (init) 호출

        sut = SeatSelectPresenter(
            view = view, reservationState = createReservationState(),
            ticketOffice = TicketOffice(),
            ticketsRepository = ticketsRepository
        )
    }

    @Test
    fun `선택되지 않은 좌석을 클릭하면 선택 상태로 바꾸고, 금액을 업데이트한다`() {
        // given
        every { view.changePredictMoney(any()) } just runs
        every { view.setConfirmClickable(any()) } just runs
        every { view.changeSeatCheckedByIndex(any()) } just runs

        // when
        sut.checkSeat(0)

        // then
        verify { view.changeSeatCheckedByIndex(0) }
        verify { view.changePredictMoney(createTicketsState().totalDiscountedMoneyState) }
    }

    @Test
    fun `선택되지 않은 좌석을 클릭했을 때 선택한 티켓 수만큼 좌석을 선택한 상태라면 예약 버튼을 활성화한다`() {
        // given
        every { view.changeSeatCheckedByIndex(any()) } just runs

        // when
        sut.checkSeat(0)

        // then
        verify { view.changeSeatCheckedByIndex(0) }
        verify { view.changePredictMoney(createTicketsState().totalDiscountedMoneyState) }
        verify { view.setConfirmClickable(true) }
    }

    @Test
    fun `예약 의사를 재확인하는 다이얼로그를 띄운다`() {
        // given
        every { view.showReservationConfirmationDialog() } just runs

        // when
        sut.showReservationConfirmationDialog()

        // then
        verify { view.showReservationConfirmationDialog() }
    }

    @Test
    fun `예약을 저장 하고, 영화 시작 전 알림을 설정하고, 예약 확인 화면을 띄운다`() {
        // given
        every { view.changeSeatCheckedByIndex(any()) } just runs
        sut.checkSeat(0)

        every { ticketsRepository.addTicket(any()) } just runs
        every { view.setReservationAlarm(any()) } just runs
        every { view.navigateReservationConfirm(any()) } just runs

        // when
        sut.reserveTickets()

        // then
        verify { ticketsRepository.addTicket(any()) }
        verify { view.setReservationAlarm(any()) }
        verify { view.navigateReservationConfirm(any()) }
    }

    private fun createTicketState(
        theater: TheaterState = createTheaterState(),
        movie: MovieState = createMovieState(),
        dateTime: LocalDateTime = reserveDateTime,
        seatPositionState: SeatPositionState = SeatPositionState(row = 1, column = 1),
        discountedMoneyState: MoneyState = MoneyState(price = 0)
    ): TicketState {
        return TicketState(
            theater = theater,
            movie = movie,
            dateTime = dateTime,
            seatPositionState = seatPositionState,
            discountedMoneyState = discountedMoneyState
        )
    }

    private fun createTicketsState(
        theater: TheaterState = createTheaterState(),
        movie: MovieState = createMovieState(),
        dateTime: LocalDateTime = reserveDateTime,
        moneyState: MoneyState = MoneyState(price = 0),
        ticketStates: List<TicketState> = listOf(createTicketState())
    ): TicketsState {
        return TicketsState(
            theater = theater,
            movie = movie,
            dateTime = dateTime,
            totalDiscountedMoneyState = moneyState,
            tickets = ticketStates
        )
    }

    private fun createReservationState(
        theater: TheaterState = createTheaterState(),
        movie: MovieState = createMovieState(),
        selectDateTime: LocalDateTime = reserveDateTime,
        selectCount: Int = 1
    ): SelectReservationState {
        return SelectReservationState(
            theater = theater,
            movie = movie,
            selectDateTime = selectDateTime,
            selectCount = selectCount
        )
    }

    private fun createMovieState(
        id: Int = 0,
        imgId: Int = 0,
        title: String = "테스트 영화",
        startDate: LocalDate = LocalDate.of(2023, 4, 1),
        endDate: LocalDate = LocalDate.of(2023, 5, 31),
        runningTime: Int = 120,
        description: String = ""
    ): MovieState {
        return MovieState(
            id = id, imgId = imgId, title = title,
            startDate = startDate, endDate = endDate,
            runningTime = runningTime, description = description
        )
    }

    private fun createTheaterState(): TheaterState {
        return TheaterState(theaterId = 0, theaterName = "")
    }
}
