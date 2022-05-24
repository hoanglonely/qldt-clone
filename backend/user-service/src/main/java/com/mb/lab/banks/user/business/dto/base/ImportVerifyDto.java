package com.mb.lab.banks.user.business.dto.base;

import java.io.Serializable;

public class ImportVerifyDto implements Serializable {

    private static final long serialVersionUID = 8050044649383731510L;

    private int nbRows;
    private int nbErrorRows;
    private String importFilePath;
    private String errorFilePath;
    private String errorFileUrl;

    public int getNbRows() {
        return nbRows;
    }

    public void setNbRows(int nbRows) {
        this.nbRows = nbRows;
    }

    public int getNbErrorRows() {
        return nbErrorRows;
    }

    public void setNbErrorRows(int nbErrorRows) {
        this.nbErrorRows = nbErrorRows;
    }

    public String getImportFilePath() {
        return importFilePath;
    }

    public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }

    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
    }

    public String getErrorFileUrl() {
        return errorFileUrl;
    }

    public void setErrorFileUrl(String errorFileUrl) {
        this.errorFileUrl = errorFileUrl;
    }

    public boolean isError() {
        return getNbErrorRows() > 0;
    }

    public int getNbSuccessRows() {
        return nbRows - nbErrorRows;
    }

}
