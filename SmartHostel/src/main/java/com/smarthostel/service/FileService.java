package com.smarthostel.service;

import com.smarthostel.model.Complaint;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/*
 * Preserved verbatim from the original service.FileService.
 * Writes every complaint as:
 *     id,studentId,type,description,status
 * into a "complaints.txt" file in the working directory, one record
 * per line. Still called by AutoSaveThread every 20 seconds.
 */
public class FileService {

    public static void save(Collection<Complaint> list)
            throws IOException {

        BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter("complaints.txt"));

        for (Complaint c : list) {

            bw.write(c.getComplaintId() + "," +
                     c.getStudentId() + "," +
                     c.getType() + "," +
                     c.getDescription() + "," +
                     c.getStatus());

            bw.newLine();
        }

        bw.close();
    }
}
