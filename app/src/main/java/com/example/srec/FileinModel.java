package com.example.srec;

public class FileinModel {
    private String fileName;
    private String fileUrl;
    private boolean blackAndWhiteChecked;
    private boolean colorChecked;
    private boolean spiralChecked;
    private boolean caligoChecked;
    private int copiesBlackAndWhite;
    private int copiesColor;
    private int totalCost;
    private String userEmail;
    private String userName;

    // Add constructor, getters, and setters
    public FileinModel() {
    }

    public FileinModel(String fileName, String fileUrl, boolean blackAndWhiteChecked, boolean colorChecked, boolean spiralChecked, boolean caligoChecked, int copiesBlackAndWhite, int copiesColor, int totalCost, String userEmail, String userName) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.blackAndWhiteChecked = blackAndWhiteChecked;
        this.colorChecked = colorChecked;
        this.spiralChecked = spiralChecked;
        this.caligoChecked = caligoChecked;
        this.copiesBlackAndWhite = copiesBlackAndWhite;
        this.copiesColor = copiesColor;
        this.totalCost = totalCost;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    // Getters and setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isBlackAndWhiteChecked() {
        return blackAndWhiteChecked;
    }

    public void setBlackAndWhiteChecked(boolean blackAndWhiteChecked) {
        this.blackAndWhiteChecked = blackAndWhiteChecked;
    }

    public boolean isColorChecked() {
        return colorChecked;
    }

    public void setColorChecked(boolean colorChecked) {
        this.colorChecked = colorChecked;
    }

    public boolean isSpiralChecked() {
        return spiralChecked;
    }

    public void setSpiralChecked(boolean spiralChecked) {
        this.spiralChecked = spiralChecked;
    }

    public boolean isCaligoChecked() {
        return caligoChecked;
    }

    public void setCaligoChecked(boolean caligoChecked) {
        this.caligoChecked = caligoChecked;
    }

    public int getCopiesBlackAndWhite() {
        return copiesBlackAndWhite;
    }

    public void setCopiesBlackAndWhite(int copiesBlackAndWhite) {
        this.copiesBlackAndWhite = copiesBlackAndWhite;
    }

    public int getCopiesColor() {
        return copiesColor;
    }

    public void setCopiesColor(int copiesColor) {
        this.copiesColor = copiesColor;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
