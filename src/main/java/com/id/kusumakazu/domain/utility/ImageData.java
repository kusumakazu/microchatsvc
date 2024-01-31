package com.id.kusumakazu.domain.utility;

import java.util.Arrays;

public class ImageData {

    private String imageName;
    private byte[] imageData;

    public ImageData(String imageName, byte[] imageData) {
        this.imageName = imageName;
        this.imageData = imageData;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "ImageData{" + "imageName='" + imageName + '\'' + ", imageData=" + Arrays.toString(imageData) + '}';
    }
}
