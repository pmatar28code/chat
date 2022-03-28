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

data class Contacts(
    val name:String = "",
    val phone:String = ""
)

data class Chat(
    var listOfChatParticipants :ArrayList<String?>
)

data class Message(
    val sentBy:String? = "",
    val message:String? = "",
    val messageTime:Long? = 0
)