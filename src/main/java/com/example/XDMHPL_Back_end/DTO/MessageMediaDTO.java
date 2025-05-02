package com.example.XDMHPL_Back_end.DTO;

public class MessageMediaDTO {
    private String mediaType;
    private String mediaURL;

    public MessageMediaDTO(int messageMediaID, String mediaType, String mediaURL) {
        this.mediaType = mediaType;
        this.mediaURL = mediaURL;
    }
    

    // Getters & Setters
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }
}
