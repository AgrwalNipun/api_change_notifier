package com.nipun.api_change_notifier.services;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

@Service
public class ParsingService {

    final List<String> mappingNames = List.of("GetMapping", "PostMapping", "PutMapping", "DeleteMapping",
            "PatchMapping");

    public void parse(File file) {
try{
                        // File file = new File("src/main/java/com/nipun/api_change_notifier/services/TestClass.java");
        CompilationUnit parsed = StaticJavaParser.parse(file);

        parsed.findAll(ClassOrInterfaceDeclaration.class).forEach(
                clas -> {

                    String basePath = clas.getAnnotationByName("RequestMapping").flatMap(
                            a -> a.toSingleMemberAnnotationExpr().map(
                                    s -> s.getMemberValue().toString()))
                            .orElse("");

                    System.out.println("BasePath :  " + basePath);

                    for (String mappingName : mappingNames) {

                        clas.getMethods().forEach(
                                method -> {
                                        
                                            

                                        method.getAnnotationByName(mappingName).ifPresent(
                                                
                                            methodAnnotations -> {

                                                String methodPath = methodAnnotations.toSingleMemberAnnotationExpr()
                                                        .map(

                                                                map -> map.getMemberValue().toString())
                                                        .orElse("");

                                                if (!methodPath.isEmpty()) {
                                                    System.out.println(
                                                            mappingName + " : " +
                                                                    basePath.replace("\"", "") +
                                                                    methodPath.replace("\"", ""));
                                                }

                                                
                                                String params = method.getParameters().toString();
                                                System.out.println(params);
                                                
                                                
                                                
                                                String returnType = method.getType().asString();
                                                System.out.println("Return type: " + returnType);
                                                
                                            }

                                );


                                });
                    }
                });
    

}catch(Exception e){
        System.out.println(e.getMessage());
}
    }

}
