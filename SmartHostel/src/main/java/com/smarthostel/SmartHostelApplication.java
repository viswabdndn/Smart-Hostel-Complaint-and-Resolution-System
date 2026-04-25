package com.smarthostel;

import com.smarthostel.service.AutoSaveThread;
import com.smarthostel.service.ComplaintService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartHostelApplication {

    @Autowired
    private ComplaintService complaintService;

    public static void main(String[] args) {
        SpringApplication.run(SmartHostelApplication.class, args);
    }

    /*
     * Preserves the exact logic from the original Main.java:
     *
     *   AutoSaveThread auto = new AutoSaveThread(complaintService);
     *   auto.setPriority(Thread.MIN_PRIORITY);
     *   auto.start();
     *
     * The thread runs in the background and saves complaints every 20 s.
     */
    @PostConstruct
    public void startAutoSave() {
        AutoSaveThread auto = new AutoSaveThread(complaintService);
        auto.setPriority(Thread.MIN_PRIORITY);
        auto.start();
    }
}
