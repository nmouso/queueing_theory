package storage

class AvailabilityStorage {

    Redis redis

    private static final String CLICKS = "_clicks"
    private static final String DISPLAYS = "_displays"
    private static final String SLOT_END = "_slot_end"
    private static final String PERIOD_END = "_period_end"

    AvailabilityStorage(Redis redis) {
        this.redis = redis
    }

    Integer getClicks() {
        return redis.get(CLICKS) ?: 0
    }

    void setClicks(Integer clicks) {
        redis.set(CLICKS, clicks)
    }

    void decrementClicks(){
        redis.decrement(CLICKS)
    }

    Integer getDisplays() {
        return redis.get(DISPLAYS) ?: 0
    }

    void decrementDisplays(){
        redis.decrement(DISPLAYS)
    }


    void setDisplays(Integer displays) {
        redis.set(DISPLAYS, displays)
    }

    Date getSlotEnd() {
        redis.get(SLOT_END)
    }

    void setSlotEnd(Date slotEnd) {
        redis.set(SLOT_END, slotEnd)
    }

    Date getPeriodEnd() {
        redis.get(PERIOD_END)
    }

    void setPeriodEnd(Date periodEnd) {
        redis.set(PERIOD_END, periodEnd)
    }

    String getStorageInfo() {
        return "Clicks: ${this.clicks} - Displays: ${this.displays} - Slot End: ${this.slotEnd} - Period End: ${this.periodEnd}"
    }
}
