package org.d3if3155.studentattandance.navigations

sealed class Screen(val route: String) {
    data object Main: Screen("mainScreen")
    data object Presence: Screen("presenceScreen")
}