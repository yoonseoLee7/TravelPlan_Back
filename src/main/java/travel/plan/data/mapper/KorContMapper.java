package travel.plan.data.mapper;

import org.apache.ibatis.annotations.Mapper;

import travel.plan.data.dto.KorContDTO;

@Mapper
public interface KorContMapper {
    public KorContDTO saveBookMark(KorContDTO KorContDTO);
    public KorContDTO deleteBookMark(KorContDTO KorContDTO);
}
