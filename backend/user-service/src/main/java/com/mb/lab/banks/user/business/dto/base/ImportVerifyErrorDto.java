package com.mb.lab.banks.user.business.dto.base;

public class ImportVerifyErrorDto extends ImportVerifyDto {

    private static final long serialVersionUID = 1;
    
    private int nbRows;
    private int nbErrorRows;

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

    public int getNbSuccessRows() {
        return nbErrorRows > 0 ? 0 : nbRows - nbErrorRows;
    }
}
