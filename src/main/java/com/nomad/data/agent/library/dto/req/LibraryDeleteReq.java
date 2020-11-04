package com.nomad.data.agent.library.dto.req;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryDeleteReq {
	
	private String libTp;
	private String pkgNm;
	private String pkgFlNm;
	private String pkgVer;
	private int instCnt;
	private String downSts;
	private Date regDt;
	private Date modDt;
}
