package com.detech_digital.nim.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// Clean, lazy logger utility attached to any class
inline fun <reified T : Any> T.logger(): Lazy<Logger> =
    lazy { LoggerFactory.getLogger(T::class.java.name.removeSuffix($$"$Companion")) }