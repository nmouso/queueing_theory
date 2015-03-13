package date

class DateTimeServiceMock implements IDateTimeService {

    Date date = new Date()

    void setDate(Date date) {
        this.date = date
    }

    @Override
    Date getDateTime() {
        return this.date
    }
}
