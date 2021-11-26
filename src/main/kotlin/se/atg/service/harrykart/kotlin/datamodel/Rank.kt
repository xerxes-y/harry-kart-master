package se.atg.service.harrykart.kotlin.datamodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(value = ["time"]) // omit time field when serializing object to JSON
class Rank(var position: Int,var horse: String?,var time: Double) : Comparable<Rank?> {

    /**
     * Comparison logic for sorting rank by race completion time
     * @param other Rank object of another participant to be compared with
     * @return int Value signifying greater than, less than, or equal to ranking based on time
     */
    override operator fun compareTo(other: Rank?): Int {
        return if (time == other!!.time) 0 else if (time > other.time) 1 else -1
    }

    /**
     * String representation of a participant's rank at the end of the race
     * @return  String Finishing position and time of the participant
     */
    override fun toString(): String {
        return "Rank{" +
                "position=" + position +
                ", horse='" + horse + '\'' +
                ", time=" + time +
                '}'
    }


}