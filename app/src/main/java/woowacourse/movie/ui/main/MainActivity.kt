package woowacourse.movie.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import woowacourse.movie.R
import woowacourse.movie.ui.fragment.movieList.MovieListFragment
import woowacourse.movie.ui.fragment.reservationList.ReservationListFragment
import woowacourse.movie.ui.fragment.setting.SettingFragment

class MainActivity : AppCompatActivity() {
    private val rv: FragmentContainerView by lazy { findViewById(R.id.container) }
    private val bottomNavigation: BottomNavigationView by lazy { findViewById(R.id.bottom_navigation_view) }

    private lateinit var movieListFragment: MovieListFragment
    private lateinit var reservationListFragment: ReservationListFragment
    private lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = supportFragmentManager.findFragmentById(rv.id)
        when (fragment) {
            is MovieListFragment -> {
                movieListFragment = fragment
                reservationListFragment = ReservationListFragment()
                settingFragment = SettingFragment()
                bottomNavigation.selectedItemId = R.id.movie_list_item
            }
            is ReservationListFragment -> {
                movieListFragment = MovieListFragment()
                reservationListFragment = fragment
                settingFragment = SettingFragment()
                bottomNavigation.selectedItemId = R.id.reservation_list_item
            }
            is SettingFragment -> {
                movieListFragment = MovieListFragment()
                reservationListFragment = ReservationListFragment()
                settingFragment = fragment
            }
            null -> {
                movieListFragment = MovieListFragment()
                reservationListFragment = ReservationListFragment()
                settingFragment = SettingFragment()
                supportFragmentManager.beginTransaction().add(
                    rv.id, movieListFragment,
                    MOVIE_LIST_TAG
                ).commit()
                bottomNavigation.selectedItemId = R.id.movie_list_item
            }
        }

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.reservation_list_item -> {
                    supportFragmentManager.beginTransaction()
                        .replace(rv.id, reservationListFragment, RESERVATION_LIST_TAG)
                        .commit()
                }
                R.id.movie_list_item -> {
                    supportFragmentManager.beginTransaction()
                        .replace(rv.id, movieListFragment, MOVIE_LIST_TAG)
                        .commit()
                }
                R.id.setting_item -> {
                    supportFragmentManager.beginTransaction()
                        .replace(rv.id, settingFragment, SETTING_TAG)
                        .commit()
                }
            }
            return@setOnItemSelectedListener true
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
    }

    companion object {
        internal const val KEY_MOVIE = "key_movie"
        internal const val KEY_ADV = "key_adb"

        private const val RESERVATION_LIST_TAG = "reservation_list_tag"
        private const val MOVIE_LIST_TAG = "movie_list_tag"
        private const val SETTING_TAG = "setting_tag"
    }
}
