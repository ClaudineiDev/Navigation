package com.claudinei.navigation.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.claudinei.navigation.R

private val navOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_rigth)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_rigth)
    .build()

fun NavController.navigateWithAnimation(destinationId: Int){
    this.navigate(destinationId, null, navOptions)
}