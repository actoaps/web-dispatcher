package dk.acto.web;

import dk.acto.web.bootstrap.SparkBootstrap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final var bootstrap = new SparkBootstrap(new DispatchHandler());

        bootstrap.init();
    }
}
