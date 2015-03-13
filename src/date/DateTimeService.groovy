package date

class DateTimeService implements IDateTimeService {

    @Override
    Date getDateTime() {
        return new Date()
    }
}
