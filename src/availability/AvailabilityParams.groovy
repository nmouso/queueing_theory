package availability

import date.DateTimeService
import date.IDateTimeService
import storage.AvailabilityStorage

class AvailabilityParams {

    //config vars
    Integer slotInSeconds
    Integer slotsPerPeriod
    Integer refreshRateInPeriods = 2

    //dynamic vars
    Double clickConvergenceRate
    Integer clicksExpectedPerSlot

    // service inj.
    IDateTimeService dateTimeService = new DateTimeService()


    Integer getDisplaysExpectedPerSlot() {
        return getClicksExpectedPerSlot() / getClickConvergenceRate()
    }

    Boolean areExpired(Date paramsExpirationDate){
        paramsExpirationDate ? (new Date() >= paramsExpirationDate) : false
    }


    void refresh(){

        //do stuff
        //service.update(this)

        Integer periodInSeconds = slotInSeconds*slotsPerPeriod
        Integer expireInSeconds = refreshRateInPeriods*periodInSeconds
        dateTimeService.nowPlusSeconds(expireInSeconds)

    }

}
