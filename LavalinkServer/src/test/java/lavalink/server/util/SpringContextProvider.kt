package lavalink.server.util

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class SpringContextProvider : ApplicationContextAware {

    lateinit var context: AbstractApplicationContext
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext as AbstractApplicationContext
        staticContext = applicationContext
        latch.countDown()
    }

    companion object {
        val latch = CountDownLatch(1)
        var staticContext: AbstractApplicationContext? = null
    }

}
