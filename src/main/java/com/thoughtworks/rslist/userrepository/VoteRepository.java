package com.thoughtworks.rslist.userrepository;

import com.thoughtworks.rslist.entity.VoteEntity;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll(Pageable pageable);

    List<VoteEntity> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
    // findBy的是Entity中自定义的属性名，不是属性
    List<VoteEntity> findByVoteTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
