package com.upload.contactselection.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "region_m")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "delete_flag")
    private Integer deleteFlag;

}
