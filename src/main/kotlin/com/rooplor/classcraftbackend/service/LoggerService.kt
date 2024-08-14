package com.rooplor.classcraftbackend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LoggerService {
    private val logger: Logger = LoggerFactory.getLogger(LoggerService::class.java)

    fun info(message: String) {
        logger.info(message)
    }

    fun error(message: String) {
        logger.error(message)
    }

    fun waring(message: String) {
        logger.warn(message)
    }
}
