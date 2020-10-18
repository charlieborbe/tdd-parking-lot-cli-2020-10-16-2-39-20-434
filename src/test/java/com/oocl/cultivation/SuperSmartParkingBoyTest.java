package com.oocl.cultivation;

import com.oocl.cultivation.Exception.NoParkingLotSpaceException;
import com.oocl.cultivation.Exception.NoTicketException;
import com.oocl.cultivation.Exception.WrongTicketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuperSmartParkingBoyTest {
    private Car car;

    private ParkingBoyList parkingBoyList = new ParkingBoyList();

    private ParkingLotManager parkingLotManager;
    List<ParkingLot> parkingLotListByManager = new ArrayList<>();
    private ParkingLotList parkingLotList;

    private SuperSmartParkingBoy superSmartParkingBoy;

    @BeforeEach
    void setUp() {
        car = new Car();

        parkingLotList = new ParkingLotList();

        parkingLotListByManager.add(new ParkingLot(10));
        parkingLotListByManager.add(new ParkingLot(5));

        parkingBoyList = new ParkingBoyList(
                superSmartParkingBoy,
                new SmartParkingBoy(new ParkingLot(parkingLotListByManager)),
                new ParkingBoy(new ParkingLot(parkingLotListByManager)));

        superSmartParkingBoy = new SuperSmartParkingBoy(new ParkingLot(parkingLotListByManager));

        parkingLotManager = new ParkingLotManager(new ParkingLot(parkingLotListByManager));

        parkingLotManager.assignParkingLotToParkingBoy(parkingBoyList.getParkingBoyList().get(0), parkingLotListByManager, parkingLotList);
    }

    @Test
    void should_return_a_parking_ticket_when_parking_given_a_car_to_parking_boy() {
        //given

        //when
        ParkingTicket parkingTicket = superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //then
        assertNotNull(parkingTicket);
    }

    @Test
    public void should_return_correct_car_when_fetching_given_a_ticket() {
        //given
        ParkingTicket parkingTicket = superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //when
        Car fetchCar = superSmartParkingBoy.fetch(parkingTicket, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //then
        assertSame(car, fetchCar);
    }

    @Test
    public void should_return_two_cars_when_fetching_given_two_correct_tickets() {
        //given
        Car secondCar = new Car();
        ParkingTicket parkingTicket1 = superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);
        ParkingTicket parkingTicket2 = superSmartParkingBoy.park(secondCar, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //when
        Car fetchFirstCar = superSmartParkingBoy.fetch(parkingTicket1, parkingBoyList.getParkingBoyList().get(0), parkingLotList);
        Car fetchSecondCar = superSmartParkingBoy.fetch(parkingTicket2, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //then
        assertSame(car, fetchFirstCar);
        assertSame(secondCar, fetchSecondCar);
    }

    @Test
    public void should_return_WrongTicketException_when_fetch_given_wrong_ticket() {
        //given

        //when
        superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);
        ParkingTicket wrongTicket = new ParkingTicket();


        //then
        assertThrows(WrongTicketException.class, () -> superSmartParkingBoy.fetch(wrongTicket, parkingBoyList.getParkingBoyList().get(0), parkingLotList), "Unrecognized Parking Ticket!");
    }

    @Test
    public void should_return_NoTicketException_when_fetch_given_no_ticket() {
        //given

        //when
        superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);
        ParkingTicket noTicket = null;

        //then
        assertThrows(NoTicketException.class, () -> superSmartParkingBoy.fetch(noTicket, parkingBoyList.getParkingBoyList().get(0), parkingLotList), "Please provide your parking ticket!");
    }

    @Test
    public void should_return_WrongTicketException_when_fetch_a_car_given_used_ticket() {
        //given
        ParkingTicket parkingTicket = superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //when
        Car fetchCarFirstTime = superSmartParkingBoy.fetch(parkingTicket, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //then
        assertSame(car, fetchCarFirstTime);
        assertThrows(WrongTicketException.class, () -> superSmartParkingBoy.fetch(parkingTicket, parkingBoyList.getParkingBoyList().get(0), parkingLotList), "Unrecognized Parking Ticket!");
    }

    @Test
    public void should_return_NoParkingLotSpaceException_when_park_a_car_given_parking_lot_is_full() {
        //given
        List<ParkingLot> parkingLotMapLists = new ArrayList<>();
        parkingLotMapLists.add(new ParkingLot(1));
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(new ParkingLot(parkingLotMapLists));
        ParkingBoyList parkingBoyList = new ParkingBoyList(superSmartParkingBoy);
        Car secondCar = new Car();
        parkingLotManager.assignParkingLotToParkingBoy(parkingBoyList.getParkingBoyList().get(0), parkingLotMapLists, parkingLotList);

        //when
        superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList);

        //then
        assertThrows(NoParkingLotSpaceException.class, () -> superSmartParkingBoy.park(secondCar, parkingBoyList.getParkingBoyList().get(0), parkingLotList), "Not Enough Position.");
    }

    @Test
    public void should_return_car_park_at_second_parking_lot_when_parking_boy_parks_a_car_given_two_parking_lot_same_available_position() {
        //given
        List<ParkingLot> parkingLotMapLists = new ArrayList<>();
        parkingLotMapLists.add(new ParkingLot(5));
        parkingLotMapLists.add(new ParkingLot(5));

        Car secondCar = new Car();

        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(new ParkingLot(parkingLotMapLists));
        ParkingBoyList parkingBoyList = new ParkingBoyList(superSmartParkingBoy);

        //when
        parkingLotManager.assignParkingLotToParkingBoy(parkingBoyList.getParkingBoyList().get(0), parkingLotMapLists, parkingLotList);
        superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList); // Parking Lot 1
        ParkingTicket parkingTicket1 = superSmartParkingBoy.park(secondCar, parkingBoyList.getParkingBoyList().get(0), parkingLotList); // Parking Lot 2
        String currentLocation = superSmartParkingBoy.getCurrentLocation(parkingLotMapLists, parkingTicket1);

        //then
        assertNotNull(parkingTicket1);
        assertSame(secondCar, superSmartParkingBoy.fetch(parkingTicket1, parkingBoyList.getParkingBoyList().get(0), parkingLotList));
        assertEquals("ParkingLot Number: 2", currentLocation);
    }

    @Test
    public void should_return_parking_lot_where_car_is_park_when_super_smart_parking_boy_parks_a_car_given_two_parking_lot_with_larger_available_position_rate() {
        //given
        List<ParkingLot> parkingLotMapLists = new ArrayList<>();
        parkingLotMapLists.add(new ParkingLot(2));
        parkingLotMapLists.add(new ParkingLot(5));

        Car secondCar = new Car();
        Car thirdCar = new Car();


        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(new ParkingLot(parkingLotMapLists));
        ParkingBoyList parkingBoyList = new ParkingBoyList(superSmartParkingBoy);

        //when
        parkingLotManager.assignParkingLotToParkingBoy(parkingBoyList.getParkingBoyList().get(0), parkingLotMapLists, parkingLotList);
        superSmartParkingBoy.park(car, parkingBoyList.getParkingBoyList().get(0), parkingLotList); // Parking Lot 1
        ParkingTicket parkingTicket2 = superSmartParkingBoy.park(secondCar, parkingBoyList.getParkingBoyList().get(0), parkingLotList); // Parking Lot 2
        superSmartParkingBoy.park(thirdCar, parkingBoyList.getParkingBoyList().get(0), parkingLotList); // Parking Lot 2
        String currentLocation = superSmartParkingBoy.getCurrentLocation(parkingLotMapLists, parkingTicket2);

        //then
        assertNotNull(parkingTicket2);
        assertSame(secondCar, superSmartParkingBoy.fetch(parkingTicket2, parkingBoyList.getParkingBoyList().get(0), parkingLotList));
        assertEquals("ParkingLot Number: 2", currentLocation);
    }
}