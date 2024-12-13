package vttp.batch5.ssf.noticeboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class HealthCheckService {
    
    @Autowired
    private NoticeRepository noticeRepo;

    public boolean isHealthy() {
        return noticeRepo.isHealthy();
    }
} 