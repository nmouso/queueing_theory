package date

import groovy.time.TimeCategory

class DateTimeServiceMock implements IDateTimeService {

    Date date = new Date()

    void setDate(Date date) {
        this.date = date
    }

    @Override
    Date getDateTime() {
        return this.date
    }

    @Override
    Date nowPlusSeconds(Integer seconds) {

        Date date = null

        use(TimeCategory) {
            date = dateTime + seconds.seconds
        }

        date
    }
}
