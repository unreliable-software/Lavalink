package lavalink.plugin

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager
import com.sedmelluq.discord.lavaplayer.track.AudioItem
import com.sedmelluq.discord.lavaplayer.track.AudioReference
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo
import dev.arbjerg.lavalink.api.AudioPlayerManagerConfiguration
import org.springframework.stereotype.Service
import java.io.DataInput
import java.io.DataOutput

@Service
class MockConfigurator : AudioPlayerManagerConfiguration {
    override fun configure(manager: AudioPlayerManager): AudioPlayerManager {
        manager.registerSourceManager(MockAudioSourceManager)
        return manager
    }

    object MockAudioSourceManager : AudioSourceManager {
        override fun getSourceName(): String {
            return "mock"
        }

        override fun loadItem(manager: AudioPlayerManager?, reference: AudioReference?): AudioItem {
            throw RuntimeException("Not implemented")
        }

        override fun isTrackEncodable(track: AudioTrack?) = false

        override fun encodeTrack(track: AudioTrack?, output: DataOutput?) {}

        override fun decodeTrack(trackInfo: AudioTrackInfo?, input: DataInput?): AudioTrack {
            throw RuntimeException("Not implemented")
        }

        override fun shutdown() {}
    }
}