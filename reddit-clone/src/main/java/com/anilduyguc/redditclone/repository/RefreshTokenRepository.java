package com.anilduyguc.redditclone.repository;

import com.anilduyguc.redditclone.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
}
