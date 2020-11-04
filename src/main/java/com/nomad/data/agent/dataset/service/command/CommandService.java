package com.nomad.data.agent.dataset.service.command;

import com.nomad.data.agent.dataset.dto.info.AipResponseInfo;
import com.nomad.data.agent.dataset.dto.req.CommandReq;

public interface CommandService {
    public AipResponseInfo execCommand(CommandReq commandReq);
}
