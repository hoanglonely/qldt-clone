package com.mb.lab.banks.user.business.dto.base;

public class CategoryDto extends DraftableDto {

    private String name;
    private String code;
    
    public CategoryDto() {}
    
    public CategoryDto(CategoryDto dto) {
        this.setId(dto.getId());
        this.setActiveStatus(dto.getActiveStatus());
        this.name = dto.getName();
        this.code = dto.getCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
