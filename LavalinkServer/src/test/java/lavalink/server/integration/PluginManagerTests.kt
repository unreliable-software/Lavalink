package lavalink.server.integration

import lavalink.plugin.MockConfigurator
import lavalink.server.bootstrap.PluginManager
import lavalink.server.util.SharedSpringContext
import lavalink.server.util.SpringContextProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SharedSpringContext::class)
class PluginManagerTests {

    @Test
    fun testJarPluginLoads(pluginManager: PluginManager) {
        Assertions.assertTrue(pluginManager.pluginManifests.any { it.name == "mock" })
        val type = pluginManager.classLoader.loadClass("dev.arbjerg.mock.MockPlugin")
        Assertions.assertNotNull(SpringContextProvider.staticContext!!.getBean(type))
    }

    @Test
    fun testClasspathPluginLoads(pluginManager: PluginManager) {
        Assertions.assertTrue(pluginManager.pluginManifests.any { it.name == "integration" })
        Assertions.assertNotNull(SpringContextProvider.staticContext!!.getBean(MockConfigurator::class.java))
    }

}