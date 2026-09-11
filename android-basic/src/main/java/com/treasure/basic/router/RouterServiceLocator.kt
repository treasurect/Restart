package com.treasure.basic.router

object RouterServiceLocator {
    private val services = mutableMapOf<Class<*>, Any>()

    fun <T> register(service: Class<T>, impl: T) {
        services[service] = impl as Any
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(service: Class<T>): T {
        return services[service] as? T
            ?: throw IllegalStateException("No implementation found for ${service.simpleName}")
    }
}
