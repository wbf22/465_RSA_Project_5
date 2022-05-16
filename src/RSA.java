import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RSA {
    private static SecureRandom random = new SecureRandom();


    //Main methods
    @Test
    public void generateParameters() {
        List<BigInteger> pq = PrimeGeneration.getPQPair();
        BigInteger p = pq.get(0);
        BigInteger q = pq.get(0);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        System.out.println("******P******");
        System.out.println(pq.get(0));
        System.out.println("******Q******");
        System.out.println(pq.get(1));
        System.out.println("******N******");
        System.out.println(p.multiply(q));
        System.out.println("******PhiN******");
        System.out.println(phiN);
        System.out.println("******E******");
        System.out.println(new BigInteger("65537"));



    }


    //unit tests
    @Test
    public void getPQPair() {
        List<BigInteger> pq = PrimeGeneration.getPQPair();
        BigInteger p = pq.get(0);
        BigInteger q = pq.get(0);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        System.out.println("******P******");
        System.out.println(pq.get(0));
        System.out.println("******Q******");
        System.out.println(pq.get(1));
        System.out.println("******N******");
        System.out.println(p.multiply(q));
        System.out.println("******PhiN******");
        System.out.println(phiN);
        System.out.println("******E******");
        System.out.println(new BigInteger("65537"));


        assertEquals(BigInteger.ONE, PrimeGeneration.GCD(phiN, new BigInteger("65537")).get(0));
        assertTrue(p.testBit(512));
        assertTrue(q.testBit(512));
    }

    @Test
    public void test() {
//        BigInteger p = PrimeGeneration.getPrimeWithHighestBitSet();
        BigInteger q = new BigInteger("340082798928912883206524389920770239396654218957311617020958100775309094911208014747882665097860838444680411029770155963359701126622506390645176854935535544482723472059080926466283351954099437835142725874844724526579238648790585381332874813469854787189117765136321319577347655309945168840146165001827996811089");
//        System.out.println(p.compareTo(q));
        System.out.println(q.bitLength());
        System.out.println(q);
        List<Integer> binary = PrimeGeneration.asBinary(q);
        System.out.println(binary);
    }


    @Test
    public void highestOrderBitSet() {
        BigInteger p = new BigInteger("376136317015817541134565651197938274527949467832977278939335518271565236964492889587764292646346828242549008563838449011089273599825220180984524321167802755");
        assertFalse(PrimeGeneration.isHighestOrderBitSet(p));

        BigInteger onlyLastBitSet = BigInteger.TWO.pow(512);
        assertTrue(PrimeGeneration.isHighestOrderBitSet(onlyLastBitSet));

        BigInteger secondToLastBitSet = BigInteger.TWO.pow(512).subtract(BigInteger.ONE);
        assertFalse(PrimeGeneration.isHighestOrderBitSet(secondToLastBitSet));
    }

    @Test
    public void GCD() {
        BigInteger a = new BigInteger("240");
        BigInteger b = new BigInteger("46");
        BigInteger gcd = PrimeGeneration.GCD(a,b).get(0);
        assertEquals(gcd, BigInteger.TWO);

        a = PrimeGeneration.getPrime();
        b = a.multiply(PrimeGeneration.getPrime());
        gcd = PrimeGeneration.GCD(a,b).get(0);
        assertEquals(gcd, a);

        a = PrimeGeneration.getPrime();
        b = PrimeGeneration.getPrime();
        gcd = PrimeGeneration.GCD(a,b).get(0);
        assertEquals(gcd, BigInteger.ONE);
    }

    @Test
    public void Diffe_Hellman() {
        BigInteger p = new BigInteger("509");
        BigInteger a = new BigInteger("1024");
        BigInteger g = new BigInteger("6546");
        BigInteger op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("376"));


        p = new BigInteger("516");
        a = new BigInteger("346");
        g = new BigInteger("8975");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("253"));

        p = new BigInteger("753");
        a = new BigInteger("4");
        g = new BigInteger("75855");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("45"));

        p = new BigInteger("156");
        a = new BigInteger("498");
        g = new BigInteger("1567");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("25"));

        p = new BigInteger("11116546516516");
        a = new BigInteger("453");
        g = new BigInteger("5354534");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("6701686931428"));

        p = new BigInteger("154444444444");
        a = new BigInteger("242");
        g = new BigInteger("2");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("87790262464"));

        p = new BigInteger("41");
        a = new BigInteger("1");
        g = new BigInteger("4");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("4"));

        p = new BigInteger("651");
        a = new BigInteger("2");
        g = new BigInteger("6516");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("36"));

        p = new BigInteger("1");
        a = new BigInteger("1");
        g = new BigInteger("1");
        op = gToTheXModP(g, a, p);
        assertEquals(op, new BigInteger("0"));

        p = PrimeGeneration.getPrime();
        a = getRandomBigInt(512);
        g = getRandomBigInt(512);
        op = gToTheXModP(g, a, p);
        System.out.println(op);

    }

    //math functions
    private static BigInteger gToTheXModP(BigInteger g, BigInteger x, BigInteger p) {

        //find powers of 2 that are less than a
        List<BigInteger> gTable = calcGTable(g, x, p);
        BigInteger remainder = x;
        BigInteger total = BigInteger.ONE;

        while (remainder.compareTo(BigInteger.ZERO) > 0) {
            if (remainder.compareTo(BigInteger.TWO) >= 0) {
                int reduction = getBiggestLessThanOrEqualRemainder(remainder, gTable);
                remainder = remainder.subtract(BigInteger.TWO.pow(reduction));
                total = total.multiply(gTable.get(reduction - 1)).mod(p);
            }

            if (remainder.compareTo(BigInteger.ONE) == 0) {
                total = total.multiply(g.mod(p)).mod(p);
                remainder = remainder.subtract(BigInteger.ONE);
            }
        }

        return total;
    }

    private static BigInteger getRandomBigInt(int numBits) {
        return BigInteger.probablePrime(numBits, random).add(BigInteger.valueOf(random.nextInt())).multiply(BigInteger.valueOf(random.nextInt(100)));
    }


    //auxiliary functions
    private static int reduce(BigInteger a) {
        int cntr = 1;
        BigInteger measure = BigInteger.TWO;
        while (a.compareTo(measure) == 0 || a.compareTo(measure) == 1) {
            measure.multiply(BigInteger.TWO);
            cntr++;
        }
        return cntr - 1;
    }

    private static List<BigInteger> calcGTable(BigInteger g, BigInteger a, BigInteger p) {
        List<BigInteger> gTable = new ArrayList<>();
        BigInteger exp = BigInteger.TWO;
        BigInteger current = g.pow(2).mod(p);
        gTable.add(current);
        while (exp.compareTo(a) < 0) {
            exp = exp.add(exp);
            current = current.multiply(current).mod(p);
            gTable.add(current);
        }
        return gTable;
    }

    private static int getBiggestLessThanOrEqualRemainder(BigInteger remainder, List<BigInteger> gTable) {
        int chosenEntry = 1;

        while(BigInteger.TWO.pow(chosenEntry).compareTo(remainder) <= 0) {
            chosenEntry++;
        }
        return chosenEntry - 1;
    }
}
