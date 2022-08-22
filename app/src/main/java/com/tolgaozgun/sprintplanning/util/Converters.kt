package com.tolgaozgun.sprintplanning.util

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun uuidToStringList(value: List<UUID>): List<String>{
        val list = mutableListOf<String>()
        for(item: UUID in value){
            list.add(item.toString())
        }
        return list.toList()
    }


}