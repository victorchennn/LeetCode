package Topics.Design;

import java.util.LinkedList;

public class Elevator {
    private static Elevator elevator;
    private static RequestProcessCenter center;
    private int currentFloor;

    public Elevator() {
        elevator = new Elevator();
        center = new RequestProcessCenter();
    }

    private int getCurrentFloor() {
        return currentFloor;
    }

    private void moveToTargetFloor(int toFloor) {
        currentFloor = toFloor;
    }

    public class User {
        String name;

        public void pressButton(int toFloor) {
            Request req = new Request(toFloor);
            center.addRequest(req);
        }
    }

    public class Request {
        private int toFloor;

        Request(int _toFloor) {
            toFloor = _toFloor;
        }

        int getToFloor() {
            return toFloor;
        }
    }

    public class RequestProcessCenter implements Runnable {
        private LinkedList<Request> queue;
        RequestProcessCenter( ) {
            queue = new LinkedList<>();
        }

        public void run() {
            processRequest();
        }

        void addRequest(Request request) {
            queue.add(request);
        }
        void removeRequest(Request request) {
            queue.remove(request);
        }

        Request getNextRequest() {
            Request shortestReq = null;
            int shortest = Integer.MAX_VALUE;
            int curFloor = elevator.getCurrentFloor();
            for (Request item : queue) {
                int distance = Math.abs(curFloor - item.getToFloor());
                if (distance < shortest) {
                    shortest = distance;
                    shortestReq = item;
                }
            }
            return shortestReq;
        }

        void processRequest() {
            Request req = getNextRequest();
            if (req != null) {
                int toFloor = req.getToFloor( );
                elevator.moveToTargetFloor(toFloor);
                queue.remove(req);
            }

        }
    }
}



