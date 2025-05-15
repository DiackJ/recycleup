package com.springboot.RecycleUp;

import com.springboot.RecycleUp.DTO.AccountProfileDTO;
import com.springboot.RecycleUp.DTO.AuthRequest;
import com.springboot.RecycleUp.DTO.ItemDTO;
import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Model.Profile;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import com.springboot.RecycleUp.Service.AccountService;
import com.springboot.RecycleUp.Service.ProfileService;
import com.springboot.RecycleUp.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/RecycleUp")
public class Controller {
    @Autowired
    public AccountService accountService;

    @Autowired
    public ProfileService profileService;

    @Autowired
    public ProfileRepository profileRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    //public endpoint for user login
    @PostMapping("/auth/log-in")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        if(auth.isAuthenticated()){
            String token = jwtUtil.generateToken(request.getEmail());

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60*60) //one hour lifespan
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Login successful!");
        }else{
            throw new BadCredentialsException("username or password is incorrect. try again!");
        }
    }

    //logout endpoint
    @PostMapping("/auth/log-out")
    public ResponseEntity<?> logout(){
        ResponseCookie cookie = ResponseCookie.from("jwt")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) //delete cookie
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("logout successful!");
    }

   //test endpoint for debugging
//    @GetMapping("/test")
//    public ResponseEntity<?> testPoint(){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth != null && auth.isAuthenticated()){
//            return ResponseEntity.ok("authenticated");
//        }else{
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthenticated");
//        }
//    }

    //public endpoint for creating a new account
    @PostMapping("/auth/create-account")
    public ResponseEntity<?> createAccount(@RequestBody AccountProfileDTO dto){
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("email cannot be blank, please input a valid email");
        }
        if (!dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("please input a valid email");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("password cannot be blank, please input a password");
        }
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("name cannot be blank, please input a valid name");
        }

        accountService.createAccountAndProfile(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("account created!");
    }

//protected endpoints
    //add a profile to an existing account
    @PostMapping("/{accountId}/add-profile")
    public ResponseEntity<?> addProfile(@PathVariable Long accountId, @RequestBody AccountProfileDTO dto){
        if(dto.getName() == null || dto.getName().isEmpty()){
            throw new IllegalArgumentException("profile name cannot be blank, please input a name");
        }
        if(dto.getGoal() <= 0){
            throw new IllegalArgumentException("goal needs to be greater than 0");
        }

        profileService.addProfileToAccount(accountId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("profile added!");
    }

    //update goal
    @PutMapping("/{id}/goal")
    public ResponseEntity<?> goalUpdate(@PathVariable Long id, @RequestBody int newGoal){
        if(newGoal < 0){
            throw new IllegalArgumentException("goal must be more than 0");
        }
        profileService.updateGoal(id, newGoal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("goal updated!");
    }

    //add item
    @PostMapping("/{id}/add-item")
    public ResponseEntity<String> addRecycleItem(@PathVariable long id, @RequestBody ItemDTO dto){
        if(dto.getAmount() < 1){
            throw new IllegalArgumentException("must recycle at least 1 item");
        }
        profileService.addOrUpdateItem(dto, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("item added!");
    }

    //view profile dashboard
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<Profile> getProfileDashboard(@PathVariable long id){
       Optional<Profile> optionalProfile = profileRepository.findById(id);
       Profile profile = optionalProfile.orElseThrow(()-> new UsernameNotFoundException("Profile not found"));

        profile.setLeaderboardList(profileService.getLeaderboard(profile.getAccount().getAccountId()));
        profile.setTopItems(profileService.getTopItems(id));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profile);
    }

    //delete profile
    @DeleteMapping("/{accountId}/delete-profile")
    public ResponseEntity<String> deleteProfile(@RequestParam String name, @PathVariable long accountId){
        profileService.deleteProfile(name, accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("profile deleted!");
    }

    //delete account and all profiles under said account
    @DeleteMapping("/{accountId}/delete-account")
    public ResponseEntity<String> deleteAccount(@PathVariable long accountId){
        accountService.deleteAccountAndAllProfiles(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("account deleted!");
    }

//exception handlers
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgsHandler(IllegalArgumentException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> BadCredentialsHandler(BadCredentialsException exception){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }
}
