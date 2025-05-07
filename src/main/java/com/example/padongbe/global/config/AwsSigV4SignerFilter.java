package com.example.padongbe.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.auth.signer.params.Aws4SignerParams;
import software.amazon.awssdk.core.signer.Signer;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.Region;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AwsSigV4SignerFilter {

    @Value("${open-search.iam.access-key}")
    private String accessKey;

    @Value("${open-search.iam.secret-key}")
    private String secretKey;

    @Value("${open-search.iam.session-token}")
    private String sessionToken;

    @Value("${open-search.iam.region}")
    private String region;

    public ExchangeFilterFunction awsSigner() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            Aws4Signer signer = Aws4Signer.create();

            AwsSessionCredentials credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);

            URI uri = clientRequest.url();

            // AWS SDK용 HTTP 요청 구성
            SdkHttpFullRequest unsignedRequest = SdkHttpFullRequest.builder()
                    .method(SdkHttpMethod.valueOf(clientRequest.method().name()))
                    .protocol(uri.getScheme())
                    .host(uri.getHost())
                    .port(uri.getPort() == -1 ? null : uri.getPort())
                    .encodedPath(uri.getRawPath())
                    .build();

            // 서명 추가
            SdkHttpFullRequest signedRequest = signer.sign(unsignedRequest,
                    Aws4SignerParams.builder()
                            .signingRegion(Region.of(region))
                            .signingName("es")
                            .awsCredentials(credentials)
                            .build());

            // WebClient에 서명된 헤더 추가
            ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .headers(headers -> {
                        for (Map.Entry<String, List<String>> entry : signedRequest.headers().entrySet()) {
                            headers.put(entry.getKey(), entry.getValue());
                        }
                    })
                    .build();

            return Mono.just(newRequest);
        });
    }
}
