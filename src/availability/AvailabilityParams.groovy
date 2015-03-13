package availability

class AvailabilityParams {

    Integer getSlotInSeconds() {
        return 5
    }

    Integer getSlotsPerPeriod() {
        return 4
    }

    Double getClickConvergenceRate() {
        return 0.2d
    }

    Integer getClicksExpectedPerSlot() {
        return 1
    }

    Integer getDisplaysExpectedPerSlot() {
        return getClicksExpectedPerSlot() / getClickConvergenceRate()
    }
}
