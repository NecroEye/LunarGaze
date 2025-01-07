package com.muratcangzm.lunargaze.common.entity

import javax.annotation.concurrent.Immutable

@Immutable
data class ImmutableWrapper<T>(val value:T)