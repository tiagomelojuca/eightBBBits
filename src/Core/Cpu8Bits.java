package Core;

import java.util.List;
import java.util.Map;

public interface Cpu8Bits
{
    public void ConnectBus(Bus _bus);
    public void Clock();
    public void Reset();
    public void InterruptRequest();
    public void NonMaskableInterrupt();
    public byte FetchData();
    public List<Instruction> GetInstructionSet();
}
