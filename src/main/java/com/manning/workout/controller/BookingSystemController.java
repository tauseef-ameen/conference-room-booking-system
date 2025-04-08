package com.manning.workout.controller;

import com.manning.workout.exception.ServiceUnavailableException;
import com.manning.workout.inventory.ConferenceRoom;
import com.manning.workout.inventory.ConferenceRoomInventory;
import com.manning.workout.reservation.RoomReservation;
import com.manning.workout.reservation.RoomReservationRepositoryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BookingSystemController {

    private final RoomReservationRepositoryClient reservationsRepository;
    private final ConferenceRoomInventory conferenceRoomInventory;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/available/{startDate}/{endDate}")
    public Collection<ConferenceRoom> availability(@PathVariable("startDate") LocalDateTime startDate,
                                                   @PathVariable("endDate") LocalDateTime endDate) {

        log.info("Getting available conference rooms for startDate {} and endDate {}", startDate, endDate);
        // obtain all conf rooms from inventory
        List<ConferenceRoom> availableRooms = conferenceRoomInventory.allRooms();
        // create a map from id to conf room
        Map<Integer, ConferenceRoom> roomsById = availableRooms
                .stream()
                .collect(Collectors.toMap(ConferenceRoom::roomId, room -> room));

        // get all current conf room Reservation List
        List<RoomReservation> roomReservationList = reservationsRepository.findAll();
        log.info("Found {} existing reservation", roomReservationList.size());
        // for each reservation, remove the conf room from the map
        roomReservationList.stream()
                .filter(reservation -> reservation.isAvailable(startDate, endDate))
                .forEach(reservation -> roomsById.remove(reservation.roomId));

        // sort map by roomId
        final List<ConferenceRoom> sortedConferenceRooms = roomsById.values()
                .stream()
                .sorted(Comparator.comparingInt(ConferenceRoom::roomId))
                .toList();

        log.info("Found {} available conference rooms", roomsById.size());
        return sortedConferenceRooms;
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody RoomReservation roomReservation) {
        // save room reservation request and generate booking ID
        RoomReservation reservation = reservationsRepository.save(roomReservation);
        return ResponseEntity.ok(reservation);
    }
}
