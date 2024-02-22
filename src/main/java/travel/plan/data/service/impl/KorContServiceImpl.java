package travel.plan.data.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.common.ApiResult;
import travel.exception.ApiStatus;
import travel.plan.data.dto.KorContDTO;
import travel.plan.data.mapper.KorContMapper;
import travel.plan.data.service.KorContService;

@Service
public class KorContServiceImpl implements KorContService{
    @Autowired
    KorContMapper korContMapper;

    @Override
    public Map<String, Object> saveBookMark(KorContDTO KorContDTO) throws Exception {
        korContMapper.saveBookMark(KorContDTO);
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,"save or update");
    }
}
