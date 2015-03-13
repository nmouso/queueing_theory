import availability.AvailabilityParams
import availability.DisplaysManager
import storage.AvailabilityStorage
import storage.RedisMock

RedisMock redis = new RedisMock()
AvailabilityStorage storage = new AvailabilityStorage(redis)
AvailabilityParams params = new AvailabilityParams()

DisplaysManager displaysManager = new DisplaysManager(params, storage)

displaysManager.recalculate()
println displaysManager.getDisplayManagerInfo()
sleep 5000

displaysManager.recalculate()
println displaysManager.getDisplayManagerInfo()
sleep 5000

displaysManager.recalculate()
println displaysManager.getDisplayManagerInfo()
sleep 5000

displaysManager.recalculate()
println displaysManager.getDisplayManagerInfo()
sleep 5000

displaysManager.recalculate()
println displaysManager.getDisplayManagerInfo() // New period