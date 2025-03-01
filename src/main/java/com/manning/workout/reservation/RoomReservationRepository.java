package com.manning.workout.reservation;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RoomReservationRepository implements InMemoryRoomReservationRepository {

    private final AtomicInteger ids = new AtomicInteger(0);
    private final List<RoomReservation> store = new CopyOnWriteArrayList<>();

    @Override
    public List<RoomReservation> findAll() {
        return Collections.unmodifiableList(store);
    }

    @Override
    public RoomReservation save(RoomReservation roomReservation) {
        roomReservation.id = ids.incrementAndGet();
        store.add(roomReservation);
        return roomReservation;
    }
}
