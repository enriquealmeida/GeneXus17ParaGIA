package com.genexus.android.maps

interface OnMapLifecycleEvent {
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}
