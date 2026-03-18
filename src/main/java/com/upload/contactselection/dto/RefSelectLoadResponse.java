package com.upload.contactselection.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefSelectLoadResponse {

    private List<RegionDto> regionList;

}