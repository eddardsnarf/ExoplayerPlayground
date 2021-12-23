package com.greensocks.exoplayerplayground.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.EventLogger
import com.greensocks.exoplayerplayground.adapter.MainAdapter
import com.greensocks.exoplayerplayground.model.VideoModel
import com.greensocks.exoplayerplayground.databinding.ActivityMainBinding
import com.greensocks.exoplayerplayground.model.TextModel
import com.greensocks.exoplayerplayground.view.AutoplayVideoManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityMainBinding.inflate(layoutInflater)) {
            setContentView(root)
            binding = this
        }
// single player layout
//        with(binding.videoView) {
//            setThumbnail("https://i.imgflip.com/atevl.jpg")
//            setMediaItem(
//                MediaItem.Builder()
//                    .setMediaId("https://i.imgur.com/TxuQoIC.mp4")
//                    .setUri("https://i.imgur.com/TxuQoIC.mp4")
//                    .build()
//            )
//            attachPlayer(
//                ExoPlayer.Builder(context).build()
//                    .apply { addAnalyticsListener(EventLogger(null)) })
//            resumePlayer()
//        }
        val factory: CacheDataSource.Factory = run {
            val httpDataSourceFactory =
                DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
            val defaultDataSourceFactory: DefaultDataSource.Factory =
                DefaultDataSource.Factory(this, httpDataSourceFactory)
            val leastRecentlyUsedCacheEvictor =
                LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
            val simpleCache =
                SimpleCache(
                    cacheDir,
                    leastRecentlyUsedCacheEvictor,
                    StandaloneDatabaseProvider(this)
                )

            CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }
        binding.recyclerView.apply {
            setupPlayerManager(
                AutoplayVideoManager(this,ExoPlayer.Builder(context).build())
            )

            adapter = MainAdapter(factory).apply {
                updateItemList(
                    arrayListOf(
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/TxuQoIC.mp4"
                        ),
                        TextModel("A Little Bird Told Me"),
                        TextModel("Playing For Keeps"),

                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/TxuQoIC.mp4"
                        ),
                        TextModel("A Busy Body"),

                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/vbEKooy.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/O0rn40A.mp4"
                        ),
                        TextModel("Back to Square One"),
                        TextModel("Beating Around the Bush"),
                        TextModel("A Lone Wolf"),

                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/SK3NXtl.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/gH4Mo81.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/xOHijRn.mp4"
                        ),
                        TextModel("Roll With the Punches"),


                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/4hMDYcR.mp4"
                        ),
                        TextModel("On the Same Page"),

                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/O0rn40A.mp4"
                        ),
                        TextModel("Ride Him, Cowboy!"),

                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/SK3NXtl.mp4"
                        ),
                        TextModel("Know the Ropes"),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/gH4Mo81.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/xOHijRn.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/4hMDYcR.mp4"
                        ), VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/O0rn40A.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/SK3NXtl.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/gH4Mo81.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/xOHijRn.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/4hMDYcR.mp4"
                        ), VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/O0rn40A.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/SK3NXtl.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/gH4Mo81.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/xOHijRn.mp4"
                        ),
                        VideoModel(
                            thumbnailUrl = "https://i.imgflip.com/atevl.jpg",
                            vidUrl = "https://i.imgur.com/4hMDYcR.mp4"
                        )
                    )
                )
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerView.resumeCurrentVideo()
    }

    override fun onStop() {
        binding.recyclerView.pauseCurrentVideo()
        super.onStop()
    }

    override fun onDestroy() {
        binding.recyclerView.destroy()
        super.onDestroy()
    }
}
