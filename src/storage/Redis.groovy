package storage

interface Redis {

    def get(String key)

    void set(String key,def value)

    def increment(key)

    def decrement(key)
}
