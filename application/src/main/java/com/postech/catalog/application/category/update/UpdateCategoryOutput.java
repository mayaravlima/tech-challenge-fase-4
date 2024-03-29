package com.postech.catalog.application.category.update;

import com.postech.catalog.domain.catagory.Category;

public record UpdateCategoryOutput(
        String id
) {

    public static UpdateCategoryOutput from(final String id) {
        return new UpdateCategoryOutput(id);
    }

    public static UpdateCategoryOutput from(final Category category) {
        return new UpdateCategoryOutput(category.getId().getValue());
    }
}
