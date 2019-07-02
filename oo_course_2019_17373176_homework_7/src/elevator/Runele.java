package elevator;

import com.oocourse.elevator3.PersonRequest;

public class Runele { // 拆分请求，并用来调度
    private Controller controller;
    private Elevator eleA;
    private Elevator eleB;
    private Elevator eleC;

    public Runele(Controller controller, Elevator eleA,
                  Elevator eleB, Elevator eleC) {
        this.controller = controller;
        this.eleA = eleA;
        this.eleB = eleB;
        this.eleC = eleC;
    }

    public void deal() {
        PersonRequest request;
        if ((request = controller.getRequest()) != null) {
            int personId = request.getPersonId();
            int fromFloor = request.getFromFloor();
            int toFloor = request.getToFloor();
            separate(personId, fromFloor, toFloor, request);
            separate2(personId, fromFloor, toFloor, request);
            separate3(personId, fromFloor, toFloor, request);
            separate4(personId, fromFloor, toFloor, request);
            separate5(personId, fromFloor, toFloor, request);
            separate6(personId, fromFloor, toFloor, request);
            separate7(personId, fromFloor, toFloor, request);
            separate8(personId, fromFloor, toFloor, request);
            separate9(personId, fromFloor, toFloor, request);
        }
    }

    public void separate(int personId, int fromFloor, int toFloor,
                         PersonRequest request) { //A to A
        if ((fromFloor >= -3 && fromFloor <= 1) && ((toFloor >= -3 &&
                toFloor <= 1) || (toFloor >= 15 && toFloor <= 20))) {
            if (eleA.getNowvolume() < eleA.getVolume()) {
                eleA.getVector().add(request);
                eleA.addVolume();
                controller.removeRequest(0);
            }
        } else if ((fromFloor >= 15 && fromFloor <= 20) && ((toFloor >= -3 &&
                toFloor <= 1) || (toFloor >= 15 && toFloor <= 20))) {
            if (eleA.getNowvolume() < eleA.getVolume()) {
                eleA.getVector().add(request);
                eleA.addVolume();
                controller.removeRequest(0);
            }
        }
    }

    public void separate2(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { //A TO B
        if (((fromFloor >= -3 && fromFloor < 1) && (toFloor == 2
                || (toFloor >= 4 && toFloor < 15)))) {
            if (eleA.getNowvolume() < eleA.getVolume()
                    && eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest a = new PersonRequest(fromFloor, 1, personId);
                PersonRequest b = new PersonRequest(1, toFloor, personId);
                eleA.getVector().add(a);
                eleA.addVolume();
                controller.removeRequest(0);
                while (eleA.getFlag() == 1) {
                    eleB.getVector().add(b);
                    eleB.addVolume();
                    break;
                }
            }
        } else if ((fromFloor > 15 && fromFloor <= 20) && (toFloor == 2
                || (toFloor >= 4 && toFloor < 15))) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest a = new PersonRequest(fromFloor, 15,
                        personId);
                PersonRequest b = new PersonRequest(15, toFloor,
                        personId);
                eleA.getVector().add(a);
                eleA.addVolume();
                controller.removeRequest(0);
                while (eleA.getFlag() == 1) {
                    eleB.getVector().add(b);
                    eleB.addVolume();
                    break;
                }
            }
        }
    }

    public void separate3(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { //A TO C
        if ((fromFloor >= -3 && fromFloor < 1) && toFloor == 3) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleC.getNowvolume() < eleC.getVolume()) {
                PersonRequest a = new PersonRequest(fromFloor, 1, personId);
                PersonRequest c = new PersonRequest(1, toFloor, personId);
                eleA.getVector().add(a);
                eleA.addVolume();
                eleC.getVector().add(c);
                eleC.addVolume();
                controller.removeRequest(0);
            }
        } else if ((fromFloor > 15 && fromFloor <= 20) && toFloor == 3) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleC.getNowvolume() < eleC.getVolume()) {
                PersonRequest a = new PersonRequest(fromFloor, 15, personId);
                PersonRequest c = new PersonRequest(15, toFloor, personId);
                eleA.getVector().add(a);
                eleA.addVolume();
                eleC.getVector().add(c);
                eleC.addVolume();
                controller.removeRequest(0);
            }
        }
    }

    public void separate4(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { // B to B
        if ((fromFloor == 2)
                && (toFloor >= 4 && toFloor <= 15)) {
            if (eleB.getNowvolume() < eleB.getVolume()) {
                eleB.getVector().add(request);
                eleB.addVolume();
                controller.removeRequest(0);
            }
        } else if ((fromFloor >= 4 && fromFloor <= 15) &&
                (toFloor >= 4 && toFloor <= 15)) {
            if (eleB.getNowvolume() < eleB.getVolume()) {
                eleB.getVector().add(request);
                eleB.addVolume();
                controller.removeRequest(0);
            }
        } else if (fromFloor >= 4 && fromFloor < 15 && toFloor == 1) {
            if (eleB.getNowvolume() < eleB.getVolume()) {
                eleB.getVector().add(request);
                eleB.addVolume();
                controller.removeRequest(0);
            }
        } else if (fromFloor == 2 && toFloor == 1) {
            if (eleB.getNowvolume() < eleB.getVolume()) {
                eleB.getVector().add(request);
                eleB.addVolume();
                controller.removeRequest(0);
            }
        } else if(fromFloor == 1 && toFloor == 2) {
            if (eleB.getNowvolume() < eleB.getVolume()) {
                eleB.getVector().add(request);
                eleB.addVolume();
                controller.removeRequest(0);
            }
        }
    }

    public void separate5(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { // B to A
        if (fromFloor == 2 && toFloor == -3) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest a = new PersonRequest(fromFloor, 1, personId);
                PersonRequest b = new PersonRequest(1, toFloor, personId);
                eleB.getVector().add(b); // 先B后A
                eleB.addVolume();
                controller.removeRequest(0);
                while (eleB.getFlag() == 1) {
                    eleA.getVector().add(a);
                    eleA.addVolume();
                    break;
                }
            }
        } else if (fromFloor == 2 && (toFloor >= 15 && toFloor <= 20)) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest b = new PersonRequest(fromFloor, 1, personId);
                PersonRequest a = new PersonRequest(1, toFloor, personId);
                eleB.getVector().add(b); // 先B后A
                eleB.addVolume();
                controller.removeRequest(0);
                while (eleB.getFlag() == 1) {
                    eleA.getVector().add(a);
                    eleA.addVolume();
                    break;
                }
            }
        } else if (fromFloor >= 4 && fromFloor < 15 && toFloor >= -3 && toFloor < 1) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest b = new PersonRequest(fromFloor, 1, personId);
                PersonRequest a = new PersonRequest(1, toFloor, personId);
                eleB.getVector().add(b); // 先B后A
                eleB.addVolume();
                controller.removeRequest(0);
                while (eleB.getFlag() == 1) {
                    eleA.getVector().add(a);
                    eleA.addVolume();
                    break;
                }
            }
        } else if (fromFloor >= 4 && fromFloor < 15 && toFloor > 15
                && toFloor <= 20) {
            if (eleA.getNowvolume() < eleA.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest b = new PersonRequest(fromFloor, 15, personId);
                PersonRequest a = new PersonRequest(15, toFloor, personId);
                eleB.getVector().add(b); // 先B后A
                eleB.addVolume();
                controller.removeRequest(0);
                while (eleB.getFlag() == 1) {
                    eleA.getVector().add(a);
                    eleA.addVolume();
                    break;
                }
            }
        }
    }

    public void separate6(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { // B TO C
        if (fromFloor >= 4 && fromFloor < 15 && toFloor == 3) {
            if (eleB.getNowvolume() < eleB.getVolume() &&
                    eleC.getNowvolume() < eleC.getVolume()) {
                PersonRequest c = new PersonRequest(5, toFloor, personId);
                PersonRequest b = new PersonRequest(fromFloor, 5, personId);
                eleB.getVector().add(b); // 先B后C
                eleB.addVolume();
                controller.removeRequest(0);
                while (eleB.getFlag() == 1) {
                    eleC.getVector().add(c);
                    eleC.addVolume();
                    break;
                }
            }
        } else if (fromFloor == 2 && toFloor == 3) {
            PersonRequest c = new PersonRequest(1, toFloor, personId);
            PersonRequest b = new PersonRequest(fromFloor, 1, personId);
            eleB.getVector().add(b); // 先B后C
            eleB.addVolume();
            controller.removeRequest(0);
            while (eleB.getFlag() == 1) {
                eleC.getVector().add(c);
                eleC.addVolume();
                break;
            }
        }
    }

    public void separate7(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { // C TO C
        /*if (fromFloor == 3 &&
                toFloor >= 1 && toFloor <= 15 && toFloor % 2 == 1) {
            if (eleC.getNowvolume() < eleC.getVolume()) {
                eleC.getVector().add(request);
                eleC.addVolume();
                controller.removeRequest(0);
            }
        }*/
    }

    public void separate8(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { // C TO A
        if (fromFloor  == 3
                && toFloor >= -3 && toFloor <= 1) {
            if (eleC.getNowvolume() < eleC.getVolume() &&
                    eleA.getNowvolume() < eleA.getVolume()) {
                PersonRequest c = new PersonRequest(fromFloor, 1, personId);
                PersonRequest a = new PersonRequest(1, toFloor, personId);
                eleC.getVector().add(c); // 先C后A
                eleC.addVolume();
                controller.removeRequest(0);
                while (eleC.getFlag() == 1) {
                    eleA.getVector().add(a);
                    eleA.addVolume();
                    break;
                }
            }
        } else if (fromFloor == 3
                && toFloor > 15 && toFloor <= 20) {
            if (eleC.getNowvolume() < eleC.getVolume() &&
                    eleA.getNowvolume() < eleA.getVolume()) {
                PersonRequest c = new PersonRequest(fromFloor, 15, personId);
                PersonRequest a = new PersonRequest(15, toFloor, personId);
                eleC.getVector().add(c); // 先C后A
                eleC.addVolume();
                controller.removeRequest(0);
                while (eleC.getFlag() == 1) {
                    eleA.getVector().add(a);
                    eleA.addVolume();
                    break;
                }
            }
        }
    }

    public void separate9(int personId, int fromFloor, int toFloor,
                          PersonRequest request) { // C to B
        if (fromFloor == 3 && toFloor == 2) {
            if (eleC.getNowvolume() < eleC.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest c = new PersonRequest(fromFloor, 2, personId);
                PersonRequest b = new PersonRequest(2, toFloor, personId);
                eleC.getVector().add(c); // 先C后B
                eleC.addVolume();
                controller.removeRequest(0);
                while (eleC.getFlag() == 1) {
                    eleB.getVector().add(b);
                    eleB.addVolume();
                    break;
                }
            }
        } else if (fromFloor == 3 && toFloor >= 4 &&
                toFloor < 15) {
            if (eleC.getNowvolume() < eleC.getVolume() &&
                    eleB.getNowvolume() < eleB.getVolume()) {
                PersonRequest c = new PersonRequest(fromFloor, 2, personId);
                PersonRequest b = new PersonRequest(2, toFloor, personId);
                eleC.getVector().add(c); // 先C后B
                eleC.addVolume();
                controller.removeRequest(0);
                while (eleC.getFlag() == 1) {
                    eleB.getVector().add(b);
                    eleB.addVolume();
                    break;
                }
            }
        }
    }
}