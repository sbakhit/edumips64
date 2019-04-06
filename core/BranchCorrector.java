package core;

import edumips64.core.BitSet64;
import edumips64.core.CPU;
import edumips64.core.IrregularWriteOperationException;
import edumips64.core.Register;
import edumips64.core.is.InstructionsUtils;
import edumips64.core.is.JumpException;
import edumips64.core.is.TwosComplementSumException;
import edumips64.utils.IrregularStringOfBitsException;

import java.util.logging.Logger;

public class BranchCorrector {

    public static void correctPrediction(boolean condition, boolean prediction, String offset, CPU cpu, Logger logger)
            throws IrregularWriteOperationException, IrregularStringOfBitsException, TwosComplementSumException, JumpException {
        if (!condition && prediction) {
            /*
             * predicted taken but is not taken
             *
             * set PC to branch+4
             */
            //increment number of misses
            cpu.incBranchMisses();

            String pc_new = "";
            Register pc = cpu.getPC();

            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
            BitSet64 bs_temp = new BitSet64();
            bs_temp.writeDoubleWord(+4);

            BitSet64 bs_PC = new BitSet64();
            bs_PC.writeDoubleWord(cpu.getBeforeLastPC());

            pc_new = InstructionsUtils.twosComplementSum(bs_PC.getBinString(), bs_temp.getBinString());

            //updating program counter
            pc.setBits(pc_new, 0);

            logger.info("goto1: " + pc.getHexString());
            // update correlating predictor
            BranchPredictor.updatePrediction(cpu.getBeforeLastPC(), false);

            throw new JumpException();
        } else if (condition && !prediction) {
            /*
             * predicted not taken but is taken
             *
             * set PC to offset
             */
            //increment number of misses
            cpu.incBranchMisses();

            String pc_new = "";
            Register pc = cpu.getPC();
            String pc_old;

            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
            BitSet64 bs_temp = new BitSet64();
            bs_temp.writeDoubleWord(+4);

            BitSet64 bs_PC = new BitSet64();
            bs_PC.writeDoubleWord(cpu.getBeforeLastPC());

            pc_old = InstructionsUtils.twosComplementSum(bs_PC.getBinString(), bs_temp.getBinString());

            //updating program counter
            pc_new = InstructionsUtils.twosComplementSum(pc_old, offset);
            pc.setBits(pc_new, 0);

            logger.info("goto2: " + pc.getHexString());
            
            // update correlating predictor
            BranchPredictor.updatePrediction(cpu.getBeforeLastPC(), true);

            throw new JumpException();
        }
    }

}
