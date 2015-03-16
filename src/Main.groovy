import availability.AvailabilityParams
import availability.Simulator
import storage.AvailabilityStorage
import storage.RedisMock

AvailabilityParams params = new AvailabilityParams(clicksExpectedPerSlot:1,clickConvergenceRate: 0.4d,slotsPerPeriod:2,slotInSeconds:30)
AvailabilityStorage storage= new AvailabilityStorage(new RedisMock())
new Simulator(params,storage).run()