package com.mb.lab.banks.user.persistence.domain.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class Category extends PODraftable {
    private static final long serialVersionUID = 1L;

    public static final String CN_NAME = "NAME";

    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Column(name = "CODE", length = 20, nullable = false)
    private String code;

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
