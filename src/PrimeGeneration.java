import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PrimeGeneration {
    private static Random random = new SecureRandom();


    public static List<BigInteger> getPQPair() {
        BigInteger p = getPrimeWithHighestBitSet();
        BigInteger q = getPrimeWithHighestBitSet();
        while (!isRelativePrime(q.subtract(BigInteger.ONE).multiply(p.subtract(BigInteger.ONE)), new BigInteger("65537")) || p.compareTo(q) == 0) {
            p = getPrimeWithHighestBitSet();
            q = getPrimeWithHighestBitSet();
            System.out.println("Phi N not Relative to 65537");
        }
        return Arrays.asList(p, q);
    }

    public static BigInteger getPrime() {
        boolean isPrime = false;
        BigInteger randomPrime = BigInteger.TWO;
        System.out.println("Generating Prime:");
        int cntr = 0;
        while (!isPrime && randomPrime.compareTo(BigInteger.ZERO) >= 0) {
            randomPrime = BigInteger.probablePrime(512, random);
            boolean oneP = randomPrime.isProbablePrime(1);
            boolean checkP = randomPrime.subtract(BigInteger.ONE).divide(BigInteger.TWO).isProbablePrime(1);
            isPrime = oneP && checkP;
            if (cntr % 100 == 0) {
                System.out.print(".");
            }
            cntr++;
        }
        System.out.println();
        return randomPrime;
    }

    public static BigInteger getPrimeWithHighestBitSet() {
        boolean isPrime = false;
        BigInteger randomPrime = BigInteger.TWO;
        BigInteger min = BigInteger.ZERO.setBit(512);
        System.out.println("Generating Prime:");
        int cntr = 0;
        while (!isPrime) {
            randomPrime = BigInteger.probablePrime(513, random);
            while (randomPrime.compareTo(min) <= 0 && !randomPrime.testBit(513)) {
                randomPrime = BigInteger.probablePrime(513, random);
            }
            boolean oneP = randomPrime.isProbablePrime(1);
            boolean checkP = randomPrime.subtract(BigInteger.ONE).divide(BigInteger.TWO).isProbablePrime(1);
            isPrime = oneP && checkP;
            if (cntr % 100 == 0) {
                System.out.print(".");
            }
            cntr++;
        }
        System.out.println();
        return randomPrime;
    }

    public static boolean isHighestOrderBitSet (BigInteger a) {
        return a.testBit(512);
    }

    public static boolean isRelativePrime(BigInteger a, BigInteger b) {
        if (GCD(a,b).get(0).compareTo(BigInteger.ONE) == 0) {
            return true;
        }
        return false;
    }

    public static List<BigInteger> GCD(BigInteger a, BigInteger b) {
        BigInteger oldRemainder = a;
        BigInteger remainder = b;
        BigInteger oldX = BigInteger.ONE;
        BigInteger x = BigInteger.ZERO;
        BigInteger oldY = BigInteger.ZERO;
        BigInteger y = BigInteger.ONE;
        BigInteger quotient;
        BigInteger temp;

        while(remainder.compareTo(BigInteger.ZERO) != 0) {
            quotient = oldRemainder.divide(remainder);

            temp = remainder;
            remainder = oldRemainder.subtract(quotient.multiply(temp));
            oldRemainder = temp;

            temp = x;
            x = oldX.subtract(quotient.multiply(temp));
            oldX = temp;

            temp = y;
            y = oldY.subtract(quotient.multiply(temp));
            oldY = temp;
        }

        List<BigInteger> GcdXY = Arrays.asList(oldRemainder, oldX, oldY);
        return GcdXY;
    }

    public static List<Integer> asBinary(BigInteger a) {
        List<Integer> bits = new ArrayList<>();
        for (int i = 0; i < a.bitLength(); i++) {
            if (a.testBit(i)) {
                bits.add(1);
            }
            bits.add(0);
        }
        return bits;
    }

}
