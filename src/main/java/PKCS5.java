import java.util.Arrays;

class PKCS5 {


    private int blockSize;


    PKCS5(int blockSize) {
        this.blockSize = blockSize;
    }


    public void pad(byte[] a) {
        int c = blockSize - a.length % blockSize;
        for (int i = 0; i < c; i++) {
            a[a.length] = (byte) c;
        }
    }


    public byte[] unpad(byte[] a) {

        int c = a.length % blockSize;
        if (c != 0) throw new Error("PKCS#5::unpad: ByteArray.length isn't a multiple of the blockSize");
        c = a[a.length - 1];

        for (int i = c; i > 0; i--) {
            int v = a[a.length - 1];
            a = Arrays.copyOf(a, a.length - 1);
            if (c != v) throw new Error("PKCS#5:unpad: Invalid padding value. expected [" + c + "], found [" + v + "]");
        }
        return a;
    }


    public void setBlockSize(int bs) {
        blockSize = bs;
    }
}


