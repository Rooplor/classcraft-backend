package com.rooplor.classcraftbackend

import com.rooplor.classcraftbackend.utils.ListMapper
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {
    @Bean
    fun modelMapper(): ModelMapper = ModelMapper()

    @Bean
    fun listMapper(): ListMapper = ListMapper.getInstance()
}
