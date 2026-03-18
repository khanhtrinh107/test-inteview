package com.upload.contactselection.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefSelectSearchResponse {

    private long total;

    private List<ContactDto> contacts;

}