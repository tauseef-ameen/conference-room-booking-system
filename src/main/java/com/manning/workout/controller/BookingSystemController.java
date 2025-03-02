package com.manning.workout.controller;

import com.manning.workout.exception.ServiceUnavailableException;
import com.manning.workout.inventory.ConferenceRoom;
import com.manning.workout.inventory.InventoryConferenceRoomClient;
import com.manning.workout.reservation.RoomReservation;
import com.manning.workout.reservation.RoomReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BookingSystemController {

    private final RoomReservationRepository reservationsRepository;
    private final InventoryConferenceRoomClient inventoryClient;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${room.state.service.url}")
    private String roomStateServiceUrl;

    @GetMapping("/available/{startDate}/{endDate}")
    public Collection<ConferenceRoom> availability(@PathVariable("startDate") LocalDateTime startDate,
                                                   @PathVariable("endDate") LocalDateTime endDate) {

        log.info("Getting available conference rooms for startDate {} and endDate {}", startDate, endDate);
        // obtain all conf rooms from inventory
        List<ConferenceRoom> availableRooms = inventoryClient.allRooms();
        // create a map from id to conf room
        Map<Integer, ConferenceRoom> roomsById = availableRooms
                .stream()
                .collect(Collectors.toMap(ConferenceRoom::roomId, room -> room));

        // get all current conf room Reservation List
        List<RoomReservation> roomReservationList = reservationsRepository.findAll();
        // for each reservation, remove the conf room from the map
        roomReservationList.stream()
                .filter(reservation -> reservation.isAvailable(startDate, endDate))
                .forEach(reservation -> roomsById.remove(reservation.roomId));

        log.info("Found {} conference rooms", roomsById.size());
        return roomsById.values();

    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody RoomReservation roomReservation) {
        // Call Room state microservice to get real time state of room
        String state = getRoomState(roomReservation.roomId);
        if (!"Available".equals(state)) {
            log.error("Room is currently in state {} and cannot be booked", state);
            return ResponseEntity.badRequest().body("Room is currently in state " +state+ "and cannot be booked");
        }
        // save room reservation request and generate booking ID
        RoomReservation reservation = reservationsRepository.save(roomReservation);
        return ResponseEntity.ok(reservation);
    }

    private String getRoomState(int roomId) {
        String url = roomStateServiceUrl + "/state/" + roomId;
        try {
            log.info("Getting room state url {}", url);
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("Service unavailable: {}", url, e);
            throw new ServiceUnavailableException("Room state service is unavailable");
        }
    }
}
