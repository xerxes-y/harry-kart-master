package se.atg.service.harrykart.kotlin.rest

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.atg.service.harrykart.kotlin.datamodel.HarryKart
import se.atg.service.harrykart.kotlin.datamodel.Rank
import se.atg.service.harrykart.kotlin.service.PlayRace
import se.atg.service.harrykart.kotlin.service.PlayRaceSerialization
import java.lang.RuntimeException


@RestController
@RequestMapping("/kotlin/api")
class HarryKartController(private val playRace: PlayRace,private val playRaceSerialization: PlayRaceSerialization) {
    @PostMapping(path = ["/play"], consumes = [MediaType.APPLICATION_XML_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun playHarryKart(@RequestBody  input : String): String {
        return try {
            val hk: HarryKart = playRaceSerialization.deserializeFromXML(input)!!
            val ranking: List<Rank?>? = playRace.getResults(hk)
            playRaceSerialization.serializeToJson(ranking)
        } catch (e: RuntimeException) {
            System.out.println("HarryKartException: " + e.printStackTrace())
            "{\"message\": " + e.printStackTrace().toString() + " }"
        }
    }
}
