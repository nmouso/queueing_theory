package availability

class AvailabilityParams {

    Integer slotInSeconds

    Integer slotsPerPeriod

    Double clickConvergenceRate

    Integer clicksExpectedPerSlot

    Integer getDisplaysExpectedPerSlot() {
        return getClicksExpectedPerSlot() / getClickConvergenceRate()
    }
}
