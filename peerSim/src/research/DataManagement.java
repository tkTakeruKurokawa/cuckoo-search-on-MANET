package research;

import peersim.core.Control;

public class DataManagement implements Control {
    private static int globalCycle = 1;

    public DataManagement(String s) {
    }

    public boolean execute() {
        for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
            Data.getData(dataID).nextCycle();
        }

        if (globalCycle % 5 == 0) {
            if (Data.getNowVariety() < Data.getMaxVariety()) {
                Data.uploadData();
            }
        }

        globalCycle++;

        return false;
    }
}