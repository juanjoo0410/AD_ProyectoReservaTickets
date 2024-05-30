package models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Airplane {
    private final int totalSeats = 30;
    private final Map<Integer, String> seats = new HashMap<>();
    private final Map<Integer, ConcurrentLinkedQueue<String>> waitingQueues = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Airplane() {
        for (int i = 1; i <= totalSeats; i++) {
            seats.put(i, null);
            waitingQueues.put(i, new ConcurrentLinkedQueue<>());
        }
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public Map<Integer, String> getSeats() {
        return seats;
    }

    public boolean reserveSeat(int seatNumber, String passenger) {
        lock.lock();
        try {
            if (seats.get(seatNumber) == null) {
                seats.put(seatNumber, passenger);
                return true;
            } else {
                waitingQueues.get(seatNumber).add(passenger);
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean cancelReservation(int seatNumber) {
        lock.lock();
        try {
            if (seats.get(seatNumber) != null) {
                seats.put(seatNumber, null);
                assignSeatToNextPassenger(seatNumber);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean changeSeat(int currentSeat, int newSeat, String passenger) {
        lock.lock();
        try {
            if (seats.get(currentSeat).equals(passenger)) {
                if (seats.get(newSeat) == null) {
                    seats.put(currentSeat, null);
                    seats.put(newSeat, passenger);
                    assignSeatToNextPassenger(currentSeat);
                    return true;
                } else {
                    waitingQueues.get(newSeat).add(passenger);
                    seats.put(currentSeat, null);
                    assignSeatToNextPassenger(currentSeat);
                    return false;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void assignSeatToNextPassenger(int seatNumber) {
        String nextPassenger = waitingQueues.get(seatNumber).poll();
        if (nextPassenger != null) {
            seats.put(seatNumber, nextPassenger);
            System.out.println("Seat " + seatNumber + " assigned to " + nextPassenger + " from waiting list.");
        }
    }

//    public void printSeats() {
//        lock.lock();
//        try {
//            for (Map.Entry<Integer, String> entry : seats.entrySet()) {
//                System.out.println("Seat " + entry.getKey() + ": " + (entry.getValue() == null ? "Available" : entry.getValue()));
//            }
//        } finally {
//            lock.unlock();
//        }
//    }
    
    public String getSeatsStatus() {
        StringBuilder status = new StringBuilder();
        lock.lock();
        try {
            for (Map.Entry<Integer, String> entry : seats.entrySet()) {
                status.append("Seat ").append(entry.getKey()).append(": ")
                      .append(entry.getValue() == null ? "Available" : entry.getValue())
                      .append("\n");
            }
        } finally {
            lock.unlock();
        }
        return status.toString();
    }
}
