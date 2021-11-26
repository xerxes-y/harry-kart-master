package se.atg.service.harrykart.kotlin

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import se.atg.service.harrykart.kotlin.datamodel.HarryKart
import se.atg.service.harrykart.kotlin.datamodel.Rank
import se.atg.service.harrykart.kotlin.service.PlayRace
import se.atg.service.harrykart.kotlin.service.PlayRaceSerialization
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("kotlin-test")
class HarryKartControllerTest {
    var hkSerializer: PlayRaceSerialization? = null
    var mapper: ObjectMapper? = null
    @BeforeAll
    @Throws(Exception::class)
    fun setUp() {
        hkSerializer = PlayRaceSerialization()
        mapper = ObjectMapper()
    }

    /**
     *
     * @param filename XML filename to be read from /resources
     * @return
     */
    private fun readFileToString(filename: String): String {
        val `in` = this.javaClass.classLoader.getResourceAsStream(filename)
        Objects.requireNonNull(`in`)
        var xmlString = ""
        try {
            BufferedReader(InputStreamReader(`in`)).use { br ->
                xmlString = br.lines().collect(Collectors.joining(System.lineSeparator()))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return xmlString
        }
        return xmlString
    }

    /**
     * Lane 1 is first, Lane 2 is second, Lane 3 is third
     * @throws HarryKartException
     */
    @Test
    @Throws(RuntimeException::class)
    fun lanesFinishInOrderTest() {
        val inputXML = readFileToString("_LanesFinishInOrderTest.xml")
        val hk: HarryKart? = hkSerializer?.deserializeFromXML(inputXML)
        // Calculate the race results
        val actualRanking: List<Rank> = PlayRace().getResults(hk) as List<Rank>
        // Expected race outcome
        val expectedRanking = ArrayList<Rank>()
        expectedRanking.add(Rank(1, "TIMETOBELUCKY", 0.0))
        expectedRanking.add(Rank(2, "CARGO DOOR", 0.0))
        expectedRanking.add(Rank(3, "HERCULES BOKO", 0.0))
        // Compare expected and actual JSON results
        val resultJson: String = hkSerializer!!.serializeToJson(actualRanking)
        val expectedJson: String = hkSerializer!!.serializeToJson(expectedRanking)
        assertEquals(resultJson, expectedJson)
    }

    @Test
    @Throws(java.lang.RuntimeException::class)
    fun minimumParticipantTest() {
        assertThrows(java.lang.RuntimeException::class.java) {
            val inputXML = readFileToString("_MinimumNumberOfParticipants.xml")
            val hk = hkSerializer!!.deserializeFromXML(inputXML)
        }
    }

    @Test
    @Throws(RuntimeException::class)
    fun invalidXmlFormatTest() {
        assertThrows(java.lang.RuntimeException::class.java){
            val inputXML = readFileToString("_InvalidHarryKartFormatTest.xml")
            val hk = hkSerializer!!.deserializeFromXML(inputXML)
        }
    }
    @Test
    @Throws(RuntimeException::class)
    fun twoWayTieTest() {
        val inputXML = readFileToString("_TwoWayTieTest.xml")
        val hk: HarryKart? = hkSerializer?.deserializeFromXML(inputXML)
        // Calculate the race results
        val actualRanking: List<Rank> = PlayRace().getResults(hk) as List<Rank>
        // Verify the finishing position of each participant
        assertEquals(actualRanking[0].position, 1)
        assertEquals(actualRanking[0].horse, "WAIKIKI SILVIO")
        assertEquals(actualRanking[1].position, 2)
        assertEquals(actualRanking[1].horse, "HERCULES BOKO")
        val thirdPlace = actualRanking[2].horse
        assertEquals(actualRanking[2].position, 3)
        assertTrue(thirdPlace == "CARGO DOOR" || thirdPlace == "TIMETOBELUCKY")
        val fourthPlace = actualRanking[3].horse
        assertEquals(actualRanking[3].position, 3)
        assertTrue(fourthPlace == "CARGO DOOR" || fourthPlace == "TIMETOBELUCKY")
    }

    /**
     * All participants finish at the same time
     * @throws HarryKartException
     */
    @Test
    @Throws(RuntimeException::class)
    fun allWayTieTest() {
        val inputXML = readFileToString("_AllWayTieTest.xml")
        val hk: HarryKart? = hkSerializer?.deserializeFromXML(inputXML)
        // Calculate the race results
        val actualRanking: List<Rank> = PlayRace().getResults(hk) as List<Rank>
        // Verify that the finishing position of each participant is #1
        actualRanking.forEach(Consumer { rank: Rank -> assertEquals(rank.position, 1) })
    }

     @Test
    @Throws(RuntimeException::class)
    fun zeroAndNegativePowerTest() {
        val inputXML = readFileToString("_ZeroAndNegativePowerTest.xml")
        val hk: HarryKart? = hkSerializer?.deserializeFromXML(inputXML)
        val actualRanking: List<Rank> = PlayRace().getResults(hk) as List<Rank>
        // Expected race outcome
        val expectedRanking = ArrayList<Rank>()
        expectedRanking.add(Rank(1, "WAIKIKI SILVIO", 0.0))
        expectedRanking.add(Rank(2, "HERCULES BOKO", 0.0))
        // Compare expected and actual JSON results
        val resultJson: String = hkSerializer!!.serializeToJson(actualRanking)
        val expectedJson: String = hkSerializer!!.serializeToJson(expectedRanking)
        assertEquals(resultJson, expectedJson)
    }
}