package se.atg.service.harrykart.kotlin.service

import org.springframework.stereotype.Service
import se.atg.service.harrykart.kotlin.datamodel.HarryKart
import se.atg.service.harrykart.kotlin.datamodel.Rank
import java.util.*
import java.util.stream.Collectors.toList

@Service
class PlayRace {
    private val TRACK_LENGTH = 1000.0

    /**
     * Calculate the rank of the race participants
     * @return List<Rank>   List of participants ranked by their order of race completion
    </Rank> */
    fun getResults(race: HarryKart?): List<Rank?>? {
        val startRacing = startRacing(race, ArrayList<Rank>())
        var finalPlacement = 1
        startRacing[0].position = finalPlacement
        for (raceNumber in 1 until startRacing.size) {
            if (startRacing[raceNumber].time == startRacing[raceNumber - 1].time) {
                startRacing[raceNumber].position = finalPlacement
            } else {
                startRacing[raceNumber].position = ++finalPlacement
            }
        }
        return startRacing.stream().filter { rank: Rank -> rank.position <= 3 }.collect(toList())


    }

    private fun startRacing(race: HarryKart?, ranking: ArrayList<Rank>): ArrayList<Rank> {
        race?.startList?.forEach { participant ->
            val rank = Rank(0, participant.name, TRACK_LENGTH / participant.baseSpeed)
            race.powerUps?.stream()
                ?.forEach { loop ->
                    loop.lanes?.stream()
                        ?.filter { lane -> lane.number === participant.lane }?.forEach { lane ->
                            val loopSpeed: Int = participant.baseSpeed + lane.powerValue
                            if (loopSpeed <= 0) {
                                rank.time =
                                    rank.time + Double.MAX_VALUE
                            } else {
                                participant.baseSpeed = participant.baseSpeed + lane.powerValue
                                rank.time = rank.time + TRACK_LENGTH / participant.baseSpeed
                            }
                        }
                }
            ranking.add(rank)
        }
        ranking.sort()
        ranking.removeIf { rank: Rank -> rank.time >= Double.MAX_VALUE }
        return ranking;
    }

}