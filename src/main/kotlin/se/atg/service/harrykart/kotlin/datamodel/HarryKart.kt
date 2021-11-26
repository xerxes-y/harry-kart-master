package se.atg.service.harrykart.kotlin.datamodel

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.Serializable


@JacksonXmlRootElement(localName = "harryKart")
data class HarryKart(@JsonProperty("numberOfLoops")var numberOfLoops: Int,@JsonProperty("startList") var startList: ArrayList<Participant>?, @JsonProperty("powerUps")var  powerUps: ArrayList<Loop>?) : Serializable{


}