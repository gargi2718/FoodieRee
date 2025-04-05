package com.example.foodieree.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodieree.R;
import com.example.foodieree.models.Reel;
import com.google.android.material.button.MaterialButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ReelViewHolder> {

    private final Context context;
    private final List<Reel> reels;
    private final Lifecycle lifecycle;
    private int currentPlayingPosition = -1;

    public ReelAdapter(Context context, List<Reel> reels, Lifecycle lifecycle) {
        this.context = context;
        this.reels = reels;
        this.lifecycle = lifecycle;
    }

    public void setCurrentPlayingPosition(int position) {
        currentPlayingPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        holder.bind(reels.get(position), position);
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    class ReelViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        TextView foodTitle, restaurantInfo;
        ImageView playPauseButton;
        MaterialButton craveOrderButton;
        ProgressBar progressBar;
        YouTubePlayer youTubePlayer;
        boolean isPlaying = false;

        ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
            foodTitle = itemView.findViewById(R.id.foodTitle);
            restaurantInfo = itemView.findViewById(R.id.restaurantInfo);
            playPauseButton = itemView.findViewById(R.id.playPauseButton);
            craveOrderButton = itemView.findViewById(R.id.craveOrderButton);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
/*
        void bind(Reel reel, int position) {
            foodTitle.setText(reel.getFoodTitle());
            restaurantInfo.setText(reel.getRestaurantName() + " • " + reel.getDistance());
            progressBar.setVisibility(View.VISIBLE);
            playPauseButton.setVisibility(View.GONE);

            lifecycle.addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer player) {
                    youTubePlayer = player;
                    String videoId = extractVideoId(reel.getVideoUrl());

                    if (position == currentPlayingPosition) {
                        youTubePlayer.loadVideo(videoId, 0);
                        isPlaying = true;
                        playPauseButton.setImageResource(R.drawable.ic_pause);
                    } else {
                        youTubePlayer.cueVideo(videoId, 0);
                        isPlaying = false;
                        playPauseButton.setImageResource(R.drawable.ic_play);
                    }

                    // Hide default controls
                    youTubePlayerView.setEnableAutomaticInitialization(false);
                    progressBar.setVisibility(View.GONE);
                    playPauseButton.setVisibility(View.VISIBLE);
                }
            });

            playPauseButton.setOnClickListener(v -> {
                if (youTubePlayer == null) return;

                if (isPlaying) {
                    youTubePlayer.pause();
                } else {
                    youTubePlayer.play();
                }

                isPlaying = !isPlaying;
                playPauseButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            });

            craveOrderButton.setOnClickListener(v -> {
                Toast.makeText(context, "Ordering " + reel.getFoodTitle(), Toast.LENGTH_SHORT).show();
            });
        }
*/
        // In ReelViewHolder class, update these methods:

        void bind(Reel reel, int position) {
            foodTitle.setText(reel.getFoodTitle());
            restaurantInfo.setText(reel.getRestaurantName() + " • " + reel.getDistance());
            progressBar.setVisibility(View.VISIBLE);
            playPauseButton.setVisibility(View.GONE);

            lifecycle.addObserver(youTubePlayerView);

            // Add a click listener to the entire view to toggle controls
            itemView.setOnClickListener(v -> togglePlayPauseButton());

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer player) {
                    youTubePlayer = player;
                    String videoId = extractVideoId(reel.getVideoUrl());

                    if (position == currentPlayingPosition) {
                        youTubePlayer.loadVideo(videoId, 0);
                        isPlaying = true;
                        hidePlayButtonWithDelay();
                    } else {
                        youTubePlayer.cueVideo(videoId, 0);
                        isPlaying = false;
                        playPauseButton.setVisibility(View.VISIBLE);
                    }

                    // Hide default controls
                    youTubePlayerView.setEnableAutomaticInitialization(false);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer,
                                          @NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState state) {
                    if (state == com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState.PLAYING) {
                        isPlaying = true;
                        hidePlayButtonWithDelay();
                    } else if (state == com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState.PAUSED) {
                        isPlaying = false;
                        playPauseButton.setVisibility(View.VISIBLE);
                    }
                }
            });

            playPauseButton.setOnClickListener(v -> {
                if (youTubePlayer == null) return;

                if (isPlaying) {
                    youTubePlayer.pause();
                    isPlaying = false;
                    playPauseButton.setVisibility(View.VISIBLE);
                } else {
                    youTubePlayer.play();
                    isPlaying = true;
                    hidePlayButtonWithDelay();
                }

                updatePlayPauseButton();
            });

            craveOrderButton.setOnClickListener(v -> {
                Toast.makeText(context, "Ordering " + reel.getFoodTitle(), Toast.LENGTH_SHORT).show();
            });
        }

        private void updatePlayPauseButton() {
            playPauseButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        }

        private void hidePlayButtonWithDelay() {
            // Hide play button after a short delay when video starts playing
            playPauseButton.postDelayed(() -> {
                if (isPlaying) {
                    playPauseButton.setVisibility(View.GONE);
                }
            }, 1000); // 1 second delay
        }

        private void togglePlayPauseButton() {
            if (playPauseButton.getVisibility() == View.VISIBLE) {
                if (isPlaying) {
                    playPauseButton.setVisibility(View.GONE);
                }
            } else {
                playPauseButton.setVisibility(View.VISIBLE);
                // Auto-hide after a delay if video is playing
                if (isPlaying) {
                    hidePlayButtonWithDelay();
                }
            }
        }
        private String extractVideoId(String url) {
            if (url.contains("watch?v=")) {
                return url.substring(url.indexOf("v=") + 2);
            } else if (url.contains("youtu.be/")) {
                return url.substring(url.lastIndexOf("/") + 1);
            } else if (url.contains("shorts/")) {
                return url.substring(url.lastIndexOf("/") + 1);
            }
            return "";
        }
    }
}


/*
package com.example.foodieree.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.example.foodieree.R;
import com.example.foodieree.models.Reel;
import java.util.List;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ReelViewHolder> {
    private static final String TAG = "ReelAdapter";
    private final Context context;
    private final List<Reel> reels;
    private int currentPlayingPosition = -1;

    public ReelAdapter(Context context, List<Reel> reels) {
        this.context = context;
        this.reels = reels;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        Reel reel = reels.get(position);
        holder.bind(reel, position);
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    public void setCurrentPlayingPosition(int position) {
        this.currentPlayingPosition = position;
        notifyDataSetChanged();
    }

    class ReelViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView foodTitle, restaurantInfo;
        ImageView playPauseButton;
        MaterialButton craveOrderButton;
        boolean isPlaying = false;

        ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            foodTitle = itemView.findViewById(R.id.foodTitle);
            restaurantInfo = itemView.findViewById(R.id.restaurantInfo);
            playPauseButton = itemView.findViewById(R.id.playPauseButton);
            craveOrderButton = itemView.findViewById(R.id.craveOrderButton);
        }

        void bind(Reel reel, int position) {
            foodTitle.setText(reel.getFoodTitle());
            restaurantInfo.setText(String.format("%s • %s", reel.getRestaurantName(), reel.getDistance()));

            setupVideoPlayer(position);
            setupPlayPauseButton();
        }

        private void setupVideoPlayer(int position) {
            try {
                // Local video path - using resource video
                String videoPath = "android.resource://" + context.getPackageName() + "/raw/sample_video";
                Uri uri = Uri.parse(videoPath);

                videoView.setVideoURI(uri);
                videoView.setOnPreparedListener(mp -> {
                    mp.setLooping(true);
                    playPauseButton.setVisibility(View.VISIBLE);

                    // Auto-play when in view
                    if (position == currentPlayingPosition) {
                        videoView.start();
                        isPlaying = true;
                        updatePlayPauseButton();
                    }
                });

                // Handle visibility changes
                itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        if (position == currentPlayingPosition) {
                            videoView.start();
                            isPlaying = true;
                            updatePlayPauseButton();
                        }
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        videoView.pause();
                        isPlaying = false;
                        updatePlayPauseButton();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setupPlayPauseButton() {
            playPauseButton.setOnClickListener(v -> {
                if (isPlaying) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
                isPlaying = !isPlaying;
                updatePlayPauseButton();
            });
            updatePlayPauseButton();
        }

        private void updatePlayPauseButton() {
            playPauseButton.setImageResource(isPlaying ?
                    R.drawable.ic_pause : R.drawable.ic_play);
        }
    }

    public void releaseAllPlayers() {
        notifyDataSetChanged();
    }
}
/*
package com.example.foodieree.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodieree.R;
import com.example.foodieree.models.Reel;
import java.util.List;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ReelViewHolder> {
    private final Context context;
    private final List<Reel> reels;

    public ReelAdapter(Context context, List<Reel> reels) {
        this.context = context;
        this.reels = reels;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        Reel reel = reels.get(position);
        holder.bind(reel);
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    class ReelViewHolder extends RecyclerView.ViewHolder {
        private final PlayerView playerView;
        private final TextView titleText;
        private final TextView restaurantText;
        private ExoPlayer player;

        ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            titleText = itemView.findViewById(R.id.foodTitle);
            restaurantText = itemView.findViewById(R.id.restaurantInfo);
        }

        void bind(Reel reel) {
            titleText.setText(reel.getFoodTitle());
            restaurantText.setText(reel.getRestaurantName());

            setupPlayer(reel.getVideoUrl());
        }

        private void setupPlayer(String videoUrl) {
            releasePlayer();

            player = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            player.setMediaItem(mediaItem);
            player.setRepeatMode(Player.REPEAT_MODE_ONE);
            player.prepare();
        }

        private void releasePlayer() {
            if (player != null) {
                player.release();
                player = null;
            }
        }
    }

    public void releaseAllPlayers() {
        notifyDataSetChanged();
    }
}
/*
package com.example.foodieree.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.google.android.material.button.MaterialButton;
import com.example.foodieree.R;
import com.example.foodieree.models.Reel;
import java.util.List;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ReelViewHolder> {
    private static final String TAG = "ReelAdapter";
    private final Context context;
    private final List<Reel> reels;
    private int currentPlayingPosition = -1;

    public ReelAdapter(Context context, List<Reel> reels) {
        this.context = context;
        this.reels = reels;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        Reel reel = reels.get(position);
        holder.bind(reel, position);
    }

    @Override
    public int getItemCount() {
        return reels.size();
    }

    class ReelViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        TextView foodTitle, restaurantInfo;
        ImageView playPauseButton;
        MaterialButton craveOrderButton;
        ExoPlayer player;
        boolean isPlaying = false;

        ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            foodTitle = itemView.findViewById(R.id.foodTitle);
            restaurantInfo = itemView.findViewById(R.id.restaurantInfo);
            playPauseButton = itemView.findViewById(R.id.playPauseButton);
            craveOrderButton = itemView.findViewById(R.id.craveOrderButton);
        }

        void bind(Reel reel, int position) {
            foodTitle.setText(reel.getFoodTitle());
            restaurantInfo.setText(String.format("%s • %s", reel.getRestaurantName(), reel.getDistance()));

            setupVideoPlayer(reel.getVideoUrl(), position);
            setupPlayPauseButton();
        }

        private void setupVideoPlayer(String videoUrl, int position) {
            try {
                releasePlayer(); // Release any existing player

                // Create new player
                player = new ExoPlayer.Builder(context).build();
                playerView.setPlayer(player);

                // Configure player
                player.setRepeatMode(Player.REPEAT_MODE_ONE);
                player.setVolume(1f);

                // Create media item
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
                player.setMediaItem(mediaItem);
                player.prepare();

                // Add player listener
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state == Player.STATE_READY) {
                            playPauseButton.setVisibility(View.VISIBLE);
                            if (position == currentPlayingPosition) {
                                player.play();
                                isPlaying = true;
                                updatePlayPauseButton();
                            }
                        }
                    }

                    @Override
                    public void onIsPlayingChanged(boolean playing) {
                        isPlaying = playing;
                        updatePlayPauseButton();
                    }
                });

                // Handle visibility
                itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        if (position == currentPlayingPosition) {
                            player.play();
                        }
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        player.pause();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error setting up video player: ", e);
            }
        }

        private void setupPlayPauseButton() {
            playPauseButton.setOnClickListener(v -> {
                if (player != null) {
                    if (isPlaying) {
                        player.pause();
                    } else {
                        player.play();
                    }
                }
            });
            updatePlayPauseButton();
        }

        private void updatePlayPauseButton() {
            playPauseButton.setImageResource(isPlaying ?
                    R.drawable.ic_pause : R.drawable.ic_play);
        }

        void releasePlayer() {
            if (player != null) {
                player.release();
                player = null;
            }
        }
    }

    public void releaseAllPlayers() {
        currentPlayingPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull ReelViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();
    }
}
*/