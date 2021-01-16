package com.hoak.diffiehellman.generator;

import com.hoak.diffiehellman.DiffieHellman;
import com.hoak.diffiehellman.participant.DHParticipant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeneratorTest {

    private static Stream<Arguments> provideGenerateSharedSecretParameters() {
        return Stream.of(
                Arguments.of(2, new DiffieHellman._1K()),
                Arguments.of(4, new DiffieHellman._1K()),
                Arguments.of(10, new DiffieHellman._1K()),
                Arguments.of(2, new DiffieHellman._2K()),
                Arguments.of(4, new DiffieHellman._2K()),
                Arguments.of(10, new DiffieHellman._2K()),
                Arguments.of(2, new DiffieHellman._3K()),
                Arguments.of(4, new DiffieHellman._3K()),
                Arguments.of(10, new DiffieHellman._3K()),
                Arguments.of(2, new DiffieHellman._4K()),
                Arguments.of(4, new DiffieHellman._4K()),
                Arguments.of(10, new DiffieHellman._4K()),
                Arguments.of(2, new DiffieHellman._8K()),
                Arguments.of(4, new DiffieHellman._8K()),
                Arguments.of(10, new DiffieHellman._8K())
        );
    }

    @ParameterizedTest
    @MethodSource("provideGenerateSharedSecretParameters")
    void generateSharedSecret(int participantCount, DiffieHellman diffieHellman) throws IllegalAccessException {
        Random random = new SecureRandom();

        List<DHParticipant> list = new ArrayList<>();
        for (int i = 0; i < participantCount; i++) {
            list.add(new TestDHParticipant(diffieHellman.randomNonZeroCandidate(random)));
        }

        Generator.GenerateSharedSecretForUserGroup(diffieHellman, list);
        BigInteger sharedSecret = ((TestDHParticipant) list.get(0)).getSharedSecret();
        BigInteger sharedSecret1 = ((TestDHParticipant) list.get(1)).getSharedSecret();
        BigInteger sharedSecret2 = ((TestDHParticipant) list.get(participantCount - 1)).getSharedSecret();

        assertEquals(sharedSecret, sharedSecret1);
        assertEquals(sharedSecret1, sharedSecret2);
    }

    @Test()
    void testSingleApplicant() {
        Random random = new SecureRandom();
        DiffieHellman diffieHellman = new DiffieHellman._1K();

        List<DHParticipant> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add(new TestDHParticipant(diffieHellman.randomNonZeroCandidate(random)));
        }

        assertThrows(IllegalArgumentException.class, () -> Generator.GenerateSharedSecretForUserGroup(diffieHellman, list));
    }

    @Test()
    void testApplicantWrongSecret() {
        Random random = new SecureRandom();
        DiffieHellman diffieHellman = new DiffieHellman._1K();

        List<DHParticipant> listZero = new ArrayList<>();
        List<DHParticipant> listNegative = new ArrayList<>();
        List<DHParticipant> listTooLarge = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            listZero.add(new TestDHParticipant(diffieHellman.randomNonZeroCandidate(random)));
            listNegative.add(new TestDHParticipant(diffieHellman.randomNonZeroCandidate(random)));
            listTooLarge.add(new TestDHParticipant(diffieHellman.randomNonZeroCandidate(random)));
        }

        listZero.add(new TestDHParticipant(BigInteger.ZERO));
        listNegative.add(new TestDHParticipant(new BigInteger(String.valueOf(-8))));
        listTooLarge.add(new TestDHParticipant(BigInteger.TEN.shiftLeft(diffieHellman.getBits())));


        assertThrows(IllegalArgumentException.class, () -> Generator.GenerateSharedSecretForUserGroup(diffieHellman, listZero));
        assertThrows(IllegalArgumentException.class, () -> Generator.GenerateSharedSecretForUserGroup(diffieHellman, listNegative));
        assertThrows(IllegalArgumentException.class, () -> Generator.GenerateSharedSecretForUserGroup(diffieHellman, listTooLarge));
    }
}