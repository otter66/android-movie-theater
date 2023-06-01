package woowacourse.movie.feature.setting

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test
import woowacourse.movie.data.setting.AlarmSetting
import woowacourse.movie.feature.TestSetting
import woowacourse.movie.util.permission.PermissionChecker

class SettingPresenterTest : TestSetting() {
    private val view: SettingContract.View = mockk()
    private val alarmSetting: AlarmSetting = mockk()
    private val permissionChecker: PermissionChecker = mockk()

    // System Under Test
    private lateinit var sut: SettingContract.Presenter

    override fun setUp() {
        sut = SettingPresenter(view, alarmSetting, permissionChecker)
    }

    @Test
    fun `영화 시작 알림 설정이 켜져 있는 경우 설정 화면의 스위치를 on한다`() {
        // given
        every { alarmSetting.isEnable } returns true
        every { view.setMovieStartReminderSwitchChecked(any()) } just runs
        every { permissionChecker.hasPermission } returns true

        // when
        sut.loadMovieStartReminderSetting()

        // then
        verify { view.setMovieStartReminderSwitchChecked(true) }
    }

    @Test
    fun `영화 시작 알림 설정이 꺼져 있는 경우 설정 화면의 스위치를 off한다`() {
        // given
        every { alarmSetting.isEnable } returns false
        every { view.setMovieStartReminderSwitchChecked(any()) } just runs
        every { permissionChecker.hasPermission } returns true

        // when
        sut.loadMovieStartReminderSetting()

        // then
        verify { view.setMovieStartReminderSwitchChecked(false) }
    }

    @Test
    fun `앱 권한이 없다면 설정 화면의 스위치를 off하고, 앱 권한을 요청한다`() {
        // given
        every { alarmSetting.isEnable } returns false
        every { permissionChecker.hasPermission } returns false
        every { view.setMovieStartReminderSwitchChecked(any()) } just runs
        every { alarmSetting.isEnable = any() } just runs
        every { view.requestPermission() } just runs

        // when
        sut.loadMovieStartReminderSetting()

        // then
        verifyRequestPermission()
    }

    @Test
    fun `설정 화면의 스위치를 on했을 때, 앱 권한이 있다면, 영화 시작 알림 설정을 on한다`() {
        // given
        every { permissionChecker.hasPermission } returns true
        every { alarmSetting.isEnable = any() } just runs

        // when
        sut.setMovieStartReminderSettingEnable(true)

        // then
        verify { alarmSetting.isEnable = true }
    }

    @Test
    fun `설정 화면의 스위치를 off했을 때, 앱 권한이 있다면, 영화 시작 알림 설정을 off한다`() {
        // given
        every { permissionChecker.hasPermission } returns true
        every { alarmSetting.isEnable = any() } just runs

        // when
        sut.setMovieStartReminderSettingEnable(false)

        // then
        verify { alarmSetting.isEnable = false }
    }

    @Test
    fun `설정 화면의 스위치를 on했을 때, 앱 권한이 없다면, 권한을 요청한다`() {
        // given
        every { permissionChecker.hasPermission } returns false
        every { alarmSetting.isEnable = any() } just runs
        every { view.setMovieStartReminderSwitchChecked(any()) } just runs
        every { view.requestPermission() } just runs

        // when
        sut.setMovieStartReminderSettingEnable(true)

        // then
        verifyRequestPermission()
    }

    private fun verifyRequestPermission() {
        verify { alarmSetting.isEnable = false }
        verify { view.setMovieStartReminderSwitchChecked(false) }
        verify { view.requestPermission() }
    }
}
