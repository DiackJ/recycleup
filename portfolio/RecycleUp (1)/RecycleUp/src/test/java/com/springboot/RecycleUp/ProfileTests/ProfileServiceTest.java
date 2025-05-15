package com.springboot.RecycleUp.ProfileTests;

import com.springboot.RecycleUp.DTO.AccountProfileDTO;
import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Model.Profile;
import com.springboot.RecycleUp.Repository.AccountRepository;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import com.springboot.RecycleUp.Service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp(){
        //loads mocked classes
        MockitoAnnotations.openMocks(this);
        //creates fake account to be able to test methods
        Account account = new Account("test@gmail.com", "testPassword123", 1);
        when(accountRepository.findById((long)1)).thenReturn(Optional.of(account));
    }

    @Test
    void addNewProfileToAccount_ShouldReturnProfile(){
        AccountProfileDTO dto = new AccountProfileDTO("user", 100);

        Profile profile = profileService.addProfileToAccount(1, dto);

        assertNotNull(profile);
    }

    @Test
    void shouldUpdateUserGoalToInputInt(){
        //arrange
        Profile profile = new Profile("user", 100);
        when(profileRepository.findById((long)1)).thenReturn(Optional.of(profile));
        //act
        profileService.updateGoal(1, 200);
        //assert
        assertEquals(200, profile.getGoal());
    }

    @Test
    void shouldUpdateProgress(){
        Profile profile = new Profile("user", 100);

        profileService.updateProgress(20, profile);

        assertEquals(20.0, profile.getProgress());
    }

    @Test
    void invalidProgressInput_shouldThrowException(){
        Profile profile = new Profile("user", 100);

        assertThrows(IllegalArgumentException.class, () -> profileService.updateProgress(-20, profile));
    }

    @Test
    void shouldUpdatePoints(){
        Profile profile = new Profile("user", 100);

        profileService.updatePoints(5, profile);

        assertEquals(50, profile.getPoints());
    }

    @Test
    void invalidAmount_shouldThrowException(){
        Profile profile = new Profile("user", 100);

        assertThrows(IllegalArgumentException.class, () -> profileService.updatePoints(-5, profile));
    }

    @Test
    void shouldUpdateTheAmountRecycled(){
        Profile profile = new Profile("user", 100);

        profileService.updateAmountRecycled(20, profile);

        assertEquals(20, profile.getItemsRecycled());
    }

    @Test
    void invalidItemAmount_shouldThrowException(){
        Profile profile = new Profile("user", 100);

        assertThrows(IllegalArgumentException.class, () -> profileService.updateAmountRecycled(-10, profile));
    }

    @Test
    void shouldSetBronzeReward(){
        Profile profile = new Profile("user", 100);

        profileService.setRewards(30, profile);

        assertTrue(profile.getBronzeReward());
    }

    @Test
    void shouldSetSilverAndBronzeReward(){
        Profile profile = new Profile("user", 100);

        profileService.setRewards(60, profile);

        assertTrue(profile.getBronzeReward());
        assertTrue(profile.getSilverReward());
    }

    @Test
    void shouldSetSilverAndBronzeAndGoldReward(){
        Profile profile = new Profile("user", 100);

        profileService.setRewards(100, profile);

        assertTrue(profile.getBronzeReward());
        assertTrue(profile.getSilverReward());
        assertTrue(profile.getGoldReward());
    }

    @Test
    void shouldSetSilverAndBronzeAndGoldAndDiamondReward(){
        Profile profile = new Profile("user", 100);

        profileService.setRewards(101, profile);

        assertTrue(profile.getBronzeReward());
        assertTrue(profile.getSilverReward());
        assertTrue(profile.getGoldReward());
        assertTrue(profile.getDiamondReward());
    }


    @Test
    void shouldRevertGoldAndDiamondReward(){
        Profile profile = new Profile("user", 200);

        profileService.setRewards(55, profile);

        assertTrue(profile.getBronzeReward());
        assertTrue(profile.getSilverReward());
        assertFalse(profile.getGoldReward());
        assertFalse(profile.getDiamondReward());
    }

}
