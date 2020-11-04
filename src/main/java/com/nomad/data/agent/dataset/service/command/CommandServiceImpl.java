package com.nomad.data.agent.dataset.service.command;

import com.nomad.data.agent.dataset.dto.info.AipResponseInfo;
import com.nomad.data.agent.dataset.dto.req.CommandReq;
import com.nomad.data.agent.utils.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommandServiceImpl implements CommandService {

    @Override
    public AipResponseInfo execCommand(CommandReq commandReq) {

        log.info(">>>>> command :{}", commandReq.getCommand().toString());

        List<String> cmd = commandReq.getCommand();

        String commandResult = CommandUtils.run(cmd);

        AipResponseInfo responseInfo = new AipResponseInfo();
        responseInfo.setLog(commandResult);

        return responseInfo;

    }

}
