package availability

import date.DateTimeService
import date.DateTimeServiceMock
import storage.AvailabilityStorage
import storage.RedisMock

class Simulator {

    AvailabilityParams params
    DisplaysManager displaysManager
    AvailabilityStorage storage

    Simulator(){
        params = new AvailabilityParams(clicksExpectedPerSlot:1,clickConvergenceRate: 0.4d,slotsPerPeriod:2,slotInSeconds:30)
        storage= new AvailabilityStorage(new RedisMock())
        displaysManager = new DisplaysManager(params, storage,new DateTimeService())
    }

    def run (){
        Random random = new Random()
        Map<Integer,List<Closure>> timeLine= [:]
        def periodDuration = params.slotsPerPeriod * params.slotInSeconds

        for(i in (1..(params.getDisplaysExpectedPerSlot()*params.slotsPerPeriod))){

            def display = random.nextInt(periodDuration)
                if (timeLine[display])
                timeLine[display]<<{ doDisplay()}
            else
                timeLine[display]=[{ doDisplay()}]

            def possibleClick = display+random.nextInt(periodDuration-display)
            def randomNumber = random.nextDouble()
            if (randomNumber<=params.clickConvergenceRate)
                if (timeLine[possibleClick])
                    timeLine[possibleClick]<<{ doClick()}
                else
                    timeLine[possibleClick]=[{ doClick()}]
        }
        println timeLine.sort()

        for(i in (1..periodDuration)){
            displaysManager.recalculate()

            println "Sec. ${i} : ${displaysManager.getDisplayManagerInfo()}"
            def actions = timeLine[i]
            if (actions){
                actions.each {action->
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
