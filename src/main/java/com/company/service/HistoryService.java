package com.company.service;

import com.company.domain.History;
import com.company.exception.HistoryException;
import com.company.repository.HistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }


    public Page<History> listHistories(String uid, Integer page){
        return historyRepository.findByUserIdAndDeletedOrderByLastViewDateDesc(uid, false, PageRequest.of(page, 10));
    }
    public void  deleteHistory(String uid, String hid) throws HistoryException {
        History history = historyRepository.findById(hid);
        if (!history.getUser().getId().equals(uid)) {
            throw new HistoryException("非法的操作");
        }
        history.setDeleted(true);
        historyRepository.save(history);


    }






}
