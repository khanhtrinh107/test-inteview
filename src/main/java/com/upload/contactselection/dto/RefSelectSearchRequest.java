package com.upload.contactselection.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefSelectSearchRequest {

    private String contactName;

    private String contactKana;

    private List<String> regionCodes;

    private Integer page;

    private Integer size;

}