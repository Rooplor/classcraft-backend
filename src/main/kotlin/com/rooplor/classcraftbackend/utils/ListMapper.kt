package com.rooplor.classcraftbackend.utils

import org.modelmapper.ModelMapper

object ListMapper {
    fun getInstance(): ListMapper = this

    fun <S, T> mapList(
        source: List<S>,
        targetClass: Class<T>,
        modelMapper: ModelMapper,
    ): List<T> = source.map { entity -> modelMapper.map(entity, targetClass) }
}
