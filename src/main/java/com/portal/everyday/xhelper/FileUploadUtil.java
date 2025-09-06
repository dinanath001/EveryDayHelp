package com.portal.everyday.xhelper;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;



public class FileUploadUtil {

    // Static method to handle file upload
    public static String saveFile(MultipartFile file, String folderName) throws IOException {

        // 1. Get the root path of the project
        String projectRoot = System.getProperty("user.dir");

        // 2. Build the full upload directory path
        String uploadPath = projectRoot + File.separator + "uploads" + File.separator + folderName;

        // 3. Create the directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // create uploads/folderName if missing
        }

        // 4. Get original file name
        String originalFilename = file.getOriginalFilename();

        // 5. Create unique file name (optional but safer)
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;

        // 6. Create the destination file object
        File destinationFile = new File(uploadDir, uniqueFilename);

        // 7. Transfer the uploaded file to the destination path
        file.transferTo(destinationFile);

        // 8. Return relative path to store in DB (e.g., "uploads/employee/xyz.jpg")
        return "uploads/" + folderName + "/" + uniqueFilename;
    }
}
