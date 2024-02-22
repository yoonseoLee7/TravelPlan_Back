package travel.plan.data.mapper;

import org.apache.ibatis.annotations.Mapper;

import travel.plan.data.dto.KorContDTO;

@Mapper
public interface KorContMapper {
    public void saveBookMark(KorContDTO KorContDTO);
}
