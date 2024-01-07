package com.projecthit.artful.constants

sealed class Screen(val route: String) {

    object Home: Screen("home")
    object Info: Screen("info")


}
