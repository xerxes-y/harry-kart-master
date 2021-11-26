package se.atg.service.harrykart.kotlin.datamodel

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable


@JacksonXmlRootElement(localName = "loop")
class Loop : Serializable {
    /**
     * Returns the ordinal number of the loop in the race
     * @return  int Loop number
     */
    @JacksonXmlProperty(isAttribute = true)
    var number = 0

    /**
     * Return the collection of lanes for the loop
     * @return ArrayList<Lane> The lanes within the loop
    </Lane> */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "lane")
    var lanes: ArrayList<Lane>? = null

    constructor() {}

    /**
     * Loop has an ordinal number and a series of lanes with power-up values
     * @param number    Loop number
     * @param lanes Collection of lane objects
     */
    constructor(number: Int, lanes: ArrayList<Lane>?) {
        this.number = number
        this.lanes = lanes
    }

    /**
     * String representation of the loop
     * @return String   Loop in string format
     */
    override fun toString(): String {
        return "Loop{" +
                "number=" + number +
                ", lanes='" + lanes + '\'' +
                '}'
    }
}