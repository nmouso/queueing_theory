package storage

import groovy.transform.Synchronized

class RedisMock implements Redis {

    Map map = [:]

    def get(String key) {
        map[key]
    }

    @Synchronized
    void set(String key,def value) {
        map[key]=value
    }
    @Synchronized
    def increment(key) {

        if (!map[key])
            map[key]=0

        map[key]+=1
    }
    @Synchronized
    def decrement(key) {

        if (!map[key])
            map[key]=0

        map[key]-=1
    }
}
