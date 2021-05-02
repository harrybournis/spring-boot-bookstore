package com.example.bookstore;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BaseTest {
}
