package com.manning.workout.reservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class RoomReservationRepositoryClient implements RoomReservationRepository {

    private final AtomicInteger ids = new AtomicInteger(0);
    private final List<RoomReservation> store = new ArrayList<>();

    @Override
    public List<RoomReservation> findAll() {
        return Collections.unmodifiableList(store);
    }

    @Override
    public RoomReservation save(RoomReservation roomReservation) {
        roomReservation.bookingId = ids.incrementAndGet();
        store.add(roomReservation);
        log.info("Room reserved with details: {}", roomReservation);
        return roomReservation;
    }
}
