package lavalink.server.util

import lavalink.server.Launcher
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.support.AbstractApplicationContext
import java.io.File
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/** Adapted from FredBoat */
class SharedSpringContext : ParameterResolver, BeforeAllCallback {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SharedSpringContext::class.java)
        private var application: AbstractApplicationContext? = null
    }

    override fun beforeAll(context: ExtensionContext) {
        // Delete mock plugin so it is always downloaded
        File("plugin").listFiles()?.forEach {
            if (it.startsWith("lavalink-mock-plugin")) it.delete()
        }

        if (application != null) return // Don't start the application again

        log.info("Initializing test context")
        thread {
            try {
                Launcher.main(emptyArray())
            } catch (t: Throwable) {
                log.error("Failed initializing context", t)
                exitProcess(-1)
            }
        }
        val start = currentTimeMillis()
        SpringContextProvider.latch.await(2, TimeUnit.MINUTES)
        log.info("Acquired Spring context after ${(currentTimeMillis() - start)/1000} seconds")
        application = SpringContextProvider.staticContext

        log.info("Successfully initialized test context ${application!!.javaClass.simpleName}")
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return getBean(parameterContext) != null
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? {
        return getBean(parameterContext)
    }

    private fun getBean(parameterContext: ParameterContext): Any? {
        val qualifier = parameterContext.parameter.annotations
            .find { it is Qualifier }
                as Qualifier?
            ?: return application!!.getBean(parameterContext.parameter.type)

        return application!!.getBean(qualifier.value, parameterContext.parameter.type)
    }
}