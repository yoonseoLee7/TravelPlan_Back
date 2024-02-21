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

    //북마크 클릭 시 저장
    @Override
    public Map<String, Object> saveBookMark(KorContDTO KorContDTO) throws Exception {
        KorContDTO save = korContMapper.saveBookMark(KorContDTO);
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,save);
    }

    //북마크 다시 클릭 시 삭제
    @Override
    public Map<String, Object> deleteBookMark(KorContDTO KorContDTO) throws Exception {
        KorContDTO delete = korContMapper.deleteBookMark(KorContDTO);
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,delete);
    }
}
