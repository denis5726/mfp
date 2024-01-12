package ru.mfp.common.event

import ru.mfp.common.dto.CommonEventDto

interface EventHandler<T : CommonEventDto> {

    fun getEventClass(): Class<T>

    fun handle(event: T)
}
