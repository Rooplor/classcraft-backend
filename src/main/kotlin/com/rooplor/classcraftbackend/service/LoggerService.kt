package com.rooplor.classcraftbackend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LoggerService {
    private fun getLogger(): Logger {
        val callerClassName = Thread.currentThread().stackTrace[3].className
        return LoggerFactory.getLogger(Class.forName(callerClassName))
    }

    fun info(message: String) {
        getLogger().info(message)
    }

    fun error(message: String) {
        getLogger().error(message)
    }

    fun waring(message: String) {
        getLogger().warn(message)
    }
}
