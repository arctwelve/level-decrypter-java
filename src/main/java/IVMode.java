
class IVMode {


    BlowFishKey key;
    PKCS5 padding;

    private byte[] iv;
    private byte[] lastIV;
    int blockSize;


    IVMode(BlowFishKey key, PKCS5 padding) {

        this.key = key;
        blockSize = key.getBlockSize();

        if (padding == null) {
            padding = new PKCS5(blockSize);
        } else {
            padding.setBlockSize(blockSize);
        }
        this.padding = padding;

        //prng = new Random;
        iv = null;
        lastIV = new byte[0];
    }


    int getBlockSize() {
        return key.getBlockSize();
    }


    void setIV(byte[] value) {
        iv = value;
        lastIV = new byte[iv.length];
        System.arraycopy(iv, 0, lastIV, 0, iv.length);
    }



    byte[] getIV4d() {
        byte[] vec = new byte[iv.length];
        if (iv.length > 0) {
            //vec.writeBytes(iv);
            // src, dest
            System.arraycopy(iv, 0, vec, 0, iv.length);
        } else {
            throw new Error("an IV must be set before calling decrypt()");
        }
        return vec;
    }
}
