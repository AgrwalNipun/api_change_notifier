   package com.nipun.api_change_notifier.services;

                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.PostMapping;
                import org.springframework.web.bind.annotation.PathVariable;
                import org.springframework.web.bind.annotation.RequestParam;
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RestController;

import com.nipun.api_change_notifier.models.Payload;

                @RestController
                @RequestMapping("/test")
                public class TestClass {

                    private int counter = 0;
                    private String appName = "DemoApp";

                    @GetMapping("/hello/{name}")
                    public Payload hello(
                            @PathVariable String name,
                            @RequestParam(required = false) String lang,
                            @RequestParam(defaultValue = "1") int version
                    ) {
                        int localValue = counter + version;
                        return new Payload();
                    }
                }

