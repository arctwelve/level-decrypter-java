
class CBCMode extends IVMode {


    CBCMode(BlowFishKey key, PKCS5 padding) {
        super(key, padding);
    }


    public byte[] decrypt(byte[] src) {

        byte[] vector = getIV4d();
        byte[] tmp = new byte[src.length];

        for (int i = 0; i < src.length; i += blockSize) {

            System.arraycopy(src, i, tmp, 0, blockSize);
            key.decrypt(src, i);

            for (int j = 0; j < blockSize; j++) {
                src[i + j] ^= vector[j];
            }
            System.arraycopy(tmp, 0, vector, 0, blockSize);
        }
        return padding.unpad(src);
    }
}


    

