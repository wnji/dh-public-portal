package com.blkchainsolutions.ogico.dh.qrCodeGenerator;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodeGenerator {

	public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;
}
