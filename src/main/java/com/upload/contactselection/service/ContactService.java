package com.upload.contactselection.service;

import com.upload.contactselection.dto.RefSelectSearchRequest;
import com.upload.contactselection.dto.RefSelectSearchResponse;

public interface ContactService {
    public RefSelectSearchResponse search(RefSelectSearchRequest request);
}
