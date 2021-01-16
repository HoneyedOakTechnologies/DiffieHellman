package com.hoak.diffiehellman.generator;

import com.hoak.diffiehellman.participant.AbstractDHParticipant;

import java.math.BigInteger;

public class TestDHParticipant extends AbstractDHParticipant {
    public TestDHParticipant(BigInteger personalSecret) {
        super(personalSecret);
    }

    BigInteger getSharedSecret() {
        return sharedSecret;
    }
}
