package com.blkchainsolutions.ogico.dh.email;


import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface EmailBiz {

	public boolean sendEmail(EmailDTO emailDTO) throws WriterException, IOException;
	
}

