package com.upload.contactselection.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    private String contactCode;

    private String contactName;

    private String contactKana;

    private String prefecture;

    private String regionName;

}