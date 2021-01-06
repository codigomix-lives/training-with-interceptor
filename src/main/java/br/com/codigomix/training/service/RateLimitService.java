package br.com.codigomix.training.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitService {

    Map<String, Bucket> buckets = new HashMap<>();

    public String getAuthenticatedUser(){
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Bucket getBucket() {

        var bucket = buckets.getOrDefault(getAuthenticatedUser(),
                                            Bucket4j.builder()
                                                    .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1)))
                                                    .build());

        buckets.put(getAuthenticatedUser(), bucket);

        return bucket;

    }




}
