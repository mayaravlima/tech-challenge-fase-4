package com.postech.catalog.application.category.retrieve.get;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String input) {
        final var categoryId = CategoryID.from(input);
        return this.categoryGateway.findById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryId));
    }

    private static Supplier<DomainException> notFound(CategoryID id) {
        return () -> NotFoundException.with(
                Category.class,
                id
        );
    }
}
