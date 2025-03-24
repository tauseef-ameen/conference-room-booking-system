package com.manning.workout.inventory;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConferenceRoomInventoryClient implements ConferenceRoomInventory {
    @Override
    public List<ConferenceRoom> allRooms() {
        return List.of(
                new ConferenceRoom("gebouw-1", 1, 1, "A"),
                new ConferenceRoom("gebouw-2", 11, 3, "D"),
                new ConferenceRoom("gebouw-3", 22, 5, "C"),
                new ConferenceRoom("gebouw-4", 33, 7, "B"),
                new ConferenceRoom("gebouw-5", 44, 9, "E")
        );
    }
}