package com.mb.lab.banks.user.business.dto.base;

import java.io.Serializable;

public class ImportAdminVerifyDto implements Serializable {

    private static final long serialVersionUID = 1;

    private int nbRows;
    private int nbErrorRows;
    private String importFilePath;
    private String errorFilePath;
    private String errorFileUrl;
    
    public ImportAdminVerifyDto(ImportVerifyDto dto) {
        this.nbRows = dto.getNbRows();
        this.nbErrorRows = dto.getNbErrorRows();
        this.importFilePath = dto.getImportFilePath();
        this.errorFilePath = dto.getErrorFilePath();
        this.errorFileUrl = dto.getErrorFileUrl();
    }

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

    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
    }

    public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
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
        return nbErrorRows > 0 ? 0 : nbRows - nbErrorRows;
    }

}
