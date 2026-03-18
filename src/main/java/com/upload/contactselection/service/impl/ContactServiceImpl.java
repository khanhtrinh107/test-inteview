package com.upload.contactselection.service.impl;

import com.upload.contactselection.dto.ContactDto;
import com.upload.contactselection.dto.RefSelectSearchRequest;
import com.upload.contactselection.dto.RefSelectSearchResponse;
import com.upload.contactselection.entity.Contact;
import com.upload.contactselection.repository.ContactRepository;
import com.upload.contactselection.service.ContactService;
import com.upload.contactselection.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public RefSelectSearchResponse search(RefSelectSearchRequest request) {

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        List<String> regionCodes =
                (request.getRegionCodes() == null || request.getRegionCodes().isEmpty())
                        ? null
                        : request.getRegionCodes();

        Page<Contact> result =
                contactRepository.searchContacts(
                        request.getContactName(),
                        request.getContactKana(),
                        regionCodes,
                        pageable
                );

        List<ContactDto> contacts =
                result.getContent()
                        .stream()
                        .map(CommonUtils::toDto)
                        .toList();

        return new RefSelectSearchResponse(
                result.getTotalElements(),
                contacts
        );
    }
}