package com.blkchainsolutions.platform.service.connector;

import com.blkchainsolutions.Configure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class IPFSConnector {
	
	@Autowired
	public Configure configure=null;
	
	private static Configure config=null;
	
	@Value("${ogcio.ipfsDownload}")
	public static String ipfsdownloadhost;
	
	@Value("${ogcio.ipfsDownload}")
	public String _ipfsDownload;
	
	@Value("${ogcio.publicIpfsDownload}")
	public static String publicIpfsDownload;
	
	
	@Value("${ogcio.publicIpfsDownload}")
	public String _publicIpfsDownload;
	
	
	
	@PostConstruct
    public void init() {
		ipfsdownloadhost=_ipfsDownload;
		publicIpfsDownload=_publicIpfsDownload;
    }
	
	public static byte[] doDownload(String hashkey, boolean usePublic) {
		URL url;
		InputStream is =null;
		try {
			if(!usePublic) {
				url = new URL(ipfsdownloadhost+ "ipfs/"+hashkey);
			}else {
				url = new URL(publicIpfsDownload+ "ipfs/"+hashkey);
			}
			log.info("ipfs "+url.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setConnectTimeout(3000);
		    conn.setReadTimeout(3000);
		    is = conn.getInputStream();
			byte[] buffer = IOUtils.toByteArray(is);
			if(buffer!=null) {
				log.info("buffer "+buffer.length);
			}else {
				log.info("buffer is null");
			}
			return buffer;
		} catch (Exception e) {
			log.error("doDownload",e);
			if(!usePublic) {
				log.info("retry to public");
				return doDownload(hashkey, false);
			}
		}finally {
			if(is!=null)try {is.close();}catch(Exception e) {}
		}
		
		return new byte[0];
	}
	
	public static byte[] doDownload(String hashkey) {
		return doDownload(hashkey, true);
	}

}
