package com.kotlindiscord.site

import kotlinx.coroutines.sync.Mutex

val infractionsMutex = Mutex()

val rolesMutex = Mutex()
val usersMutex = Mutex()
