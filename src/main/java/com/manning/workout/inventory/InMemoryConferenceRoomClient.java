package com.manning.workout.inventory;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryConferenceRoomClient implements InventoryConferenceRoomClient{
    @Override
    public List<ConferenceRoom> allRooms() {
        return List.of(
                new ConferenceRoom(1, "gebouw-1"),
                new ConferenceRoom(2, "gebouw-2"),
                new ConferenceRoom(3, "gebouw-3"),
                new ConferenceRoom(4, "gebouw-4"),
                new ConferenceRoom(5, "gebouw-5")
        );
    }
}
