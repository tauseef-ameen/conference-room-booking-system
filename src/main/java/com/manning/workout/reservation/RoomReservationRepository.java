package com.manning.workout.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class RoomReservationRepository implements InMemoryRoomReservationRepository {

    private final AtomicInteger ids = new AtomicInteger(0);
    private final List<RoomReservation> store = new CopyOnWriteArrayList<>();

    @Override
    public List<RoomReservation> findAll() {
        return Collections.unmodifiableList(store);
    }

    @Override
    public RoomReservation save(RoomReservation roomReservation) {
        log.info("input reservation: {}", roomReservation);
        roomReservation.bookingId = ids.incrementAndGet();
        store.add(roomReservation);
        log.info("output reservation: {}", roomReservation);
        return roomReservation;
    }
}
