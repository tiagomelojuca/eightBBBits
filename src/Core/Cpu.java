package Core;

import java.util.List;public interface Cpu
{
    public void Clock();
    public void Reset();
    public void InterruptRequest();
    public void NonMaskableInterrupt();
    public byte FetchData();
    public List<Instruction> GetInstructionSet();
}
