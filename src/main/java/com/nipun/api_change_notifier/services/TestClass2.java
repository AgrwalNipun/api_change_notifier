package com.nipun.api_change_notifier.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

                @RestController
                @RequestMapping("/test2")
                public class TestClass2 {

                    private int counter = 0;
                    private String appName = "DemoApp";

                    @GetMapping("/hello/{name}")
                    public String hello(
                            @PathVariable String name,
                            @RequestParam(required = false) String source
                    ) {
                        int localValue = counter + 1;
                        return appName + " - " + name + " - " + source + " - " + localValue;
                    }

                    @PostMapping("/byebye/{id}")
                    public String byeBye(
                            @PathVariable int id,
                            @RequestParam(defaultValue = "false") boolean force,
                            @RequestParam(required = false) String reason
                    ) {
                        int localValue = counter + id;
                        return appName + " - id=" + id + " - force=" + force + " - reason=" + reason;
                    }
                  
}
