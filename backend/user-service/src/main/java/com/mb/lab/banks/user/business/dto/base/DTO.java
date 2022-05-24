package com.mb.lab.banks.user.business.dto.base;

import com.mb.lab.banks.user.persistence.domain.base.PO;

public class DTO {

    private Long id;
    
    public DTO() { }

    public DTO(Long id) {
        super();

        this.id = id;
    }

    public DTO(PO po) {
        super();

        if (po.getId() != null) {
            this.id = po.getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
