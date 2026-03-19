package com.compareproduct.meli.config;

import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Base test configuration for all unit tests
 */
@ExtendWith({MockitoExtension.class, AllureJunit5.class})
public abstract class BaseUnitTest {
}