package travel.plan.api.search.service.impl;

import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.service.SearchService;
import travel.plan.api.search.vo.SearchAreaVO;
import travel.plan.api.search.vo.SearchDetailVO;
import travel.plan.api.search.vo.SearchLocationVO;
import travel.plan.api.search.vo.SearchPuzzleVO;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    ObjectMapper objectMapper;

    /*
     * TODO Value 값 null 뜨는거 확인 필요
     */
    @Value("${app.sk.api-key}")
    private String skKey;

    @Value("${app.visitkorea.api-key}")
    private String tourKey;

    // 장소통합검색
    @Override
    public List<SearchAreaVO> searchArea(String searchText) throws Exception {
        String baseUrl = "https://apis.openapi.sk.com/tmap/pois?";
        String jsonString = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader("appKey", "cm5DqAKbPy5a0cAgsLE9A9Yz1euxbeCCaxpY19Yt")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("searchKeyword", searchText)
                        .build())
                .retrieve().bodyToFlux(String.class).blockLast();

        JsonNode jsonNode = objectMapper.readTree(jsonString).get("searchPoiInfo").get("pois").get("poi");

        List<SearchAreaVO> searchArea = objectMapper.readValue(jsonNode.toString(),
                new TypeReference<List<SearchAreaVO>>() {
                });
        return searchArea;
    }

    // 장소혼잡도검색
    @Override
    public SearchPuzzleVO searchPuzzle(SearchPuzzleDTO searchPuzzleDTO) throws Exception {
        String baseUrl = "https://apis.openapi.sk.com/puzzle/place/congestion/rltm/pois/poiId="
                + searchPuzzleDTO.getPoiId() + "?";
        String jsonString = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader("appKey", "cm5DqAKbPy5a0cAgsLE9A9Yz1euxbeCCaxpY19Yt")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", searchPuzzleDTO.getNoorLat())
                        .queryParam("lng", searchPuzzleDTO.getNoorLon())
                        .build())
                .retrieve()
                .bodyToFlux(String.class)
                .blockLast();

        JsonNode jsonNode = objectMapper.readTree(jsonString).get("contents").get("rltm").get(0);

        /*
         * TODO congestin 받아올때 값이 지수로 표현되는 부분 수정
         */
        SearchPuzzleVO searchPuzzle = objectMapper.readValue(jsonNode.toString(), SearchPuzzleVO.class);
        return searchPuzzle;
    }

    // 위치기반관광정보검색
    @Override
    public List<SearchLocationVO> searchLocation(SearchLocationDTO searchLocationDTO) throws Exception {
        String baseUrl = "http://apis.data.go.kr/B551011/KorService1/locationBasedList1?";
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey",
                        "yLQPmT7JIJF5Bd28tbZu1IhswhfeBfNXj%2BxyzHFtG3YxegDuvgDAfFouxMT9yAQUcU%2B7TGc2JhHEFQP7aQdX0A%3D%3D")
                .queryParam("MobileOS", searchLocationDTO.getMobileOS())
                .queryParam("MobileApp", searchLocationDTO.getMobileApp())
                .queryParam("mapX", searchLocationDTO.getMapX())
                .queryParam("mapY", searchLocationDTO.getMapY())
                .queryParam("radius", searchLocationDTO.getRadius())
                .queryParam("_type", "json")
                .build(true)
                .toUri();

        String jsonString = WebClient.builder().baseUrl(baseUrl)
                .build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode jsonNode = objectMapper.readTree(jsonString).get("response").get("body").get("items").get("item");

        List<SearchLocationVO> searchLocation = objectMapper.readValue(jsonNode.toString(),
                new TypeReference<List<SearchLocationVO>>() {
                });
        return searchLocation;
    }

    // 공통정보검색
    @Override
    public SearchDetailVO searchDetail(SearchDetailDTO searchDetailDTO) throws Exception {
        String baseUrl = "http://apis.data.go.kr/B551011/KorService1/detailCommon1?";
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey",
                        "yLQPmT7JIJF5Bd28tbZu1IhswhfeBfNXj%2BxyzHFtG3YxegDuvgDAfFouxMT9yAQUcU%2B7TGc2JhHEFQP7aQdX0A%3D%3D")
                .queryParam("MobileOS", searchDetailDTO.getMobileOS())
                .queryParam("MobileApp", searchDetailDTO.getMobileApp())
                .queryParam("contentId", searchDetailDTO.getContentId())
                .queryParam("defaultYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("addrinfoYN", "Y")
                .queryParam("overviewYN", "Y")
                .queryParam("_type", "json")
                .build(true)
                .toUri();

        String jsonString = WebClient.builder().baseUrl(baseUrl)
                .build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode jsonNode = objectMapper.readTree(jsonString).get("response").get("body").get("items").get("item")
                .get(0);

        SearchDetailVO searchDetail = objectMapper.readValue(jsonNode.toString(), SearchDetailVO.class);
        return searchDetail;
    }

    /* 
    URLConnection을 사용하여 데이터 가져오기
    @Override
    public List<SearchPuzzleDTO> searchArea(String searchText) throws Exception {
        
        StringBuilder stringBuilder = new StringBuilder("https://apis.openapi.sk.com/tmap/pois");
        stringBuilder.append("?searchKeyword=" + URLEncoder.encode(searchText,"UTF-8"));    
    
        URL url = new URL(stringBuilder.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("appKey", "cm5DqAKbPy5a0cAgsLE9A9Yz1euxbeCCaxpY19Yt");
    
        BufferedReader bufferedReader;
        if(urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() <= 300) {
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
        }
    
        StringBuilder resultStringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            resultStringBuilder.append(line);
        }
        bufferedReader.close();
        urlConnection.disconnect();
    
        String resultString = resultStringBuilder.toString();;
        List<SearchPuzzleDTO> result = new ArrayList<>();
        return result;
    }
    */
}
