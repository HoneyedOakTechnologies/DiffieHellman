package com.hoak.diffiehellman.participant;

import java.math.BigInteger;

public abstract class AbstractDHParticipant implements DHParticipant {
    private final BigInteger personalSecret;
    protected BigInteger sharedSecret;

    public AbstractDHParticipant(BigInteger personalSecret) {
        this.personalSecret = personalSecret;
    }

    @Override
    public void setSharedSecret(BigInteger sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    @Override
    public BigInteger getPersonalSecret() {
        return personalSecret;
    }
}
