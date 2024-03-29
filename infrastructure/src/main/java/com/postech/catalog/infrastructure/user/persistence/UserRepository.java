package com.postech.catalog.infrastructure.user.persistence;

import com.postech.catalog.domain.user.UserPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends JpaRepository<UserJpaEntity, String> {
    @Query("""
            select distinct new com.postech.catalog.domain.user.UserPreview(
                  u.id,
                  u.name,
                  u.email,
                  u.createdAt
            )
            from User u
                left join u.videos favorites
            where
                ( :terms is null or UPPER(u.name) like :terms )
            and
                ( :favorites is null or favorites.id.videoId in :favorites )
            """)
    Page<UserPreview> findAll(
            @Param("terms") String terms,
            @Param("favorites") Set<String> favorites,
            Pageable page
    );

    @Query("SELECT COUNT(DISTINCT uv.id.videoId) FROM User u JOIN u.videos uv")
    long totalFavorites();

    @Modifying
    @Query(value = "Delete from UserVideo uv where uv.id.userId = :userId")
    void deleteUserFromUserVideo(@Param("userId") String userId);

}
