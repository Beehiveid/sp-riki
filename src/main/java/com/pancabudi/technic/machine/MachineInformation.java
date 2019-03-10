package com.pancabudi.technic.machine;

public class MachineInformation extends Machine {
    private int outstanding;

    MachineInformation(Machine m, int count) {
        super(m);
        this.outstanding = count;
    }

    public int getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(int outstanding) {
        this.outstanding = outstanding;
    }
}
