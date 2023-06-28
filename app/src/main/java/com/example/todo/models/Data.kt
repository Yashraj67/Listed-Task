package com.example.todo.models

import com.google.gson.JsonObject

data class Data(
    val overall_url_chart: JsonObject,
    val recent_links: List<RecentLink>,
    val top_links: List<TopLink>
)