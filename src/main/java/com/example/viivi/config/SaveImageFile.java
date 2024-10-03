package com.example.viivi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Configuration
public class SaveImageFile {

    /**
     * This method handles the file upload and converts the image to a base64-encoded string,
     * which is returned to be stored or displayed as needed.
     *
     * @param imageFile the uploaded image file
     * @return the base64-encoded image string with a proper data URI prefix, or null if there's an error
     */
    public String saveImageFile(MultipartFile imageFile) {
        // Check if the file is not empty and is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Check file size (e.g., max 5 MB)
                if (imageFile.getSize() > 5 * 1024 * 1024) {
                    throw new IOException("File size exceeds 5MB limit.");
                }

                // Get the file bytes
                byte[] photoBytes = imageFile.getBytes();

                // Convert file bytes to Base64
                String photoBase64 = Base64.getEncoder().encodeToString(photoBytes);

                // Generate the Base64 image string with the proper data URI prefix
                // Example: "data:image/jpeg;base64,{base64EncodedImage}"
                String photoBase64WithPrefix = "data:" + imageFile.getContentType() + ";base64," + photoBase64;

                // Return the base64 encoded string with the data URI prefix
                return photoBase64WithPrefix;

            } catch (IOException e) {
                e.printStackTrace();  // Log the error properly in production
                return null;  // Return null in case of an error
            }
        }

        // Return null if the file is empty or null
        return null;
    }
}
