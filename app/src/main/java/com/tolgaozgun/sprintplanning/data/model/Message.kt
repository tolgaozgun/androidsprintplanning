package com.tolgaozgun.sprintplanning.data.model

import java.util.*

data class Message(val id: UUID,
                   val senderId: UUID,
                   val roomId: UUID,
                   val timeSent: Long,
                   val content: String)
{

}