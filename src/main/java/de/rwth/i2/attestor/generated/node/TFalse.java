/* This file was generated by SableCC (http://www.sablecc.org/). */

package de.rwth.i2.attestor.generated.node;

import de.rwth.i2.attestor.generated.analysis.*;

@SuppressWarnings("nls")
public final class TFalse extends Token
{
    public TFalse()
    {
        super.setText("F");
    }

    public TFalse(int line, int pos)
    {
        super.setText("F");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TFalse(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFalse(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TFalse text.");
    }
}