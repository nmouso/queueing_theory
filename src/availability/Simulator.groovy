package availability

import date.DateTimeService
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout
import storage.AvailabilityStorage

import static grails.async.Promises.task

class Simulator {

    AvailabilityParams params
    DisplaysManager displaysManager
    AvailabilityStorage storage

    Simulator(AvailabilityParams availabilityParams, AvailabilityStorage storage){
        this.params=availabilityParams
        this.storage=storage
        displaysManager = new DisplaysManager(params, storage,new DateTimeService())
    }

    def run (){
        Random random = new Random()
        Map<Integer,List<Closure>> timeLine= [:]

        def totalDuration = params.slotsPerPeriod * params.slotInSeconds*params.refreshRateInPeriods
        def totalDisplays = params.getDisplaysExpectedPerSlot()*params.slotsPerPeriod*params.refreshRateInPeriods

        for(i in (1..totalDisplays)){
            def displayTime = random.nextInt(totalDuration)
            if (!timeLine[displayTime])
                timeLine[displayTime] =[]
            timeLine[displayTime]<<{ doDisplay()}

            def randomNumber = random.nextDouble()
            if (randomNumber<=params.clickConvergenceRate){
                def clickTime = displayTime+random.nextInt(totalDuration-displayTime)
                if (!timeLine[clickTime])
                    timeLine[clickTime] =[]
                timeLine[clickTime]<<{ doClick()}
            }

        }
        println timeLine.sort()

        for(i in (1..totalDuration)){
            displaysManager.recalculate()

            println "Sec. ${i} : ${displaysManager.getDisplayManagerInfo()}"
            def actions = timeLine[i]
            actions?.each{action->
                task {
                    action.call()
                }
            }

            sleep(1000)
        }
    }

    public doClick(){
        println "click"
        storage.decrementClicks()
    }

    public doDisplay(){
        println "display"
        storage.decrementDisplays()
    }
}
