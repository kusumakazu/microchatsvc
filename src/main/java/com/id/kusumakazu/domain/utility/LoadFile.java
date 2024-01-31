package com.id.kusumakazu.domain.utility;

import java.util.Arrays;

public class LoadFile {

    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return (
            "LoadFile{" +
            "filename='" +
            filename +
            '\'' +
            ", fileType='" +
            fileType +
            '\'' +
            ", fileSize='" +
            fileSize +
            '\'' +
            ", file=" +
            Arrays.toString(file) +
            '}'
        );
    }
}
