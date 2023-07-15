package org.coderead.mybatis;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * @author HanBin_Yang
 * @since 2023/7/13 23:13
 */
public class State {
    private static final BigInteger POW64 = BigInteger.valueOf(2).pow(64);

    private static final BigInteger A = new BigInteger("16f11fe89b0d677c", 16);

    private static final BigInteger B = new BigInteger("b480a793d8e6c86c", 16);

    private static final BigInteger C = new BigInteger("6fe2e5aaf078ebc9", 16);

    private static final BigInteger D = new BigInteger("14f994a4c5259381", 16);

    private static final BigInteger T = new BigInteger("6eed0e9da4d94a4f", 16);

    private BigInteger a;

    private BigInteger b;

    private BigInteger c;

    private BigInteger d;

    public State() {
        this.a = A;
        this.b = B;
        this.c = C;
        this.d = D;
    }

    private void writeU64(BigInteger x) {
        BigInteger a = this.a;
        a = diffuse(a.xor(x));
        this.a = this.b;
        this.b = this.c;
        this.c = this.d;
        this.d = a;
    }

    private BigInteger finish(BigInteger total) {
        BigInteger x = this.a.xor(this.b).xor(this.c).xor(this.d).xor(total);
        return diffuse(x);
    }

    private BigInteger readInt(byte[] c4) {
        BigInteger x = BigInteger.valueOf(0);
        for (int i = c4.length - 1; i >= 0; i--) {
            byte b = c4[i];
            x = x.shiftLeft(8);
            x = x.or(BigInteger.valueOf(b));
        }
        return x;
    }

    private BigInteger diffuse(BigInteger x) {
        x = x.multiply(T);
        if (x.compareTo(POW64) >= 0) {
            x = x.mod(POW64);
        }
        BigInteger a = x.shiftRight(32);
        BigInteger b = x.shiftRight(60);
        x = x.xor(a.shiftRight(b.intValue()));

        x = x.multiply(T);
        if (x.compareTo(POW64) >= 0) {
            x = x.mod(POW64);
        }
        return x;
    }

    public long seaHash(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        int chunk = 0;
        byte[] c4 = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            c4[chunk++] = bytes[i];
            if (i == bytes.length - 1) {
                writeU64(readInt(c4));
            } else if (chunk == 8) {
                writeU64(readInt(c4));
                c4 = new byte[8];
                chunk = 0;
            }
        }
        BigInteger res = finish(BigInteger.valueOf(bytes.length));
        return res.longValue();
    }
}
