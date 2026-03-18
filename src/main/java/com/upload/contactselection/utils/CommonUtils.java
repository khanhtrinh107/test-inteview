package com.upload.contactselection.utils;

import com.upload.contactselection.dto.ContactDto;
import com.upload.contactselection.entity.Contact;

public class CommonUtils {
    public static ContactDto toDto(Contact contact) {

        return new ContactDto(
                contact.getContactCode(),
                contact.getContactName(),
                contact.getContactKana(),
                contact.getPrefecture(),
                contact.getRegion().getRegionName()
        );
    }
}
