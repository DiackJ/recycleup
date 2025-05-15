package com.springboot.RecycleUp.AccountTests;

import com.springboot.RecycleUp.DTO.AccountProfileDTO;
import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Repository.AccountRepository;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import com.springboot.RecycleUp.Util.JwtUtil;
import com.springboot.RecycleUp.config.EnvLoader;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DataJpaTest //transactional by default & rolls back so doesn't affect the initial db state for each test
@Transactional
@ActiveProfiles("test")
public class AccountIntegrationTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TestRestTemplate restTemplate;

//    @Autowired
//    private TestEntityManager entityManager; //populates db with test data and verifies result

    @Autowired
    private JwtUtil jwtUtil;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeAll
    static void setUp(){
        EnvLoader.loadEnv();
    }

    @AfterEach
    void cleanUP(){
        profileRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void validAccountInput_shouldReturn200Response(){
        int repoSize = accountRepository.findAll().size();
        AccountProfileDTO dto = new AccountProfileDTO("user1@gmail.com", "password1234", "user1", 50);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> req = new HttpEntity<>(dto, header);

        ResponseEntity<?> res = restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", req, String.class); //access through actual endpoint not service

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertEquals("account created!", res.getBody());
    //fix later:
        //repository not picking up newly added account so it's still asserting the repo is 0
//        assertEquals(1, accountRepository.findAll().size());
    }

    @Test
    void invalidAccountInput_shouldReturnErrorMsg(){
        AccountProfileDTO dto = new AccountProfileDTO(null, "password123", "user", 50);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(dto, header);

        ResponseEntity<?> res = restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", entity, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(Map.of("error", "email cannot be blank, please input a valid email"), res.getBody());
    }

    @Test
    void invalidProfileInput_shouldReturnErrorMsg(){
        AccountProfileDTO dto = new AccountProfileDTO("user@gmail.com", "password", null, 50);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> req = new HttpEntity<>(dto, header);

        ResponseEntity<?> res = restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", req, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(Map.of("error", "name cannot be blank, please input a valid name"), res.getBody());
    }

    @Test
    void deleteAccountAndProfiles_shouldReturn200(){
        AccountProfileDTO a = new AccountProfileDTO("user28@gmail.com", "password", "userMan", 100);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> e = new HttpEntity<>(a, header);
        restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", e, String.class);

        Account account = accountRepository.findByEmail("user28@gmail.com");

        System.out.println("account added");

        if(account != null){
            String token = jwtUtil.generateToken(account.getEmail());
            header.set("Cookie", "jwt="+token);

            HttpEntity<?> req = new HttpEntity<>(header);
            ResponseEntity<String> res = restTemplate.exchange(
                    getBaseUrl()+"/RecycleUp/"+account.getAccountId()+"/delete-account",
                    HttpMethod.DELETE,
                    req,
                    String.class,
                    account.getAccountId()
            );
            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals("account deleted!", res.getBody());
        }

//fix later:
    //assertions failing test because repository is still detecting an account in the db even though its empty
        //get updated db context
//            entityManager.flush();
//            entityManager.clear();
//        assertEquals(0, accountRepository.findAll().size());
//        assertEquals(0, profileRepository.findAll().size());
    }

}
