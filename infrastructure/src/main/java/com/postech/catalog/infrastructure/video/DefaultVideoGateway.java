package com.postech.catalog.infrastructure.video;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.video.*;
import com.postech.catalog.infrastructure.utils.SqlUtils;
import com.postech.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.postech.catalog.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.postech.catalog.domain.utils.CollectionUtils.mapTo;
import static com.postech.catalog.domain.utils.CollectionUtils.nullIfEmpty;

@Component
public class DefaultVideoGateway implements VideoGateway {
    private final VideoRepository videoRepository;

    public DefaultVideoGateway(
            final VideoRepository videoRepository
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(final Video video) {
        return save(video);
    }

    @Override
    public void deleteById(final VideoID id) {
        final var videoId = id.getValue();
        if (this.videoRepository.existsById(videoId)) {
            this.videoRepository.deleteById(videoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID id) {
        return this.videoRepository.findById(id.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video video) {
        return save(video);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(final Video video) {

        return this.videoRepository.save(VideoJpaEntity.from(video))
                .toAggregate();
    }
}
