package com.springboot.RecycleUp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.springboot.RecycleUp.Model.Item;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByMaterialAndProfile_Id(String material, long profileId);

    @Query("SELECT i FROM Item i WHERE i.profile.id = :pId")
    List<Item> itemList(@Param("pId") Long pId);
}
