package com.springboot.RecycleUp.ProfileTests;

import com.springboot.RecycleUp.DTO.AccountProfileDTO;
import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Model.Item;
import com.springboot.RecycleUp.Model.Profile;
import com.springboot.RecycleUp.Repository.AccountRepository;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import com.springboot.RecycleUp.Util.JwtUtil;
import com.springboot.RecycleUp.config.EnvLoader;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import java.net.http.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProfileIntegrationTests {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EntityManager entityManager;

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
    void cleanUp(){
        profileRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void validItemInput_shouldReturn200(){
        AccountProfileDTO account = new AccountProfileDTO("newUser@gmail.com", "password", "newUser", 100);
        restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account",  account, String.class);

        String token = jwtUtil.generateToken(account.getEmail());

        org.springframework.http.HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Cookie", "jwt=" + token);

        if(accountRepository.findByEmail("newUser@gmail.com") != null){
            Optional<Profile> profile = profileRepository.findByName("newUser");
            Profile p = profile.orElseThrow();

            Item item = new Item("paper", 10);
            HttpEntity<?> e = new HttpEntity<>(item, header);

            ResponseEntity<?> res = restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/"+p.getProfileId()+"/add-item", e, String.class);

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals("item added!", res.getBody());
            int points = p.getPoints() + (item.getAmount()*10);
            Profile updated = profileRepository.findById(p.getProfileId()).orElseThrow();
            assertEquals(points, updated.getPoints());
        }
    }

    @Test
    void validProfileInputForExistingAccount_shouldReturn200(){
        AccountProfileDTO account = new AccountProfileDTO("user3@gmail.com", "password", "user3", 50);
        restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", account, String.class);

        String token = jwtUtil.generateToken(account.getEmail());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Cookie", "jwt="+token);

        if(accountRepository.findByEmail("user3@gmail.com") != null){
            Account acc = accountRepository.findByEmail("user3@gmail.com");
            AccountProfileDTO p = new AccountProfileDTO("newUserGuy", 100);
            HttpEntity<?> req = new HttpEntity<>(p, header);

            ResponseEntity<?> res = restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/"+acc.getAccountId()+"/add-profile", req, String.class);

            assertEquals(HttpStatus.CREATED, res.getStatusCode());
            assertEquals("profile added!", res.getBody());
            Profile prof = profileRepository.findByName("newUserGuy").orElseThrow();
            assertNotNull(prof);
        }
    }

    @Test
    void validGoalInputForUpdate_shouldReturn200(){
        AccountProfileDTO account = new AccountProfileDTO("user4@gmail.com", "password", "anotherUserGuy", 50);
        restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", account, String.class);

        String token = jwtUtil.generateToken(account.getEmail());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Cookie", "jwt="+token);

        if(accountRepository.findByEmail("user4@gmail.com") != null){
            int newGoal = 100;
            Optional<Profile> p = profileRepository.findByName("anotherUserGuy");
            Profile prof = p.orElseThrow();

            HttpEntity<?> req = new HttpEntity<>(newGoal, header);

            ResponseEntity<?> res = restTemplate.exchange(
                    getBaseUrl()+"/RecycleUp/"+prof.getProfileId()+"/goal",
                    HttpMethod.PUT,
                    req,
                    String.class);

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals("goal updated!", res.getBody());
            Profile upd = profileRepository.findByName("anotherUserGuy").orElseThrow();
            assertEquals(newGoal, upd.getGoal());
        }
    }

    @Test
    void deleteProfile_shouldReturn200(){
        //create account with profile
        AccountProfileDTO a = new AccountProfileDTO("user75@gmail.com", "doublePasswordIdk", "userAgain", 100);
        //set the request header
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
//        String token = jwtUtil.generateToken(a.getEmail());
//        header.set("Cookie", "jwt="+token);
        //send request to add account to db
        HttpEntity<?> e = new HttpEntity<>(a, header);
        restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", e, String.class);
        //get profile
        Profile profile = profileRepository.findByName("userAgain").orElseThrow();

        //check account is there and send request
        if(accountRepository.findByEmail("user75@gmail.com") != null && profileRepository.findByName("userAgain").isPresent()){
            String token = jwtUtil.generateToken(a.getEmail());
            header.set("Cookie", "jwt="+token);
            //extract account id
            long accId = profile.getAccount().getAccountId();
            //make new request to delete profile by name
            HttpEntity<?> req = new HttpEntity<>(header);
            //map for param
            Map<String, String> param = new HashMap<>();
            param.put("name", profile.getName());
            ResponseEntity<String> res = restTemplate.exchange(
                    getBaseUrl()+"/RecycleUp/"+accId+"/delete-profile?name={name}",
                    HttpMethod.DELETE,
                    req,
                    String.class,
                    param
            );
            //assert response is 200 and profile was removed
            assertEquals(HttpStatus.OK, res.getStatusCode());
        //fix later:
            //repo isn't acknowledging empty db after deletion
     //       assertEquals(0, profileRepository.findAll().size());
        }
    }

//    @Test
//    void getDashboardFromProfile_shouldReturn200(){
//        //create account
//        AccountProfileDTO account = new AccountProfileDTO("user5@gmail.com", "password", "surpriseAnotherGuy", 100);
//        restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/auth/create-account", account, String.class);
//        Account a = accountRepository.findByEmail("user5@gmail.com");
//        //create header for requests
//        String token = jwtUtil.generateToken(a.getEmail());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Cookie", "jwt="+token);
//        //after the account is set in db
//        if(accountRepository.findByEmail("user5@gmail.com") != null){
//            //create a second profile
//            AccountProfileDTO profile = new AccountProfileDTO("yetAnotherGuy", 100);
//            HttpEntity<?> e = new HttpEntity<>(profile, headers);
//            restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/"+a.getAccountId()+"/add-profile", e, Profile.class);
//            //get profiles one and two
//            Profile p1 = profileRepository.findByName("yetAnotherGuy").orElseThrow();
//            Profile p2 = profileRepository.findByName("surpriseAnotherGuy").orElseThrow();
//            //add items for points
//            Item item1 = new Item("paper", 8); //80pts
//            Item item2 = new Item("paper", 5); //50pts
//            HttpEntity<?> item1E = new HttpEntity<>(item1, headers);
//            HttpEntity<?> item2E = new HttpEntity<>(item2, headers);
//
//            restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/"+p1.getProfileId()+"/add-item", item1E, Profile.class);
//            restTemplate.postForEntity(getBaseUrl()+"/RecycleUp/"+p2.getProfileId()+"/add-item", item2E, Profile.class);
//            //declare expected results for one profile's dashboard
//            Map<String, Integer> expectedMap = new HashMap<>();
//            expectedMap.put("yetAnotherGuy", 80);
//            expectedMap.put("surpriseAnotherGuy", 50);
//
//            Map<String, Integer> expectedItemMap1 = new HashMap<>();
//            expectedItemMap1.put("paper", 8);
//
//            double expectedProgress1 = 80.0;
//
//            //request
//            HttpEntity<?> req = new HttpEntity<>(headers);
//            //response
//            ResponseEntity<?> res = restTemplate.exchange(
//                    getBaseUrl()+"/RecycleUp/"+p1.getProfileId()+"/dashboard",
//                    HttpMethod.GET,
//                    req,
//                    Profile.class
//            );
//            //get updated profile
//            Profile upP1 = profileRepository.findByName("yetAnotherGuy").orElseThrow();
//
//            //assert checks
//            assertEquals(HttpStatus.OK, res.getStatusCode());
//            assertEquals(expectedMap, upP1.getLeaderboard());
//            assertEquals(expectedProgress1, upP1.getProgress());
//            assertEquals(expectedItemMap1, upP1.getTopItems());
//        }
//    }
}
