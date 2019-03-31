/*
 * BNE.java
 *
 * may 2006
 * Instruction BNE of the MIPS64 Instruction Set
 * (c) 2006 EduMips64 project - Trubia Massimo, Russo Daniele
 *
 * This file is part of the EduMIPS64 project, and is released under the GNU
 * General Public License.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edumips64.core.is;
import core.BranchPredictor;
import edumips64.core.*;
import edumips64.utils.*;
/** <pre>
 *         Syntax: BNE rt, rs, immediate
 *    Description: if rs != rt then branch
 *                 To compare GPRs then do a PC-relative conditional branch
 *  </pre>
  * @author Trubia Massimo, Russo Daniele
 */

public class BNE extends FlowControl_IType {
    final String OPCODE_VALUE="000101";
    
    public BNE() {
        super.OPCODE_VALUE = OPCODE_VALUE;
        syntax="%R,%R,%B";
	name="BNE";
    }

    public void ID() throws RAWException, IrregularWriteOperationException, IrregularStringOfBitsException,TwosComplementSumException, JumpException {
        if(cpu.getRegister(params.get(RS_FIELD)).getWriteSemaphore()>0 || cpu.getRegister(params.get(RT_FIELD)).getWriteSemaphore()>0)
            throw new RAWException();
        //getting registers rs and rt
        String rs=cpu.getRegister(params.get(RS_FIELD)).getBinString();
        String rt=cpu.getRegister(params.get(RT_FIELD)).getBinString();
         //converting offset into a signed binary value of 64 bits in length
        BitSet64 bs=new BitSet64();
        bs.writeHalf(params.get(OFFSET_FIELD));
        String offset=bs.getBinString();

        //calculating actual branch outcome
        boolean condition=!rs.equals(rt);
        logger.info("actual: " + condition);
//        if(condition) {
//            String pc_new="";
//            Register pc=cpu.getPC();
//            String pc_old=cpu.getPC().getBinString();
//
//            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
//            BitSet64 bs_temp=new BitSet64();
//            bs_temp.writeDoubleWord(-4);
//            pc_old=InstructionsUtils.twosComplementSum(pc_old,bs_temp.getBinString());
//
//            //updating program counter
//            pc_new=InstructionsUtils.twosComplementSum(pc_old,offset);
//            pc.setBits(pc_new,0);
//
//            logger.info("pc_old=" + pc_old);
//            logger.info("pc_new=" + pc_new);
//            logger.info("pc_old(from cpu)=" + cpu.getLastPC().getBinString());
//            throw new JumpException();
//        }

        /* outcome   :   prediction
        *  False  :   False: we cool
        *  False  :   True: jump back to branch+4
        *  True   :   False: take the branch
        *  True   :   True: we cool
        */
        logger.info("pc: " + cpu.getPC());
        logger.info("pc_old: " + cpu.getLastPC());
//        while(!CPU.mutex.tryAcquire()) {
//
//        }
//        try {
//            CPU.mutex.acquire();
//            try {
//                logger.info("mutex acquired by " + this.getFullName() + " ID");
//            } finally {
//                logger.info("mutex released by " + this.getFullName() + " ID");
//                CPU.mutex.release();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        boolean prediction = BranchPredictor.getPrediction(cpu.getBeforeLastPC());
        logger.info("predicted: " + prediction);

        if(!condition && prediction) {
            /* misprediction: branch taken but should be not taken
            * go back to branch
            * fetch next instruction
            * cpu.PC should be branch+4
            */

            String pc_new="";
            Register pc=cpu.getPC();
            String pc_old=cpu.getPC().getBinString();

            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
            BitSet64 bs_temp=new BitSet64();
            bs_temp.writeDoubleWord(+4);

            BitSet64 bs_PC=new BitSet64();
            bs_PC.writeDoubleWord(cpu.getBeforeLastPC());

            pc_new=InstructionsUtils.twosComplementSum(bs_PC.getBinString(),bs_temp.getBinString());

//            updating program counter
//            BitSet64 zeroSet = new BitSet64();
//            zeroSet.writeHalf(0);
//            String zero = zeroSet.getBinString();
//            pc_new=InstructionsUtils.twosComplementSum(pc_old,zero);
            pc.setBits( pc_new,0);

//            assert pc.equals(pc_old);

            logger.info("goto1: " + cpu.getPC().getBinString());

            throw new JumpException();
        } else if(condition && !prediction) {
            // misprediction: branch not taken but should be taken
            String pc_new="";
            Register pc=cpu.getPC();
            String pc_old=cpu.getPC().getBinString();

            //subtracting 4 to the pc_old temporary variable using bitset64 safe methods
            BitSet64 bs_temp=new BitSet64();
            bs_temp.writeDoubleWord(-4);
            pc_old=InstructionsUtils.twosComplementSum(pc_old,bs_temp.getBinString());

            //updating program counter
            pc_new=InstructionsUtils.twosComplementSum(pc_old,offset);
            pc.setBits(pc_new,0);

            logger.info("goto2: " + cpu.getPC().getBinString());

            throw new JumpException();
        }
    }

}
