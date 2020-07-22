package com.ndb.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

fun executeGetRequest(mockMvc: MockMvc, resource: String, responseStatus: ResultMatcher): String =
    mockMvc.perform(
        MockMvcRequestBuilders.get(resource)
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andExpect(responseStatus)
        .andReturn()
        .response.contentAsString

fun executePostRequest(mockMvc: MockMvc, resource: String, body: String, responseStatus: ResultMatcher): String =
    mockMvc.perform(
        MockMvcRequestBuilders.post(resource).content(body)
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andExpect(responseStatus)
        .andReturn()
        .response.contentAsString

val objectMapper = ObjectMapper().apply {
    registerModule(KotlinModule())
    registerModule(JavaTimeModule())
}
