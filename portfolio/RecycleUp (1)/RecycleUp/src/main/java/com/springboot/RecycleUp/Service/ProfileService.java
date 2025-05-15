package com.springboot.RecycleUp.Service;

import com.springboot.RecycleUp.DTO.AccountProfileDTO;
import com.springboot.RecycleUp.DTO.ItemDTO;
import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Model.Item;
import com.springboot.RecycleUp.Model.Profile;
import com.springboot.RecycleUp.Repository.AccountRepository;
import com.springboot.RecycleUp.Repository.ItemRepository;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private final ProfileRepository profileRepository;

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final ItemRepository itemRepository;

    public ProfileService(ProfileRepository profileRepository, AccountRepository accountRepository, ItemRepository itemRepository) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
        this.itemRepository = itemRepository;
    }

    //add a new profile to an existing account
    public Profile addProfileToAccount(long accountId, AccountProfileDTO dto) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        Account account = optionalAccount.orElseThrow(()-> new UsernameNotFoundException("Account not found"));

        Profile profile = new Profile(dto.getName(), dto.getGoal());
        profile.setAccount(account);

        //accountRepository.save(account);
        profileRepository.save(profile);

        return profile;
    }

    //set or update goal (and progress)
    public Profile updateGoal(long id, int newGoal) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        Profile profile = optionalProfile.orElseThrow();

        profile.setGoal(newGoal);
        profile.setProgress(((double) profile.getItemsRecycled() / newGoal) * 100);
        setRewards(profile.getProgress(), profile);

        return profileRepository.save(profile);
    }

    //add item or update item if already exits to profile
    public Profile addOrUpdateItem(ItemDTO dto, long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id); //find profile
        Profile profile = optionalProfile.orElseThrow();
        Item itemToAdd = new Item(dto.getMaterial(), dto.getAmount()); //newItem(dto); //create our item

        itemToAdd.setProfileId(profile);
        itemToAdd.setAccountId(profile.getAccount().getAccountId());

        Item existingItem = itemRepository.findByMaterialAndProfile_Id(dto.getMaterial(), id);//check if item type already exists under profile

        if (existingItem == null) {//if item doesn't exist under profile, add it to repo and list
            itemRepository.save(itemToAdd);
        } else {
            existingItem.setAmount(existingItem.getAmount() + dto.getAmount());//if it does exist, take current amount + input amount
        }
        //update progress, points and amount of items recycled based on item addition
        updatePoints(itemToAdd.getAmount(), profile);
        updateAmountRecycled(itemToAdd.getAmount(), profile);
        updateProgress(profile.getItemsRecycled(), profile);
        setRewards(profile.getProgress(), profile);

        return profileRepository.save(profile);//save changes
    }

    //sets progress based on goal
    public void updateProgress(int recycled, Profile profile) {
        if(recycled < 0){
            throw new IllegalArgumentException("item number must be more than 0");
        }
        double p = ((double) recycled / profile.getGoal()) * 100;
        profile.setProgress(p);
    }

    //sets points based on item amount
    public void updatePoints(int itemAmount, Profile profile) {
        if(itemAmount < 0){
            throw new IllegalArgumentException("invalid item amount");
        }
        int p = itemAmount * 10;
        profile.setPoints(profile.getPoints() + p);
    }

    //sets amount recycled based on amount of each item added
    public void updateAmountRecycled(int itemAmount, Profile profile) {
        if(itemAmount <0){
            throw new IllegalArgumentException("invalid item amount");
        }
        int newAmount = profile.getItemsRecycled() + itemAmount;
        profile.setItemsRecycled(newAmount);
    }

    //sets the rewards based on goal progress
    public void setRewards(double progress, Profile profile) {
        //reset everything to false in case of goal update
        profile.setBronzeReward(false);
        profile.setSilverReward(false);
        profile.setGoldReward(false);
        profile.setDiamondReward(false);

        if (progress >= 25) {
            profile.setBronzeReward(true);
        }
        if (progress >= 50) {
            profile.setSilverReward(true);
        }
        if (progress >= 100) {
            if (progress == 100) {
                profile.setGoldReward(true);
            } else {
                profile.setGoldReward(true);
                profile.setDiamondReward(true);
            }
        }
    }

    //leaderboard
    public Map<String, Integer> getLeaderboard(long accountId) {
        List<Profile> profileList = profileRepository.profileList(accountId);
        Map<String, Integer> lb = new HashMap<>();

        for (Profile profile : profileList) {
            lb.put(profile.getName(), profile.getPoints());
        }

        return lb.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    //set top recycled items
    public Map<String, Integer> getTopItems(long profileId){
        List<Item> itemList = itemRepository.itemList(profileId);
        Map<String, Integer> map = new HashMap<>();

        for(Item item : itemList){
            map.put(item.getMaterial(), item.getAmount());
        }

        return map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


    public void deleteProfile(String name, long accountId){
        System.out.println("ey yo");
            System.out.println("yo");
            profileRepository.deleteByName(name, accountId);
    }
}
