package com.blkchainsolutions.ogico.dh.ipfs;

import com.blkchainsolutions.Configure;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


@Controller
@Slf4j
@Data
public class IPFSController {

	@Autowired
	public Configure configure=null;

	private static Configure config=null;

	@PostConstruct
	public void init() {
		config = configure;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String shifted=encodeString("QmUkj6rcWaMy9QgBsqgG39BBAPopMCfwWxDPUum1ZELDeY",10);
		System.out.println(shifted);
		shifted=decodeString(shifted,10);
		System.out.println(shifted);
	}

	private static String encodeString(String srcString, int shiftValue) {
		char[] result = new char[srcString.length()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (char) (srcString.charAt(i) + shiftValue);
		}
		try {
			return URLEncoder.encode(new String(result),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("encodeString",e);
		}
		return "";
	}

	private static String decodeString(String encodeString, int shiftValue) {

		try {
			int NegshiftValue=shiftValue*(-1);
			String srcString=URLDecoder.decode(encodeString,"UTF-8");
			char[] result = new char[srcString.length()];
			for (int i = 0; i < result.length; i++) {
				result[i] = (char) (srcString.charAt(i) + NegshiftValue);
			}

			return new String(result);
		} catch (UnsupportedEncodingException e) {
			log.error("encodeString",e);
		}
		return "";
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason="You are not authorized")
	public class ForbiddenException extends RuntimeException {}

	//http://localhost:9090/public/20/10/%5Bw_ut%40%7CmakW%C2%83C%5BqL%7D%7BqQ%3DCLLKZyzWMp%C2%81a%C2%82NZ_%7Fw%3BdOVNoc/image.jpg
	@RequestMapping(path="/download/{userId}/{t10Id}/{ipfsid}/{fileName}",method= RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadDocumentV1(
			@PathVariable(value="userId", required=true) int userId,
			@PathVariable(value="t10Id", required=true) int t10Id,
			@PathVariable(value="ipfsid", required=true) String ipfsid,
			@PathVariable(value="fileName", required=true) String fileName) throws IOException {

		if(!isAuthorizedToOpen(userId,t10Id)) {
			throw new ForbiddenException();
		}

		log.info("ipfs request: "+ ipfsid);

		//String ipfshaskey=decodeString(ipfsid,t10Id);
		URL url = new URL(config.getIpfsDownload() + "ipfs/"+ipfsid);
		log.info(url.toString());
		byte[] buffer = new byte[0];
		String suffixName=".pdf";
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			InputStream is = conn.getInputStream();
			String [] str=conn.getContentType().split("/");
			suffixName = "."+str[1];
			buffer = IOUtils.toByteArray(is);
		}catch(Exception e) {
			log.error("IPFS timeout"+e);
		}
//		InputStream is = url.openStream();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Content-Disposition", "attachment; filename=\""+fileName+suffixName+"\"");

//        InputStreamResource resource = new InputStreamResource(is);
		ByteArrayInputStream bais = new
				ByteArrayInputStream(buffer);
		InputStreamResource resource = new InputStreamResource(bais);

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}

	private boolean isAuthorizedToOpen(int userId, int t10Id) {

		return true;
	}



}
