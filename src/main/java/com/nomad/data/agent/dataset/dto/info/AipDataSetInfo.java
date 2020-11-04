package com.nomad.data.agent.dataset.dto.info;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AipDataSetInfo {

	@NotEmpty
	@ApiModelProperty(position = 1, required = true, value = "데이터셋 아이디")
	private String dataId;
	
	@NotEmpty
	@ApiModelProperty(position = 2, required = true, value = "데이터셋 이름")
	private String dataNm;
	
	@ApiModelProperty(position = 3, required = false, value = "데이터셋 설명")
	private String dataDesc;
	
	@NotEmpty
	@ApiModelProperty(position = 4, required = true, value = "데이터셋 종류")
	private String dataTp;
	
	@ApiModelProperty(position = 5, required = false, value = "쿼리디비 종류")
	private String queryDbTp;
	
	@ApiModelProperty(position = 6, required = false, value = "쿼리문")
	private String queryStr;
	
	@ApiModelProperty(position = 7, required = false, value = "쿼리 상태")
	private String querySts;
	
	@ApiModelProperty(position = 8, required = false, value = "파일이름")
	private String fileNm;
	
	@ApiModelProperty(position = 9, required = false, value = "파일경로")
	private String filePath;
	
	@ApiModelProperty(position = 10, required = false, value = "파일크기")
	private Long fileSz;
	
	@NotEmpty
	@ApiModelProperty(position = 11, required = true, value = "소유자 아이디")
	private String ownerId;
	
	@ApiModelProperty(position = 12, required = false, value = "접근 권한")
	private String accAuth;
	
	@NotEmpty
	@ApiModelProperty(position = 13, required = true, value = "공개여부")
	private String pubFl;
	
	@ApiModelProperty(position = 14, required = false, value = "예상시작일")
	private String exptStDt;
	
	@ApiModelProperty(position = 15, required = false, value = "예상종료일")
	private String exptEdDt;
	
	@NotEmpty
	@ApiModelProperty(position = 16, required = true, value = "삭제여부")
	private String delFl;
	
	@NotEmpty
	@ApiModelProperty(position = 17, required = true, value = "등록일")
	private String regDt;
	
	@NotEmpty
	@ApiModelProperty(position = 18, required = true, value = "수정일")
	private String modDt;
}
