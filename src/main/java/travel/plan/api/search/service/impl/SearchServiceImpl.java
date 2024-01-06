package travel.plan.api.search.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import travel.plan.api.search.dto.SearchDetailDTO;
import travel.plan.api.search.dto.SearchLocationDTO;
import travel.plan.api.search.dto.SearchPuzzleDTO;
import travel.plan.api.search.service.SearchService;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    // 장소통합검색
    @Override
    public List<SearchPuzzleDTO> searchArea(String searchText) throws Exception {
        String baseUrl = "https://apis.openapi.sk.com/tmap/pois?";
        String jsonString = WebClient.builder().baseUrl(baseUrl)
            .defaultHeader("appKey", "cm5DqAKbPy5a0cAgsLE9A9Yz1euxbeCCaxpY19Yt")
            .build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("searchKeyword", searchText)
                .build())
            .retrieve().bodyToFlux(String.class).blockLast();

        System.out.println(jsonString);
        
        List<SearchPuzzleDTO> result = new ArrayList<>();
        return result;
    }

    // 장소혼잡도검색
    @Override
    public void searchPuzzle(SearchPuzzleDTO searchPuzzleDTO) throws Exception {
        String baseUrl = "https://apis.openapi.sk.com/puzzle/place/congestion/rltm/pois/poiId=" + searchPuzzleDTO.getPoiId() + "?";
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

        System.out.println(jsonString);
    }

    // 위치기반관광정보검색
    @Override
    public List<SearchDetailDTO> searchLocation(SearchLocationDTO searchLocationDTO) throws Exception {
        String baseUrl = "http://apis.data.go.kr/B551011/KorService1/locationBasedList1?";

        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
            .queryParam("serviceKey", "yLQPmT7JIJF5Bd28tbZu1IhswhfeBfNXj%2BxyzHFtG3YxegDuvgDAfFouxMT9yAQUcU%2B7TGc2JhHEFQP7aQdX0A%3D%3D")
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

        System.out.println(jsonString);
        
     
        
        List<SearchDetailDTO> result = new ArrayList<>();
        return result;
    }

    // 공통정보검색
    @Override
    public void searchDetail(SearchDetailDTO searchDetailDTO) throws Exception {
        String baseUrl = "http://apis.data.go.kr/B551011/KorService1/detailCommon1?";
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
            .queryParam("serviceKey", "yLQPmT7JIJF5Bd28tbZu1IhswhfeBfNXj%2BxyzHFtG3YxegDuvgDAfFouxMT9yAQUcU%2B7TGc2JhHEFQP7aQdX0A%3D%3D")
            .queryParam("MobileOS", searchDetailDTO.getMobileOS())
            .queryParam("MobileApp", searchDetailDTO.getMobileApp())
            .queryParam("contentId", searchDetailDTO.getContentId())
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

        System.out.println(jsonString);
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
