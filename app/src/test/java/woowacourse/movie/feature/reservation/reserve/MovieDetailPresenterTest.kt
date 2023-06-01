package woowacourse.movie.feature.reservation.reserve

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test
import woowacourse.movie.feature.TestSetting
import woowacourse.movie.model.MovieState
import woowacourse.movie.model.SelectTheaterAndMovieState
import woowacourse.movie.model.TheaterState
import java.time.LocalDate
import java.time.LocalDateTime

internal class MovieDetailPresenterTest : TestSetting() {
    private val view: MovieDetailContract.View = mockk()
    private val theaterAndMovieState: SelectTheaterAndMovieState = SelectTheaterAndMovieState(
        theater = TheaterState(theaterId = 0, theaterName = ""),
        movie = MovieState(
            id = 0, imgId = 0, title = "테스트 영화",
            startDate = LocalDate.of(2023, 4, 1),
            endDate = LocalDate.of(2023, 5, 31),
            runningTime = 0, description = ""
        ),
        allowTimes = listOf()
    )
    private val dateTime: LocalDateTime = LocalDateTime.of(2023, 5, 29, 11, 50)
    private val reservationCount: Int = 1

    // system under test
    private lateinit var sut: MovieDetailContract.Presenter

    override fun setUp() {
        sut = MovieDetailPresenter(view)
    }

    @Test
    fun `좌석 선택 화면을 띄워준다`() {
        // given
        every { view.showSeatSelectActivity(any()) } just runs

        // when
        sut.navigateSeatSelectActivity(
            theaterAndMovieState = theaterAndMovieState,
            dateTime = dateTime,
            reservationCount = reservationCount
        )

        // then
        verify { view.showSeatSelectActivity(any()) }
    }
}
