 function encrypt (){
     
 enc = GibberishAES.enc("This sentence is super secret", "ultra-strong-password");
    alert(enc);
    GibberishAES.dec(enc, "ultra-strong-password");

    // Now change size to 128 bits
    GibberishAES.size(128);
    enc = GibberishAES.enc("This sentence is not so secret", "1234");
    GibberishAES.dec(enc, "1234");

    // And finally 192 bits
    GibberishAES.size(192);
    enc = GibberishAES.enc("I can't decide!!!", "whatever");
    GibberishAES.dec(enc, "whatever");


}
function encrypt(inpByte, key, xform) {
Cipher cipher = Cipher.getInstance(xform);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ips);
        return cipher.doFinal(inpByte);
};
        function decrypt(inpByte, key, xform) {
        Cipher cipher = Cipher.getInstance(xform);
                IvParameterSpec ips = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, key, ips);
                return cipher.doFinal(inpByte);
        };
        
        