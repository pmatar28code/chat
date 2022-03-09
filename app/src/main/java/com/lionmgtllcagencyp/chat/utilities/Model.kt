package com.lionmgtllcagencyp.chat.utilities

data class User(
    val email:String ?= "",
    val phone:String ?= "",
    val name:String ?= "",
    val imageUrl:String ?= "",
    val status:String ?= "",
    val statusUrl:String ?= "",
    val statusTime:String ?=""

)