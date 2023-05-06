package woowacourse.movie.feature.setting

import woowacourse.movie.data.setting.AlarmSetting

class SettingPresenter(
    val view: SettingContract.View,
    val userSetting: AlarmSetting
) : SettingContract.Presenter {

    override fun setMovieReminderChecked(hasPermission: Boolean) {
        view.setMovieReminderChecked(userSetting.enabled)

        if (!hasPermission) {
            userSetting.enabled = false
            view.setMovieReminderChecked(false)
            view.requestPermission()
        }
    }

    override fun changeMovieReminderChecked(hasPermission: Boolean, switchChecked: Boolean) {
        if (!userSetting.enabled && !hasPermission) {
            view.setMovieReminderChecked(false)
            view.requestPermission()
            return
        }

        userSetting.enabled = switchChecked
    }
}
