package availability

import date.DateTimeServiceMock
import storage.AvailabilityStorage
import storage.RedisMock

class DisplaysManagerTest extends GroovyTestCase {

    AvailabilityStorage storage

    DateTimeServiceMock dateTimeService

    public void setUp() {
        storage = new AvailabilityStorage(new RedisMock())
        dateTimeService = new DateTimeServiceMock()
    }

    public void testPeriodWithNoClicks() {

        // GIVEN:
        AvailabilityParams params = new AvailabilityParams([slotInSeconds: 5, slotsPerPeriod: 2, clickConvergenceRate: 0.2d, clicksExpectedPerSlot: 1])
        DisplaysManager displaysManager = new DisplaysManager(params, storage, dateTimeService)

        // Period 1 - Slot 1
        dateTimeService.setDate(getDate("01/01/2015 00:00:00"))
        displaysManager.recalculate()

        assert storage.displays == 5
        assert storage.clicks == 1
        assert storage.slotEnd == getDate("01/01/2015 00:00:05")
        assert storage.periodEnd == getDate("01/01/2015 00:00:10")

        // Period 1 - Slot 2
        dateTimeService.setDate(getDate("01/01/2015 00:00:05"))
        displaysManager.recalculate()

        assert storage.displays == 10
        assert storage.clicks == 2
        assert storage.slotEnd == getDate("01/01/2015 00:00:10")
        assert storage.periodEnd == getDate("01/01/2015 00:00:10")

        // Period 2 - Slot 1
        dateTimeService.setDate(getDate("01/01/2015 00:00:10"))
        displaysManager.recalculate()

        assert storage.displays == 5
        assert storage.clicks == 1
        assert storage.slotEnd == getDate("01/01/2015 00:00:15")
        assert storage.periodEnd == getDate("01/01/2015 00:00:20")
    }

    Date getDate(String date) {
        Date.parse("dd/MM/yyyy HH:mm:ss", date)
    }
}
