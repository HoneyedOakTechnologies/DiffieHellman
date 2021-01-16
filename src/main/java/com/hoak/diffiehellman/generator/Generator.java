package com.hoak.diffiehellman.generator;

import com.hoak.diffiehellman.DiffieHellman;
import com.hoak.diffiehellman.participant.DHParticipant;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    public static void GenerateSharedSecretForUserGroup(DiffieHellman diffieHellman, List<DHParticipant> dhParticipantList) throws IllegalArgumentException {
        if (dhParticipantList.size() < 2) {
            throw new IllegalArgumentException("please provide a list of at least two participants");
        }

        if (dhParticipantList.stream().map(DHParticipant::getPersonalSecret).anyMatch(bigInteger -> bigInteger.bitLength() > diffieHellman.getBits() || bigInteger.signum() <= 0)) {
            throw new IllegalArgumentException(String.format("a DiffieHellman participant has chosen a secret equal to zero or negative or bigger than the supported number of bits (%d)", diffieHellman.getBits()));
        }

        GenerateSharedSecretForUserGroup(diffieHellman, diffieHellman.getG(), dhParticipantList);
    }

    private static void GenerateSharedSecretForUserGroup(DiffieHellman diffieHellman, BigInteger sharedSecret, List<DHParticipant> DHParticipantList) {
        BigInteger sharedSecret1 = sharedSecret;
        BigInteger sharedSecret2 = sharedSecret;
        List<DHParticipant> first = new ArrayList<>(DHParticipantList.subList(0, (DHParticipantList.size() + 1) / 2));
        List<DHParticipant> second = new ArrayList<>(DHParticipantList.subList((DHParticipantList.size() + 1) / 2, DHParticipantList.size()));

        for (DHParticipant dhParticipant : first) {
            sharedSecret1 = diffieHellman.powerModN(sharedSecret1, dhParticipant.getPersonalSecret());
            dhParticipant.setSharedSecret(sharedSecret1);
        }

        for (DHParticipant dhParticipant : second) {
            sharedSecret2 = diffieHellman.powerModN(sharedSecret2, dhParticipant.getPersonalSecret());
            dhParticipant.setSharedSecret(sharedSecret2);
        }

        if (first.size() > 1) {
            GenerateSharedSecretForUserGroup(diffieHellman, sharedSecret2, first);
        } else {
            first.get(0).setSharedSecret(diffieHellman.powerModN(sharedSecret2, first.get(0).getPersonalSecret()));
        }

        if (second.size() > 1) {
            GenerateSharedSecretForUserGroup(diffieHellman, sharedSecret1, second);
        } else {
            second.get(0).setSharedSecret(diffieHellman.powerModN(sharedSecret1, second.get(0).getPersonalSecret()));
        }
    }
}
