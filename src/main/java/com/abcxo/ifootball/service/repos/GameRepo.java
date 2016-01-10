package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface GameRepo extends JpaRepository<Game, Long> {
    Page<Game> findByUidInAndUid2In(List<Long> uids, List<Long> uid2s, Pageable pageable);

}
