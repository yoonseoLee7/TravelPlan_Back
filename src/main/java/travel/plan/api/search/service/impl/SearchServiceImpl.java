package travel.plan.api.search.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import travel.common.ApiResult;
import travel.exception.ApiException;
import travel.exception.ApiStatus;
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
                .queryParam("mapinfoYN", "Y")
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

    // 지도 위치 및 혼잡도 표시
    @Override
    public Map<String, Object> congestion(SearchAreaVO vo) throws Exception {
        System.out.println("aaaaaaa" + vo);
        SearchPuzzleDTO dto = new SearchPuzzleDTO();
        dto.setPoiId(vo.getId());
        dto.setNoorLat(vo.getNoorLat());
        dto.setNoorLon(vo.getNoorLon());

        var level = searchPuzzle(dto).getCongestionLevel();
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, level);
    }

    // 검색 리스트 아이템 선택 시 추천방문지 호출용
    @Override
    public Map<String, Object> suggest(SearchAreaVO vo) throws Exception {
        SearchLocationDTO searchLocationDTO = new SearchLocationDTO();
        searchLocationDTO.setMobileApp("DEMO");
        searchLocationDTO.setMobileOS("WIN");
        searchLocationDTO.setMapX(vo.getNoorLon());
        searchLocationDTO.setMapY(vo.getNoorLat());
        searchLocationDTO.setRadius(500);
        
        List<SearchDetailVO> detailList = locationToDetail(searchLocationDTO);
        List<SearchDetailVO> sortList = new ArrayList<>();
        
        int[] rank = getDistance(vo, detailList);
        for(int i = 0; i < detailList.size(); i++) {
            for(int j = 0; j < rank.length; j++) {
                if (i == rank[j] - 1) {
                    sortList.add(detailList.get(j));
                }
            }
        }
        return ApiResult.getHashMap(ApiStatus.AP_SUCCESS, sortList);
    }

    // 두 좌표 사이의 거리 값 측정
    public int[] getDistance(SearchAreaVO vo, List<SearchDetailVO> detailList) {
        List<Double> distance = new ArrayList<>();
        for(var detail: detailList) {
            var result = getDistanceOne(vo.getNoorLat(), vo.getNoorLon(), detail.getMapy(), detail.getMapx());
            distance.add(result);
        }

        // 가져온 좌표들의 가까운 순위대로 표시
        int[] ranks = new int[distance.size()];
        for(int i = 0; i < distance.size(); i++) {
            int rank = distance.size();
            for(int j = 0; j < distance.size(); j++) {
                if(distance.get(i) < distance.get(j)) {
                    rank--;
                }
                ranks[i] = rank;
            }
        }
        return ranks;
    }

    public double getDistanceOne(double latFirst, double lngFirst, double latSecond, double lngSecond) {
        double radius = 6371;
        double dLat = Math.toRadians(latSecond - latFirst);
        double dLng = Math.toRadians(lngSecond = lngFirst);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latFirst)) * Math.cos(Math.toRadians(latSecond))
            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c * 1000;
        return d;
    }

    private List<SearchDetailVO> locationToDetail(SearchLocationDTO searchLocationDTO) throws Exception {
        List<SearchLocationVO> locationList = searchLocation(searchLocationDTO);
        List<SearchDetailVO> detailList = new ArrayList<SearchDetailVO>();
        SearchDetailDTO detailDTO = new SearchDetailDTO();
        for(int i = 0; i < locationList.size(); i++) {
            detailDTO.setContentId(locationList.get(i).getContentid());
            detailDTO.setMobileApp("DEMO");
            detailDTO.setMobileOS("WIN");
            detailList.add(searchDetail(detailDTO));
        }

        return detailList;
    }

    @Override
    public Map<String, Object> searchList(String searchText) throws Exception {
        List<SearchAreaVO> searchArea = new ArrayList<SearchAreaVO>();

        try {
            searchArea = searchArea(searchText);
            return ApiResult.getHashMap(ApiStatus.AP_SUCCESS,searchArea);
        } catch (Exception e) {
            log.error("api request error", e);
            throw new ApiException(ApiStatus.AP_FAIL, "장소통합검색 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }


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
