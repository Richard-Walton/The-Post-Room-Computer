package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.computer.*;

/**
 * Listener for Instruction Decoder events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public interface InstructionDecoderListener {
    /**
     * Called when the instruction decoder has decoded an instruction
     * @param decoder The instruction decoder which decoded the instruction
     * @param instruction The instruction
     */
    public void instructionDecoded(InstructionDecoder decoder, String instruction);
    
    /**
     * Called when the instruction decoder throws an exception due to a malformed instruction
     * @param decoder The instruction decoder which attempted to decode the instruction
     * @param instruction The malformed instruction
     * @param error Java exception which was thrown
     */
    public void decodeError(InstructionDecoder decoder, String instruction, Throwable error);
}