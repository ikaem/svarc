package com.imkaem.android.svarc.core.utils.helpers

import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateHelpers {

    companion object {
        fun millisecondsToInstant(millis: Long): Instant {
            val instant = Instant.ofEpochMilli(millis)
            /* TODO instant is already with locked UTC time zone*/
//                .atZone(ZoneOffset.UTC)

            /* TODO just checking here */
            val zonedDateTime = instant.atZone(ZoneOffset.UTC)
            val localDateTime = zonedDateTime.toLocalDateTime()
            val localDate = localDateTime.toLocalDate()

            Log.d("DateHelpers", "----------------")
            Log.d("DateHelpers", "millisecondsToInstant: millis: $millis")
            Log.d("DateHelpers", "millisecondsToInstant: instant: $instant")
            Log.d("DateHelpers", "millisecondsToInstant: zonedDateTime (UTC): $zonedDateTime")
            Log.d("DateHelpers", "millisecondsToInstant: localDateTime: $localDateTime")
            Log.d("DateHelpers", "millisecondsToInstant: localDate: $localDate")
            Log.d("DateHelpers", "----------------")

            /* checking how to add hour and time to existing instant */
//            instant.plus

            val hours = 11
            val minutes = 12


            val hourSeconds = hours * 3600
            val minuteSeconds = minutes * 60 // -> or we can just convert this to milliseconds, and add to millis directly

            val hourMinuteSeconds = hourSeconds + minuteSeconds

            val updatedInstant = instant.plusSeconds(hourMinuteSeconds.toLong())
            val updatedZonedDateTime = updatedInstant.atZone(ZoneOffset.UTC)
            val updatedLocalDateTime = updatedZonedDateTime.toLocalDateTime()
            val updatedLocalDate = updatedLocalDateTime.toLocalDate()

            Log.d("DateHelpers", "----------")

//            Log.d("DateHelpers", "updatedInstant: ${updatedInstant.}")
            Log.d("DateHelpers", "updatedInstant after adding $hours hours and $minutes minutes: $updatedInstant")
            Log.d("DateHelpers", "updatedZonedDateTime (UTC): $updatedZonedDateTime")
            Log.d("DateHelpers", "updatedLocalDateTime: $updatedLocalDateTime")
            Log.d("DateHelpers", "updatedLocalDate: $updatedLocalDate")

            Log.d("DateHelpers", "----------")

            return instant
        }

        /* will convert to string in form 21 Sept 1985 in system default timezone */
        fun instantToLocalDateFormattedString(instant: Instant): String {

            /* this will create LocalDate in system default timezone */
            val utcLocalDate = instant.atZone(ZoneOffset.UTC).toLocalDate()
            val startOfDay = utcLocalDate.atStartOfDay(ZoneId.systemDefault())


            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

            val formattedDate = formatter.format(startOfDay)

            Log.d("DateHelpers", "---------------")
            Log.d("DateHelpers", "instantToLocalDateFormattedString: instant: $instant")
            Log.d("DateHelpers", "instantToLocalDateFormattedString: utcLocalDate (UTC): $utcLocalDate")
            Log.d("DateHelpers", "instantToLocalDateFormattedString: startOfDay (system default): $startOfDay")
            Log.d("DateHelpers", "instantToLocalDateFormattedString: formattedDate: $formattedDate")
            Log.d("DateHelpers", "---------------")

            return formattedDate;

        }
    }
}