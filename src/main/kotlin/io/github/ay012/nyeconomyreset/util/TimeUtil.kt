package io.github.ay012.nyeconomyreset.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {

	fun getCurrentTime(): String {
		val current = LocalDateTime.now()
		val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

		return current.format(formatter)
	}
}