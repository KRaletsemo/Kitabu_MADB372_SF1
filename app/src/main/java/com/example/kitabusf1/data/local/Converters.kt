package com.example.kitabusf1.data.local

//This kt file will be used to help convert any needed data types to aid the Database receiving said information

import androidx.room.TypeConverter
import com.example.kitabusf1.data.BookingStatus

class Converters {

    @TypeConverter
    fun fromStatus(status: BookingStatus): String = status.name //Turns enum into its name as text when saving

    @TypeConverter
    fun toStatus(value: String): BookingStatus = BookingStatus.valueOf(value) //Turns to text when reading
}