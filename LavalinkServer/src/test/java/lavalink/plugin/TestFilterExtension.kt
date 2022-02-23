package lavalink.plugin

import com.sedmelluq.discord.lavaplayer.filter.FloatPcmAudioFilter
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat
import dev.arbjerg.lavalink.api.AudioFilterExtension
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class TestFilterExtension : AudioFilterExtension {
    val latch = CountDownLatch(1)
    override fun getName() = "test"
    override fun build(data: JSONObject, format: AudioDataFormat, output: FloatPcmAudioFilter): FloatPcmAudioFilter {
        latch.countDown()
        return output
    }
    override fun isEnabled(data: JSONObject) = true
}
