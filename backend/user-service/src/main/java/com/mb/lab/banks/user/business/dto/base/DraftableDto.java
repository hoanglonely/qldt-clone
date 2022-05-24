package com.mb.lab.banks.user.business.dto.base;

import com.mb.lab.banks.user.persistence.domain.base.PODraftable;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;

public class DraftableDto extends DTO {

    private ActiveStatus activeStatus;
    
    public DraftableDto() { }

    public DraftableDto(PODraftable poDraftable) {
        super(poDraftable);

        this.activeStatus = poDraftable.getActiveStatus();
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

}
