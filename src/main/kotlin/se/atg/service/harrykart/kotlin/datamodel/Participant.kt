package se.atg.service.harrykart.kotlin.datamodel

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable

@JacksonXmlRootElement(localName = "participant")
class Participant(@JsonProperty("lane")var lane: Int, @JsonProperty("name") var name: String?,@JsonProperty("baseSpeed") var baseSpeed: Int) : Serializable {

    /**
     * String representation of the Participant
     * @return String Participant values
     */
    override fun toString(): String {
        return "Participant{" +
                "lane=" + lane +
                ", name='" + name + '\'' +
                ", baseSpeed=" + baseSpeed +
                '}'
    }
}