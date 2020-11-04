package com.nomad.data.agent.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommandUtils {

	private static final String LINE = System.lineSeparator();

	public static final String BASH = "/bin/sh";
	public static final String OPT  = "-c";

	public static String run(List<String> cmd) {

		String[] command = { BASH, OPT, String.join("&&", cmd) };

		String result = "";

		try {
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(command);

			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String cl = null;
			while((cl = in.readLine()) != null) {
				sb.append(cl).append(LINE);
			}
			result = sb.toString();

		}catch(Exception e) {
			log.error(">>>>> command run error", e);
			throw new CustomException(ErrorCodeType.COMMON_COMMAND_RUN_FAIL);
		}

		return result;
	}

	public static String run(String cmd) {

		List<String> cmdList = new ArrayList<>();
		cmdList.add(cmd);

		return run(cmdList);
	}
}
