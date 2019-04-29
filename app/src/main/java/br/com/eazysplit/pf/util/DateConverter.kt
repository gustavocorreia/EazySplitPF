// Baseado no código do Professor Helder
package br.com.eazysplit.pf.util

import android.arch.persistence.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter @JvmStatic
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }
    @TypeConverter @JvmStatic
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    //@TypeConverter @JvmStatic
    //fun toString(date: Date?): String? {
    //    return date?.toString()
    //}

}
