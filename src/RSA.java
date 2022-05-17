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
    public void encrypt() {
        BigInteger m = new BigInteger("213125769610681219222423285489127921180276917615238827344623759847555851056051461716385716076387700789559362056775918847885906524280601834392438907503180701598362345638784202336522350723217891862697530875779151405616399169080437548119138189700127503594687765637091675497307706824937882405139844117736268310066");
        BigInteger n = new BigInteger("338616656331092895553138513209827192502797990587740050337702210386724049267043356830602442760300727369576346577281872964842407008451014978418132547574509132789599139727667782724013154907562635130134530328217753036660010175451255469353518625186023423677500149578355784081300929429465468988495421439510560741181");
        BigInteger e = new BigInteger("65537");
        BigInteger c = gToTheXModP(m, e, n );

        System.out.println("******C******");
        System.out.println(c);
        System.out.println();
    }

    @Test
    public void decrypt() {
        BigInteger c = new BigInteger("9673256701314478651367290736504569435820344301256587070567293557114073470334597168208333603199215500416859816371351098483783174614471878950311612874309189824707472663202606079889020342571927876702628073566079498576248370159810388097504010493924469628281502021150449915424142277552620232173373901668219440133");
        BigInteger n = new BigInteger("521217552844375778228191317260279098537971845010655897752314463265844541848164910052844041560207298098262766290169405074169022978010895678793168335315620006680112429542462118279461910801424757380684629444138264424429081056406037124357055002924409344767312929611189346910045265970489442059681697803132009606201");
        BigInteger d = new BigInteger("309929475477139998436800824475228900773986645712578548536058938210018185083586165750024143607447371818809222306909100443114070303081993448015163495845853638111865354836741283395155682891325664648401874835765912530787515014188370192718124291130504602044107735540434261723570004003062239817788897372572070497193");
        BigInteger m = gToTheXModP(c, d, n );

        System.out.println("******M******");
        System.out.println(m);
        System.out.println();
    }

    @Test
    public void generateParameters() {
        List<BigInteger> pq = PrimeGeneration.getPQPair();
        BigInteger p = pq.get(0);
        BigInteger q = pq.get(1);
        BigInteger e = new BigInteger("65537");
        BigInteger n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));


        assertEquals(BigInteger.ONE, PrimeGeneration.GCD(phiN, new BigInteger("65537")).get(0));
        assertTrue(p.testBit(512));
        assertTrue(q.testBit(512));

        BigInteger d = PrimeGeneration.GCD(e, phiN).get(1);
        while (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(phiN);
        }

        assertEquals(BigInteger.ONE, e.multiply(d).mod(phiN));

        for (int i = 0; i < 50; i++) {
            BigInteger m = BigInteger.probablePrime(512, random);
            if (n.compareTo(m) > 0) {
                assertEquals(m, gToTheXModP(gToTheXModP(m, e, n), d, n));
            }
        }


        System.out.println("******P******");
        System.out.println(p);
        System.out.println("******Q******");
        System.out.println(q);
        System.out.println("******N******");
        System.out.println(n);
        System.out.println("******PhiN******");
        System.out.println(phiN);
        System.out.println("******E******");
        System.out.println(e);
        System.out.println("******D******");
        System.out.println(d);
        System.out.println();

    }


    //unit tests
    @Test
    public void getPQPairAndD() {
        List<BigInteger> pq = PrimeGeneration.getPQPair();
        BigInteger p = pq.get(0);
        BigInteger q = pq.get(0);
        BigInteger e = new BigInteger("65537");
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));


        assertEquals(BigInteger.ONE, PrimeGeneration.GCD(phiN, new BigInteger("65537")).get(0));
        assertTrue(p.testBit(512));
        assertTrue(q.testBit(512));

        System.out.println("******P******");
        System.out.println(p);
        System.out.println("******Q******");
        System.out.println(q);
        System.out.println("******N******");
        System.out.println(p.multiply(q));
        System.out.println("******PhiN******");
        System.out.println(phiN);
        System.out.println("******E******");
        System.out.println(e);

        BigInteger d = PrimeGeneration.GCD(e, phiN).get(1);
        while (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(phiN);
        }


        assertEquals(BigInteger.ONE, e.multiply(d).mod(phiN));

        System.out.println("******D******");
        System.out.println(d);
        System.out.println();

    }

    @Test
    public void test() {
        BigInteger m = new BigInteger("213125769610681219222423285489127921180276917615238827344623759847555851056051461716385716076387700789559362056775918847885906524280601834392438907503180701598362345638784202336522350723217891862697530875779151405616399169080437548119138189700127503594687765637091675497307706824937882405139844117736268310066");
        BigInteger n = new BigInteger("338616656331092895553138513209827192502797990587740050337702210386724049267043356830602442760300727369576346577281872964842407008451014978418132547574509132789599139727667782724013154907562635130134530328217753036660010175451255469353518625186023423677500149578355784081300929429465468988495421439510560741181");
        System.out.println(m.compareTo(n) < 0);
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
