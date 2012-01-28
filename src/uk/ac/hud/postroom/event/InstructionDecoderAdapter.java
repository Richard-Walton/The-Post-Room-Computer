package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.computer.*;

/**
 * Listener adapter for instruction decoder events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class InstructionDecoderAdapter implements InstructionDecoderListener {

    /** @inheritDoc **/
    public void instructionDecoded(InstructionDecoder decoder, String instruction) {}

    /** @inheritDoc **/
    public void decodeError(InstructionDecoder decoder, String instruction, Throwable error) {}
}