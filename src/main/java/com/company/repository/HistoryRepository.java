package com.company.repository;

import com.company.domain.History;
import com.company.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
@org.springframework.stereotype.Repository
public interface HistoryRepository extends Repository<History, String> {
    Page<History> findByUserIdAndDeletedOrderByLastViewDateDesc(String id, boolean deleted, Pageable pageable);


    History findById(String id);

    History save(History history);


}
