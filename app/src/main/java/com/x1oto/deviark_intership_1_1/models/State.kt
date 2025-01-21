package com.x1oto.deviark_intership_1_1.models

sealed interface State {
    object Loading: State
    class Success(val data: List<Book>): State
    class Error(val message: String): State
}