package com.moove.tickets.data.local

import com.moove.tickets.data.local.dto.RyderDTO
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class TicketsLocalDataSource(
    private val moshi: Moshi
) {

    // TODO json structure is bad
    private val data = "{\n" +
            "  \"Adult\": {\n" +
            "    \"fares\": [\n" +
            "      { \"description\": \"2.5 Hour Ticket\", \"price\": 2.5 },\n" +
            "      { \"description\": \"1 Day Pass\", \"price\": 5.0 },\n" +
            "      { \"description\": \"30 Day Pass\", \"price\": 100 }\n" +
            "    ],\n" +
            "    \"subtext\": null\n" +
            "  },\n" +
            "  \"Child\": {\n" +
            "    \"fares\": [\n" +
            "      { \"description\": \"2.5 Hour Ticket\", \"price\": 1.5 },\n" +
            "      { \"description\": \"1 Day Pass\", \"price\": 2.0 },\n" +
            "      { \"description\": \"30 Day Pass\", \"price\": 40.0 }\n" +
            "    ],\n" +
            "    \"subtext\": \"Ages 8-17\"\n" +
            "  },\n" +
            "  \"Senior\": {\n" +
            "    \"fares\": [\n" +
            "      { \"description\": \"2.5 Hour Ticket\", \"price\": 1.0 },\n" +
            "      { \"description\": \"1 Day Pass\", \"price\": 2.0 },\n" +
            "      { \"description\": \"30 Day Pass\", \"price\": 40.0 }\n" +
            "    ],\n" +
            "    \"subtext\": \"Ages 60+\"\n" +
            "  }\n" +
            "}\n"

    fun getData(): Map<String, RyderDTO> {
        val type = Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            RyderDTO::class.java
        )
        val adapter: JsonAdapter<Map<String, RyderDTO>> = moshi.adapter<Map<String, RyderDTO>>(type)
            .serializeNulls()
            .lenient()
            .nullSafe()

        return adapter.fromJson(data)!!
    }
}
