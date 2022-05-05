package com.company;

import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProfileServiceTest {
    @Autowired
    private ProfileService profileService;

    @Test
    void createProfile() {
        ProfileDTO dto = new ProfileDTO();
        dto.setName("Vali");
        dto.setSurname("Valiyev");
        dto.setEmail("valish@gmail.com");
        dto.setPassword("valish123");
        dto.setRole(ProfileRole.ADMIN);
        System.out.println(profileService.create(dto));
    }
}
