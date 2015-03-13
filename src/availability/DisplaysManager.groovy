package availability

import date.IDateTimeService
import groovy.time.TimeCategory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import storage.AvailabilityStorage

class DisplaysManager {

    AvailabilityStorage storage

    AvailabilityParams availabilityParams

    IDateTimeService dateTimeService

    Log log = LogFactory.getLog(DisplaysManager.class)

    DisplaysManager(AvailabilityParams availabilityParams, AvailabilityStorage storage, IDateTimeService dateTimeService) {

        this.availabilityParams = availabilityParams
        this.storage = storage
        this.dateTimeService = dateTimeService
    }

    def recalculate() {

        TimeDuration period = new Period()

        if (period.shouldRecalculate()) {
            period.recalculate()
        }
        else {

            TimeDuration slot = new Slot()

            if (slot.shouldRecalculate())
                slot.recalculate()
        }

    }

    abstract class TimeDuration {

        void recalculate() {

            // Calculate instance vars
            Integer displays = getDisplays()
            Integer clicks = storage.getClicks()

            // Initial values
            displays += availabilityParams.displaysExpectedPerSlot

            def clickCorrection = correctClicks(displays, clicks)

            displays = clickCorrection.displays
            clicks = clickCorrection.clicks + availabilityParams.clicksExpectedPerSlot

            // Generate end dates
            Date slotEnd = nowPlusSeconds(availabilityParams.slotInSeconds)

            storage.setClicks(clicks)
            storage.setDisplays(displays)
            storage.setSlotEnd(slotEnd)

            doRecalculate()
        }

        abstract void doRecalculate()
        abstract Integer getDisplays()
        abstract Boolean shouldRecalculate()

        Map correctClicks(Integer displays, Integer clicks) {

            if (clicks < 0) {
                Integer extraClicks = Math.abs(clicks)
                displays -= (extraClicks * availabilityParams.clickConvergenceRate)
                clicks -= extraClicks
            }

            [displays: displays, clicks: clicks]
        }

        Date nowPlusSeconds(Integer seconds) {

            Date date = null

            use(TimeCategory) {
                date = dateTimeService.dateTime + seconds.seconds
            }

            date
        }
    }

    class Slot extends TimeDuration {

        @Override
        void doRecalculate() {

        }

        @Override
        Integer getDisplays() {
            return storage.getDisplays()
        }

        @Override
        Boolean shouldRecalculate() {

            Date slotEnd = storage.getSlotEnd()
            slotEnd ? (dateTimeService.dateTime >= slotEnd) : true
        }
    }

    class Period extends TimeDuration {

        @Override
        void doRecalculate() {

            Date periodEnd = nowPlusSeconds(availabilityParams.slotInSeconds * availabilityParams.slotsPerPeriod)
            storage.setPeriodEnd(periodEnd)
        }

        @Override
        Integer getDisplays() {
            return 0
        }

        @Override
        Boolean shouldRecalculate() {

            Date periodEnd = storage.getPeriodEnd()
            periodEnd ? (dateTimeService.dateTime >= periodEnd) : true
        }

        Map correctClicks(Integer displays, Integer clicks) {

            if (clicks > 0)
                [displays: displays, clicks: 0]
            else
                return super.correctClicks(displays, clicks)
        }
    }

    String getDisplayManagerInfo() {
        return storage.getStorageInfo()
    }
}
