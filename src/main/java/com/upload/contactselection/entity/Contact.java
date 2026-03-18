package com.upload.contactselection.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_m")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @Column(name = "contact_code")
    private String contactCode;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_kana")
    private String contactKana;

    @Column(name = "prefecture")
    private String prefecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code")
    private Region region;

    @Column(name = "delete_flag")
    private Integer deleteFlag;

}