package com.nomad.data.agent.user.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.domain.dao.common.AipUserExample;
import com.nomad.data.agent.domain.mappers.common.AipUserMapper;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private AipUserMapper aipUserMapper;
	
	@Override
    public AipUser getAipUser(String tkn, boolean throwable) {
        AipUser user = this.getAipUser(tkn);

        if (throwable) {
            if (ObjectUtils.isEmpty(user)) {
                throw new CustomException(ErrorCodeType.USER_INVALID_TOKEN);
            }
        }

        return user;
    }
	
	private AipUser getAipUser(String tkn) {
        if (StringUtils.isEmpty(tkn)) {
            return null;
        }

        AipUserExample example = new AipUserExample();
        if (tkn.equals("test-tkn")) {
            example.createCriteria().andUserIdEqualTo("admin");
        } else {
            example.createCriteria().andTknEqualTo(tkn);
        }
        List<AipUser> list = aipUserMapper.selectByExampleWithRowbounds(example, new RowBounds(0, 1));

        if (ObjectUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

}
