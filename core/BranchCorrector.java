package core;

import edumips64.core.BitSet64;
import edumips64.core.IrregularWriteOperationException;
import edumips64.core.Register;
import edumips64.core.is.InstructionsUtils;
import edumips64.core.is.JumpException;
import edumips64.core.is.TwosComplementSumException;
import edumips64.utils.IrregularStringOfBitsException;

import java.util.logging.Logger;

public class BranchCorrector {

    public static void correctPrediction(boolean condition, boolean prediction, String offset, Long PC_Branch, Register pc, Logger logger)
            throws IrregularWriteOperationException, IrregularStringOfBitsException, TwosComplementSumException, JumpException {
        if (!condition && prediction) {
            /*
             * predicted taken but is not taken
             *
             * set PC to branch+4
             */
            String pc_new = "";
            String pc_old = pc.getBinString();

            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
            BitSet64 bs_temp = new BitSet64();
            bs_temp.writeDoubleWord(+4);

            BitSet64 bs_PC = new BitSet64();
            bs_PC.writeDoubleWord(PC_Branch);

            pc_new = InstructionsUtils.twosComplementSum(bs_PC.getBinString(), bs_temp.getBinString());

            //updating program counter
            pc.setBits(pc_new, 0);

            logger.info("goto1: " + pc.getHexString());

            throw new JumpException();
        } else if (condition && !prediction) {
            /*
             * predicted not taken but is taken
             *
             * set PC to offset
             */
            String pc_new = "";
            String pc_old = pc.getBinString();

            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
            BitSet64 bs_temp = new BitSet64();
            bs_temp.writeDoubleWord(-4);
            pc_old = InstructionsUtils.twosComplementSum(pc_old, bs_temp.getBinString());

            //updating program counter
            pc_new = InstructionsUtils.twosComplementSum(pc_old, offset);
            pc.setBits(pc_new, 0);

            logger.info("goto2: " + pc.getHexString());

            throw new JumpException();
        }
    }

}
