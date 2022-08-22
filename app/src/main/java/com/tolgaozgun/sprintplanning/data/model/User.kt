package com.tolgaozgun.sprintplanning.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey                         var id: UUID,
    @ColumnInfo(name="name")            var name: String?,
    @ColumnInfo(name="avatar_url")      var avatarUrl: String?,
    @ColumnInfo(name="vote")            var vote: Int?,
    @ColumnInfo(name="has_voted")       var hasVoted: Boolean,
)



