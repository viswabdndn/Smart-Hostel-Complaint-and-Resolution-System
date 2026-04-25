package com.smarthostel.service;

/*
 * Preserved verbatim from the original service.AutoSaveThread:
 *   - extends Thread, daemon=true
 *   - sleeps 20 seconds, then calls FileService.save(...)
 *   - interrupting it stops the loop; other exceptions are swallowed silently
 *   - no printing
 *
 * It is NOT a Spring @Component: the original code instantiates it
 * manually in Main with `new AutoSaveThread(complaintService)` and calls
 * `.start()`, so we keep the same lifecycle in SmartHostelApplication's
 * @PostConstruct.
 */
public class AutoSaveThread extends Thread {

    private ComplaintService service;
    private boolean running = true;

    public AutoSaveThread(ComplaintService service) {
        this.service = service;
        setDaemon(true);   // runs in background safely
    }

    public void stopAutoSave() {
        running = false;
    }

    public void run() {

        while (running) {

            try {

                Thread.sleep(20000);   // 20 seconds

                FileService.save(
                        service.getAllComplaints()
                );

                // NO printing here

            } catch (InterruptedException e) {
                running = false;
            } catch (Exception e) {
                // silent handling
            }
        }
    }
}
