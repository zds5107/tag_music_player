package com.zachstopper.tagmusicplayer.model

data class TagFilterRules(
    val exclude: Set<Long>,
    val includeTier1: Set<Long>,
    val includeTier2: Set<Long>
)