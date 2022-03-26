package com.example.regexApplication.controller;

import com.example.regexApplication.model.InputRegex;
import com.example.regexApplication.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.net.SocketTimeoutException;
import java.rmi.ServerException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


@RestController
public class RegexController {
	
	@RequestMapping("/welcome")
	public String getWelcome() {
		return "Welcome to rest";
	}

	@PostMapping(path = "regex",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Result> create(@RequestBody InputRegex inputRegex) throws TimeoutException {

		Result result =new Result();
		String regex = inputRegex.getRegex();
		String text = inputRegex.getTextBody();
		//null and empty check for regex
		if (regex.isEmpty() || text.isEmpty()) {
			System.out.println("Please enter proper value");
			return new ResponseEntity<>(result, HttpStatus.PRECONDITION_FAILED);
		} else {
			//implementing the matcher logic here
			Pattern p = Pattern.compile(regex);

			Matcher m = p.matcher(text);
			StopWatch stopWatch = new StopWatch();
			final long RE_TIMEOUT = 50L;
			stopWatch.start();
			while(m.find()){

				result.setMatch(text);
				result.setError(false);
				break;
			}
			stopWatch.stop();
			//throw Exception when timeout occurs
			if(stopWatch.getTotalTimeMillis()>RE_TIMEOUT){
				throw new TimeoutException("Matched timeout");
			}
			 if(result.getMatch()==null || result.getMatch().isEmpty() ){
				result.setMatch("");
				result.setError(true);
			}
		}
		return new ResponseEntity<>(result, HttpStatus.FOUND);
	}
}
