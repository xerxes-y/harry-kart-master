package se.atg.service.harrykart.kotlin.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Service
import org.xml.sax.SAXException
import se.atg.service.harrykart.kotlin.datamodel.HarryKart
import se.atg.service.harrykart.kotlin.datamodel.Rank
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.StringReader
import java.net.URL
import java.util.*
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator

@Service
class PlayRaceSerialization {
    private var xmlMapper: XmlMapper = XmlMapper()
     fun isXmlValid(xmlString: String?): Boolean {
            val schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            return try {
                val schema: Schema = schemaFactory.newSchema(File(getResource(SCHEMA_FILE)))
                val validator: Validator = schema.newValidator()
                validator.validate(StreamSource(StringReader(xmlString)))
                true
            } catch (e: SAXException) {
                false
            } catch (e: IOException) {
                false
            }
        }

        fun isNumberOfLoopsValid(hk: HarryKart?): Boolean {
            return hk!!.numberOfLoops == hk.powerUps!!.size + 1
        }

        fun numberOfParticipants(hk: HarryKart?): Int {
            return hk!!.startList!!.size
        }

        @Throws(FileNotFoundException::class)
        private fun getResource(filename: String): String {
            val resource: URL = javaClass.classLoader.getResource(filename)
            Objects.requireNonNull(resource)
            return resource.getFile()
        }
        @Throws(RuntimeException::class)
        fun deserializeFromXML(xmlString: String?): HarryKart? {
            if (!isXmlValid(xmlString)) {
                throw RuntimeException("The Harry Kart input XML is not in a valid format.")
            }
            var harryKartObj: HarryKart? = null
            try {
                harryKartObj = xmlMapper.readValue(xmlString, HarryKart::class.java)
             if (!isNumberOfLoopsValid(harryKartObj)) {
                    throw RuntimeException("<numberOfLoops> value in the input XML does not match the number of loops provided")
                }
                if (numberOfParticipants(harryKartObj) < 2) {
                    throw RuntimeException("Harry Kart race cannot have less than 2 participants")
                }
            } catch (e: IOException) {
                println("IOException while trying to de-serialize input XML")
                println(e)
            }
            return harryKartObj
        }
        fun serializeToJson(ranking: List<Rank?>?): String {
            val mapper = ObjectMapper()
            var jsonRanking = "[]"
            jsonRanking = try {
                mapper.writeValueAsString(ranking)
            } catch (e: JsonProcessingException) {
                e.printStackTrace()
                return "{\"json-processing-error\": " + e.message + " }"
            }
            return "{\"ranking\": $jsonRanking }"
        }

        companion object {
            private const val SCHEMA_FILE = "input.xsd"
        }

        init {
            xmlMapper = XmlMapper()
            xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
