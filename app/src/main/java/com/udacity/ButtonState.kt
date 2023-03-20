package com.udacity


sealed class ButtonState(var buttonText: Int) {
    object Default : ButtonState(R.string.button_default_status)
    object Loading : ButtonState(R.string.button_loading_status)
    object Completed : ButtonState(R.string.button_completed_status)
}