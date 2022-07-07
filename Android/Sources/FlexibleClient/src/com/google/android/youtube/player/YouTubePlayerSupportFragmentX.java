package com.google.android.youtube.player;

// youtube-android-player-api-1.2.2.jar fragment doesn't support AndroidX,
// use this one based on https://gist.github.com/medyo/f226b967213c3b8ec6f6bebb5338a492

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.youtube.player.internal.ab;

@SuppressWarnings("deprecation")
public class YouTubePlayerSupportFragmentX extends Fragment implements YouTubePlayer.Provider {
	private final A aObj = new A();
	private Bundle bundle;
	private YouTubePlayerView playerView;
	private String string;
	private YouTubePlayer.OnInitializedListener listener;
	private boolean bool;

	public static YouTubePlayerSupportFragmentX newInstance() {
		return new YouTubePlayerSupportFragmentX();
	}

	public YouTubePlayerSupportFragmentX() {
	}

	@Override
	public void initialize(String var1, YouTubePlayer.OnInitializedListener var2) {
		this.string = ab.a(var1, "Developer key cannot be null or empty");
		this.listener = var2;
		this.a();
	}

	private void a() {
		if (this.playerView != null && this.listener != null) {
			this.playerView.a(this.bool);
			this.playerView.a(this.getActivity(), this, this.string, this.listener, this.bundle);
			this.bundle = null;
			this.listener = null;
		}
	}

	@Override
	public void onCreate(Bundle var1) {
		super.onCreate(var1);
		this.bundle = var1 != null ? var1.getBundle("YouTubePlayerSupportFragment.KEY_PLAYER_VIEW_STATE") : null;
	}

	@Override
	public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
		this.playerView = new YouTubePlayerView(this.getActivity(), (AttributeSet)null, 0, this.aObj);
		this.a();
		return this.playerView;
	}

	@Override
	public void onStart() {
		super.onStart();
		this.playerView.a();
	}

	@Override
	public void onResume() {
		super.onResume();
		this.playerView.b();
	}

	@Override
	public void onPause() {
		this.playerView.c();
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle var1) {
		super.onSaveInstanceState(var1);
		Bundle var2 = this.playerView != null ? this.playerView.e() : this.bundle;
		var1.putBundle("YouTubePlayerSupportFragment.KEY_PLAYER_VIEW_STATE", var2);
	}

	@Override
	public void onStop() {
		this.playerView.d();
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		this.playerView.c(this.getActivity().isFinishing());
		this.playerView = null;
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (this.playerView != null) {
			FragmentActivity var1 = this.getActivity();
			this.playerView.b(var1 == null || var1.isFinishing());
		}

		super.onDestroy();
	}

	private final class A implements YouTubePlayerView.b {
		private A() {
		}

		@Override
		public final void a(YouTubePlayerView var1, String var2, YouTubePlayer.OnInitializedListener var3) {
			YouTubePlayerSupportFragmentX.this.initialize(var2, YouTubePlayerSupportFragmentX.this.listener);
		}

		@Override
		public final void a(YouTubePlayerView var1) {
		}
	}
}
