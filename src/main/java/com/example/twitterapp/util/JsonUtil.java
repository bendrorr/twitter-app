package com.example.twitterapp.util;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class JsonUtil {
    private final Faker faker;

    public String createContent() {
        return faker.lorem()
                .sentence(new Random().nextInt(9) + 1);
    }

    public String createAuthor() {
        return faker.name()
                .fullName();
    }


    public LocalDateTime createLocalDateTime() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return faker.date()
                .between(start, end)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public List<String> createHashtags() {
        return IntStream.range(0, new Random().nextInt(9) + 1)
                .mapToObj(i -> "#" + faker.lorem().word())
                .collect(Collectors.toList());
    }
}
