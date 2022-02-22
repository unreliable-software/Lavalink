package lavalink.server.integration

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import lavalink.plugin.MockConfigurator
import lavalink.server.util.SharedSpringContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SharedSpringContext::class)
class ConfiguratorTest {

    @Test
    fun configuratorTest(apm: AudioPlayerManager) {
        Assertions.assertNotNull(apm.source(MockConfigurator.MockAudioSourceManager::class.java))
    }

}