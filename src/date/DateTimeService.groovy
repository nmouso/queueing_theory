package date

import groovy.time.TimeCategory

class DateTimeService implements IDateTimeService {

    @Override
    Date getDateTime() {
        return new Date()
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
