package core;

import edumips64.core.BitSet64;
import edumips64.core.CPU;
import edumips64.core.Register;
import edumips64.core.is.Instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class BranchPredictor {
    private static ArrayList<long[]> globalHistoryTable = new ArrayList<>();
    private static int globalBranchHistory = 0;
    private static int m = 2;
    private static int n = 1;


    public static long makePrediction(Instruction nextInstruction, Register pc, Logger logger) {
        String commandName = nextInstruction.getName();
        if (commandName.equals("BEQ") || commandName.equals("BEQZ") || commandName.equals("BGEZ") || commandName.equals("BNE")
                || commandName.equals("BNEZ")) {
            //Current command in IF is a branch
            logger.info("Handling Branch instruction");
            boolean entryNotFound = true;
            for (long[] longs : globalHistoryTable) {
                if (longs[0] == (pc.getValue()))
                    entryNotFound = false;
            }
            if (entryNotFound) {
                long[] newEntry = new long[2 + (int) Math.pow(2, m)];
                Arrays.fill(newEntry, n-1);
                newEntry[0] = pc.getValue();
                newEntry[newEntry.length - 1] = 1;
                globalHistoryTable.add(newEntry);
            }
            // {PC,A,B,C,D, PreviousPrediction}
            long[] branchPredictionRow = getRow(pc.getValue());
            //First entry in this row is the PC, we want to look at all the other entries {A/B/C/D...}
            long prediction = branchPredictionRow[1 + globalBranchHistory];

            if (prediction / n == 0) {
                //Predict Not taken
                long[] row = getRow(pc.getValue());
                row[row.length - 1] = 0;
                return 4;
            } else {
                //Predict Taken
                BitSet64 bs = new BitSet64();

                //The offset value is found at different places depending on the Branch type
                int offsetField = 1;
                if (commandName.equals("BEQ") || commandName.equals("BNE")) {
                    offsetField = 2;
                }
                //set the PreviousPrediction to true
                long[] row = getRow(pc.getValue());
                row[row.length - 1] = 1;
                // Next instruction will be at offset + 4
                int offset = nextInstruction.getParams().get(offsetField);
                return offset + 4;
            }
        }
        return 4;
    }

    public static boolean getPrediction(Long pc) {
        long[] row = getRow(pc);
        return row[row.length - 1] == 1;
    }

    public static void reset() {
        globalHistoryTable = new ArrayList<>();
        globalBranchHistory = 0;
    }

    public static void updatePrediction(Long branchPC, boolean taken) {
        long[] modifiedRow = getRow(branchPC);
        setLocalBranchHistory(modifiedRow, taken);
        setGlobalBranchHistory(taken);
        for (int i =0; i<globalHistoryTable.size() ; i++ ) {
            if (globalHistoryTable.get(i)[0] == modifiedRow[0]) {
                globalHistoryTable.set(i,modifiedRow);
            }
        }
    }

    private static void setLocalBranchHistory(long[] row, boolean taken){
        long valueToUpdate = row[1+globalBranchHistory];
        row[1+globalBranchHistory] = updateValueInRowOrGlobalBranchHistory(taken, valueToUpdate, n);
    }

    private static void setGlobalBranchHistory(boolean taken){
        globalBranchHistory = (int) updateValueInRowOrGlobalBranchHistory(taken, globalBranchHistory, m);
    }

    private static long updateValueInRowOrGlobalBranchHistory(boolean taken, long valueToUpdate, int sizeOfElement) {
        //taken increments the localStateMachine
        //not taken decrements the localStateMachine
        if (taken){
            if(valueToUpdate == Math.pow(2,sizeOfElement-1) -1){
                valueToUpdate = (long) (Math.pow(2,sizeOfElement) -1);
                return valueToUpdate;
            }
            valueToUpdate++;
            // We have 2^m columns in our table. The max index of predictors is (2^m)-1
            if (valueToUpdate > Math.pow(2,sizeOfElement) -1){
                valueToUpdate = (int) (Math.pow(2,sizeOfElement) -1);
            }
        } else {
            if (valueToUpdate == Math.pow(2,sizeOfElement-1)){
                valueToUpdate = 0;
                return valueToUpdate;
            }
            valueToUpdate--;
            if (valueToUpdate < 0) {
                valueToUpdate = 0;
            }
        }
        return valueToUpdate;
    }

    private static long[] getRow(Long pc){
        for (long[] row : globalHistoryTable) {
            if (row[0] == pc) {
                return row;
            }
        }
        throw new IllegalArgumentException();
    }
}
