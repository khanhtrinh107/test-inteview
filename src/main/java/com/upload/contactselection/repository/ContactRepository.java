package com.upload.contactselection.repository;

import com.upload.contactselection.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, String> {

    @Query("""
        SELECT c FROM Contact c
        WHERE c.deleteFlag = 0
        AND (:contactName IS NULL OR c.contactName LIKE %:contactName%)
        AND (:contactKana IS NULL OR c.contactKana LIKE %:contactKana%)
        AND (:regionCodes IS NULL OR c.region.regionCode IN :regionCodes)
    """)
    Page<Contact> searchContacts(
            @Param("contactName") String contactName,
            @Param("contactKana") String contactKana,
            @Param("regionCodes") List<String> regionCodes,
            Pageable pageable
    );

}