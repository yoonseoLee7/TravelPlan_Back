package travel.plan.data.service;

import java.util.Map;

import travel.plan.data.dto.KorContDTO;

public interface KorContService {
    //북마크 저장
    public Map<String, Object> saveBookMark(KorContDTO KorContDTO) throws Exception;
}
