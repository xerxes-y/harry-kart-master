package se.atg.service.harrykart.kotlin.datamodel

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import java.io.Serializable


class Lane : Serializable {
    /**
     * Returns the lane number
     * @return int  The lane number
     */
    @JacksonXmlProperty(isAttribute = true)
    var number = 0

    /**
     * Returns the power-up value
     * @return int  The power-up value
     */
    @JacksonXmlText
    var powerValue = 0


    /**
     * The lane in string form
     * @return  String  Representation of the lane
     */
    override fun toString(): String {
        return "Lane{" +
                "number=" + number +
                ", power='" + powerValue + '\'' +
                '}'
    }
}