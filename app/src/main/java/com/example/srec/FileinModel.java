package com.example.srec;

public class FileinModel {
    private String fileName;
    private String fileUrl;
    private boolean isBlackAndWhiteChecked;
    private boolean isColorChecked;
    private boolean isSpiralChecked;
    private boolean isCaligoChecked;
    private int copiesBlackAndWhite;
    private int copiesColor;
    private int totalCost;
    private String userName;
    private String userEmail;

    public FileinModel() {
        // Default constructor required for calls to DataSnapshot.getValue(FileUploadModel.class)
    }

    public FileinModel(String fileName, String fileUrl, boolean isBlackAndWhiteChecked, boolean isColorChecked, boolean isSpiralChecked, boolean isCaligoChecked, int copiesBlackAndWhite, int copiesColor, int totalCost, String userName, String userEmail) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.isBlackAndWhiteChecked = isBlackAndWhiteChecked;
        this.isColorChecked = isColorChecked;
        this.isSpiralChecked = isSpiralChecked;
        this.isCaligoChecked = isCaligoChecked;
        this.copiesBlackAndWhite = copiesBlackAndWhite;
        this.copiesColor = copiesColor;
        this.totalCost = totalCost;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public boolean isBlackAndWhiteChecked() {
        return isBlackAndWhiteChecked;
    }

    public boolean isColorChecked() {
        return isColorChecked;
    }

    public boolean isSpiralChecked() {
        return isSpiralChecked;
    }

    public boolean isCaligoChecked() {
        return isCaligoChecked;
    }

    public int getCopiesBlackAndWhite() {
        return copiesBlackAndWhite;
    }

    public int getCopiesColor() {
        return copiesColor;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
