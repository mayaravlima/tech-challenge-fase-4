package com.postech.catalog.infrastructure.category.models.videos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UpdateVideoRequest(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("url") String url,
        @JsonProperty("categories") Set<String> categories
) {
}
