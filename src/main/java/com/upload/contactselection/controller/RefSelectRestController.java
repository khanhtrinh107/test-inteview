package com.upload.contactselection.controller;

import com.upload.contactselection.dto.RefSelectSearchRequest;
import com.upload.contactselection.dto.RefSelectSearchResponse;
import com.upload.contactselection.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefSelectRestController {

    private final ContactService contactService;

    @PostMapping("/purchase/event_entry/event_info/ajax/ref_select_load")
    public RefSelectSearchResponse search(
            @RequestBody RefSelectSearchRequest request
    ) {
        return contactService.search(request);
    }
}
