package com.hoak.diffiehellman.participant;

import java.math.BigInteger;

public interface DHParticipant {

    void setSharedSecret(BigInteger sharedSecret);

    BigInteger getPersonalSecret();
}
